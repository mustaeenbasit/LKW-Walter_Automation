package com.sugarcrm.test.calls;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class Calls_27698 extends SugarTest {
	FieldSet customData = new FieldSet();
	DateTime today = new DateTime();
	String oppName = "";
	
	public void setup() throws Exception {

		// Initialize test data sets, variables
		customData = testData.get(testName).get(0);
		FieldSet qaUser = sugar().users.getQAUser(); 
		oppName = sugar().opportunities.getDefaultData().get("name");
		
		// Initialize records
		sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		CallRecord myCall = (CallRecord) sugar().calls.api.create();
		
		// Login as qauser
		sugar().login(qaUser);

		// Update Call: Start date = today, assigned user = qaUser, related to default opp
		today = DateTime.now();
		customData.put("date_start_date",today.toString("MM/dd/yyyy"));
		customData.put("assignedTo",qaUser.get("userName"));
		customData.put("relatedToParentType",sugar().opportunities.moduleNameSingular);
		customData.put("relatedToParentName",oppName);
		myCall.edit(customData);

		// Update Guests fields, add Contact as invitee.
		// TODO: VOOD-2230 - Method name typos
		myCall.editAllReocurrenses();
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(sugar().contacts.getDefaultData().get("firstName"));
		// Update Guests fields, add Lead as invitee.
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(sugar().leads.getDefaultData().get("firstName"));		
		
		// Save call
		sugar().calls.recordView.save();
	}

	/**
	 * Verify that recurring calls appear in the Calendar
	 * @throws Exception
	 */
	@Test
	public void Calls_27698_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify 3 Call are created in list view 
		sugar().calls.navToListView();
		// Sort by start date ascending to get parent call on top
		sugar().calls.listView.sortBy("headerDatestart", true);

		// Verify List items properties
		int rowsCount = Integer.parseInt(customData.get("repeatOccur"));
		Assert.assertTrue("Number of calls listed is not 3", sugar().calls.listView.countRows() == rowsCount);
		sugar().calls.listView.verifyField(1, "date_start_date", today.toString("MM/dd/yyyy"));
		sugar().calls.listView.verifyField(2, "date_start_date", today.plusWeeks(1).toString("MM/dd/yyyy"));
		sugar().calls.listView.verifyField(3, "date_start_date", today.plusWeeks(2).toString("MM/dd/yyyy"));

		// Navigate to Calendar Module.
		sugar().navbar.navToModule("Calendar");
		
		// Initialize variables, controls 
		FieldSet callsData = sugar().calls.getDefaultData();
		// TODO: VOOD-863 - Lib support for Calendar module		
		VoodooControl infoContent = new VoodooControl("div", "css", ".ui-dialog-content");
		VoodooControl nextWeekButton = new VoodooControl("a", "css", ".monthHeader div:nth-child(3) a");
		VoodooControl callInfoButton = new VoodooControl("div", "css", "#cal-grid > div:nth-child(2) .act_item.call_item .adicon");
		
		// Verify all 3 calls
		for (int i = 0; i < rowsCount; i++) {
			// Switch focus to default and then back to bwc frame
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			
			// Open Call Info tooltip for call visible in current week
			callInfoButton.click();

			// Verify Call properties - related Opp, Call name, Call status, Call time, Call date
			infoContent.assertContains(oppName, true);
			infoContent.assertContains(callsData.get("name"), true);
			infoContent.assertContains(callsData.get("status"), true);
			infoContent.assertContains(callsData.get("date_start_time"), true);
			infoContent.assertContains(today.plusWeeks(i).toString("MM/dd/yyyy"), true);

			// Navigate to Next Week in calendar
			nextWeekButton.click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}