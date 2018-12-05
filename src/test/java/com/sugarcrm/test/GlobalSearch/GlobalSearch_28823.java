package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28823 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify Tag ribbon shouldn't appear again after selecting a Tag pill in global search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28823_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Contact record and link the tag to the Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("tags").set(testName);
		sugar().contacts.recordView.save();

		// Inserting a tag name in the Global Search bar
		sugar().navbar.getControl("globalSearch").set(testName);
		VoodooUtils.waitForReady();

		// Clicking on first tag in tag ribbon
		// TODO: VOOD-1774 - Need library support for Global search tag ribbon
		new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag").click();
		VoodooUtils.waitForReady();

		// Verifying the Tag ribbon is not visible once user clicked on a tag in tag ribbon
		// TODO: VOOD-1774 - Need library support for Global search tag ribbon
		new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}