package com.sugarcrm.test.leads;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_29006 extends SugarTest {
	ArrayList<Record> meetingRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		// Get Today's Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		date = c1.getTime();
		String todaysDate = sdf.format(date);
		FieldSet meetingData = new FieldSet();
		for(int i = 1; i <=2; i++) {
			meetingData.put("date_start_date", todaysDate);
			meetingData.put("name", testName + "_" + i);
			meetingRecords.add(sugar().meetings.api.create(meetingData));
			meetingData.clear();
		}

		// Get Future Date
		for(int i = 1; i <= 2; i++) {
			c1.add(Calendar.DATE, (2*i));
			date = c1.getTime();
			String futureDate = sdf.format(date);
			meetingData.put("date_start_date", futureDate);
			meetingData.put("name", testName.substring(0, 3) + "_" + i);
			meetingRecords.add(sugar().meetings.api.create(meetingData));
			meetingData.clear();
		}

		// Create a lead record
		sugar().leads.api.create();
		sugar().login();

		// Schedule Meeting record for a Lead
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		StandardSubpanel meetingsSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.linkExistingRecords(meetingRecords);
	}

	/**
	 * Verify that Loading.. is not showing at Planned Activities dashlet of record view of Leads Module
	 * @throws Exception
	 */
	@Test
	public void Leads_29006_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashboard = testData.get(testName).get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Add Planned Activities dashlet to RHS of an Leads record
		VoodooControl dashboardTitle = sugar().leads.dashboard.getControl("dashboard");
		if(!dashboardTitle.queryContains(dashboard.get("dashboardname"), true))
			sugar().leads.dashboard.chooseDashboard(dashboard.get("dashboardname"));

		// TODO:VOOD-960
		VoodooControl today_Meeting = new VoodooControl("button", "css", ".dashlet-group:nth-child(1) button:nth-child(1)");
		today_Meeting.click();

		// Verifying Loading message doesn't appears
		sugar().alerts.getProcess().assertVisible(false);

		// Verifying related Meetings under today's tab
		VoodooControl meetings = new VoodooControl("ul", "css", ".dashlet-unordered-list .tab-content ul");
		for(int i = 1; i <= 2; i++){
			// Using assertContains as the order of the meetings listed is changing every time.
			meetings.assertContains(testName + "_" + i, true);
		}

		// TODO:VOOD-960
		VoodooControl future_Meeting = new VoodooControl("button", "css", ".dashlet-group:nth-child(1) button:nth-child(2)");
		future_Meeting.click();

		// Verifying Loading message doesn't appears
		sugar().alerts.getProcess().assertVisible(false);

		// Verifying related Meetings appears under Future's tab
		for(int i = 1; i <= 2; i++){
			meetings.assertContains(testName.substring(0,3) + "_" + i, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}