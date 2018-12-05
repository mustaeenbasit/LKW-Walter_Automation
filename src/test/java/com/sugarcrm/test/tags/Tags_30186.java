package com.sugarcrm.test.tags;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_30186 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify no user in "Assigned to" field is displayed while set this field blank during tag creation
	 * @throws Exception
	 */
	@Test
	public void Tags_30186_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to tags module
		sugar().tags.navToListView();
		sugar().tags.listView.create();
		sugar().tags.createDrawer.getEditField("name").set(testName);

		// Click on "X"/close icon button in "Assigned to" drop-down control
		// TODO: VOOD-629
		new VoodooControl("abbr", "css", ".fld_assigned_user_name .select2-search-choice-close").click();

		// Save createDrawer
		sugar().tags.createDrawer.save();

		// Verify that No user is displayed in "Assigned to" field.
		// TODO: VOOD-874
		new VoodooControl("span", "css", ".list.fld_assigned_user_name").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}