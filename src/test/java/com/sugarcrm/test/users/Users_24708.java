package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24708 extends SugarTest {
	DataSource userDataSet;
	UserRecord demoUser;
	FieldSet charSetData;

	public void setup() throws Exception {
		charSetData = testData.get(testName+"_1").get(0);
		userDataSet = testData.get(testName);
		sugar.login();
	}

	/**
	 * Verify if a new user's import/export character set can be overridden from the default character set set by the ADMIN for all users. 
	 * @throws Exception
	 */
	@Test
	public void Users_24708_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-820
		new VoodooControl("a", "id", "locale").click();
		new VoodooControl("select", "css", "[name='default_export_charset']").set(charSetData.get("char_set_admin"));
		new VoodooControl("input", "css", "[name='save']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
				
		// create demo user
		demoUser = (UserRecord) sugar.users.create(userDataSet.get(0));
		sugar.users.navToListView();
		sugar.users.listView.clickRecord(1);
		sugar.users.detailView.edit();

		// Go to the users -> Advanced option, change the Import/Export character set to something different from UTF-8
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.userPref.getControl("tab4").click();
		new VoodooControl("select", "css", "[name='default_export_charset']").set(charSetData.get("char_set_user"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		// Logout as Admin and login as demo user
		sugar.logout();
		sugar.login(demoUser);
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Click Advanced tab
		new VoodooControl("a", "id", "tab2").click();
		new VoodooControl("slot", "css", "#settings tr:nth-child(7) td:nth-child(2) slot").assertEquals(charSetData.get("char_set_user"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}