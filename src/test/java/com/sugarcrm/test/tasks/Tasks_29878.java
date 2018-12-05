package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_29878  extends SugarTest {

	public void setup() throws Exception {
		sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Verify that while merging records Tags field of primary does not contain its default value
	 * @throws Exception
	 */
	@Test
	public void Tasks_29878_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Task having Tag field filled
		sugar().tasks.navToListView();
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		FieldSet tagData = testData.get(testName).get(0);
		sugar().tasks.createDrawer.getEditField("tags").set(tagData.get("tag"));
		sugar().tasks.createDrawer.save();

		// Sort the records so that Task record having tag appears below, so that is appears in non-primary record
		sugar().tasks.listView.sortBy("headerName", true);

		// Select all the records
		sugar().tasks.listView.toggleSelectAll();
		sugar().tasks.listView.openActionDropdown();

		// Click Merge Option from the Action Dropdown list
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		VoodooUtils.waitForReady();

		// Select Radio button 'Tag' field (test) in non-primary column.
		// TODO: VOOD-721
		new VoodooControl("input", "css", ".col:nth-child(2) [name='copy_tag']").click();

		// Verify Tag (test) of non primary column is getting displayed in primary column in Tags Field. 
		VoodooControl primaryTag = new VoodooControl("ul", "css", ".select2-choices-pills-close ul");
		primaryTag.assertEquals(tagData.get("tag"),true);

		// Click Tags field radio button on Primary Record.
		new VoodooControl("input", "css", ".primary-edit-mode [name='copy_tag']").click();

		// Verify No tags is getting displayed in Tags field in Primary Record
		primaryTag.assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}