package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_26989 extends SugarTest {
	String currentDate,qaUserName;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Get current date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(dt);
		currentDate = sdf.format(dt);

		// 2 meetings having 1 with default data and another with custom data
		sugar().meetings.api.create();
		FieldSet secondMeetingData = new FieldSet();
		secondMeetingData.put("name", testName);
		secondMeetingData.put("status", customData.get("status"));
		secondMeetingData.put("date_start_date", currentDate);
		secondMeetingData.put("date_end_date", currentDate);
		sugar().meetings.api.create(secondMeetingData);

		// login as admin
		sugar().login();

		// Edit team and assigned to field (proper coverage with all sort header lists)
		qaUserName = sugar().users.getQAUser().get("userName");
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("assignedTo").set(qaUserName);
		sugar().meetings.recordView.getEditField("teams").set(qaUserName);
		sugar().meetings.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that subject, status, start date, end date, user, team column is a sortable correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_26989_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		// Enable End date and Team field
		// TODO: VOOD-1375
		VoodooControl moreColCtrl = new VoodooControl("div", "css", "th.morecol div");
		moreColCtrl.click();
		new VoodooControl("button", "css", "button[data-field-toggle='date_end']").click();
		new VoodooControl("button", "css", "button[data-field-toggle='team_name']").click();
		moreColCtrl.click();

		// sort by 'subject' in descending and ascending order
		sugar().meetings.listView.sortBy("headerName", false);
		sugar().meetings.listView.verifyField(1, "name", testName);
		sugar().meetings.listView.verifyField(2, "name", sugar().meetings.getDefaultData().get("name"));

		sugar().meetings.listView.sortBy("headerName", true);
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.listView.verifyField(2, "name", testName);

		// sort by 'date start' in descending and ascending order
		sugar().meetings.listView.sortBy("headerDatestart", false);
		sugar().meetings.listView.verifyField(1, "date_start_date", sugar().meetings.getDefaultData().get("date_start_date"));
		sugar().meetings.listView.verifyField(2, "date_start_date", currentDate);

		sugar().meetings.listView.sortBy("headerDatestart", true);
		sugar().meetings.listView.verifyField(1, "date_start_date", currentDate);
		sugar().meetings.listView.verifyField(2, "date_start_date", sugar().meetings.getDefaultData().get("date_start_date"));

		// sort by 'date end' in descending and ascending order
		sugar().meetings.listView.sortBy("headerDateend", false);
		sugar().meetings.listView.verifyField(1, "date_end_date", sugar().meetings.getDefaultData().get("date_end_date"));
		sugar().meetings.listView.verifyField(2, "date_end_date", currentDate);

		sugar().meetings.listView.sortBy("headerDateend", true);
		sugar().meetings.listView.verifyField(1, "date_end_date", currentDate);
		sugar().meetings.listView.verifyField(2, "date_end_date", sugar().meetings.getDefaultData().get("date_end_date"));

		// sort by 'status' in descending and ascending order
		sugar().meetings.listView.sortBy("headerStatus", false);
		sugar().meetings.listView.verifyField(1, "status", sugar().meetings.getDefaultData().get("status"));
		sugar().meetings.listView.verifyField(2, "status", customData.get("status"));

		sugar().meetings.listView.sortBy("headerStatus", true);
		sugar().meetings.listView.verifyField(1, "status", customData.get("status"));
		sugar().meetings.listView.verifyField(2, "status", sugar().meetings.getDefaultData().get("status"));

		// sort by 'assigned to' in descending and ascending order
		sugar().meetings.listView.sortBy("headerAssignedusername", false);
		sugar().meetings.listView.verifyField(1, "assignedTo", qaUserName);
		sugar().meetings.listView.verifyField(2, "assignedTo", customData.get("admin"));

		sugar().meetings.listView.sortBy("headerAssignedusername", true);
		sugar().meetings.listView.verifyField(1, "assignedTo", customData.get("admin"));
		sugar().meetings.listView.verifyField(2, "assignedTo", qaUserName);

		// sort by 'teams' in descending and ascending order
		VoodooControl teamsFieldNeedToScroll = sugar().meetings.listView.getDetailField(1, "teams");
		teamsFieldNeedToScroll.scrollIntoViewIfNeeded(false);
		sugar().meetings.listView.sortBy("headerTeamname", false);
		sugar().meetings.listView.verifyField(1, "teams", qaUserName);
		sugar().meetings.listView.verifyField(2, "teams", customData.get("global_team"));

		teamsFieldNeedToScroll.scrollIntoViewIfNeeded(false);
		sugar().meetings.listView.sortBy("headerTeamname", true);
		sugar().meetings.listView.verifyField(1, "teams", customData.get("global_team"));
		sugar().meetings.listView.verifyField(2, "teams", qaUserName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}