package com.sugarcrm.test.calls;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21071 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Export call_Verify that user unable to click Export button without any calls selected
	 * @throws Exception
	 */
	@Test
	public void Calls_21071_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();

		// Verify the action dropdown list is disabled when no record is selected and export button is not visible
		Assert.assertTrue("Expected action dropdown to be disabled.", sugar.calls.listView.getControl("actionDropdown").isDisabled());
		sugar.calls.listView.getControl("exportButton").assertVisible(false);

		// Verify export option only when record is checked
		sugar.calls.listView.checkRecord(1);
		sugar.calls.listView.openActionDropdown();
		sugar.calls.listView.getControl("exportButton").assertVisible(true);
		sugar.calls.listView.getControl("actionDropdown").click(); // to close dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}