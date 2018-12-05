package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19981 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the "Forgot Password?" link on log in page
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19981_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		
		// TODO VOOD-989
		new VoodooControl("input", "css", "#forgotpassword_checkbox").set(ds.get(0).get("forgotPass"));
		new VoodooControl("input", "css", "#captcha_id").set(ds.get(0).get("reCAPTCHA"));
		sugar().admin.passwordManagement.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForAlertExpiration();
		sugar().logout();
		
		// TODO VOOD
		new VoodooControl("a", "xpath", "//form[@class='tcenter']//a[text()='"+ds.get(0).get("link")+"']").click();
		new VoodooControl("input", "css", "form[name='forgotpassword'] input[name='username'][placeholder='"+ds.get(0).get("username")+"']").assertVisible(true);
		new VoodooControl("input", "css", "form[name='forgotpassword'] input[name='email'][placeholder='"+ds.get(0).get("email")+"']").assertVisible(true);
		new VoodooControl("a", "css", "a[name='forgotPassword_button']").assertVisible(true);
		new VoodooControl("a", "css", "a[name='cancel_button']").click();
		
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}