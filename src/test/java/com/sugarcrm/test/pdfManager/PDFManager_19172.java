package com.sugarcrm.test.pdfManager;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19172 extends SugarTest {
	
	FieldSet fs;
	VoodooControl navigationCtrl, creatPdfCtrl,searchCtrl, nameCtrl,
	baseModuleCtrl, fieldCtrl, insertCtrl, templateCtrl, saveButtonCtrl;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.login();
		
		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1158
		new VoodooControl("a", "id", "pdfmanager").click();
		sugar.alerts.waitForLoadingExpiration();
	
		// From the PDF Manager menu, select Create PDF Template.
		navigationCtrl = new VoodooControl("a", "css", "#header div.module-list  ul  li.dropdown.active button  i");
		navigationCtrl.click(); 	
		
		creatPdfCtrl = new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']");
		creatPdfCtrl.click();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Enter required field like Name, Author.
		templateCtrl = new VoodooControl("input", "id", "name");
		templateCtrl.set(fs.get("pdfName"));
		
		baseModuleCtrl = new VoodooControl( "select", "id", "base_module");
		baseModuleCtrl.set(sugar.opportunities.moduleNamePlural);
		
		fieldCtrl = new VoodooControl( "select", "id", "field");
		fieldCtrl.set(fs.get("fieldName"));		
		
		insertCtrl = new VoodooControl( "input", "id", "pdfManagerInsertField");
		insertCtrl.click();
		
		fieldCtrl.set(fs.get("subFieldName"));
		insertCtrl.click();
		fieldCtrl.set(fs.get("linkName"));
		
		new VoodooControl("select", "id", "subField").set(fs.get("linkSubField"));
		insertCtrl.click();
		
		new VoodooControl("input", "id", "author").set(fs.get("author"));
		saveButtonCtrl = new VoodooControl("input", "id", "SAVE_HEADER");
		saveButtonCtrl.click();
		VoodooUtils.focusDefault();
		
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify an existing pdf template can be duplicated
	 * 
	 */
	@Test
	public void PDFManager_19172_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin > PDF Manager.
		sugar.navbar.navToModule("PdfManager");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		nameCtrl = new VoodooControl("input", "id", "name_basic");
		
		//  Select the pdf template PDF for opp to detail view. 
		nameCtrl.set(fs.get("pdfName"));
		
		searchCtrl =new VoodooControl("input", "id", "search_form_submit");
		searchCtrl.click();
		
		// click the pdf  
		new VoodooControl("a", "css", ".oddListRowS1 a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		//  From the action drop down, select Copy.
		new VoodooControl("span", "css", "#detail_header_action_menu > li > span").click();
		new VoodooControl("a", "id", "duplicate_button").click();
		sugar.alerts.waitForLoadingExpiration();

		// Assert the pdf name and module name on pdf detail view
		templateCtrl.assertContains(fs.get("pdfName"), true);
		baseModuleCtrl.assertContains(sugar.opportunities.moduleNamePlural, true);
		
		//  Click Save
		saveButtonCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		sugar.navbar.navToModule("PdfManager");
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl.set("");
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		nameCtrl.set(fs.get("pdfName"));
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that there are 2 PDF template with the same name i.e PDF for opp.
		new VoodooControl("a", "css", ".oddListRowS1 a").assertContains(fs.get("pdfName"), true);
		new VoodooControl("a", "css", ".evenListRowS1 a").assertContains(fs.get("pdfName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}