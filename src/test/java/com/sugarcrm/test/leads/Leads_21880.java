package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21880 extends SugarTest {
	VoodooControl dashboardTitle;
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Schedule Meeting_Verify that the meeting is not scheduled for lead when using "Cancel" function
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_21880_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Leads module and select 'My Dashboard'
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Toggle to My Dashboard
		dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if (!dashboardTitle.getText().contains("My Dashboard"))
			sugar().dashboard.chooseDashboard("My Dashboard");

		// Click "Schedule Meeting" in a Planned Activities tab
		// TODO: VOOD-1305: Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		// TODO: VOOD-960: Dashlet selection
		new VoodooControl("a", "css", ".dashlet-toolbar .btn-invisible").click();
		new VoodooControl("a", "css",".dropdown-menu li:nth-child(1) [data-dashletaction='createRecord']").click();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.cancel();

		// Verifying record count is '0' for meeting in Planned Activity dashlet
		// TODO: VOOD-1305: Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		int recCount= Integer.parseInt(new VoodooControl("span", "css", ".dashlet-tab.active .count").getText().trim());
		Assert.assertTrue("Meeting record count is not zero in Planned Activities dashlet header" , recCount == 0);

		// Verifying 'No data available' is shown for meeting in Planned Activity dashlet
		// TODO: VOOD-1305: Dashlet: Planned Activities - Need lib support for RHS Planned Activities Dashlets
		String dashletMeassge = new VoodooControl("div", "css", ".tab-pane.active div").getText().trim();
		Assert.assertTrue("Meeting record is present in Planned Activities dashlet", dashletMeassge.equalsIgnoreCase("No data available."));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}