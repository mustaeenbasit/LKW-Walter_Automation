package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28824 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the original search term entered in global search bar should get over written by the selected tag pill
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28824_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet tagFS = testData.get(testName).get(0);
		
		// Creating Tag in existing account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("tags").set(tagFS.get("tags"));
		sugar().accounts.recordView.save();

		// Search the created tag name in global search 
		sugar().navbar.setGlobalSearch(tagFS.get("tags"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-1774
		// Need library support for Global search tag ribbon
		VoodooControl tagRibbonControl = new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag");
		
		// Verifying entering only tag name returns no results were found 
		VoodooControl searchResult = sugar().navbar.search.getControl("searchResults");
		searchResult.assertEquals(tagFS.get("searchResult"), true);
		tagRibbonControl.click();
		VoodooUtils.waitForReady();
		
		// Verifying after clicking on tag ribbon it gets over written with its associated account name.
		tagRibbonControl.assertVisible(false);
		searchResult.assertContains(sugar().accounts.defaultData.get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}