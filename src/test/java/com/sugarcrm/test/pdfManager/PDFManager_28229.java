package com.sugarcrm.test.pdfManager;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_28229 extends SugarTest {	
	FieldSet fs;
	VoodooControl nameCtrl,downloadPDF,emailPDF,searchCtrl;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.leads.api.create();
		sugar.login();
	}

	/**
	 * Verify that "Download PDF and E-mail PDF" Link should enable if module has created PDF template
	 * 
	 */
	@Test
	public void PDFManager_28229_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Leads record view
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.openPrimaryButtonDropdown();
		
		// Verify "Download PDF" and "Email PDF" links are not in record action menu if module has not created PDF template
		// TODO: VOOD-691
		downloadPDF = new VoodooControl("a", "css", ".fld_download-pdf a");
		emailPDF = new VoodooControl("a", "css", ".fld_email-pdf a");
		downloadPDF.assertExists(false);		
		emailPDF.assertExists(false);
		
		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("pdfManager").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO: VOOD-1158
		// Set-up Pdf template for respective module
		new VoodooControl("a", "css", "#header div.module-list  ul  li.dropdown.active button  i").click(); 	
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(fs.get("pdfName"));
		new VoodooControl( "select", "id", "base_module").set(sugar.leads.moduleNamePlural);
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();		
		sugar.alerts.waitForLoadingExpiration();
		
		// Go to Leads record view
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.openPrimaryButtonDropdown();
		
		// Verify "Download PDF" and "Email PDF" links record action menu if module has created any PDF template
		// TODO: VOOD-691
		downloadPDF.assertExists(true);
		emailPDF.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}