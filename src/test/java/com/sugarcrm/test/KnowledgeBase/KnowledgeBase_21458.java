package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21458 extends SugarTest {
	DataSource kbRecords = new DataSource();

	public void setup() throws Exception {
		// Pre-requisite: KB records should exist.
		kbRecords = testData.get(testName);
		sugar().knowledgeBase.api.create(kbRecords);

		// Login as Admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * KB:Verify the kb records can be searched correctly by Status in KB's list view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21458_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying four records are displaying before applying filter
		sugar().knowledgeBase.navToListView();
		Assert.assertTrue("No. of Records are not equal to four", sugar().knowledgeBase.listView.countRows() == 4);

		// Select create new filter
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		FieldSet filterData = testData.get(testName + "_filterData").get(0);
		new VoodooSelect("div", "css", "div[data-filter='field']").set(filterData.get("filterField"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(filterData.get("filterOperator"));
		VoodooControl filterValueCtrl = new VoodooControl("input", "css", ".detail.fld_status .select2-search-field input");
		VoodooControl filterValueSelectCtrl = new VoodooControl("span", "css", ".select2-match");
		VoodooControl resetButtonCtrl = sugar().knowledgeBase.listView.filterCreate.getControl("resetButton");

		// Applying filter for each status in kb list view 
		for (int i = 0; i < kbRecords.size(); i++) {
			filterValueCtrl.set(kbRecords.get(i).get("status"));
			filterValueSelectCtrl.click();
			VoodooUtils.waitForReady();

			// Verifying only one record is displaying
			Assert.assertTrue("No. of Records not equal to one", sugar().knowledgeBase.listView.countRows() == 1);

			// Verifying correct record is displaying
			sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbRecords.get(i).get("name"), true);
			sugar().knowledgeBase.listView.getDetailField(1, "status").assertEquals(kbRecords.get(i).get("status"), true);

			// click on reset button
			resetButtonCtrl.click();
			VoodooUtils.waitForReady();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}