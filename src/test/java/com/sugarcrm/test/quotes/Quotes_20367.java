package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_20367 extends SugarTest {
	public void setup() throws Exception {
		sugar().quotes.api.create();

		// Login
		sugar().login();
	}

	/**
	 * Verify that meeting record is displayed in "Activities" sub-panel of "Quote" detail view page when scheduling a meeting.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_20367_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Quote detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click on the Schedule Meeting button on the Activities sub-panel
		// TODO: VOOD-826 and VOOD-1713
		VoodooControl activitiesSubpanelCtrl = new VoodooControl("span", "css", "#list_subpanel_activities .sugar_action_button .ab");
		activitiesSubpanelCtrl.scrollIntoViewIfNeeded(true); // Scroll and Click
		new VoodooControl("a", "id", "Activities_schedulemeeting_button_create_").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Fill in all required fields and click the Save button
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the scheduled meeting record is displayed in "Activities" sub-panel
		// TODO: VOOD-826 and VOOD-1713
		new VoodooControl("a", "css", "#list_subpanel_activities .oddListRowS1 td:nth-child(2) a").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}