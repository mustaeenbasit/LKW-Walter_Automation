package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_27897 extends SugarTest {
	FieldSet myData;
	
	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify no alert pops up when XSS is injected in the URL
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_27897_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin Module
		sugar().navbar.navToAdminTools();
		
		// In URL after module, enter script "<<SCRIPT>alert("XSS");//<</SCRIPT>"
		String currentUrl = VoodooUtils.getUrl();
		String url = currentUrl + myData.get("script");
		VoodooUtils.voodoo.log.info("Go to url " + url + "...");
		VoodooUtils.go(url);
		
		// Verify "Bad data passed in" message is shown along with Return to Home link
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("body", "css", "body").assertEquals(myData.get("message"), true);
		VoodooControl linkToHome = new VoodooControl("a", "css", "body a");
		linkToHome.assertContains(myData.get("linkToHome"), true);
		
		// Click "Return to Home" link
		linkToHome.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that Home page is displayed
		sugar().home.dashboard.assertContains(myData.get("atHomePage"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}