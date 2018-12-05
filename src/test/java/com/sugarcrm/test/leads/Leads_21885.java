package com.sugarcrm.test.leads;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21885 extends SugarTest {
	VoodooControl dashboardTitle;

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Schedule Call_Verify that call with "Scheduled" status can be scheduled for a lead in "Planned Activities" dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_21885_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Leads module and select 'My Dashboard'
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Toggle to My Dashboard
		dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if (!dashboardTitle.getText().contains("My Dashboard"))
			sugar().dashboard.chooseDashboard("My Dashboard");

		// Click "Log Call" in a Planned Activities dashlet
		// TODO: VOOD-1305
		VoodooControl plusIcon = new VoodooControl("a", "css", ".dashlet-toolbar .btn-invisible");
		plusIcon.click();
		VoodooControl logCall = new VoodooControl("a", "css",".dropdown-menu li:nth-child(2) [data-dashletaction='createRecord']");
		logCall.click();

		//Create Call for Today's Date
		sugar().calls.createDrawer.getEditField("name").set(testName);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance(); 
		Date date = cal.getTime();
		String todaysDateTime = sdf.format(date);
		sugar().calls.createDrawer.getEditField("date_start_date").set(todaysDateTime);
		sugar().calls.createDrawer.save();

		// Create Call for Tomorrow's Date
		plusIcon.click();
		logCall.click();
		sugar().calls.createDrawer.getEditField("name").set(testName+"_1");
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		String tomorrowsDateTime = sdf.format(date);
		sugar().calls.createDrawer.getEditField("date_start_date").set(tomorrowsDateTime);
		sugar().calls.createDrawer.save();

		// Verify one call is Scheduled for today in Planned Activity dashlet
		VoodooControl callsTab = new VoodooControl("span", "css", ".dashlet-tabs-row :nth-child(2)");
		callsTab.click();
		int recCount= Integer.parseInt(new VoodooControl("span", "css", ".dashlet-tabs.tab2 :nth-child(2) .count").getText().trim());
		Assert.assertTrue("Calls record count equals zero for Today" , recCount == 1);

		// Verifying the record name of today's call in Planned Activity dashlet
		VoodooControl recordName = new VoodooControl("a", "css", ".tab-pane.active p a:nth-child(2)");
		recordName.assertEquals(testName, true);

		// Verify one call is Scheduled for tomorrow in Planned Activity dashlet
		new VoodooControl("span", "css", ".btn-group.dashlet-group :nth-child(2)").click();
		recCount= Integer.parseInt(new VoodooControl("span", "css", ".dashlet-tabs.tab2 :nth-child(2) .count").getText().trim());
		Assert.assertTrue("Calls record count equals zero for Tomorrow" , recCount == 1);
		new VoodooControl("a", "css", ".dashlet-tabs-row a").click();
		callsTab.click();

		// Verifying the record name of tomorrow's call in Planned Activity dashlet
		recordName.waitForVisible();
		recordName.assertEquals(testName+"_1", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}