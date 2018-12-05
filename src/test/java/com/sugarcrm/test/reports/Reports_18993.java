package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18993 extends SugarTest {
	FieldSet myCustomData = new FieldSet();
	
	public void setup() throws Exception {
		FieldSet emailSettings = testData.get("env_email_setup").get(0);
		myCustomData = testData.get(testName).get(0);
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSettings);

		// Navigate to Emails module and save email in Drafts folder.
		// TODO: VOOD-792
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "composeButton").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-843 - Lib support to handle email composer UI
		new VoodooControl("input", "id", "addressTO1").set(myCustomData.get("emailAddress")); 
		new VoodooControl("input","id","emailSubject1").set(myCustomData.get("subject"));
		VoodooUtils.focusFrame("htmleditor1_ifr"); // focus email body iframe
		new VoodooControl("body", "id", "tinymce").set(myCustomData.get("bodyText"));
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// click on 'Save to Draft' button in compose mail UI
		new VoodooControl("button", "css", "#composeHeaderTable1 tr:nth-child(1)  td:nth-child(1) button:nth-child(2)").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Email subject is displayed as a link on reports.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_18993_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-643
		// Navigate to report module and click on Create Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// click on Rows and Coloumns Report
		new VoodooControl("img", "css", "#report_type_div [name='rowsColsImg']").click();

		// click on Emails module 
		new VoodooControl("a", "css", "#Emails tbody tr td:nth-child(2) a").click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();
		new VoodooControl("tr", "id", "Emails_name").click();
		nextBtn.click();
		new VoodooControl("input", "id", "save_report_as").set(myCustomData.get("report_name"));

		// save and run the report 
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify that Email subject is displayed as a link on reports.
		VoodooControl emailSubject = new VoodooControl("a", "css", "#report_results table tbody tr td a");
		emailSubject.assertEquals(myCustomData.get("subject"), true);

		// Click on the link and verify that Email module opens up with correct email displayed.
		emailSubject.click();
		VoodooUtils.focusWindow(1);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".moduleTitle").assertEquals(myCustomData.get("subject"), true);
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(7) td:nth-child(2) slot").assertContains(myCustomData.get("subject"), true);
		new VoodooControl("div", "id", "html_div").assertContains(myCustomData.get("bodyText"), true);
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}