package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models the login screen of SugarCRM.
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class LoginScreen extends View {
	protected static LoginScreen loginScreen;

	public static LoginScreen getInstance() throws Exception {
		if (loginScreen == null) loginScreen = new LoginScreen();
		return loginScreen;
	}

	/**
	 * Initializes the login screen.
	 * @throws Exception
	 */
	public LoginScreen() throws Exception {
		super();
		addControl("loginUserName", "input", "css", ".fld_username input");
		addControl("loginPassword", "input", "css", ".fld_password input");
		addControl("login", "a", "css", ".fld_login_button a");
	}

	/**
	 * Logs into SugarCRM as the user configured in grimoire.properties
	 * @throws Exception
	 */
	public void login() throws Exception {
		getControl("loginUserName").waitForVisible(30000);
		getControl("loginUserName").set(VoodooUtils.getGrimoireConfig().getValue("sugar_user", "admin"));
		getControl("loginPassword").set(VoodooUtils.getGrimoireConfig().getValue("sugar_pass", "asdf"));
		getControl("login").click();
		// TODO: This was originally 75000 seconds total, see VOOD-1378
		sugar().alerts.waitForLoadingExpiration(150000);

		sugar().dashboard.getControl("firstDashlet").waitForVisible();
	}
	// TODO: Other login methods.
// To be implemented at a later date.
//	public void login(String username, String password) throws Exception {
//		
//	}
//	
//	public void login(UserRecord user) throws Exception {
//		
//	}
	
	public void login(FieldSet userData) throws Exception {
		getControl("loginUserName").waitForVisible(300000);
		getControl("loginUserName").set(userData.get("userName"));
		getControl("loginPassword").set(userData.get("password"));
		getControl("login").click();

		sugar().alerts.waitForLoadingExpiration(60000);
		try {
			sugar().newUserWizard.getControl("nextButton").waitForVisible();
		} catch (Exception e) {
			VoodooUtils.voodoo.log.warning("New User Wizard did not appear when expected.");
		}
		
		if(sugar().newUserWizard.getControl("nextButton").queryVisible()) {
			sugar().newUserWizard.setupNewUser(userData);
		}
		sugar().alerts.waitForLoadingExpiration(60000);
		sugar().dashboard.getControl("firstDashlet").waitForVisible();
	}
	
	/**
	* Navigate to the SugarCRM Application URL.
	* <p>
	* @throws Exception
	*/
	public void navigateToSugar() throws Exception {
		VoodooUtils.go(new SugarUrl().getBaseUrl());
		sugar().alerts.waitForLoadingExpiration();
	}
}
