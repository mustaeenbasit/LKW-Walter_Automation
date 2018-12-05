package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29799 extends SugarTest {
	public void setup() throws Exception {
		// Creating two records of meetings
		sugar().meetings.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().meetings.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify that 'Merge' option is not available in action drop-down for Meetings module
	 * @throws Exception
	 */
	@Test
	public void Meetings_29799_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Meetings list view and OpenActionDropdown
		sugar().meetings.navToListView();
		sugar().meetings.listView.toggleSelectAll();
		sugar().meetings.listView.openActionDropdown();

		// Asserting the existence of Merge option in Action dropdown on Meetings list view 
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list").assertVisible(false);
		sugar().meetings.listView.getControl("actionDropdown").click(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}