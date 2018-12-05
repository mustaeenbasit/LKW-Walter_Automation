package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28849 extends SugarTest {
	DataSource oppData = new DataSource();
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		oppData = testData.get(testName);
		fs = sugar().accounts.getDefaultData();
		sugar().accounts.api.create();
		sugar().opportunities.api.create(oppData);
		sugar().login();

		// Link Opp records to Account record.
		// TODO: VOOD-444 & VOOD-1587 Perform mass update once this VOOD is resolved
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(fs.get("name"));
		sugar().opportunities.listView.saveRecord(1);
		sugar().opportunities.listView.editRecord(2);
		sugar().opportunities.listView.getEditField(2, "relAccountName").set(fs.get("name"));
		sugar().opportunities.listView.saveRecord(2);
	}

	/**
	 * Account name updates correctly for the Opportunity when a user converts a lead.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_28849_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("accountName").set(fs.get("name"));
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		sugar().leads.createDrawer.save();

		// Click convert
		sugar().leads.listView.openRowActionDropdown(1);
		// TODO: VOOD-498 - Need ListView functionality for all row actions
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady(30000);

		// Verify User would find one account already selected in the duplicate search panel.
		// TODO: VOOD-585
		new VoodooControl("div", "css", "[data-voodoo-name='dupecheck-list-select']").assertContains(fs.get("name"), true);

		// Click 'Select Account' button.
		VoodooControl accountsCtrl = new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a");
		accountsCtrl.click();
		VoodooUtils.waitForReady();

		// Verify User would view the linked opportunities associated with that selected 'Account'.
		VoodooControl oppLayoutCtrl = new VoodooControl("tbody", "css", ".layout_Opportunities .left-actions.right-actions table tbody");
		oppLayoutCtrl.assertContains(oppData.get(0).get("name"), true);
		oppLayoutCtrl.assertContains(oppData.get(1).get("name"), true);

		// Click 'Reset' link of the Account panel
		new VoodooControl("span", "css", "div[data-module='Accounts'] .fld_reset_button").click();
		// Click 'Ignore and create new' link.
		new VoodooControl("a", "css", "div[data-module='Accounts'] .toggle-link.dupecheck").click();
		// Change Account Name to a different (new) name.
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(testName);
		// Click 'Create Account' button.
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		// Enter new opportunity required fields
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input").set(sugar().opportunities.getDefaultData().get("name"));
		// Click Create Opportunity button.
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert-panel-header a").click();
		// Click Save and Convert button.
		new VoodooControl("a", "css", ".convert-headerpane a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Click on 'Opportunity Associated' name of the newly saved and converted lead.
		new VoodooControl("a", "css", ".converted-results tr:nth-child(3) td div a").click();
		VoodooUtils.waitForReady();

		// Verify User must see the Account Name of the opportunity as updated
		sugar().opportunities.recordView.getDetailField("relAccountName").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}