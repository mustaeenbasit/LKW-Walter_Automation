package com.sugarcrm.test.subpanels;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17557c extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * 17557c: Verify user can create related records (for an account) from subpanels --c. quotedLineItems
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17557c_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "+" in quotedLineItems sub-panel to create new quote
		sugar().accounts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).addRecord();

		// Verify account fields are populated by default on quotedLineItems create page
		sugar().quotedLineItems.createDrawer.getEditField("accountName")
		.assertContains(sugar().accounts.getDefaultData().get("name"),true);

		// Complete all required information for quotedLineItems
		sugar().quotedLineItems.createDrawer.getEditField("name")
		.set(sugar().quotedLineItems.getDefaultData().get("name"));

		//Save quote
		sugar().quotedLineItems.createDrawer.save();

		// Verify that created quotedLineItems record is displayed in quotedLineItems sub-panel
		// TODO: VOOD-1000 Subpanel methods not working for Quote and QuoteLineItem
		new VoodooControl("a", "css", ".list.fld_name")
		.assertContains(sugar().quotedLineItems.getDefaultData().get("name"), true);

		// Verify account name is displayed in quotedLineItems sub-panel
		// TODO: VOOD-1000 Subpanel methods not working for Quote and QuoteLineItem
		new VoodooControl("a", "css", ".list.fld_account_name")
		.assertContains(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}  

	public void cleanup() throws Exception {}
}