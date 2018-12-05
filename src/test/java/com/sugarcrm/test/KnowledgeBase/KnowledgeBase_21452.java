package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21452 extends SugarTest {
	
	DataSource kbData = new DataSource();
	FieldSet testFS = new FieldSet();
	public void setup() throws Exception {
		
		// Initialize test data
		kbData = testData.get(testName+"_records");
		testFS = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create(kbData);

		// Login as admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * KB body filter Advanced_search_containing_these_words
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21452_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB List View page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewArticles");

		// Create new Custom Filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// 1. Select 'Body' for field and 'aaaa' string into "Containing these words" field
		// TODO: VOOD-1785
		new VoodooSelect("div", "css", "div[data-filter='field']").set(testFS.get("filterType"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(testFS.get("filterOperatorContain"));
		VoodooControl filterValue01Ctrl = new VoodooControl("input", "css", "input[name='kbdocument_body']");
		filterValue01Ctrl.set(testFS.get("searchString1"));
		VoodooUtils.waitForReady();

		// 1. Verify articles containing string 'aaaa' in their bodies are listed.i.e. articles 1,3,4 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of KB record listed is not 3", sugar().knowledgeBase.listView.countRows() == 3);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(3).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", kbData.get(0).get("name"));
		
		// 2. Enter another string 'cccc' into current filter 
		filterValue01Ctrl.set(testFS.get("searchString3"));
		VoodooUtils.waitForReady();
		
		// 2. Verify articles containing string 'cccc' in their bodies are listed.  i.e. article 4 is listed. 
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 1", sugar().knowledgeBase.listView.countRows() == 1);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(3).get("name"));

		// 3. Enter another string 'aaaa bbbb' into current filter 
		filterValue01Ctrl.set(testFS.get("searchString1") + " " + testFS.get("searchString2"));
		VoodooUtils.waitForReady();

		// 3. Verify articles excluding string 'aaaa bbbb' in their bodies are listed.  In this case, only articles 1,2,3,4 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 4", sugar().knowledgeBase.listView.countRows() == 4);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(3).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", kbData.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(4, "name", kbData.get(0).get("name"));
		
		// 4. Enter no word into "Containing these words" field
		filterValue01Ctrl.set("");
		VoodooUtils.waitForReady();

		// 3. Verify all articles are listed.  In this case, articles 1,2,3,4 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 4", sugar().knowledgeBase.listView.countRows() == 4);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(3).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", kbData.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(4, "name", kbData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}