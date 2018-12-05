package com.sugarcrm.test.pdfManager;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19208 extends SugarTest {
	FieldSet fs = new FieldSet();
	DataSource pdfTable = new DataSource();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		pdfTable = testData.get(testName+"_pdfTable");
		sugar.login();
	}

	/**
	 * Verify pdf template can be created using TinyMCE editor and variable can be inserted at cursor position.
	 * 
	 */
	@Test
	public void PDFManager_19208_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1158
		sugar().admin.adminTools.getControl("pdfManager").click();
		VoodooUtils.focusDefault();

		// From the PDF Manager menu, select Create PDF Template.
		VoodooControl dropDownActive = new VoodooControl("i", "css", "li[data-module='PdfManager'] i.fa-caret-down");

		dropDownActive.click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();

		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1158
		// Enter required field like Name, Author.
		new VoodooControl("input", "id", "name").set(pdfTable.get(0).get("name"));
		new VoodooControl("input", "id", "author").set(pdfTable.get(0).get("author"));
		new VoodooControl("a", "id", "body_html_code").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("textarea", "id", "htmlSource").set(fs.get("html_content"));
		new VoodooControl("input", "id", "insert").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Put the cursor in 2nd row 1st column. Select Name from Field drop down. Click Insert.
		VoodooUtils.focusFrame("body_html_ifr");
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(1)").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "field").set(pdfTable.get(0).get("column1"));
		new VoodooControl("input", "id", "pdfManagerInsertField").click();
		VoodooUtils.focusFrame("body_html_ifr");
		
		// Verify, {$fields.name} is inserted in 2nd row, 1st column.
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(1)").assertContains(pdfTable.get(1).get("column1"), true);
		
		// Put the cursor in 2nd row 2nd column. Select Type from Field drop down. Click Insert.
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(2)").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "field").set(pdfTable.get(0).get("column2"));
		new VoodooControl("input", "id", "pdfManagerInsertField").click();
		VoodooUtils.focusFrame("body_html_ifr");
		
		// Verify, {$fields.account_type} is inserted in 2nd row, 2nd column.
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(2)").assertContains(pdfTable.get(1).get("column2"), true);
		
		// Put the cursor in 2nd row 3rd column. Select Shipping City from Field drop down. Click Insert.
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(3)").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "field").set(pdfTable.get(0).get("column3"));
		new VoodooControl("input", "id", "pdfManagerInsertField").click();
		VoodooUtils.focusFrame("body_html_ifr");
		
		// Verify, {$fields.shipping_address_city} is inserted in 2nd row, 3rd column.
		new VoodooControl("td", "css", "#tinymce tr:nth-child(2) td:nth-child(3)").assertContains(pdfTable.get(1).get("column3"), true);
		
		// Save the pdf.
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// From the action drop down, select the custom pdf.
		dropDownActive.click();
		new VoodooControl("a", "css", "li[data-module='PdfManager'] li:nth-child(5) a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify, the pdf layout should display according to the design.
		new VoodooControl("div", "css", ".moduleTitle").assertContains(pdfTable.get(0).get("name"), true);
		new VoodooControl("td", "css", "#body_html tr:nth-child(2) td:nth-child(1)").assertContains(pdfTable.get(1).get("column1"), true);
		new VoodooControl("td", "css", "#body_html tr:nth-child(2) td:nth-child(2)").assertContains(pdfTable.get(1).get("column2"), true);
		new VoodooControl("td", "css", "#body_html tr:nth-child(2) td:nth-child(3)").assertContains(pdfTable.get(1).get("column3"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
