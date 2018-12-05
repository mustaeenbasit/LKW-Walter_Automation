package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models the login screen of SugarCRM.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class NewUserWizard extends View {
	protected static NewUserWizard newUserwizard;

	public static NewUserWizard getInstance() throws Exception {
		if (newUserwizard == null) newUserwizard = new NewUserWizard();
		return newUserwizard;
	}

	/**
	 * Initializes the login screen.
	 * @throws Exception
	 */
	public NewUserWizard() throws Exception {
		super();

		// Common to all pages
		addControl("nextButton", "a", "css", "a[name='next_button']");
		
		// First page of new user wizard
		addControl("firstName", "input", "css", ".fld_first_name input");
		addControl("lastName", "input", "css", ".fld_last_name input");
		addControl("phone", "input", "css", ".fld_phone_work input");
		addControl("emailAddress", "input", "css", ".fld_email input");
		
		// Second page of new user wizard
		addControl("previousButton", "a", "css", "a[name='previous_button']");
		addSelect("timeZone", "a", "css", ".fld_timezone a");
		addSelect("timeZoneSelection", "span", "css", ".fld_timezone span");
		addSelect("timeFormat", "a", "css", ".fld_timepref a");
		
		// Third page of new user wizard
		addControl("startSugarButton", "a", "css", "a[name='start_sugar_button']");
	}

	/**
	 * Clicks on the Next Button in the new user wizard.
	 * 
	 * @throws Exception
	 */
	public void clickNextButton() throws Exception {
		getControl("nextButton").click();
		VoodooUtils.waitForReady(60000);
	}
	
	/**
	 * Clicks the start sugar button the last step of new user wizard.
	 * 
	 * @throws Exception
	 */
	public void clickStartSugar() throws Exception {
		getControl("startSugarButton").click();
	}

	/**
	 * Perform actions for a users first log in.
	 *
	 * Once done this method will leave the user at the Home Page screen.
	 *
	 * @param userData FieldSet of data of the user to setup.
	 * @throws Exception
	 */
	public void setupNewUser(FieldSet userData) throws Exception {
		getControl("nextButton").waitForVisible();

		if(!(getControl("emailAddress").getText().equals(userData.get("emailAddress")))) {
			getControl("emailAddress").set(userData.get("emailAddress"));
			getControl("phone").click();
		}
		clickNextButton();

		getControl("timeZoneSelection").waitForVisible();
		if(!(getControl("timeZoneSelection").getText().equals(userData.get("timeZone")))) {
			getControl("timeZone").set(userData.get("timeZone"));
			VoodooUtils.waitForReady(60000);
		}
		clickNextButton();

		getControl("startSugarButton").waitForVisible();
		clickStartSugar();
	}
}
