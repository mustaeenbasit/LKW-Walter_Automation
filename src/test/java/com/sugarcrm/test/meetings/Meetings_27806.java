

package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27806 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		sugar().meetings.api.deleteAll();
		fs = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Edit all occurrence" action appears when switching a non-repeat type to a repeat type
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27806_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Click on repeat field and select Daily
		// TODO: VOOD-1169 Provide support for Calls and Meetings Scheduling functionality
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(fs.get("repeat_type"));
		sugar().meetings.recordView.save();

		// Verify that "Edit all occurrence" action appears when switching a non-repeat type to a repeat type
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		new VoodooControl ("li", "css",".btn-group.open ul li:nth-child(1)").assertContains("Edit All Recurrences", true);
		sugar().meetings.navToListView();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}

