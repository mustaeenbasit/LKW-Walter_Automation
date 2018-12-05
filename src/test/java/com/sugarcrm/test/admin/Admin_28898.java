package com.sugarcrm.test.admin;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28898 extends SugarTest {
	VoodooControl sugarUpdates, checkBox, checkNowButton, cancelButton, saveButton;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that clicking on cancel the sugar updates checkbox state should not be saved
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_28898_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1568
		checkBox = new VoodooControl("input", "css", ".checkbox input");
		checkNowButton = new VoodooControl("input", "css", "input[name='checknow']");
		cancelButton = new VoodooControl("input", "css", "input[title='Cancel']");
		saveButton = new VoodooControl("input", "css", "input[title='Save']");

		// Navigate to Admin -> Sugar Updates
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugarUpdates = sugar().admin.adminTools.getControl("sugarUpdates");
		sugarUpdates.click();

		// Now uncheck the 'Automatically Check For Updates' checkbox
		checkBox.click();

		// Click on 'Check Now' button and then 'Cancel'
		checkNowButton.click();
		cancelButton.click();

		// Verify that clicking on 'Cancel' the sugar updates checkbox state should not be saved
		sugarUpdates.click();
		Assert.assertTrue("Sugar Updates checkbox is not checked", checkBox.isChecked());

		// Now check the 'Automatically Check For Updates' checkbox and 'Save'
		checkBox.click();
		saveButton.click();

		// Verify that clicking on 'Save' button should save the changes for sugar updates checkbox state
		Assert.assertFalse("Sugar Updates checkbox is checked", checkBox.isChecked());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}