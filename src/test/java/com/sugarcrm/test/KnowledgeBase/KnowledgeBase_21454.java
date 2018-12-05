package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21454 extends SugarTest {
	DataSource kbRecords = new DataSource();

	public void setup() throws Exception {
		// Creating custom  KB records
		kbRecords = testData.get(testName+"_records");
		sugar().knowledgeBase.api.create(kbRecords);

		// Login as admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * KB:Verify the kb records can be searched correctly in list view by containing/excluding_these_words filter for body field.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21454_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB List View page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewArticles");

		// 1. Create new Custom Filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Select 'Body' for field and 'aaaa' string into "Containing these words" field
		// TODO: VOOD-1785
		FieldSet filterData = testData.get(testName).get(0);
		new VoodooSelect("div", "css", "div[data-filter='field']").set(filterData.get("filterType"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(filterData.get("filterOperatorContain"));
		VoodooControl containingValueCtrl = new VoodooControl("input", "css", "input[name='kbdocument_body']");
		containingValueCtrl.set(filterData.get("searchString1"));
		VoodooUtils.waitForReady();

		// Add a new condition for current filter (by clicking + button): 'bbbb' string into "Excluding these words" field 
		sugar().knowledgeBase.listView.filterCreate.getControl("addFilterRow01").click();
		new VoodooSelect("div", "css", "[data-filter='row']:nth-of-type(2) div[data-filter='field']").set(filterData.get("filterType"));
		new VoodooSelect("div", "css", "[data-filter='row']:nth-of-type(2) div[data-filter='operator']").set(filterData.get("filterOperatorExclude"));
		VoodooControl excludingValueCtrl = new VoodooControl("input", "css", "[data-filter='row']:nth-of-type(2) input[name='kbdocument_body']");
		excludingValueCtrl.set(filterData.get("searchString2"));
		VoodooUtils.waitForReady();

		// Verify articles containing string 'aaaa' and excluding string 'bbbb' in their bodies are listed.i.e. articles 1,4 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of records in list view is not equal to 2", sugar().knowledgeBase.listView.countRows() == 2);
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbRecords.get(0).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbRecords.get(3).get("name"));

		// 2. Enter string 'bbbb cccc' into "Excluding these words" field for second condition of the current filter 
		excludingValueCtrl.set(String.format("%s %s", filterData.get("searchString2"), filterData.get("searchString3")));
		VoodooUtils.waitForReady();

		// 2. Verify articles containing string 'aaaa' and excluding string 'bbbb cccc' in their bodies are listed.  i.e. articles 1 is listed. 
		// Verify count of records on the list view 
		Assert.assertTrue("The number of records in list view is not equal to 1", sugar().knowledgeBase.listView.countRows() == 1);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbRecords.get(0).get("name"));

		// 3. Enter string 'aaaa bbbb' into "Containing these words" field and Enter string 'cccc' into "Excluding these words" field 
		containingValueCtrl.set(String.format("%s %s", filterData.get("searchString1"), filterData.get("searchString2")));
		excludingValueCtrl.set(filterData.get("searchString3"));
		VoodooUtils.waitForReady();

		// 3. Verify articles Containing string 'aaaa bbbb' and excluding string 'cccc' in their bodies are listed. In this case, only articles 1,2,3 are listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of records in list view is not equal to 3", sugar().knowledgeBase.listView.countRows() == 3);
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbRecords.get(0).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbRecords.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(3, "name", kbRecords.get(2).get("name"));

		// 4. Enter string 'aaaa dddd' into "Containing these words" field and Enter string 'bbbb cccc' into "Excluding these words" field 
		containingValueCtrl.set(String.format("%s %s", filterData.get("searchString1"), filterData.get("searchString4")));
		excludingValueCtrl.set(String.format("%s %s", filterData.get("searchString2"), filterData.get("searchString3")));
		VoodooUtils.waitForReady();

		// 4. Verify articles Containing string 'aaaa dddd' and excluding string 'bbbb cccc' in their bodies are listed.  In this case, only articles 1 is listed.
		// Verify count of records on the list view 
		Assert.assertTrue("The number of records in list view is not equal to 1", sugar().knowledgeBase.listView.countRows() == 1);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbRecords.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}