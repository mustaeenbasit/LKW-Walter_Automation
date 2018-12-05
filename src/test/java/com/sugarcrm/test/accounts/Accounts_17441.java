package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17441 extends SugarTest {
	StandardSubpanel callsSubpanel,meetingsSubpanel,tasksSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();

		CallRecord callData = (CallRecord)sugar().calls.api.create();
		MeetingRecord meetingData = (MeetingRecord)sugar().meetings.api.create();
		TaskRecord taskData = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Navigate to Accounts Module
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Link Records in different Subpanels
		callsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		tasksSubpanel = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		callsSubpanel.linkExistingRecord(callData);
		meetingsSubpanel.linkExistingRecord(meetingData);
		tasksSubpanel.linkExistingRecord(taskData);

	}

	/**
	 * Verify user can search for a record in the subpanel view
	 * @throws Exception
	 */			
	@Test
	public void Accounts_17441_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterChoice = testData.get(testName).get(0);

		// Click the DataView
		sugar().accounts.recordView.showDataView();

		// Related Subpanel is selected by default
		sugar().accounts.recordView.getControl("relatedSubpanelChoice").assertEquals(filterChoice.get("filterSelected"), true);

		// Verify when "All" is selected then All Related modules are shown in Subpanel
		callsSubpanel.assertVisible(true);
		meetingsSubpanel.assertVisible(true);
		tasksSubpanel.assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);
		sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural).assertVisible(true);

		// TODO: VOOD-1344
		new VoodooControl("div", "css", "div[data-voodoo-name='CampaignLog']").assertVisible(true);

		// Enter the Search String
		sugar().accounts.recordView.getControl("searchFilter").set(filterChoice.get("searchString"));
		VoodooUtils.waitForReady();

		// Verify the module which contains the search string displays the record
		callsSubpanel.getDetailField(1, "name").assertEquals(sugar().calls.getDefaultData().get("name"), true);
		Assert.assertTrue("Meetings Subpanel is not Empty", meetingsSubpanel.isEmpty());
		Assert.assertTrue("Tasks Subpanel is not Empty", tasksSubpanel.isEmpty());
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}