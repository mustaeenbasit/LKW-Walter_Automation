package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_17051 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().meetings.api.create(ds);
		sugar().login();
	}

	/**
	 * Should hide close action of a meeting record in list view if the action not applicable for the record
	 * @throws Exception
	 */
	@Test
	public void Meetings_17051_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// Meeting is Cancelled
		sugar().meetings.listView.openRowActionDropdown(1);

		// TODO: VOOD-742 - close method in ActivityRecordView, similarly we required for listview
		// once fixed remove L#36 L#41 and use L#37, L#42
		VoodooControl closeCtrl = new VoodooControl("a", "css", ".fld_record-close.list a");
		closeCtrl.assertVisible(true);
		// sugar().meetings.listview.getControl("close").assertVisible(true);
		sugar().meetings.listView.getControl(String.format("dropdown%02d", 1)).click(); // to close dropdown action

		// Meeting is Held
		sugar().meetings.listView.openRowActionDropdown(2);
		closeCtrl.assertVisible(false);
		// sugar().meetings.listview.getControl("close").assertVisible(true);
		sugar().meetings.listView.getControl(String.format("dropdown%02d", 2)).click(); // to close dropdown action

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}