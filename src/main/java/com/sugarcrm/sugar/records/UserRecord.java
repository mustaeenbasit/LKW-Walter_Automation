package com.sugarcrm.sugar.records;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class UserRecord extends BWCRecord {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().users;
	}

	/**
	 * Log in as this user.
	 * 
	 * @throws Exception
	 */
	public void login() throws Exception {
		sugar().loginScreen.getControl("loginUserName").set(get("userName"));
		sugar().loginScreen.getControl("loginPassword").set(get("password"));
		sugar().loginScreen.getControl("login").click();
		sugar().alerts.waitForLoadingExpiration(30000);
	}

	/**
	 * Navigates to the record's detailView and deletes the User record
	 * Overrides the BWC record delete because of the extra Cancel for the
	 * reassign objects
	 */
	@Override
	public void delete() throws Exception {
		VoodooUtils.voodoo.log.info("Deleting record " + getRecordIdentifier()
				+ ".");

		VoodooUtils.focusDefault();
		navToRecord();
		module.detailView.delete();
		new VoodooControl("button", "css", ".first-child button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", ".button[value='Cancel']").click();
		VoodooUtils.focusDefault();
		sugar().users.listView.clearSearchForm();
		sugar().users.listView.submitSearchForm();
	}

	/**
	 * delete() will navigate and delete the user record that calls this
	 * function. Re-assigns Users record to no-one.
	 * Delete its private team also
	 * 
	 * @param user
	 *            - User Record to be deleted
	 * @throws Exception
	 */
	public void delete(UserRecord user) throws Exception {
		delete();
		sugar().teams.deleteTeam(user.getRecordIdentifier());
	}

	/**
	 * getRecordIdentifier will return the User Record User Name
	 * @return - String of the records Identification (userName)
	 */
	@Override
	public String getRecordIdentifier() {
		return get("userName");
	}

	/**
	 * Navigate to the User Record object
	 */
	public void navToRecord() throws Exception {
		// Navigate to ListView of Record Object module
		module.navToListView();

		// Search for the User to make it the only record in the ListView
		VoodooUtils.focusFrame("bwc-frame");
		module.listView.getControl("nameBasic").set(get("firstName"));
		module.listView.getControl("searchButton").click();
		VoodooUtils.pause(500);
		// Click on record to go to its detailview
		VoodooUtils.voodoo.log.info("Clicking on '" + get("firstName") + " "
				+ get("lastName") + "'...");
		module.listView.getControl("firstRecordListView").click();
		VoodooUtils.pause(1500);
		VoodooUtils.focusDefault();
	}

	/**
	 * Login and perform the user setup needed for this users first login.
	 * 
	 * @throws Exception
	 */
	public void firstLogin() throws Exception {
		sugar().login(this);
	}
} // end UserRecord