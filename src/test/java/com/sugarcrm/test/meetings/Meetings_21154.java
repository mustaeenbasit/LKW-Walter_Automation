package com.sugarcrm.test.meetings;

import static org.junit.Assert.*;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21154 extends SugarTest {	
	VoodooControl meetingPanelCtrl, filterInputCtrl;
	DataSource meetingDS;
	
	public void setup() throws Exception {
		meetingDS = testData.get(testName);
		FieldSet customFS = testData.get(testName+"_data").get(0);
		sugar().meetings.api.create(meetingDS);
		sugar().login();
		
		// Add related to field on basic search layout
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");		
		sugar().admin.adminTools.getControl("studio").click();
		
		// TODO: VOOD-938 -Need library support for studio subpanel
		meetingPanelCtrl = new VoodooControl("a", "css" ,"#studiolink_Meetings");
		meetingPanelCtrl.click();
		new VoodooControl("a", "css" ,"td#layoutsBtn a").click();	
		new VoodooControl("a", "css" ,"td#searchBtn a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"td#FilterSearchBtn a").click();
		VoodooUtils.waitForReady();
		
		// Verify that start date already in Default section
		new VoodooControl("li", "css" ,"td#Default [data-name='date_start']").assertExists(true);
		VoodooUtils.focusDefault();
		
		// Create filter
		sugar().meetings.navToListView();
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();

		// TODO: VOOD-1462
		new VoodooSelect("a", "css", "[data-filter='field'] [data-voodoo-type='field'] a").set(customFS.get("type"));
		new VoodooSelect("a", "css", "[data-filter='operator'] [data-voodoo-type='field'] a").set(customFS.get("operator"));
		filterInputCtrl = new VoodooControl("input", "css", ".controls.span6 .inherit-width");
		filterInputCtrl.set(customFS.get("filterName"));
		VoodooUtils.waitForReady();
	}

	/**
	 * Search meeting by "Start Date" field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21154_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Total size of DataSource(meetingDS) & total number of rows o
		int totalNoOfMeetingsInList = sugar().meetings.listView.countRows();
		int totalNoOfMeetings = meetingDS.size();
		
		// Asserting that all the 3 cases got associated with the account
		assertTrue("No meetings found in the listView",totalNoOfMeetingsInList == totalNoOfMeetings);
		
		// Verify record with valid date search
		VoodooControl dateCtrl = new VoodooControl("input", "css", ".fld_date_start.detail input");
		dateCtrl.set(meetingDS.get(1).get("date_start_date"));
		filterInputCtrl.click(); // to change focus from date field
		VoodooUtils.waitForReady();
		sugar().meetings.listView.verifyField(1, "name", meetingDS.get(1).get("name"));
		
		// Search with second date
		dateCtrl.set(meetingDS.get(0).get("date_start_date"));
		filterInputCtrl.click(); // to change focus from date field
		VoodooUtils.waitForReady();
		sugar().meetings.listView.verifyField(1, "name", meetingDS.get(0).get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}