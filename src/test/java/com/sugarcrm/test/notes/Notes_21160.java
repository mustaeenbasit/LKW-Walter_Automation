package com.sugarcrm.test.notes;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21160 extends SugarTest {

	public void setup() throws Exception {
		sugar().notes.api.create();
		sugar().login();
	}
	/**
	 * Verify (all) actions (and execute any 1 of the action) in action dropdown list on notes list view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21160_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Notes and select all records in the listView
		sugar().notes.navToListView();
		sugar().notes.listView.getControl("actionDropdown").assertVisible(true);
		boolean isDisabled = sugar().notes.listView.getControl("actionDropdown").isDisabled();
		Assert.assertTrue("Action dropdown is in enabled state",isDisabled);
		sugar().notes.listView.getControl("selectAllCheckbox").click();
		sugar().notes.listView.openActionDropdown();

		// Asserting the options available in Action DropDown & Executing Delete function
		sugar().notes.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().notes.listView.getControl("exportButton").assertVisible(true);

		// TODO: VOOD-689
		new VoodooControl("a","css",".fld_merge_button a").assertVisible(true);
		VoodooControl delete = sugar().tasks.listView.getControl("deleteButton");
		delete.assertVisible(true);

		// Triggering delete function
		delete.click();
		sugar().alerts.getWarning().confirmAlert();
		sugar().notes.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}