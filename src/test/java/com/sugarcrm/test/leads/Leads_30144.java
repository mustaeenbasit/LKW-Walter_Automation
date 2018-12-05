package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_30144 extends SugarTest {
	DataSource customData = new DataSource();
	VoodooControl systemSettingCtrl, leadConvertingCtrl, systemSettingSaveCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().leads.api.create();
		sugar().login();

		// Navigate to admin page
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Navigate to system setting
		systemSettingCtrl = sugar().admin.adminTools.getControl("systemSettings");
		systemSettingCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1903
		// Changing lead conversion option from 'Do Nothing' to 'Move'
		leadConvertingCtrl = new VoodooControl("select", "css", "[name='lead_conv_activity_opt']");
		leadConvertingCtrl.set(customData.get(0).get("leadConversionOption"));

		// Saving the system settings
		systemSettingSaveCtrl = sugar().admin.systemSettings.getControl("save");
		systemSettingSaveCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Records in the subpanel is Copied/Moved using lead conversion option
	 * @throws Exception
	 */
	@Test
	public void Leads_30144_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to lead module
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// Adding one call record in lead 
		sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural).addRecord();
		sugar().calls.createDrawer.getEditField("name").set(customData.get(0).get("callName"));
		sugar().calls.createDrawer.save();

		// Clicking on edit action dropdown of lead in record view
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click "Convert Lead" button in "Lead" record view.
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Fill in Account name and click Associate Account
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Fill in Opportunity name and click Associate Opportunity
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").set(sugar().opportunities.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Opportunities'] .fld_associate_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Navigate contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verifying the Call record in associated Contact module of lead
		sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural).getDetailField(1, "name").assertEquals(customData.get(0).get("callName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}