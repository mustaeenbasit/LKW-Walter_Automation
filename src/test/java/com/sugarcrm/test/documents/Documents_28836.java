package com.sugarcrm.test.documents;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Documents_28836 extends SugarTest {
	VoodooControl studioCtrl, documentsCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Records are searched using field "File name" in Documents
	 * @throws Exception
	 */
	@Test
	public void Documents_28836_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Documents module in studio
		// TODO: VOOD-542
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		documentsCtrl = new VoodooControl("a", "id", "studiolink_Documents");
		documentsCtrl.click();

		// Navigating to Basic search layout
		// TODO: VOOD-1509
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "BasicSearchBtn").click();
		VoodooUtils.waitForReady();

		// Adding 'File Name' to Default list in Basic search of Documents module
		VoodooControl fileNameHidden = new VoodooControl("li", "css", "#Hidden li[data-name='filename']");
		VoodooControl addToDefaultList = new VoodooControl("td", "css", "#Default li[data-name='document_name']");
		fileNameHidden.dragNDrop(addToDefaultList);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create a document record with custom file name 
		sugar().navbar.selectMenuItem(sugar().documents, "createDocument");
		VoodooUtils.focusFrame("bwc-frame");
		String fileName = testName + ".csv";
		VoodooControl fileCtrl = sugar().documents.editView.getEditField("fileNameFile");
		VoodooFileField browseToImport = new VoodooFileField(fileCtrl.getTag(), fileCtrl.getStrategyName(), fileCtrl.getHookString());
		browseToImport.set("src/test/resources/data/" + fileName);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().documents.editView.save();

		// Perform basic search in documents list view
		sugar().documents.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "filename_basic").set(fileName);
		VoodooUtils.focusDefault();
		sugar().documents.listView.submitSearchForm();

		// Assert the records based on search done.
		// TODO: VOOD-1980
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(4) a").assertEquals(fileName, true);
		new VoodooControl("a", "css", ".oddListRowS1 a.tabDetailViewDFLink").assertEquals(fileName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}