package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17415 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * launch the activity streams messages page
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17415_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click down arrow next to sugar cube on Navigation bar -> Click Activities link
		sugar().navbar.selectMenuItem(sugar().home, "activityStream");

		FieldSet activityStreamData = testData.get(testName).get(0);

		// Verify that it launch the activity streams messages page
		// TODO: VOOD-474
		new VoodooControl("div", "css", ".layout_Activities .fld_title div").assertEquals(activityStreamData.get("activityStream"), true);
		ActivityStream stream = new ActivityStream();
		stream.getControl("submit").assertEquals(activityStreamData.get("submit"), true);
		stream.getControl("streamInput").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}