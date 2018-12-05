package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28778 extends SugarTest {
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData =testData.get(testName);

		// At least 6 KB records.Each one is active revision itself.You also can make KB name is searchable as like this - sarah kb1, test kb1 and etc
		sugar().knowledgeBase.api.create(kbData);

		// Logoin as an Admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Active Revision should not be searchable in KB
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28778_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName + "_KB").get(0);

		// Logout from the Admin user and  Log in as QAuser user
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// In global search window, select KB module
		sugar().navbar.getControl("globalSearch").click();
		VoodooUtils.waitForReady(); // Wait needed
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		sugar().navbar.search.getControl("searchKnowledgeBase").click();

		// Enter common string in search query
		sugar().navbar.getControl("globalSearch").set(kbData.get(0).get("name").substring(0, 6));
		VoodooUtils.waitForReady();

		// Verify that 5 KB records are appearing in the searching result list at search window
		// TODO: VOOD-1849 and VOOD-1868
		int searchedRecordCount = new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass();

		// Assert that type ahead drop down lists 5 records on page, and there is a "View All Results" link
		Assert.assertTrue("Not show 5 records in the Search result dropdown", searchedRecordCount == 5);

		// Verify that "Active Revision" = 1 is not appearing
		sugar().navbar.search.getControl("searchResults").assertContains(customData.get("activeRevision"), false);

		// Verify that you only see KB module icon and Name of the KB
		VoodooControl modulesIconCtrl = sugar().navbar.search.getControl("searchModuleIcons");
		modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().knowledgeBase.moduleNamePlural).assertEquals(sugar().knowledgeBase.moduleNamePlural.substring(0, 2), true);

		// Verify that "View all result" link appears. Click on it, bring you to the result page
		VoodooControl viewAllResultsBtnCtrl = sugar().navbar.search.getControl("viewAllResults");
		viewAllResultsBtnCtrl.assertContains(customData.get("viewAllResults"), true);
		viewAllResultsBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that all 6 KB records are displayed
		// TODO: VOOD-1848 and VOOD-1868
		VoodooControl globalSearchResultPageCtrl = new VoodooControl("li", "css", ".layout_simple .nav.search-results");
		Assert.assertTrue("Not show 6 records in the Search result dropdown", new VoodooControl("li", "css", ".layout_simple .nav.search-results .search-result").countWithClass() == kbData.size());
		for(int i = 0; i < kbData.size(); i++) {
			globalSearchResultPageCtrl.assertContains(kbData.get(i).get("name"), true);;
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}