package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28825 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify selected Tag pill no longer appear in tag ribbon once is selected in global search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28825_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Contact record and link the tags to the Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		FieldSet fs = testData.get(testName).get(0);
		VoodooControl tagEditField = sugar().contacts.recordView.getEditField("tags");
		tagEditField.set(fs.get("tag1"));
		tagEditField.set(fs.get("tag2"));
		sugar().contacts.recordView.save();

		// Inserting a text 'Global' in the Global Search bar
		VoodooControl globalSearchInput = sugar().navbar.getControl("globalSearch");
		String searchValue = fs.get("tag1").substring(0, 6);
		globalSearchInput.set(searchValue);
		VoodooUtils.waitForReady();

		// Clicking on tag 'Global_2015' in tag ribbon
		// TODO: VOOD-1774 - Need library support for Global search tag ribbon
		new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag").click();
		VoodooUtils.waitForReady();

		// Again inserting a text 'Global' in the Global Search bar
		globalSearchInput.set(searchValue);
		VoodooUtils.waitForReady();

		// Verifying the the tag 'Global_2015' is not present in Tag ribbon, when searched again
		// TODO: VOOD-1774 - Need library support for Global search tag ribbon
		new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu").assertContains(fs.get("tag1"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}