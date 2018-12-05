package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28783 extends SugarTest {
	DataSource kbData = new DataSource();
	FieldSet massUpdateData = new FieldSet();

	public void setup() throws Exception {
		kbData = testData.get(testName);
		massUpdateData = testData.get(testName + "_massUpdate").get(0);

		// Create 6 KB records and 6 Account records
		sugar().accounts.api.create(kbData);
		sugar().knowledgeBase.api.create(kbData);

		// Login as an Admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Tags field should have 'show me' for all Accounts records
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(massUpdateData.get("massUpdateField"));
		// TODO: VOOD-851
		VoodooControl tagMassupdateField = new VoodooControl("input", "css", ".massupdate.fld_tag ul input");
		// TODO: CB-252, VOOD-1437
		tagMassupdateField.set(massUpdateData.get("massUpdateValue") + '\uE007');
		sugar().accounts.massUpdate.update();
		VoodooUtils.waitForReady(); //Needed extra wait

		// Tags field should have 'show me' for all knowledgeBase records
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(massUpdateData.get("massUpdateField"));
		// TODO: CB-252, VOOD-1437
		tagMassupdateField.set(massUpdateData.get("massUpdateValue") + '\uE007');
		sugar().knowledgeBase.massUpdate.update();
		VoodooUtils.waitForReady(); //Needed extra wait
	}

	/**
	 * Verify that KB's tags is able to search correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28783_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from admin and login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// At the global search window, type in "show me"
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		globalSearchCtrl.set(massUpdateData.get("massUpdateValue"));
		VoodooUtils.waitForReady();

		// Verify that the Tag ribbon appear at the left side of the query string. Mouse over the string, which is click-able on the search string
		// TODO: VOOD-1774
		VoodooControl tagRibbonCtrl = new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag-icon");
		VoodooControl tagTextCtrl = new VoodooControl("a", "css", ".quicksearch-tags.dropdown-menu .qs-tag a");
		tagRibbonCtrl.assertVisible(true);
		tagTextCtrl.assertEquals(massUpdateData.get("massUpdateValue"), true);

		// Click on "show me" link beside of the tag ribbon.
		tagTextCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that 5 Accounts or KB or mixed records appear (depending on last updated)
		// TODO: VOOD-1849 and VOOD-1868
		Assert.assertTrue("Not showing 5 records in the Search result dropdown", new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass() == 5);

		// Click on "View all results"
		VoodooControl viewAllResultsCtrl = sugar().navbar.search.getControl("viewAllResults");
		viewAllResultsCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that 12 KB and Accounts records appear
		// TODO: VOOD-1848 and VOOD-1868
		Assert.assertTrue("Not showing 12 records in the Search result dropdown", new VoodooControl("li", "css", ".layout_simple .nav.search-results .search-result").countWithClass() == 2*kbData.size());

		// Dismiss the search string and go back to KB modules
		sugar().navbar.search.getControl("cancelSearch").click();
		sugar().knowledgeBase.navToListView();

		// At the global search window, select KB module only
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		sugar().navbar.search.getControl("searchKnowledgeBase").click();

		// Type "show me" to search again
		globalSearchCtrl.set(massUpdateData.get("massUpdateValue"));
		VoodooUtils.waitForReady();

		// Click on "show me" link beside of the tag ribbon
		tagRibbonCtrl.assertVisible(true);
		tagTextCtrl.assertEquals(massUpdateData.get("massUpdateValue"), true);
		tagTextCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that 5 KB records appear
		// TODO: VOOD-1849 and VOOD-1868
		Assert.assertTrue("Not showing 5 records in the Search result dropdown", new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass() == 5);

		// Click on "View all results"
		viewAllResultsCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that 6 KB records appear
		// TODO: VOOD-1848 and VOOD-1868
		Assert.assertTrue("Not showing 12 records in the Search result dropdown", new VoodooControl("li", "css", ".layout_simple .nav.search-results .search-result").countWithClass() == kbData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}