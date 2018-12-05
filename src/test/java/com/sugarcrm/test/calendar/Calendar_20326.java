package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20326 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
	}

	/**
	 * Schedule call_Verify that a call scheduling by edit view is canceled
	 *
	 * @throws Exception
	 */
	@Test
	public void Calendar_20326_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleCall");

		// enter call data and click Cancel button
		sugar().calls.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().calls.createDrawer.cancel();

		// verify call record not exists in Calls list view
		sugar().calls.navToListView();
		sugar().calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}