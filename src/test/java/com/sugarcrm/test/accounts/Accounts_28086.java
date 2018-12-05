package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Accounts_28086 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * To Verify that Account name should hyperlink in summation report.
	 * @throws Exception
	 */
	@Test
	public void Accounts_28086_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Go to Reports > Create Report "Summation" type
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822 - need lib support of reports module
		new VoodooControl("img", "css", "[name='summationImg']").click();
		// Select Accounts module
		new VoodooControl("table", "id", "Accounts").click();
		// Click Next
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		// Select Account > Name
		new VoodooControl("tr", "id", "Accounts_name").click();
		// Click Next
		nextBtnCtrl.click();
		// Select "Accounts > Name, Click Next
		nextBtnCtrl.click();
		// Do not select a chart and click next. 
		new VoodooControl("input", "css", "#chart_options_div #nextButton").click();
		// Report name
		new VoodooControl("input", "id", "save_report_as").set(testName);
		// Save and Run
		new VoodooControl("input", "id", "saveAndRunButton").click();

		String accountName = sugar().accounts.getDefaultData().get("name");

		// Verify Accounts name is a hyperlink 
		VoodooControl hyperLinkCtrl = new VoodooControl("a", "css", "[target='_blank']");
		hyperLinkCtrl.assertAttribute("target", "_blank");
		hyperLinkCtrl.assertEquals(accountName, true);
		hyperLinkCtrl.click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusWindow(1);

		// Verify redirection is correct.
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountName, true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}