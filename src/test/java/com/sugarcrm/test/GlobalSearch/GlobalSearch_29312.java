package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_29312 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify html tags are not explicitly displayed in the global search results
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29312_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbRecords = testData.get(testName);

		// TODO: VOOD-1854
		// To write text in html source editor
		VoodooControl htmlEditorButton = new VoodooControl("a", "css", "[title='Edit HTML Source']");
		VoodooControl htmlEditorTextarea = new VoodooControl("textarea", "id", "htmlSource");
		VoodooControl htmlEditorUpdateButton = new VoodooControl("input", "css", "[value='Update']");
		VoodooControl searchResultCtrl;

		// Creating two KB article with body text in html source editor.
		for (int i = 0; i < kbRecords.size(); i++) {
			sugar().knowledgeBase.navToListView();
			sugar().knowledgeBase.listView.create();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(kbRecords.get(i).get("name"));
			htmlEditorButton.click();
			VoodooUtils.focusFrame(1);
			htmlEditorTextarea.set(kbRecords.get(i).get("kbBody"));
			htmlEditorUpdateButton.click();
			sugar().knowledgeBase.createDrawer.save();
		}

		// Search the first kb article in global search
		sugar().navbar.setGlobalSearch(kbRecords.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Verifying correct KB article is displaying.
		searchResultCtrl = sugar().navbar.search.getControl("searchResults");

		// Verifying correct KB article is displaying.
		searchResultCtrl.assertContains(kbRecords.get(0).get("name"), true);

		// Verifying KB body is not containing tags.
		searchResultCtrl.assertContains(kbRecords.get(0).get("kbBody"), false);
		searchResultCtrl.assertContains(kbRecords.get(0).get("kbBodyText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}