package com.sugarcrm.test.pdfManager;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19162 extends SugarTest {
	FieldSet fs;
	VoodooControl searchCtrl, nameCtrl;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify creating a pdf template can be canceled
	 * 
	 */
	@Test
	public void PDFManager_19162_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1158
		new VoodooControl("a", "id", "pdfmanager").click();
		sugar.alerts.waitForLoadingExpiration();
	
		// From the PDF Manager menu, select Create PDF Template.
		new VoodooControl("a", "css", "#header div.module-list  ul  li.dropdown.active button  i").click(); 	
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Enter required field like Name, Author.
		new VoodooControl("input", "id", "name").set(fs.get("pdfName"));
		new VoodooControl("input", "id", "author").set(fs.get("author"));
		
		// Click Cancel
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl = new VoodooControl("input", "id", "name_basic");
		nameCtrl.set(fs.get("pdfName"));
		searchCtrl =new VoodooControl("input", "id", "search_form_submit");
		searchCtrl.click();
		// Assert that  PDF template is not saved/shown in the list.  
		new VoodooControl("p", "css", "#MassUpdate > div > p").assertContains(fs.get("pdfName"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
