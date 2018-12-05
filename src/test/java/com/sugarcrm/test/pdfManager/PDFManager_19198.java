package com.sugarcrm.test.pdfManager;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19198 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify two built-in pdf templates are available after install.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PDFManager_19198_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin -> PDF Manager
		sugar().admin.navToAdminPanelLink("pdfManager");
		VoodooUtils.focusFrame("bwc-frame");

		FieldSet pdfTemplatesData = testData.get(testName).get(0);

		// Verify Quote and Invoice pdf templates are displayed in the list view.
		// TODO: VOOD-1158 - Need Lib support for PDF Manager
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 a").assertContains(pdfTemplatesData.get("template2"), true);
		new VoodooControl("a", "css", "#MassUpdate tr.evenListRowS1 a").assertContains(pdfTemplatesData.get("template1"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}