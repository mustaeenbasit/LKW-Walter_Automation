package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21455 extends SugarTest {
	FieldSet filterData = new FieldSet();

	public void setup() throws Exception {
		// Pre-requisite: KB record should exist.
		sugar().knowledgeBase.api.create();

		// Login as Admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating one more kb record with 'Approved by' field
		filterData = testData.get(testName).get(0);
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("tags").set(filterData.get("filterValue"));
		sugar().knowledgeBase.createDrawer.save();
	}

	/**
	 * KB:Verify tags can be searched correctly in KB's list view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21455_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying two records are displaying before applying filter
		Assert.assertTrue("No. of Records are not equal to two", sugar().knowledgeBase.listView.countRows() == 2);

		// Select create new filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		// Selecting filter Tags -> is any of -> AdminData 
		new VoodooSelect("div", "css", "div[data-filter='field']").set(filterData.get("filterField"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(filterData.get("filterOperator"));
		new VoodooControl("input", "css", ".detail.fld_tag input").set(filterData.get("filterValue"));
		new VoodooControl("span", "css", ".select2-match").click();
		VoodooUtils.waitForReady();

		// Verifying only one record is displaying
		Assert.assertTrue(sugar().knowledgeBase.listView.countRows() == 1);

		// Verifying correct record is displaying
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}