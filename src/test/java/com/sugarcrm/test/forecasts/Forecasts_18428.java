package com.sugarcrm.test.forecasts;

import com.sugarcrm.sugar.SugarUrl;
import org.junit.Test;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_18428 extends SugarTest {
	Configuration config;
	String baseUrl;
	DataSource users;
	FieldSet firstUser, secondUser, thirdUser;
	UserRecord myFirstUser, mySecondUser, myThirdUser;

	public void setup() throws Exception {
		// TODO The VoodooControl references in Forecasts will be replaced by
		// VOOD-725 Forecasts Lib
		config = VoodooUtils.getGrimoireConfig();
		baseUrl = new SugarUrl().getBaseUrl();
		users = testData.get("Forecasts_18428");
		firstUser = users.get(0);
		secondUser = users.get(1);
		thirdUser = users.get(2);
		sugar.login();
		// Enable default Forecast settings
		sugar.navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").click();
		// Pause needed here as the Forecasts refreshes the screen a couple of
		// times after reset
		VoodooUtils.pause(10000);
		// Create 3 users and set up 2 as Managers
		// TODO VOOD-444 - API Creating relationships
		myFirstUser = (UserRecord) sugar.users.create(firstUser);
		mySecondUser = (UserRecord) sugar.users.create(secondUser);
		myThirdUser = (UserRecord) sugar.users.create(thirdUser);
		// Log out of Sugar as admin
		VoodooUtils.focusDefault();
		VoodooUtils.pause(2000);
		sugar.logout();
	}

	/**
	 * Verify that menu item "Assign Quota" appears in actions dropdown on
	 * manager worksheet in Forecast module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_18428_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// The first user is a Manager so assign Quota options should be visible
		sugar.login(myFirstUser);
		sugar.navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary")
				.click();
		new VoodooControl("a", "css", ".fld_assign_quota").assertVisible(true);
		sugar.logout();
		VoodooUtils.pause(2000);

		// The third user is not a Manager so assign Quota options should not be
		// visible
		sugar.login(thirdUser);
		sugar.navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary")
				.click();
		new VoodooControl("a", "css", ".fld_assign_quota").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
