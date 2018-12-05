package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_26066 extends PortalTest {
	FieldSet  portalSetupData, portalUserSetting;
	AccountRecord myAccount;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		portalUserSetting = testData.get(testName+"_user").get(0);
		portalSetupData = testData.get("env_portal_contact_setup").get(0);

		myAccount = (AccountRecord)sugar().accounts.api.create();
		
		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();
		
		// Set up one contact as a portal user.		
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		
		// Link Contact with Accounts
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	
		sugar().logout();
	}
	
	/**
	 * Verify that password input box works after dismiss tooltip in Portal login
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_26066_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		portal().loginScreen.navigateToPortal();
		// click on forget password link
		new VoodooControl("a", "id", "forgot-password").click();
		
		// Verify that tooltip have content
		new VoodooControl("div", "css", "body > div.tooltip.fade.top.in .tooltip-inner").assertContains("You need to contact your Sugar Admin to reset your password", true);
		new VoodooControl("input", "css", "input[name='username']").click(); // Click on other place on login page
		new VoodooControl( "input", "css", "input[name='password']").click(); // Click on input password
		new VoodooControl( "input", "css", "input[name='password']").set(portalUserSetting.get("password")); // Type some content to the input
		new VoodooControl( "input", "css", "input[name='password']").assertContains(portalUserSetting.get("password"), true); // Verify that input password have content

		// Fill user-name and password to login to Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify portal user login successfully
		new VoodooControl("div", "css", ".dashboard .thumbnail").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " completed...");
	}
 
	public void cleanup() throws Exception {}
} 