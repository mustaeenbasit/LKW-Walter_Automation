package com.sugarcrm.test.leads;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_29757 extends SugarTest {
	DataSource meetingRecords;
	
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
				
		// Today date
		String todayDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		
		// Future (tomorrow) date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.DATE, 9);
		Date td = c.getTime();
		String dateAfter9Days = sdf.format(td);
		
		// Create 2 meetings records with today and future date
		meetingRecords = testData.get(testName);
		ArrayList<Record> myMeetings;
		meetingRecords.get(0).put("date_start_date", todayDate);
		meetingRecords.get(1).put("date_start_date", dateAfter9Days);
		myMeetings = sugar().meetings.api.create(meetingRecords);
		
		// Linking the Meetings record with the Leads record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecords(myMeetings);
	}

	/**
	 * Verify that Loading.. is not showing at Planned Activities dashlet of record view of Leads Module
	 * @throws Exception
	 */
	@Test
	public void Leads_29757_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Navigate to Leads recordview
		sugar().leads.navToListView();	
		sugar().leads.listView.clickRecord(1);
		
		// Always better to check default dashboard availability
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains("My Dashboard", true))
			sugar().cases.dashboard.chooseDashboard("My Dashboard");
		
		// TODO: VOOD-1305
		VoodooControl todayCtrl = new VoodooControl("button", "css", "[data-voodoo-name='planned-activities'] [value='today']");
		VoodooControl futureCtrl = new VoodooControl("button", "css", "[data-voodoo-name='planned-activities'] [value='future']");
		
		// Click Today button
		todayCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that related record displayed at Meetings tab instead of Loading...
		VoodooControl meetingCtrl = new VoodooControl("p", "css",".tab-pane.active [data-action='pagination-body'] p");
		meetingCtrl.assertContains(meetingRecords.get(0).get("name"), true);
		
		// Click Future button
		futureCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that related record displayed at Meetings tab instead of Loading...
		meetingCtrl.assertContains(meetingRecords.get(1).get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}