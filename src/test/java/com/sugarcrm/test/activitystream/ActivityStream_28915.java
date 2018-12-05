package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_28915 extends SugarTest {
	public void setup() throws Exception {
		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify that 'Activity stream' should display as its page title.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_28915_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet activityStreamData = testData.get(testName).get(0);

		// Verify that Home page will display
		sugar().home.dashboard.getControl("dashboardTitle").assertEquals(activityStreamData.get("dashboardTitle"), true);

		// Click on Activity stream from sugar cube icon.
		sugar().navbar.selectMenuItem(sugar().home, "activityStream");

		// Verify that the page title should be there with respect to respective page(Activity stream).
		// TODO: VOOD-474
		new VoodooControl("div", "css", ".layout_Activities .fld_title div").assertEquals(activityStreamData.get("activityStreamTitle"), true);

		// Also verifying it launch the activity streams messages page
		ActivityStream stream = new ActivityStream();
		stream.getControl("submit").assertVisible(true);
		stream.getControl("streamInput").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}