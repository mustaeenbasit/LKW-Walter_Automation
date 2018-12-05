package com.sugarcrm.test.pdfManager;

import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19139 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl pdfManagerCtrl, dropDownCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();

		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1158
		pdfManagerCtrl = sugar.admin.adminTools.getControl("pdfManager");
		dropDownCtrl = new VoodooControl("i", "css", "li[data-module='PdfManager'] i.fa-caret-down");
		VoodooControl newPDFRecord = new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']");

		// Create PDF Template
		pdfManagerCtrl.click();
		VoodooUtils.focusDefault();
		dropDownCtrl.click();
		newPDFRecord.waitForVisible();
		newPDFRecord.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Save PDF with custom values
		new VoodooControl("input", "id", "name").set(customData.get("name"));
		new VoodooControl("input", "id", "author").set(customData.get("author"));
		new VoodooControl("input", "id", "pdfManagerInsertField").click();
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify a warning message is displayed if you duplicate a pdf template and change module
	 * @throws Exception
	 */
	@Test
	public void PDFManager_19139_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1158
		// List PDF
		dropDownCtrl.click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_LIST']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// PDF record detail View 
		new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-child(3) td:nth-child(2) a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Duplicate/Copy PDF
		new VoodooControl("span", "css", ".ab").click();
		new VoodooControl("a", "id", "duplicate_button").click();
		new VoodooControl("select", "id", "base_module").set(sugar.cases.moduleNamePlural);

		// Verify warning message with okay buton 
		Assert.assertTrue(VoodooUtils.isDialogVisible());
		
		// TODO: VOOD-1045. Remove below line when VOOD-1045 will get resolved.
		Assert.assertTrue(customData.get("message").equals(VoodooUtils.iface.wd.switchTo().alert().getText()));
		
		// Discard changes and save template
		VoodooUtils.acceptDialog();
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "base_module").assertContains(sugar.cases.moduleNamePlural, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}