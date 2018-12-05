package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21453 extends SugarTest {
	
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
	 * KB body filer Advanced_search_excluding_these_words
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21453_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB List View page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewArticles");

		// 1. Create new Custom Filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Select 'Body' for field and 'aaaa' string into "Containing these words" field
		// TODO: VOOD-1785
		new VoodooSelect("div", "css", "div[data-filter='field']").set(testFS.get("filterType"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(testFS.get("filterOperatorContain"));
		new VoodooControl("input", "css", "input[name='kbdocument_body']").set(testFS.get("searchString1"));
		VoodooUtils.waitForReady();

		// Add a new condition for current filter (by clicking + button): 'bbbb' string into "Excluding these words" field 
		sugar().knowledgeBase.listView.filterCreate.getControl("addFilterRow01").click();
		new VoodooSelect("div", "css", "[data-filter='row']:nth-of-type(2) div[data-filter='field']").set(testFS.get("filterType"));
		new VoodooSelect("div", "css", "[data-filter='row']:nth-of-type(2) div[data-filter='operator']").set(testFS.get("filterOperatorExclude"));
		new VoodooControl("input", "css", "[data-filter='row']:nth-of-type(2) input[name='kbdocument_body']").set(testFS.get("searchString2"));
		VoodooUtils.waitForReady();

		// 1. Verify articles containing string 'aaaa' and excluding string 'bbbb' in their bodies are listed.i.e. articles 1,3 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 2", sugar().knowledgeBase.listView.countRows() == 2);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(0).get("name"));

		// 2. Enter another string 'cccc' into "Excluding these words" field for second condition of the current filter 
		new VoodooControl("input", "css", "[data-filter='row']:nth-of-type(2) input[name='kbdocument_body']").set(testFS.get("searchString3"));
		VoodooUtils.waitForReady();
		
		// 2. Verify articles containing string 'aaaa' and excluding string 'cccc' in their bodies are listed.  i.e. articles 1,2 are listed. 
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 2", sugar().knowledgeBase.listView.countRows() == 2);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(0).get("name"));

		// 3. Reset current filter
		sugar().knowledgeBase.listView.filterCreate.cancel();

		// Create another filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Add a new condition: strings 'cccc' into "Excluding these words" field for 'Body' field
		new VoodooSelect("div", "css", "div[data-filter='field']").set(testFS.get("filterType"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(testFS.get("filterOperatorExclude"));
		new VoodooControl("input", "css", "input[name='kbdocument_body']").set(testFS.get("searchString3"));
		VoodooUtils.waitForReady();

		// 3. Verify articles excluding string 'cccc' in their bodies are listed.  In this case, only articles 1,2 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 2", sugar().knowledgeBase.listView.countRows() == 2);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(0).get("name"));

		// 4. Reset current filter
		sugar().knowledgeBase.listView.filterCreate.cancel();

		// Create another filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Add a new condition: Body for field and 'bbbb cccc' string into "Excluding these words" field
		new VoodooSelect("div", "css", "div[data-filter='field']").set(testFS.get("filterType"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(testFS.get("filterOperatorExclude"));
		new VoodooControl("input", "css", "input[name='kbdocument_body']").set(testFS.get("searchString2") + " " + testFS.get("searchString3"));
		VoodooUtils.waitForReady();

		// 4. Verify articles excluding strings 'aaaa' or 'bbbb' in their bodies are listed. In this case, article 1 is listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed in not 1", sugar().knowledgeBase.listView.countRows() == 1);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}