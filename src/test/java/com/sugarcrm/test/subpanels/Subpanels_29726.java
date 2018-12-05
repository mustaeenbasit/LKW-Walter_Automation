package com.sugarcrm.test.subpanels;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_29726 extends SugarTest {
	DataSource customData = new DataSource();
	StandardSubpanel callsSubpanel, meetingsSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		customData = testData.get(testName);
		ArrayList<Record> callRecords = sugar().calls.api.create(customData);
		ArrayList<Record> meetingRecords = sugar().meetings.api.create(customData);
		callsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		sugar().login();

		// TODO: VOOD-444 Once resolved, assignedTo dependency field should create via API 
		// Navigate to Accounts Record View and Link Calls & Meetings to Account Record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		callsSubpanel.linkExistingRecords(callRecords);
		meetingsSubpanel.linkExistingRecords(meetingRecords);

		// Update qauser in assigned user field.in particular Call & Meeting Record.
		String qauser = sugar().users.getQAUser().get("userName");
		callsSubpanel.editRecord(1);
		callsSubpanel.getEditField(1,"assignedTo").set(qauser);
		callsSubpanel.saveAction(1);
		meetingsSubpanel.editRecord(1);
		meetingsSubpanel.getEditField(1,"assignedTo").set(qauser);
		meetingsSubpanel.saveAction(1);
	}

	/**
	 * Verify that assigned user field is sortable in Calls/Meetings Subpanel
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29726_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that Assigned UserName field is sortable in Calls/Meetings Subpanel
		String assignedUserName = VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("assigned_user_name"));
		callsSubpanel.getControl(assignedUserName).assertAttribute("class","sorting", true);
		meetingsSubpanel.getControl(assignedUserName).assertAttribute("class","sorting", true);

		// Sort the Assigned User Name field in Calls in Ascending Order
		callsSubpanel.sortBy("headerAssignedusername", true);

		// Verify Call Records are sorted in Ascending Order
		for (int i = 0; i < customData.size(); i++) {
			callsSubpanel.getDetailField(i+1, "assignedTo").assertEquals(customData.get(i).get("name"), true);
		}

		// Sort the Assigned User Name field in Calls in Descending Order
		callsSubpanel.sortBy("headerAssignedusername", false);

		// Verify Call Records are sorted in Descending Order
		for (int j = 0; j < customData.size(); j++) { 
			callsSubpanel.getDetailField(j+1, "assignedTo").assertEquals(customData.get(1-j).get("name"), true);
		}

		// Sort the Meeting Records in Descending Order according to Assigned UserName
		meetingsSubpanel.sortBy("headerAssignedusername", true);

		// Verify Meeting Records are sorted in Descending Order according to assigned User Name
		for (int k = 0; k < customData.size(); k++) {
			meetingsSubpanel.getDetailField(k+1, "assignedTo").assertEquals(customData.get(1-k).get("name"), true);
		}

		// Sort the Meeting Records in Ascending according to Assigned UserName
		meetingsSubpanel.sortBy("headerAssignedusername", false);

		// Verify Meeting Records are sorted in Ascending Order according to assigned User Name
		for (int l = 0; l < customData.size(); l++) {
			meetingsSubpanel.getDetailField(l+1, "assignedTo").assertEquals(customData.get(1-l).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}