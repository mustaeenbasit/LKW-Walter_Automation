package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24755 extends SugarTest {
	UserRecord chrisUser;

	public void setup() throws Exception {
		sugar().login();
		chrisUser = (UserRecord) sugar().users.create();
		sugar().logout();
		
		// Login as qauser
		sugar().login(chrisUser);
	}

	/**
	 *  Admin User can see user specified Export Delimiter
	 * @throws Exception
	 */
	@Test
	public void Users_24755_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Fetching '&' from env_specialchars.csv
		FieldSet punctuations = testData.get("env_specialchars").get(0);
		String ampersand = punctuations.get("punctuation").substring(7, 8);
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl userProfileAdvanceTab = sugar().users.editView.getControl("advancedTab");
		userProfileAdvanceTab.click();

		// TODO: VOOD-563
		// Setting & in export Delimeter in Chris user profile
		VoodooControl exportDelimeter = new VoodooControl("input", "css", "[name='export_delimiter']");
		exportDelimeter.set(ampersand);
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as admin
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusDefault();
		sugar().users.listView.basicSearch(sugar().users.getDefaultData().get("fullName"));
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		userProfileAdvanceTab.click();
		
		// Verifying export delimeter in chris user profile by admin login
		exportDelimeter.assertEquals(ampersand, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}