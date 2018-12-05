package com.sugarcrm.test.bugs;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18605 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.bugs.api.create();
	}

	/** 
	 * New action dropdown list in bug tracker list view page
	 * @throws Exception
	 */
	@Test
	public void Bugs_18605_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs list view
		sugar.bugs.navToListView();

		// Click checkbox on one bug record in list view
		sugar.bugs.listView.checkRecord(1);

		// Open action drop down list in list view
		sugar.bugs.listView.openActionDropdown();

		// Verify the following actions in the drop down list 
		sugar.bugs.listView.getControl("massUpdateButton").assertExists(true);  // Mass Update
		sugar.bugs.listView.getControl("deleteButton").assertExists(true);  // Delete
		sugar.bugs.listView.getControl("exportButton").assertExists(true);  // Export
		
		// TODO: VOOD-721
		new VoodooControl("input","css","ul.dropdown-menu:nth-child(3) li:nth-child(2) a").assertExists(true); // Merge	
		
		// Trigger the delete action to delete the checked record in list view
		sugar.bugs.listView.delete();
		sugar.alerts.getWarning().confirmAlert();

		// Verify the bug record is deleted
		sugar.bugs.navToListView();
		Assert.assertEquals("List View rows should be zero after deleting record", 0, sugar.bugs.listView.countRows());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}