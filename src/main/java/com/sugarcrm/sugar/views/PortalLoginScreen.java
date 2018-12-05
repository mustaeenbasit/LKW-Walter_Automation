package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.PortalAppModel;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models the login screen of SugarCRM Portal.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class PortalLoginScreen extends View {
	protected static PortalLoginScreen loginScreen;

	public static PortalLoginScreen getInstance() throws Exception {
		if (loginScreen == null) loginScreen = new PortalLoginScreen();
		return loginScreen;
	}

	/**
	 * Initializes the login screen.
	 * @throws Exception
	 */
	public PortalLoginScreen() throws Exception {
		super("div", "css", ".welcome");
		addControl("loginUserName", "input", "css", "input[name='username']");
		addControl("loginPassword", "input", "css", "input[name='password']");
		addControl("login", "a", "css", "a[name='login_button']");
		
		addControl("forgotPassword", "a", "id", "forgot-password");
		addControl("signUp", "a", "css", "a[name='signup_button']");
	}

	/**
	 * Log into Portal using the user data provided.
	 * <p>
	 * Can only be used to log into Portal.<br>
	 * User data must be for an active portal user.<br>
	 * @param userData FieldSet of user data to use to log into Portal
	 * @throws Exception
	 */
	public void login(FieldSet userData) throws Exception {
		getControl("loginUserName").waitForVisible(300000);
		getControl("loginUserName").set(userData.get("userName"));
		getControl("loginPassword").set(userData.get("password"));
		getControl("login").click();

		loginWait();
	}
	
	private void loginWait() throws Exception {
		portal().alerts.waitForLoadingExpiration(120000); // Wait for the 'Loading...' alert dialog to NOT be visible
		// TODO: VOOD-1030 -- Portal Application timing, control definitions
		VoodooUtils.pause(1000);
		new VoodooControl("a", "css", ".dashboard .thumbnail .headerpane h1 a").waitForVisible();
	}
	
	/**
	 * Navigate to Portal.
	 * @throws Exception
	 */
	public void navigateToPortal() throws Exception {
		VoodooUtils.go(new SugarUrl().getPortalUrl());
		portal().alerts.waitForLoadingExpiration(240000);
		getControl("loginUserName").waitForVisible();
	}
	
	/**
	 * Clicks on the Sign Up link to start the new Portal User sign up process.
	 * <p>
	 * Takes you to the Portal Sign Up screen when used.<br>
	 * @throws Exception
	 */
	public void startSignup() throws Exception {
		getControl("signUp").click();
	}
}
