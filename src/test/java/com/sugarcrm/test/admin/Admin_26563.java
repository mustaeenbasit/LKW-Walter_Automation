package com.sugarcrm.test.admin;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26563 extends SugarTest {
	public void setup() throws Exception {	
		// Create product type record
		sugar().productTypes.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify that Product Type record can be deleted successfully. 
	 * @throws Exception
	 */
	@Test
	public void Admin_26563_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin panel -> Product Type module
		sugar().productTypes.navToListView();
		VoodooUtils.focusDefault();

		// Click delete button for one of the records
		sugar().productTypes.listView.deleteRecord(1);
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Verify that the Product Type record is deleted successfully 
		Assert.assertTrue("Product Type record is not deleted successfully", sugar().productTypes.listView.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}