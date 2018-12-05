package com.sugarcrm.test.documents;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Documents_30578 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Create multiple document records
		ds = testData.get(testName);
		for (int i = 0; i < ds.size(); i++) {
			sugar().navbar.selectMenuItem(sugar().documents, "createDocument");
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooFileField("input", "css", "#filename_file").set("src/main/resources/data/" + ds.get(i).get("fileName"));
			VoodooUtils.focusDefault();
			sugar().documents.editView.save();
		}
	}
	/**
	 * Verify that filename is properly displayed in "Search and Add Documents".
	 * @throws Exception
	 */
	@Test
	public void Documents_30578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Search and Select Drawer of Documents
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).clickLinkExisting();
		sugar().documents.searchSelect.selectRecord(1);
		VoodooUtils.waitForReady();
		sugar().documents.searchSelect.selectRecord(2);
		VoodooUtils.waitForReady();

		// Verify Selected FileNames are properly getting displayed in "Search and Add Documents"
		// TODO: VOOD-1487
		VoodooControl selectedRecordsCtrl = new VoodooControl("div", "css", ".selected-records");
		selectedRecordsCtrl.assertContains(ds.get(0).get("fileName"), true);
		selectedRecordsCtrl.assertContains(ds.get(1).get("fileName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}