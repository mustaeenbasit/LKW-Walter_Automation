package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28765 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify clicking a tag on a record to go to the global search page should also populate the search bar with the specified tag
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Account record and link the tag to the Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("tags").set(testName);
		sugar().accounts.recordView.save();

		// Clicking on tag on Accounts record view
		// TODO: VOOD-1843
		String tagLink = String.format("%s .tag-name" , sugar().accounts.recordView.getDetailField("tags").getHookString());
		new VoodooControl("a", "css", tagLink).click();
		
		// Verifying the user is navigated to Global search page 
		sugar().globalSearch.getControl("headerpaneTitle").assertContains(testName, true);

		// Asserting the Tag in the Global Search bar
		// TODO: VOOD-1849
		new VoodooControl("div", "css", ".quicksearch-selected-tags div").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}