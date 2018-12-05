package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21061 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().meetings.api.create(ds);
		sugar().login();
	}

	/**
	 * Verify that corresponding record is displayed in meeting detail view when clicking the pagination control link in meeting detail view.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21061_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.getDetailField("name").assertEquals(ds.get(1).get("name"), true);

		// Verify next record
		sugar().meetings.recordView.gotoNextRecord();
		sugar().meetings.recordView.getDetailField("name").assertEquals(ds.get(0).get("name"), true);

		// Verify prev record
		sugar().meetings.recordView.gotoPreviousRecord();
		sugar().meetings.recordView.getDetailField("name").assertEquals(ds.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}