package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28742 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Search Title show Tag icon in search result page
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28742_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tagRecords = testData.get(testName).get(0);
		String accountName = sugar().accounts.getDefaultData().get("name");
		String tag1 = tagRecords.get("tagName1");
		String tag2 = tagRecords.get("tagName2");
		VoodooControl tagEditField = sugar().accounts.createDrawer.getEditField("tags");
		VoodooControl globalSearchInput = sugar().navbar.getControl("globalSearch");

		// Create an Account record with tags 
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(accountName);
		tagEditField.set(tag1);
		tagEditField.set(tag2);
		sugar().accounts.createDrawer.save();

		// In global search bar, enter Tag1, Tag2
		globalSearchInput.set(tag1);
		VoodooUtils.waitForReady();
		// TODO: VOOD-1774 - Need library support for Global search tag ribbon
		VoodooControl highlightedTag = new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag");
		highlightedTag.click();
		VoodooUtils.waitForReady();

		globalSearchInput.set(tag2);
		VoodooUtils.waitForReady();
		highlightedTag.click();
		VoodooUtils.waitForReady();

		// Enter the accountName to be searched and hit Enter
		globalSearchInput.set(accountName + '\uE007');
		VoodooUtils.waitForReady();

		/**
		 * Assert that In search results page, verify the search title format is start with query term first 
		 * and then 1 tag icon followed by all comma separated tag filters.
		 * e.g Search Results for : "Account 1", tag icon "Tag1, Tag2"
		 */		
		sugar().globalSearch.getControl("headerpaneTitle").assertEquals(tagRecords.get("searchText") + 
				"\""+ accountName + "\"" + "  " + "\"" + tag1 + ", "+ tag2 +"\"", true);
		// TODO: VOOD-1848
		new VoodooControl("i", "css", ".headerpane [data-fieldname=title] div .fa-tag").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}