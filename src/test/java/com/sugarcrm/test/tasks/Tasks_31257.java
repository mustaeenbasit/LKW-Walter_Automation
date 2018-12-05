package com.sugarcrm.test.tasks;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;


public class Tasks_31257 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that no error message appears when searching any record in "Related to" field while merging.
	 *
	 * @throws Exception
	 */
	@Test
	public void Tasks_31257_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Create two tasks, "Task1" with 'Related to' field by selecting any valid "Account" and
		// "Task2" with 'Related to' field by selecting any valid "Contact".
		sugar().tasks.create(ds);

		// Select 'Task1' first and then select 'Task2'.
		sugar().tasks.listView.checkRecord(2);
		sugar().tasks.listView.checkRecord(1);
		sugar().tasks.listView.openActionDropdown();
		// Click Merge Option from the Action Dropdown list
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-721
		// Ensure that primary record is the Task related to Account i.e. Task1.
		new VoodooControl("div", "css", ".fld_name.edit input").assertContains(ds.get(0).get("subject"), true);

		// select radio button of 'Related To' field of secondary record i.e. Task2.
		new VoodooControl("input", "css", "div:nth-child(2) > div.box > .merge-row-field-parent  input").click();

		// Click to change Contact of primary record now by entering any character for search.
		new VoodooControl("span", "css", ".flex-relate-record .select2-arrow").click();
		new VoodooControl("input", "css", "#select2-drop input").set(ds.get(1).get("relRelatedToParent"));

		// User must see list of contacts as per the character(s) entered by the user
		new VoodooControl("div", "css", ".select2-result-label[role='option']").assertContains(ds.get(1).get("relRelatedToParent"), true);

		// Select the record
		new VoodooControl("span", "css", ".select2-match").click();

		// Verify that no error message appears on the page
		sugar().alerts.getError().assertVisible(false);

		// merge both the records
		new VoodooControl("a", "css", ".merge-duplicates-headerpane.fld_save_button  [name='save_button']").click();

		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify that second task get deleted after merge
		sugar().tasks.listView.getDetailField(1, "subject").assertContains(ds.get(1).get("subject"), false);
		// Verify that merged task is related to correct contact record
		sugar().tasks.listView.getDetailField(1, "relRelatedToParent").assertContains(ds.get(1).get("relRelatedToParent"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}