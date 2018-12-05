package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.PortalAppModel;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;

/**
 * Models the signup screen of SugarCRM Portal.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class PortalSignUpScreen extends View {
	protected static PortalSignUpScreen signup;

	public static PortalSignUpScreen getInstance() throws Exception {
		if (signup == null)
			signup = new PortalSignUpScreen();
		return signup;
	}

	/**
	 * Initializes the signup screen.
	 * @throws Exception
	 */
	public PortalSignUpScreen() throws Exception {
		super("div", "css", ".welcome");

		// Sign up fields
		addControl("firstName", "input", "css", ".fld_first_name.edit input[name='first_name']");
		addControl("lastName", "input", "css", ".fld_last_name.edit input[name='last_name']");
		addControl("email", "input", "css", ".fld_email.edit input[name='email']");
		addControl("phoneWork", "input", "css", ".fld_phone_work.edit input[name='phone_work']");
		addSelect("country", "a", "css", ".fld_country.edit a");
		addSelect("state", "a", "css", ".fld_state.edit a");
		addControl("company", "input", "css", ".fld_company.edit input[name='company']");
		addControl("title", "input", "css", ".fld_title.edit input[name='title']");

		// Sign up controls
		addControl("signup", "a", "css", ".fld_signup_button.edit a[name='signup_button']");
		addControl("cancel", "a", "css", ".fld_cancel_button.edit a[name='cancel_button']");
		
		// Second page of signup
		addControl("back", "a", "css", ".fld_cancel_button.edit a[name='cancel_button']"); // Yes its the same css as cancel, go figure.
		addControl("signupMessage", "div", "css", ".alert.alert-success.tleft"); // Basically an alerts success div
	}

	/**
	 * Sign up for portal access to SugarCRM.
	 * <p>
	 * NOTE:<br>
	 * This method creates a Sugar Lead Record that will need to be converted to a Contact 
	 * and then given Portal Access for use in Portal!<br>
	 * 
	 * @param leadData FieldSet of data to create contact record.
	 * @return LeadRecord with passed in leadData.
	 * @throws Exception
	 */
	public LeadRecord signUp(FieldSet leadData) throws Exception {
		String state = leadData.get("state");
		leadData.remove("state");
		for(String controlName : leadData.keySet()){
			if (leadData.get(controlName) != null && getControl(controlName) != null) {
				String toSet = leadData.get(controlName);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " field to: " + toSet);
				VoodooUtils.pause(100);
				getControl(controlName).set(toSet);
				VoodooUtils.pause(100);
			} else {
				VoodooUtils.voodoo.log.warning("Field " + controlName +
					" was in the supplied record data, but no value was given or no Edit Control was available.");
			}
		}
		getControl("state").set(state);
		VoodooUtils.pause(100);
		getControl("signup").click();
		portal().alerts.waitForLoadingExpiration();
		getControl("back").click();
		leadData.put("state", state);
		return new LeadRecord(leadData);
	}
}
