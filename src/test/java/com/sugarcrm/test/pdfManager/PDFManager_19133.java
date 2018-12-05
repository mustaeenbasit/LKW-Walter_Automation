package com.sugarcrm.test.pdfManager;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19133 extends SugarTest {
	FieldSet customData;
	VoodooControl pdfManagerCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify you can create a pdf template by just entering the required fields
	 * @throws Exception
	 */
	@Test
	public void PDFManager_19133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1158
		pdfManagerCtrl = new VoodooControl("a", "id", "pdfmanager");
		pdfManagerCtrl.click();
		VoodooUtils.focusDefault();

		// Create PDF
		VoodooControl dropDownCtrl = new VoodooControl("i", "css", "li[data-module='PdfManager'] i.fa-caret-down");
		dropDownCtrl.click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify required fields Module & team (i.e default values)
		new VoodooControl("select", "id", "base_module").assertContains(sugar.accounts.moduleNamePlural, true);
		new VoodooControl("input", "id", "EditView_team_name_collection_0").assertContains(customData.get("team_name"), true);

		// Save PDF with custom values
		new VoodooControl("input", "id", "name").set(customData.get("name"));
		new VoodooControl("input", "id", "author").set(customData.get("author"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();

		// Verify PDF record Detail View 
		new VoodooControl("span", "id", "name").assertEquals(customData.get("name"), true);
		new VoodooControl("span", "id", "author").assertEquals(customData.get("author"), true);

		// List PDF
		VoodooUtils.focusDefault();
		dropDownCtrl.click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_LIST']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify PDF record List View 
		new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-child(3) td:nth-child(2) a").assertEquals(customData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}