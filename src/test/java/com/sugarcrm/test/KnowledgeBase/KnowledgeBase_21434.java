package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21434 extends SugarTest {
	FieldSet kbFilterData = new FieldSet();

	public void setup() throws Exception {
		kbFilterData = testData.get(testName).get(0);

		// Create an article, such as "Title" -> article1, "Body" -> bbbb aaaa. 
		FieldSet kbData = new FieldSet();
		kbData.put("name", kbFilterData.get("name"));
		kbData.put("body", kbFilterData.get("body"));
		sugar().knowledgeBase.api.create(kbData);
		kbData.clear();

		// Create another article with default data
		sugar().knowledgeBase.api.create();

		// Login as a valid user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Search_Condition_Subtraction_Sign
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21434_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Knowledge Base" tab on navigation bar.
		sugar().knowledgeBase.navToListView();

		// Click on Create filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Set following criteria: Select field: Body -> "containing these words" -> Value : "bbbb -aaaa"
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		new VoodooSelect("div", "css", "div[data-filter='field']").set(kbFilterData.get("filterFor"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(kbFilterData.get("operator"));
		new VoodooControl("input", "css", "input[name='kbdocument_body']").set(kbFilterData.get("bodyValueToBeSearch"));
		VoodooUtils.waitForReady();

		// Verify that the Article record should be searched
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbFilterData.get("name"), true);
		Assert.assertTrue("Record(s) are not searched accordingly", sugar().knowledgeBase.listView.countRows() == 1);

		// Cancel the filter pane from the list view
		sugar().knowledgeBase.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}