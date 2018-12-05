package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18413 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel rliSubpanelOfAccount, rliSubpanelOfOpp, oppSubpanelOfAccount;
	DataSource testDS;

	public void setup() throws Exception {
		testDS = testData.get(testName);
		sugar().accounts.api.create();
		FieldSet rliFS = new FieldSet();
		sugar().login();

		// Create an Opportunity 
		myOpp = (OpportunityRecord) sugar().opportunities.create();

		// Create Revenue Line Items with sales stage closed won,closed lost and Prospecting.
		for (int i = 0; i < 3; i++) {
			rliFS.put("name", testDS.get(i).get("name"));
			rliFS.put("salesStage", testDS.get(i).get("salesStage"));
			rliFS.put("relOpportunityName", myOpp.getRecordIdentifier());
			sugar().revLineItems.create(rliFS);
			rliFS.clear();
		}
	}

	/**
	 * Verify closed sales stage Revenue Line Item record is not deleted in subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18413_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Test 1: RLI/Opp Subpanel under Accounts: Mass Update disabled- Check boxes have been removed
		// Navigate to RLI/Opp subpanels of the related account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		rliSubpanelOfAccount = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		oppSubpanelOfAccount = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);

		// Assert check boxes do not exist
		rliSubpanelOfAccount.expandSubpanel();
		rliSubpanelOfAccount.getControl("selectAllCheckbox").assertExists(false);
		oppSubpanelOfAccount.expandSubpanel();
		oppSubpanelOfAccount.getControl("selectAllCheckbox").assertExists(false);

		// Test 2 RLI Mass Update from Accounts Subpanel
		// Navigate to RLIs subpanel of the related Opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		rliSubpanelOfOpp = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// Try to delete all RLI records
		rliSubpanelOfOpp.expandSubpanel();
		rliSubpanelOfOpp.toggleSelectAll();
		rliSubpanelOfOpp.openActionDropdown();
		rliSubpanelOfOpp.delete();

		// Verify that no RLIs are deleted and warning message is displayed
		sugar().alerts.getWarning().assertContains(testDS.get(0).get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		rliSubpanelOfOpp.assertContains(testDS.get(0).get("name"), true);
		rliSubpanelOfOpp.assertContains(testDS.get(1).get("name"), true);
		rliSubpanelOfOpp.assertContains(testDS.get(2).get("name"), true);

		// TODO: VOOD-1020 - verification of the checkboxes status should be changed when story is implemented
		VoodooControl checkbox1 = new VoodooControl("input", "css", "tbody tr:nth-child(1) input[type='checkbox']");
		VoodooControl checkbox2 = new VoodooControl("input", "css", "tbody tr:nth-child(2) input[type='checkbox']");
		VoodooControl checkbox3 = new VoodooControl("input", "css", "tbody tr:nth-child(3) input[type='checkbox']");

		// Verify the status of listview checkboxes
		// TODO: VOOD-1020 - verification of the checkboxes status should be changed when story is implemented
		Assert.assertTrue("Should be checked", (Boolean.parseBoolean(checkbox1.getAttribute("checked"))));
		Assert.assertFalse("Should be unchecked", (Boolean.parseBoolean(checkbox2.getAttribute("checked"))));
		Assert.assertFalse("Should be unchecked", (Boolean.parseBoolean(checkbox3.getAttribute("checked"))));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}