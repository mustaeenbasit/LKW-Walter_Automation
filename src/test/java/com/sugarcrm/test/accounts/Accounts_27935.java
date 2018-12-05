package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_27935 extends SugarTest {
	StandardSubpanel revLineItemsSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that created RLI is displayed in RLI sub-panel when create new Opportunity with RLI 
	 * from Account record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_27935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "+" in Opportunities sub-panel to create new opportunity
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).addRecord();

		// Verify account name field is populated by default on opportunities create Drawer
		sugar().opportunities.createDrawer.getEditField("relAccountName").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		FieldSet RLIrecord = new FieldSet();
		RLIrecord.put("name", sugar().revLineItems.getDefaultData().get("name"));
		RLIrecord.put("dateClosed", sugar().revLineItems.getDefaultData().get("date_closed"));

		// Complete all required information for both opportunity and RLI
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(RLIrecord.get("name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(RLIrecord.get("dateClosed"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.save();
		
		// TODO: Remove below 2 lines after SFA-3428 is fixed
		VoodooUtils.refresh();
		sugar().alerts.waitForLoadingExpiration();

		// Observe RLI sub-panel of Account record view
		revLineItemsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.expandSubpanel();

		// Verify that created RLI record is displayed in RLI sub-panel on Account record view
		revLineItemsSubpanel.verify(1, RLIrecord, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}