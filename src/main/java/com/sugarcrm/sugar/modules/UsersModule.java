package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.sugar.views.View;

/**
 * User module object which contains tasks associated with the User module like
 * create
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class UsersModule extends BWCModule {
	protected static UsersModule module;
	public FieldSet qaUser = new FieldSet();
	public View userPref;

	public static UsersModule getInstance() throws Exception {
		if (module == null)
			module = new UsersModule();
		return module;
	}

	private UsersModule() throws Exception {
		moduleNameSingular = "User";
		moduleNamePlural = "Users";
		bwcSubpanelName = "Users";
		recordClassName = UserRecord.class.getName();
		userPref = new View();

		// TODO: Load more fields into the HashMap
		loadFields();

		// Sugar7 UI Elements
		editView.addControl("passwordTab", "a", "id", "tab2");
		editView.addControl("profileTab", "a", "id", "tab1");
		editView.addControl("reportsTo", "input", "id", "reports_to_name");
		editView.addControl("reportsToSelect", "button", "id", "btn_reports_to_name");
		editView.addControl("save", "input", "id", "SAVE_HEADER");
		editView.addControl("confirmCreate", "a", "css", "#sugarMsgWindow .container-close");
		editView.addControl("confirmCreatePopup", "div", "css", "#sugarMsgWindow");

		editView.addControl("advancedTab", "a", "id", "tab4");

		userPref.addControl("edit", "a", "id", "edit_button");	// edit button
		userPref.addControl("tab1", "a", "id", "tab1");			// User Profile tab
		userPref.addControl("tab4", "a", "id", "tab4");			// Advanced tab
		userPref.addControl("advanced_grouping_seperator", "input", "id", "default_number_grouping_seperator");	// 1000s separator
		userPref.addControl("advanced_decimal_separator", "input", "id", "default_decimal_seperator");	// Decimal Symbol
		userPref.addControl("advanced_preferedCurrency", "select", "id", "currency_select");	// Preferred Currency
		userPref.addControl("advanced_showpreferedCurrency", "checkbox", "id", "currency_show_preferred");	//	Show Preferred Currency
		userPref.addControl("advanced_significant_digits", "select", "id", "sigDigits");	// Currency Significant Digits
		userPref.addControl("advanced_dateFormat", "select", "name", "dateformat");			// Date Format
		userPref.addControl("advanced_timeFormat", "select", "name", "timeformat");			// Time Format
		userPref.addControl("advanced_timeZone"  , "select", "name", "timezone");			// Time Zone:
		userPref.addControl("advanced_nameFormat", "select", "id", "default_locale_name_format"); // Name Display Format
		userPref.addControl("save", "input", "id", "SAVE_HEADER");	// Save button
		userPref.addControl("newUserWizard", "input", "name", "ut");

		popupSearch.addControl("userName", "input", "id", "user_name_advanced");
		popupSearch.addControl("search", "input", "id", "search_form_submit");
		popupSearch.addControl("firstRecordPopupView", "a", "css",
				"table.list.view tr.oddListRowS1 td.oddListRowS1 a");

		listView.addControl("nameBasic", "input", "css", "#search_name_basic");
		listView.addControl("searchButton", "input", "css",
				"#search_form_submit");
		listView.addControl("clearButton", "input", "css", "#search_form_clear");
		listView.addControl("firstRecordListView", "a", "css",
				"#MassUpdate table.list.view td:nth-of-type(3) a");

		// Override link Column and regenerate affected part of listView
		listView.setLinkColumn(3);

		// TODO: Move this into a CSV file.
		qaUser.put("userName", "qauser");
		qaUser.put("password", "QAuser1");
		qaUser.put("emailAddress", "qa.sugar.qa.79@gmail.com");
		qaUser.put("newPassword", "QAuser1");
		qaUser.put("confirmPassword", "QAuser1");
		qaUser.put("firstName", "qauser");
		qaUser.put("lastName", "qauser");
		qaUser.put("timeZone", "America/Los Angeles (GMT-7:00)");

		// Populate default data.
		for (String currentFieldName : fields.keySet()) {
			SugarField currentField = fields.get(currentFieldName);
			String defaultValue = currentField.get("default_value");

			if (defaultValue != null && !(defaultValue.isEmpty()))
				defaultData.put(currentFieldName, defaultValue);
		}

		// Users Module Menu Items
		menu = new Menu(this);
		menu.addControl("createNewUser", "a", "css",
				"li[data-module='Users'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_USER']");
		menu.addControl("createGroupUser", "a", "css",
				"li[data-module='Users'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_GROUP_USER']");
		menu.addControl("createPortalApiUser", "a", "css",
				"li[data-module='Users'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PORTAL_USER']");
		menu.addControl("reassignRecords", "a", "css",
				"li[data-module='Users'] ul[role='menu'] a[data-navbar-menu-item='LNK_REASSIGN_RECORDS']");
		menu.addControl("importUsers", "a", "css",
				"li[data-module='Users'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_USERS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Creates a single user record via the UI from the data in a FieldSet. This
	 * method overloads the BWCModule method with the same signature because the
	 * Users module has non-standard create/edit/detail views.
	 *
	 * @param	userData	a FieldSet containing the data for the new record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet userData) throws Exception {
		VoodooUtils.voodoo.log.info("Reconciling user data.");
		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(userData);

		VoodooUtils.voodoo.log.info("Creating a user via UI...");
		// Navigate to Users List View
		navToListView();

		// Start the Create process
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");

		// Find the elements in the fieldset and set their values
		VoodooUtils.focusFrame("bwc-frame"); // focus on iframe document to get
		// access to elements in
		// backwards compatible mode
		for (String controlName : recordData.keySet()) {
			if (recordData.get(controlName) != null) {
				if (editView.getEditField(controlName) == null)
					continue;
				String toSet = recordData.get(controlName);
				if (controlName.equalsIgnoreCase("reportsTo")) {
					// TODO: Update flow when user create is accessible withing
					// the Sugar7 UI
					editView.getControl("reportsToSelect").click();
					VoodooUtils.focusWindow(1); // focus on user search pop up
					popupSearch.getControl("userName").set(toSet);
					// TODO: VOOD-1787 - Lib support needed to use waitForReady() in a secondary popup window
					// waitForReady will not work here as this a popup window and no Voodoo object is available here
					VoodooUtils.pause(500);
					popupSearch.getControl("search").click();
					popupSearch.getControl("firstRecordPopupView").click();
					// TODO: VOOD-1787 - Lib support needed to use waitForReady() in a secondary popup window
					// waitForReady will not work here as this a popup window and no Voodoo object is available here
					VoodooUtils.pause(500);
					VoodooUtils.focusWindow(0); // focus on main browser tab
					VoodooUtils.focusFrame("bwc-frame"); // focus on iframe
					// document to get
					// access to
					// elements in
					// backwards
					// compatible mode
				} else if (controlName.contains("Password")) { // Check element,
					// if it has the
					// word password
					// in it,
					// perform the
					// following
					// steps.
					editView.getControl("passwordTab").click();
					VoodooUtils.waitForReady();
					VoodooUtils.voodoo.log.fine("Setting " + controlName
							+ " to " + toSet);
					editView.getEditField(controlName).set(toSet);
					VoodooUtils.waitForReady();
					editView.getControl("profileTab").click();
				} else if (controlName.contains("newUserWizard")) {
					VoodooUtils.voodoo.log.fine("Setting " + controlName + " to " + toSet);
					editView.getControl("advancedTab").click();
					VoodooUtils.waitForReady();
					userPref.getControl("advanced_timeZone").scrollIntoView();
					VoodooUtils.waitForReady();
					editView.getEditField(controlName).set(toSet);
					VoodooUtils.waitForReady();
					editView.getControl("profileTab").click();
					VoodooUtils.waitForReady();
				} else if (controlName.contains("formatDate")) {
					VoodooUtils.voodoo.log.fine("Setting " + controlName + " to " + toSet);
					userPref.getControl("advanced_dateFormat").scrollIntoView();
					editView.getControl("advancedTab").click();
					VoodooUtils.waitForReady();
					userPref.getControl("advanced_dateFormat").set(toSet);
					editView.getControl("profileTab").click();
					VoodooUtils.waitForReady();
				}
				else {
					VoodooUtils.voodoo.log.fine("Setting " + controlName
							+ " to " + toSet);
					editView.getEditField(controlName).set(toSet);
				}
			}
		}
		// Click Save
		editView.getControl("save").click();
		VoodooUtils.waitForReady(40000);
		// TODO: VOOD-1963 - Make waitForReady() robust enough to deal with running as well waiting in-queue processes
		VoodooUtils.waitForReady(40000);
		editView.getControl("confirmCreate").click();
		VoodooUtils.focusDefault(); // focus back to default Sugar7 UI
		Record toReturn = (Record) Class
				.forName(UsersModule.module.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		return toReturn;
	}

	/**
	 * simple helper method to navigate to the user's listview
	 */
	public void navToListView() throws Exception {
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); // focus on iframe document to get
		// access to elements in
		// backwards compatible mode
		sugar().admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Method to return the data for qauser
	 *
	 * @return	a FieldSet containing the default record data for qauser.
	 * @throws Exception
	 */
	public FieldSet getQAUser() throws Exception {
		return qaUser.deepClone();
	}

	/**
	 * Method to set user preferences based on the specified field set.
	 *
	 * @param	userPrefs	a FieldSet of values to set in user preferences.
	 *
	 * @throws Exception
	 */
	public void setPrefs(FieldSet userPrefs) throws Exception {
		sugar().navbar.navToProfile();

		VoodooUtils.focusFrame("bwc-frame"); // focus on iframe document to get
		// access to elements in
		// backwards compatible mode
		VoodooUtils.pause(1000);
		userPref.getControl("edit").click();
		for (String controlName : userPrefs.keySet()) {
			VoodooUtils.voodoo.log.info(controlName);
			if (userPrefs.get(controlName) != null) {
				String toSet = userPrefs.get(controlName);
				if (controlName.contains("advanced_")) {   // if control's prefix begins with advanced, go to advanced tab
					userPref.getControl("tab4").click();
					VoodooUtils.voodoo.log.info("Setting " + controlName
							+ " to " + toSet);
					userPref.getControl(controlName).set(toSet);
					VoodooUtils.pause(300);
					userPref.getControl("tab1").click();  // return to initial tab after each set
					VoodooUtils.pause(300);
				} else {
					VoodooUtils.voodoo.log.info("Setting " + controlName
							+ " to " + toSet);
					userPref.getControl(controlName).set(toSet);
					VoodooUtils.pause(300);
				}
			}
		}
		userPref.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
	}

}// end UsersModule
