package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20186 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.disableSubpanelDisplayViaJs(sugar().leads);
	}

	/**
	 * Verify that hidden subpanels still display in related module folder when create report
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20186_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Navigate to reports module and click Create new Report
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.waitForReady();
		
		// Select rows and columns report TODO VOOD-822
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "div#report_type_div img[alt='Rows and Columns Report']").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click Accounts module in the Reports  and Verify that Leads module is availible  
		new VoodooControl("img", "css", "table#buttons_table table#Accounts a.studiolink img").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("a", "xpath", "//div[@id='module_tree']//a[text()='"+sugar().leads.moduleNamePlural+"']").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}