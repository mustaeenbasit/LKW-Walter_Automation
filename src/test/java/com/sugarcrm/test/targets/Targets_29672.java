package com.sugarcrm.test.targets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_29672 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}
	/**
	 * Verify that Save button is not enabled after hiting save button once.
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_29672_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to target module then click create button then Give Last name and click on save button
		sugar().targets.navToListView();
		sugar().targets.listView.create();
		sugar().targets.createDrawer.getEditField("lastName").set(testName);
		sugar().targets.createDrawer.getControl("saveButton").click();

		// Verify the save button should not be enabled after hiting save button once.
		FieldSet customFS = testData.get(testName).get(0);
		sugar().alerts.getProcess().assertContains(customFS.get("verifyText"), true);
		sugar().targets.createDrawer.getControl("saveButton").waitForAttributeToContain("class", "disabled");
		Assert.assertTrue("The save button is not Disabled", sugar().targets.createDrawer.getControl("saveButton").isDisabled());
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}