package com.sugarcrm.test.admin;

import org.junit.Ignore;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_27013 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify once you logout from sugar, it will logout all if you have mutiple tabs open
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1165: Need lib support to open a new tab on same browser")
	@Test
	public void Admin_27013_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		String pageUrl = VoodooUtils.getUrl();
//		VoodooUtils.iface.wd.findElement(By.tagName("body")).sendKeys(Keys.TAB);
//		VoodooUtils.focusFrame(1);
		VoodooUtils.go(pageUrl);
//		VoodooUtils.focusFrame(0);
		sugar().logout();
		
		// Verify that once you logout from sugar, it will logout all if you have mutiple tabs open
		VoodooUtils.focusFrame(1);
		new VoodooControl("a", "css", ".help-block a.btn-link").assertContains("Forgot Password?", true);
		VoodooUtils.focusFrame(0);
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
