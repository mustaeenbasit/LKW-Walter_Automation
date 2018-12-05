package com.sugarcrm.test.pdfManager;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PDFManager_19136 extends SugarTest {
	FieldSet customData;
	VoodooControl pdfManagerCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify field variables are dynamically changed when you select a different module
	 * @throws Exception
	 */
	@Test
	public void PDFManager_19136_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin -> PDF Manager
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1158
		pdfManagerCtrl = new VoodooControl("a", "id", "pdfmanager");
		pdfManagerCtrl.click();
		VoodooUtils.focusDefault();

		// Create PDF Template
		new VoodooControl("i", "css", "li[data-module='PdfManager'] i.fa-caret-down").click();
		VoodooUtils.pause(3000); // three second wait is necessary to make test run more consistently on fast-connection machines
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Accounts Module and field contains Industry
		VoodooControl moduleCtrl = new VoodooControl("select", "id", "base_module");
		moduleCtrl.assertContains(sugar.accounts.moduleNamePlural, true);
		VoodooControl fieldCtrl = new VoodooControl("select", "id", "field");
		fieldCtrl.assertContains(customData.get("account_field"), true);

		// Change module from Accounts to Cases Module, Verify for Resolution field not industry
		moduleCtrl.set(sugar.cases.moduleNamePlural);	
		fieldCtrl.assertContains(customData.get("account_field"), false);
		fieldCtrl.assertContains(customData.get("cases_field"), true);

		// Select Accounts field under the links from dropdown and verify its sub field
		fieldCtrl.set(customData.get("account_link"));
		VoodooControl subFieldCtrl = new VoodooControl("select", "id", "subField");
		subFieldCtrl.assertContains(customData.get("account_field"), true);

		// Select Created User under the links from dropdown and verify its sub field
		fieldCtrl.set(customData.get("user_field"));
		subFieldCtrl.assertContains(customData.get("cases_field"), false);
		subFieldCtrl.assertContains(customData.get("user_sub_field"), true);		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}