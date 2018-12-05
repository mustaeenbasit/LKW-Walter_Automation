package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21130 extends SugarTest {
	public void setup() throws Exception {
		FieldSet meetingName = new FieldSet();

		// Data source is not used as required difference in record creation date time
		for(int i = 5; i > 0; i--) {
			meetingName.put("name", testName + "_" + i);
			sugar().meetings.api.create(meetingName);
			if (i != 1){

				// TODO: VOOD-1450
				VoodooUtils.pause(30000);
			}
		}
		sugar().login();
	}
	/**
	 * Verify Meetings list view is sorted by "Date Created" by default
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_21130_execute() throws Exception {
		// Go to Meetings list view.
		sugar().meetings.navToListView();

		//  Verify "Date Created" is in list view by default.
		sugar().meetings.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// TODO: VOOD-1473 :Unable to verify "Date Created" is the farthest to the right in list view
		// List view is sorted by "Date Created" by default, in descending order.
		for(int i = 1; i <= 5; i++) {
			sugar().cases.listView.verifyField(i, "name", testName + "_" + i);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}