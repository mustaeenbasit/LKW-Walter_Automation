package com.sugarcrm.test.KnowledgeBase;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29047 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Logout as Admin and login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Creating  knowledgeBase record assigned to QA User
		ds = testData.get(testName+"_records");
		sugar().knowledgeBase.create(ds);
	}

	/**
	 * Verify that header for results of KB Search button is appearing as "My Articles"
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29047_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Setting the filter My Articles
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterAssignedToMe();

		// Verify the filter label in List View
		// TODO: VOOD-1752
		FieldSet knowledgeBaseFilter = testData.get(testName).get(0);
		new VoodooControl("span", "css", ".choice-filter-label").assertEquals(knowledgeBaseFilter.get("knowledgeBaseFilter"), true);

		// Asserting the records in List View
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		int listViewCount = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("In-Correct records are appearing in KB list View", listViewCount == 2 );
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(ds.get(0).get("name"), true);
		sugar().knowledgeBase.listView.getDetailField(2, "name").assertEquals(ds.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}