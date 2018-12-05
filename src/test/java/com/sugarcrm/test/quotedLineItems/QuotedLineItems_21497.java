package com.sugarcrm.test.quotedLineItems;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_21497 extends SugarTest {

	public void setup() throws Exception {
		for (int i = 0 ; i < 3 ; i++){
			sugar().quotedLineItems.api.create();
		}

		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * New action dropdown list in Quote Line Items list view page
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_21497_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotedLineItems.navToListView();
		// Verify action dropdown is disabled when no record is selected
		Assert.assertTrue(sugar().quotedLineItems.listView.getControl("actionDropdown").isDisabled());

		// Select one or more records
		sugar().quotedLineItems.listView.toggleSelectAll();
		// Click the down arrow action
		sugar().quotedLineItems.listView.openActionDropdown();
		// Verify dropdown list has all the actions: Mass Update, Recalculate Values, Merge, Delete, Export
		sugar().quotedLineItems.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().quotedLineItems.listView.getControl("deleteButton").assertVisible(true);
		sugar().quotedLineItems.listView.getControl("exportButton").assertVisible(true);
		// Merge
		// TODO: VOOD-689 Create a Lib for Merge
		new VoodooControl("a", "css", ".fld_merge_button.list a").assertVisible(true);
		// Recalculate Values
		new VoodooControl("a", "css", "[name='calc_field_button']").assertVisible(true);

		// Click on action "Delete
		sugar().quotedLineItems.listView.delete();
		sugar().alerts.getWarning().confirmAlert();
		// Verify "Delete" action is triggered
		sugar().quotedLineItems.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}