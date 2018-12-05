package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17180 extends SugarTest {
	DataSource customData = new DataSource();
	StandardSubpanel leadSubpanel, callSubpanel, contactSubpanel;

	public void setup() throws Exception {
		// Pre-Condition : Account Records exist with relate records
		sugar().accounts.api.create();
		LeadRecord leadRecord = (LeadRecord)sugar().leads.api.create();
		CallRecord callRecord = (CallRecord)sugar().calls.api.create();
		ContactRecord conRecord = (ContactRecord)sugar().contacts.api.create();
		sugar().login();

		// Relate Opp, Call, Contact data with account record.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		customData = testData.get(testName);
		leadSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubpanel.linkExistingRecord(leadRecord);
		callSubpanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSubpanel.linkExistingRecord(callRecord);
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(conRecord);
	}

	/**
	 * Verify subpanel header shows number of related records
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17180_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that each of the headers displays the number of records in parenthesis next to the module name.
		for(int i = 0 ; i < customData.size() ; i++) {
			sugar().accounts.recordView.subpanels.get(customData.get(i).get("subpanel")).getControl("count").assertContains(customData.get(0).get("count"), true);
			sugar().accounts.recordView.subpanels.get(customData.get(i).get("subpanel")).getControl("subpanelName").assertContains(customData.get(i).get("subpanel"), true);
		}

		// Add a related record to Leads subpanel by clicking the "+" icon
		leadSubpanel.addRecord();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();
		VoodooUtils.waitForReady();

		// Verify count is incremented correctly
		leadSubpanel.getControl("count").assertContains(customData.get(0).get("incrementedCount"), true);

		// Unlink a record from another subpanel by clicking the action drop down then selecting "Unlink" and Confirm button
		callSubpanel.expandSubpanel();
		callSubpanel.unlinkRecord(1);
		VoodooUtils.waitForReady();

		// Verify that the count is decremented correctly.
		callSubpanel.getControl("count").assertContains(customData.get(1).get("count"), true);

		// Verify other subpanels for the correct counts of records.
		contactSubpanel.getControl("count").assertContains(customData.get(2).get("count"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}