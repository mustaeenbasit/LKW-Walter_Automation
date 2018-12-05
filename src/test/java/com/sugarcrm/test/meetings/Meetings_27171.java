package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27171 extends SugarTest {
	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Help Dashboard in Meetings listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27171_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		
		// TODO: VOOD-963
		// Confirm that "Help Dashboard" is visible
		// Also see VOOD-1248 - Unexpected behaviour of the test script (Dashboard does not display when script execute)
		new VoodooControl("a", "css", "span.fld_name.detail a").waitForVisible();
		new VoodooControl("a", "css", "span.fld_name.detail a").assertContains("Help Dashboard", true);
		
		// Confirm that "List View Help" Dashlet is visible
		new VoodooControl("div", "css", 
				"div.dashboard-pane li.row-fluid.sortable ul.dashlet-cell.rows.row-fluid > li:nth-child(1) div.dashlet-header")
				.assertVisible(true);

		// Confirm that "List View Help" Dashlet contains appropriate title
		new VoodooControl("div", "css", 
				"div.dashboard-pane li.row-fluid.sortable ul.dashlet-cell.rows.row-fluid > li:nth-child(1) div.dashlet-header")
				.assertContains("List View Help", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}