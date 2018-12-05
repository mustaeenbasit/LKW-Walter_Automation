package com.sugarcrm.test.users;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_27803 extends SugarTest {
	UserRecord user1;
	FieldSet myUsers = new FieldSet();

	public void setup() throws Exception {
		myUsers = testData.get(testName).get(0);
		
		// Calculate TimeZone Offset via library to account for DST shift
		DateTimeZone zone = DateTimeZone.getDefault();
		int currentOffsetMilliseconds = zone.getOffset(Instant.now());
		int currentOffsetHours = currentOffsetMilliseconds / (60 * 60 * 1000);
		String timeZone = myUsers.get("timeZone") + currentOffsetHours + ":00)";
		myUsers.put("timeZone", timeZone);

		sugar().login();

		// create users
		user1 = (UserRecord) sugar().users.create(myUsers);
		sugar().logout();
	}

	/**
	 * Verify that Shortcut link isn't available when a user first time log in setup
	 * @throws Exception
	 */
	@Test
	public void Users_27803_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login via user1
		sugar().loginScreen.getControl("loginUserName").set(myUsers.get("userName"));
		sugar().loginScreen.getControl("loginPassword").set(myUsers.get("password"));
		sugar().loginScreen.getControl("login").click();
		sugar().alerts.waitForLoadingExpiration(); // Wait for the 'Loading...'

		// user1 profile setup
		sugar().newUserWizard.setupNewUser(myUsers);

		// TODO: VOOD-1361
		// Verify that Shortcuts link isn't there
		new VoodooControl("button", "css", "footer [data-voodoo-name='footer-actions'] button:nth-child(1)").assertContains("Shortcuts", false);

		// Wait for visible dashboard
		sugar().dashboard.getControl("firstDashlet").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}