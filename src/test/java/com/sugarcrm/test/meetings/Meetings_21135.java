package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_21135 extends SugarTest {	
	VoodooControl meetingPanelCtrl;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.api.create();
		sugar().login();
		
		// Relate meeting with account 
		FieldSet acctData = new FieldSet();
		acctData.put("relatedToParentType", sugar().accounts.moduleNameSingular);
		acctData.put("relatedToParentName", sugar().accounts.getDefaultData().get("name"));
		myMeeting.edit(acctData);
		
		// Add related to field on basic search layout
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");		
		sugar().admin.adminTools.getControl("studio").click();
		
		// TODO: VOOD-542
		meetingPanelCtrl = new VoodooControl("a", "css" ,"#studiolink_Meetings");
		meetingPanelCtrl.click();
		new VoodooControl("a", "css" ,"td#layoutsBtn a").click();	
		new VoodooControl("a", "css" ,"td#searchBtn a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"td#FilterSearchBtn a").click();
		VoodooUtils.waitForReady();
		VoodooControl subject = new VoodooControl("li", "css" ,"td#Default li:nth-of-type(1)");
		VoodooControl related = new VoodooControl("li", "css" ,"td#Hidden li[data-name='parent_name']");
		related.dragNDrop(subject);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"#savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the search functionality of "relate to" field in basic search.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21135_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		VoodooControl searchFilter = sugar().meetings.listView.getControl("searchFilter");
		
		// Verify that nothing is filled in the Relate To search field
		searchFilter.assertEquals("", true);		
		searchFilter.set(sugar().accounts.moduleNameSingular);
		
		// Verify that search out matched meeting record
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}