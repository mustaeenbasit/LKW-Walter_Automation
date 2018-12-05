package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_21502 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		
		// Create 2 accounts with all required information
		sugar().accounts.api.create();
		sugar().accounts.api.create(fs);
		sugar().login();

		// Create an Opportunity and Revenue Line Item related to first Account
		sugar().opportunities.create();
	}

	/**
	 * Verify that related Revenue Line Items are not orphaned when merging Accounts
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_21502_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Accounts listview, check off the both the created accounts
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();

		// Click 'Merge'
		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		VoodooUtils.waitForReady();

		// Verify that Primary record is that which is not related to Revenue Line Items
		new VoodooControl("span", "css", ".record-name").assertContains(testName, true);

		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_save_button.merge-duplicates-headerpane a").click();
		VoodooUtils.waitForReady();
		sugar().alerts.getAlert().confirmAlert();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_name.list").waitForVisible();

		// Once merge operation complete, go to the detail view of remaining account
		sugar().accounts.listView.clickRecord(1);

		// Verify that Revenue is related to the remaining account
		FieldSet dataToVerify = new FieldSet();
		dataToVerify.put("name", sugar().revLineItems.getDefaultData().get("name"));
		dataToVerify.put("relOpportunityName", sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		StandardSubpanel rliSubpanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		rliSubpanel.verify(1, dataToVerify, true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}