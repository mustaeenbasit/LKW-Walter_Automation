package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_27274 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Verify that Start Date and End Date are editable on Calls inline edit on the record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27274_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dateFS = testData.get(testName).get(0);

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.getDetailField("date_start_date").hover();

		// TODO: VOOD-854
		// inline edit in record-view
		new VoodooControl("i", "css", "span[data-name='duration'] .fa.fa-pencil").click();
		sugar.calls.recordView.getEditField("date_start_date").set(dateFS.get("date_custom"));
		sugar.calls.recordView.getEditField("date_start_time").set(dateFS.get("date_start_time"));
		sugar.calls.recordView.getEditField("date_end_date").set(dateFS.get("date_custom"));
		sugar.calls.recordView.getEditField("date_end_time").set(dateFS.get("date_end_time"));
		sugar.calls.recordView.save();
		if (sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		// Verify new Start Date, End date and Duration is saved
		String dateTimeStr = String.format("%s %s - %s %s", dateFS.get("date_custom"), dateFS.get("date_start_time"), dateFS.get("date_end_time"), dateFS.get("duration"));
		sugar.calls.recordView.getDetailField("date_start_date").assertContains(dateTimeStr, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}