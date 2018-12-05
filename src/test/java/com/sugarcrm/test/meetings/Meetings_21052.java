package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21052 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().meetings.api.create(ds);
	}

	/**
	 * Verify that selected meetings are deleted by delete link under action menu.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21052_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.checkRecord(1);
		sugar().meetings.listView.checkRecord(2);
		sugar().meetings.listView.openActionDropdown();
		sugar().meetings.listView.getControl("deleteButton").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify only check records are deleted
		sugar().meetings.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar().meetings.listView.getControl("checkbox02").assertExists(false);
		sugar().meetings.listView.getControl("checkbox03").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}