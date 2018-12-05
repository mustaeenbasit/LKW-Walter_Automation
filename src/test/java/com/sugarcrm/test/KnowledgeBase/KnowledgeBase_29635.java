package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29635 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that the Recently modified record should appear on the top of the KB listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29635_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbData = testData.get(testName);

		// Create KB records. e.g Record1 and Record2
		sugar().knowledgeBase.create(kbData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Define KB list view controls
		VoodooControl firstRecordCtrl = sugar().knowledgeBase.listView.getDetailField(1, "name");
		VoodooControl secondRecordCtrl = sugar().knowledgeBase.listView.getDetailField(2, "name");

		// Verify that in the list view of KB, "Record2" appears on the top(i.e index 1) and "Record1" at second
		firstRecordCtrl.assertEquals(kbData.get(1).get("name"), true);
		secondRecordCtrl.assertEquals(kbData.get(0).get("name"), true);

		// Now Edit "Record1" (i.e at index 2 in list view) from the list view itself (change the Name e.g "testName") and save
		sugar().knowledgeBase.listView.editRecord(2);
		sugar().knowledgeBase.listView.getEditField(2, "name").set(testName);
		sugar().knowledgeBase.listView.saveRecord(2);

		// Reload the KB list view and observe the Records listing
		VoodooUtils.refresh();

		// Verify that the edited KB record should be displayed on the top in the list view (because its the recently modified record)
		firstRecordCtrl.assertEquals(testName, true);
		secondRecordCtrl.assertEquals(kbData.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}