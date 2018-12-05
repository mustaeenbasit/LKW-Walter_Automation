package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29142 extends SugarTest {	
	DataSource meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName);
		sugar().meetings.api.create(meetingData);
		sugar().login();
	}

	/**
	 * Verify that Searching Meeting invitees should not display other meetings
	 * @throws Exception
	 */
	@Test
	public void Meetings_29142_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigating to Meetings module and selecting the meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.setSearchString(meetingData.get(0).get("name"));
		sugar().meetings.listView.clickRecord(1);
		
		// Putting the meeting record in Edit View
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.clickAddInvitee();
		
		// Inputting the character 'Q' in add invitees search box to search for invitees
		VoodooSelect inviteeSearchSelect = (VoodooSelect)sugar().meetings.recordView.getControl("searchInvitee");
		inviteeSearchSelect.selectWidget.getControl("searchBox")
				.set(Character.toString(meetingData.get(1).get("name").charAt(0)));
		VoodooUtils.waitForReady();
		
		// TODO VOOD-629 :  Add support for accessing and manipulating individual components of a VoodooSelect.
		// Verify that the other meeting is not displayed in the result box
		new VoodooControl("ul", "css", ".select2-drop-active ul[role='listbox']").
				assertContains(meetingData.get(1).get("name"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}