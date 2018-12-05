package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27754_Quotes extends SugarTest {

	public void setup() throws Exception {
		// Creating test quote record
		sugar().quotes.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that repeatable meeting is created from sub panel for Quotes
	 * @throws Exception
	 */
	@Test
	public void Meetings_27754_Quotes_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to the quote record detail view created above
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click on the Schedule Meeting button on the Activities sub-panel
		// TODO: VOOD-1713 - Improve BWCSubpanel functionality
		VoodooControl activitiesSubpanelCtrl = new VoodooControl("span", "css", "#list_subpanel_activities .sugar_action_button .ab");
		activitiesSubpanelCtrl.scrollIntoViewIfNeeded(true); // Scroll and Click
		new VoodooControl("a", "id", "Activities_schedulemeeting_button_create_").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Select Daily, 3 Repeat Occurrence
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(customData.get("repeat_occurrence"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customData.get("repeat_count"));
		sugar().meetings.createDrawer.save();
		VoodooUtils.focusFrame("bwc-frame");
		activitiesSubpanelCtrl.waitForVisible();
		
		int count = Integer.parseInt(customData.get("repeat_count"));
		
		// Verify that 3 meetings are created and listed in the Activitie sub panel
		// TODO: VOOD-972 - Not working methods in BWC subpanels: verify, assertContains, create
		for (int i = 1; i <= count; i++) {
			new VoodooControl("a", "css", ".list.view tr:nth-child("+ ( i+2 ) + ") td:nth-child(2) a").assertContains(testName, true);
		}
		VoodooUtils.focusDefault();

		// Navigate to Meetings
		sugar().meetings.navToListView();
		
		String quoteName = sugar().quotes.getDefaultData().get("name");
		
		// Verify quote name is displayed as Related to record w.r.t each meeting  
		for (int i = 1; i <= count; i++) {
			sugar().meetings.listView.getDetailField(i, "relatedToParentName").assertEquals(quoteName, true);
		}
		
		// Open a meeting record
		sugar().meetings.listView.clickRecord(1);

		// Verify Quote name is in Related To field in the meeting record
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(quoteName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}

