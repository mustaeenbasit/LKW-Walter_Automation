package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_29945 extends SugarTest {
	UserRecord chris;

	public void setup() throws Exception {
		sugar().login();
		// create a new user
		chris = (UserRecord)sugar().users.create();
	}

	/**
	 * Verify that advance search works for email field in users module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29945_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().users.navToListView();
		
		VoodooUtils.focusFrame("bwc-frame");
		// Click Advanced Link on the Users List View
		sugar().users.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// Input the email address in Advance Search panel
		// TODO: VOOD-975
		FieldSet userdefaultData = sugar().users.getDefaultData();
		new VoodooControl("input", "id", "email_advanced").set(userdefaultData.get("emailAddress"));
		VoodooControl advancedSearchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		// Click on Search button
		advancedSearchBtnCtrl.click();
		VoodooUtils.waitForReady();
		// Verify the searched user records on the basis of the "Email" id entered
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(7) a").assertEquals(userdefaultData.get("emailAddress"), true);
		// navigate back to basic search 
		sugar().users.listView.getControl("basicSearchLink").click();
		VoodooUtils.focusDefault();
		
		// clear the search form
		sugar().users.listView.clearSearchForm();
		sugar().users.listView.submitSearchForm();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}