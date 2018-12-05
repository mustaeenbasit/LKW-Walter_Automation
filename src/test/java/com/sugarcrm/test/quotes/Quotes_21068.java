package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_21068 extends SugarTest {
	FieldSet multiPurposeData, multiPurposeFS;

	public void setup() throws Exception {
		multiPurposeFS = new FieldSet();
		multiPurposeData = testData.get(testName).get(0);
		sugar().quotes.api.create();

		// Login to sugar
		sugar().login();

		// Navigate to Admin -> System Settings -> Sub panels Items per page "2".
		multiPurposeFS.put("maxEntriesPerSubPanel", multiPurposeData.get("maxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(multiPurposeFS);
		multiPurposeFS.clear();

		// Navigate to Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		
		// TODO: VOOD-1000
		// Controls
		VoodooControl createTasks = new VoodooControl("a", "id", "Activities_createtask_button_create_");

		// Create 5 Tasks in the Activities Subpanel of Quotes
		for(int i = 0; i < 5; i++ ) {
			VoodooUtils.focusFrame("bwc-frame");
			createTasks.click();
			VoodooUtils.focusDefault();
			sugar().tasks.createDrawer.getEditField("subject").set(testName + "_" + i);
			sugar().tasks.createDrawer.save();
		}
	}

	/**
	 * View call_Verify that the list view for each sub-panel in "Call Detail View" page can be paginated.
	 * @throws Exception
	 */
	@Test
	public void Quotes_21068_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1000
		// Activities sub-panel controls
		VoodooControl activitiesCtrl = new VoodooControl("div", "css", "#list_subpanel_activities");
		VoodooControl activitiesPageNumberCtrl = new VoodooControl("span", "css", activitiesCtrl.getHookString()+ " .pageNumbers");

		// Activity sub-panel Next, Previous, End and Start button controls
		VoodooControl activitiesNextBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl activitiesPreviousBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl activitiesEndBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl activitiesStartBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// Click "Next" button on the top right corner of Activities sub-panel.
		activitiesNextBtnCtrl.click();

		// Verify the next Activities records list view comparing to current one is displayed.
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("nextPageValue"), true);

		//  Click "Previous" button on the top right corner of Activities sub-panel.
		activitiesPreviousBtnCtrl.click();

		// Verify the previous Activities records list view comparing to current one is displayed.
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);

		// Click "End" button on the top right corner of Activities sub-panel.
		activitiesEndBtnCtrl.click();

		// Verify the last Activities records list view is displayed.
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("endPageValue"), true);

		// Click "Start" button on the top right corner of Activities sub-panel.
		activitiesStartBtnCtrl.click();

		// Verify the first Activities records list view is displayed.
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}