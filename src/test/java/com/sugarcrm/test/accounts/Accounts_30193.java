package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_30193 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify that RLI sub panel header displays correct number of RLI records linked to the account before sub panel is expanded
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_30193_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to record view of created account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Define Opportunity and RLI sub panels controls
		StandardSubpanel opportunitySubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		StandardSubpanel rliSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// Click plus button to create new *OPPORTUNITY*  (not RLI )
		opportunitySubpanelCtrl.addRecord();

		// Create opportunity record with one or more RLI record(s) and save. 
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("rli_name").set(testName + "_" + sugar().revLineItems.moduleNamePlural);
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(testName.substring((testName.length() - 4)));
		sugar().opportunities.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that the User is returned to Account record view 
		sugar().accounts.recordView.getDetailField("name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		FieldSet subpanelData = testData.get(testName).get(0);

		// Verify that on Accounts record view, the Opportunities sub-panel expanded and one record displayed in the sub-panel
		opportunitySubpanelCtrl.getControl("subpanelStatus").assertAttribute("class", subpanelData.get("closed"), false);
		opportunitySubpanelCtrl.getDetailField(1, "name").assertEquals(testName, true);

		// Verify that the Number of RLI records linked to the account is displayed in the header of RLI sub panel
		rliSubpanelCtrl.getControl("count").assertContains(subpanelData.get("numberOfRliRecords"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}