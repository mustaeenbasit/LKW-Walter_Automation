package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29968 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that correct result should be shown after meet the filter criteria in KB.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29968_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName).get(0);

		// Create Article record with Name and Body fields filled.
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		VoodooUtils.focusFrame(0);
		sugar().knowledgeBase.createDrawer.getEditField("body").set(filterData.get("filterValue"));
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.save();

		// Verify two KB Records are shown before applying filter
		int rows = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", rows == 2);

		// Create custom filter to search Body field having specific words.
		// TODO: VOOD-1478
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("filterOperator"));
		new VoodooControl("input", "css", ".fld_kbdocument_body input").set(filterData.get("filterValue"));
		sugar().knowledgeBase.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.listView.filterCreate.save();

		// Verify only 1 record is shown in the KB ListView after applying filter.
		int filteredRow = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", filteredRow == 1);

		// Verify the Correct result meeting the filter criteria is displayed.
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}