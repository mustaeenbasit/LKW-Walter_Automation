package com.sugarcrm.test.documents;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Documents_30238 extends SugarTest {

	public void setup() throws Exception {
		// Creating document to be later used as the related document
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * Verify that user should be able to relate a document with another Document
	 * @throws Exception
	 */
	@Test
	public void Documents_30238_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a document record  
		sugar().navbar.selectMenuItem(sugar().documents, "createDocument");
		VoodooUtils.focusFrame("bwc-frame");

		String fileName = testName + ".csv";

		VoodooControl fileCtrl = sugar().documents.editView.getEditField("fileNameFile");
		VoodooFileField browseToImport = new VoodooFileField(fileCtrl.getTag(), fileCtrl.getStrategyName(), fileCtrl.getHookString());

		// Getting the file for Document
		browseToImport.set("src/test/resources/data/" + fileName);
		VoodooUtils.waitForReady();

		// Click the 'Select' button corresponding to the 'Related Document:' field
		// TODO: VOOD-1980: Need lib support for Documents module List View and Detail View
		new VoodooControl("input", "css", "[name='btn2']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Click the related document link on the SSV window
		// TODO: VOOD-805
		new VoodooControl("a", "css", ".oddListRowS1 td a").click();

		// Move back the focus to default window
		VoodooUtils.focusWindow(0);  

		// Save
		sugar().documents.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that selected document is displayed as a link
		// TODO: VOOD-1980
		VoodooControl relatedDocCtrl = new VoodooControl("a", "css", "#LBL_DOCUMENT_INFORMATION tbody tr:nth-child(7) td:nth-child(2) a");
		
		relatedDocCtrl.assertEquals(sugar().documents.getDefaultData().get("documentName"), true);
		FieldSet customData = testData.get(testName).get(0);
		Assert.assertTrue("Selected document is not link", relatedDocCtrl.getTag().equals(customData.get("data")));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}