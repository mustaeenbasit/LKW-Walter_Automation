package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_24712 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Employee - Mass Update_Verify that no "LBL" labels are displayed in employee mass update panel.
	 * @throws Exception
	 */
	@Test
	public void Employees_24712_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// TODO VOOD-1041 -need lib support of employees module
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on Action link
		new VoodooControl("span", "css", "#selectLinkTop > li > span").click();
		
		// Verify that Mass Update link not exist
		new VoodooControl("li", "xpath", "//*[@id='selectLinkTop']/li/ul//li[contains(.,'Mass Update')]").assertExists(false);
		
		// Verify that Mass Update LBL not exist
		new VoodooControl("span", "css", "#massupdate_form > table > tbody > tr > td > h3 > span").assertContains("Mass Update", false);
		
		// Click on mass all checkbox
		new VoodooControl("input", "id", "massall_top").click();
		
		// Click on Mass Update link
		new VoodooControl("a", "id", "massupdate_listview_top").click();
		
		// Verify that Mass Update LBL not exist
		new VoodooControl("span", "css", "#massupdate_form > table > tbody > tr > td > h3 > span").assertContains("Mass Update", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}