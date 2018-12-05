package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21122 extends SugarTest {

	public void setup() throws Exception {
		sugar().notes.api.create();
		sugar().login();
	}
	/**
	 * Update Assigned to field of Note with blank
	 * @throws Exception
	 */
	@Test
	public void Notes_21122_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();

		// TODO:VOOD-806
		new VoodooControl("span", "css", ".fld_assigned_user_name.edit .select2-search-choice-close").click();
		sugar().notes.recordView.save();

		// Verify that No required Field message comes
		sugar().alerts.getError().assertVisible(false);

		// Verify that assignedTo field is blank
		sugar().notes.recordView.getDetailField("relAssignedTo").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}