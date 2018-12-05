package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19741 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().meetings.api.create(ds);
		sugar().login();
	}

	/**
	 * Verify that meeting status changed to "Held" automatically when clicking "Close" icon for a meeting record in "Meeting List" view
	 * @throws Exception
	 */
	@Test
	public void Meetings_19741_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// Verify meeting status is Scheduled/planned
		// TODO: VOOD-1217 - once fixed below lines will work
		//sugar().meetings.listView.verifyField(1, "status", sugar().meetings.getDefaultData().get("status"));
		//sugar().meetings.listView.verifyField(2, "status", sugar().meetings.getDefaultData().get("status"));

		VoodooControl firstRecord = new VoodooControl("span", "css", ".table tr.single:nth-of-type(1) .fld_status.list span");
		VoodooControl secondRecord = new VoodooControl("span", "css", ".table tr.single:nth-of-type(2) .fld_status.list span");
		firstRecord.assertEquals(sugar().meetings.getDefaultData().get("status"), true);
		secondRecord.assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		sugar().meetings.listView.openRowActionDropdown(1);

		// TODO: VOOD-742 - close method in ActivityRecordView, similarly we required for listview
		// sugar().meetings.listview.close();
		new VoodooControl("a", "css", ".fld_record-close.list a").click();

		// TODO: Possibly update timing to wait for status change in the Record to occur
		if(sugar().alerts.getSuccess().getControl("closeAlert").queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// TODO: VOOD-1217 - once fixed below lines will work
		//sugar().meetings.listView.verifyField(1, "status", "Held");
		//sugar().meetings.listView.verifyField(2, "status", sugar().meetings.getDefaultData().get("status"));
		firstRecord.assertEquals(ds.get(1).get("name"), true);
		secondRecord.assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// Verify on record view
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.getDetailField("name").assertEquals(ds.get(1).get("name"), true);
		sugar().meetings.recordView.getDetailField("status").assertEquals(ds.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}