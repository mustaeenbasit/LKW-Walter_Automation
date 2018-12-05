package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20005 extends SugarTest {	
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Administrator set "Login Lockout" Attempts times
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20005_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Updating new Password settings by Admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();

		// Updating Password Lockout settings by Admin
		// TODO: VOOD-989
		ds = testData.get(testName);
		new VoodooControl("input", "css", "#required_lockout_exp_login").set(ds.get(0).get("lockout"));
		new VoodooControl("input", "css", "#passwordsetting_lockoutexpirationlogin").set(ds.get(0).get("number"));
		new VoodooControl("input", "css", "#passwordsetting_lockoutexpirationtime").set(ds.get(0).get("time"));
		sugar().admin.passwordManagement.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		sugar().logout();

		// Trying to login with qauser with in-correct password multiple times to get error message 
		UserRecord qaUser = new UserRecord(sugar().users.getQAUser());
		qaUser.put("password", ds.get(0).get("password"));
		for(int i =0;i<=Integer.parseInt(ds.get(0).get("number"));i++) {
			sugar().loginScreen.getControl("loginUserName").set(qaUser.get("userName"));
			sugar().loginScreen.getControl("loginPassword").set(qaUser.get("password"));
			sugar().loginScreen.getControl("login").click();
		}

		// Assert the Lockout error message on home page after several unsuccessful login attempts
		sugar().loginScreen.getControl("login").waitForVisible();
		sugar().alerts.getError().assertElementContains(ds.get(0).get("assert1"), true);
		sugar().alerts.closeAllError();

		// Assert the error message after login with Admin and navigate to qauser record view
		sugar().login();
		qaUser.navToRecord();
		VoodooUtils.focusFrame("bwc-frame"); 

		// TODO: VOOD-994
		new VoodooControl("div", "css", "div#sugarMsgWindow div.bd").assertContains(ds.get(0).get("assert2"), true);
		sugar().users.editView.getControl("confirmCreate").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}