package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21456 extends SugarTest {
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
	 * KB:Verify the kb records can be searched correctly by name in KB's list view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21456_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying three records are displaying before applying filter
		sugar().knowledgeBase.navToListView();
		Assert.assertTrue("No. of Records are not equal to three", sugar().knowledgeBase.listView.countRows() == 3);

		// Search the KB record with valid name.
		sugar().knowledgeBase.listView.setSearchString(kbRecords.get(0).get("name"));

		// Verifying only one record is displaying in kb's list view
		Assert.assertTrue("No. of Records are not equal to one", sugar().knowledgeBase.listView.countRows() == 1);

		// Verifying correct record is displaying in kb's list view
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbRecords.get(0).get("name"), true);

		// Search the KB with invalid name.
		sugar().knowledgeBase.listView.setSearchString(testName);

		// Verifying no record is displaying in kb's list view
		Assert.assertTrue("Records displayed in KB list view", sugar().knowledgeBase.listView.countRows() == 0);

		// Search the KB with Wild card from name field.
		String nameSubString = kbRecords.get(1).get("name").substring(2, 4);
		StringBuilder wildCardString = new StringBuilder(nameSubString).insert(0,"%");
		sugar().knowledgeBase.listView.setSearchString(wildCardString.toString());

		// Verifying only two record are displaying in kb's list view
		Assert.assertTrue("No. of Records are not equal to two", sugar().knowledgeBase.listView.countRows() == 2);

		// Verifying correct records are displaying in kb's list view
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbRecords.get(1).get("name"), true);
		sugar().knowledgeBase.listView.getDetailField(2, "name").assertEquals(kbRecords.get(2).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}