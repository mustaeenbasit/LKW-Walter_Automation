package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29432 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the correct tooltip is showing at remove icon at Business Rules Builder
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29432_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Process Business Rules 
		sugar().processBusinessRules.navToListView();

		// Create new rule and click on 'Save & Design' button
		sugar().processBusinessRules.listView.create();
		sugar().processBusinessRules.createDrawer.getEditField("name").set(testName);
		VoodooUtils.waitForReady();
		sugar().processBusinessRules.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();

		// Click in the rectangle just under the Conditions title and Select an item from the list
		// TODO: VOOD-1936
		VoodooControl helpTextCtrl = new VoodooControl("button", "css", "#businessruledesigner .decision-table-close-button[title='Remove column']");
		
		// Hover on remove icon as per attached image & Check
		helpTextCtrl.hover();
		
		// Verify that the Related tooltip saying "Remove column" should be displayed.
		helpTextCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}