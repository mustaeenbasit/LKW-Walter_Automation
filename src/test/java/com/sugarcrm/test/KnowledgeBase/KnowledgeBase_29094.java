package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29094 extends SugarTest {
	VoodooControl pdfManagerCtrl;
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create();
		
		// TODO: VOOD-1158: Need Lib support for PDF Manager
		pdfManagerCtrl = sugar().admin.adminTools.getControl("pdfManager");
		sugar().login();
		
		// Enable Kb module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Admin >> PDF Manager
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl newPDFRecord = new VoodooControl("a", "css","a[data-navbar-menu-item='LNK_NEW_RECORD']");

		// Create PDF Template
		pdfManagerCtrl.click();
		VoodooUtils.focusDefault();
		new VoodooControl("i", "css", "li[data-module='PdfManager'] i").click();
		newPDFRecord.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Save PDF with custom values for KB Module
		new VoodooControl("input", "id", "name").set(customData.get("templateName"));
		new VoodooControl("select", "id", "published").set(customData.get("isPublished"));
		new VoodooControl("select", "id", "base_module").set(customData.get("moduleName"));
		VoodooControl recordField = new VoodooControl("select", "id", "field");
		recordField.set(customData.get("tempalteField1"));
		VoodooControl insertButton = new VoodooControl("select", "id", "pdfManagerInsertField");
		insertButton.click();
		recordField.set(customData.get("tempalteField2"));
		insertButton.click();
		new VoodooControl("select", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that KB module is listed in the "Related To" dropdown in Email Composer
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29094_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-843: Lib support to handle new email composer UI
		new VoodooControl("a", "css", ".dropdown-inset [data-action='email']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Closing the Email configuration pop up on Email compose page 
		// (as Email settings are not configured)
		if (new VoodooControl("div", "id", "sugarMsgWindow_h").queryVisible())
			new VoodooControl("a", "class", "container-close").click();
		
		// Verify that the module KB is displayed in 'Related to' dropdown
		new VoodooControl("select", "id", "data_parent_type1").assertContains
			(customData.get("moduleName"), true);
		
		// Verify that the KB name is displayed in 'Related to' textfield
		new VoodooControl("input", "id", "data_parent_name1").assertEquals
			(sugar().knowledgeBase.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
