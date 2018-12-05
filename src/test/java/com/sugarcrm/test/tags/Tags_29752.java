package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_29752 extends SugarTest {

	public void setup() throws Exception {
		// Tag record exist.
		sugar().tags.api.create();
		sugar().login();

		// Add "Assigned To" field
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);
		sugar().tags.recordView.edit();
		sugar().tags.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().tags.recordView.save();
	}

	/**
	 * Tags:Verify user is able to edit "Assigned to" field in tags module.
	 * @throws Exception
	 */
	@Test
	public void Tags_29752_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit Tag record.
		sugar().tags.recordView.edit();

		// Make the "Assigned To" field blank.
		// TODO: VOOD-629, VOOD-1891
		new VoodooControl("abbr", "css", ".fld_assigned_user_name.edit .select2-search-choice-close").click();

		// Save
		sugar().tags.recordView.save();

		// Verify Assigned to field should be set to blank
		sugar().tags.recordView.getDetailField("relAssignedTo").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}