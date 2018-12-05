package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29434 extends SugarTest {
	public void setup() throws Exception {
		// Login
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Body content is showing in detail view and in preview of KB Templates correctly
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29434_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Navigate to Knowledge Base -> Create Template
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createTemplate");

		// Enter required fields and save the record
		// TODO: VOOD-695 & VOOD-1756
		new VoodooControl("input", "css", "[name='name']").set(testName);
		VoodooUtils.focusFrame("mce_0_ifr");
		new VoodooControl("body", "css", "#tinymce").set(customFS.get("templateDescription"));
		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", ".create.fld_save_button [name='save_button']").click();
		VoodooUtils.waitForReady();

		// Navigate to Template list view , Click on preview button or click on record for detail view
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewTemplates");
		sugar().knowledgeBase.listView.previewRecord(1);
		sugar().previewPane.setModule(sugar().knowledgeBase);

		// Verify that Body Content should display in detail view and in preview of KB Templates.
		sugar().previewPane.getPreviewPaneField("name").assertContains(testName, true);

		// Go to template detailView
		sugar().knowledgeBase.listView.clickRecord(1);

		// Verify that Body Content should display in detail view and in preview of KB Templates.
		// TODO: VOOD-1756 
		sugar().knowledgeBase.recordView.getDetailField("name").assertContains(testName, true);
		new VoodooControl("div", "css", ".detail.fld_body div").assertContains(customFS.get("templateDescription"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}