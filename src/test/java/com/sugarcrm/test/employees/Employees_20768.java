package com.sugarcrm.test.employees;


import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20768 extends SugarTest {
	FieldSet ds;
	String adminEmail = "";
	
	public void setup() throws Exception {
		ds = testData.get(testName).get(0);
		sugar().login();
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		adminEmail = new VoodooControl("a", "css", "#email_span a").getText();
		VoodooUtils.focusDefault();
	}

	/**
	 * Set the default email address for Test Notification.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20768_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("emailSettings").click();
		
		// Config outgoing mail server.
		sugar().admin.emailSettings.getControl("gmailButton").click();
		sugar().admin.emailSettings.getControl("userName").set(ds.get("userName"));
		
		if(sugar().admin.emailSettings.getControl("passwordLink").queryVisible()){
			sugar().admin.emailSettings.getControl("passwordLink").click();
		}
		sugar().admin.emailSettings.getControl("password").set(ds.get("password"));
				
		// Click Send Test Email button.
		new VoodooControl("input", "css", "[value='Send Test Email']").click();
		
		// Verify, the email address shown in the input box of 'Email Address For Test Notification' is
		// that of admin and not that has been input in Email settings
		new VoodooControl("input", "id", "outboundtest_from_address").assertContains(ds.get("userName"), false);
		new VoodooControl("input", "id", "outboundtest_from_address").assertContains(adminEmail, true);
		
		new VoodooControl("input", "css", "#testOutbound tr:nth-child(2) td input:nth-child(1)").click();
		
		VoodooUtils.waitForReady(30000); // Pause need to let the message visible.
		new VoodooControl("div", "css", ".bd").waitForVisible();
		
		// Verify that email is sent.
		new VoodooControl("div", "css", ".bd").assertContains(ds.get("message"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
