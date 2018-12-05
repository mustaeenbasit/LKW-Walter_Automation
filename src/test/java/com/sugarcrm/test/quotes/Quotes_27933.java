package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_27933 extends SugarTest {

	public void setup() throws Exception {
		sugar.quotes.api.create();
		sugar.login();
	}

	/**
	 * Verify that the only supported actions in Quotes list view are: Delete, Mass Update, Export, & Recalculate Values
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_27933_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.quotes.navToListView();
		sugar.quotes.listView.toggleSelectAll();
		sugar.quotes.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that drop down shows all of the actions in the drop down list: Delete, Mass Update, Export
		sugar.quotes.listView.getControl("deleteButton").assertVisible(true);
		sugar.quotes.listView.getControl("massUpdateButton").assertVisible(true);
		sugar.quotes.listView.getControl("exportButton").assertVisible(true);

		// Click on any action in the list(Click on Delete button)
		sugar.quotes.listView.getControl("deleteButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Verify that the action is triggered(Assert that in list view no delete and select all button shows that list view is empty)
		sugar.quotes.listView.getControl("selectAllCheckbox").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}