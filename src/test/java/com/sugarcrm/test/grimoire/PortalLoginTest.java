package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.PortalTest;

public class PortalLoginTest extends PortalTest {
	FieldSet portalUser = new FieldSet();

	public void setup() throws Exception {
		// Setup Portal Access
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().contacts.api.create(portalSetupData);	
		portalUser.put("userName", portalSetupData.get("portalName"));
		portalUser.put("password", portalSetupData.get("password"));

		sugar().login();

		// Enable portal
		sugar().admin.portalSetup.enablePortal();
	}

	@Test
	public void portalLoginAfterSugarLogout() throws Exception {
		VoodooUtils.voodoo.log.info("Running portalLoginAfterSugarLogout()...");

		sugar().logout(); // force logout after setup to verify portal login

		// Log into Portal
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		VoodooUtils.voodoo.log.info("If this message appears, that means we logged in successfully without error.");
		portal.navbar.userAction.assertVisible(true);

		VoodooUtils.voodoo.log.info("portalLoginAfterSugarLogout() completed...");
	}

	/**
	 * NOTE:
	 * This test purely tests that when you are logged into Sugar and then navigate away and log into Portal,
	 * your session is still active when you navigate back to sugar(). This means once a test that follows this
	 * behavior is executed and Cleanup is run, the go(baseURL) will not require us to log in.
	 *
	 * @throws Exception
	 */
	@Test
	public void portalLoginAfterSugarLogin() throws Exception {
		VoodooUtils.voodoo.log.info("Running portalLoginAfterSugarLogin()...");

		// Log into Portal
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		VoodooUtils.voodoo.log.info("If this message appears, that means we logged in successfully without error.");
		portal.navbar.userAction.assertVisible(true);

		VoodooUtils.voodoo.log.info("portalLoginAfterSugarLogin() completed.");
	}

	/**
	 * NOTE:
	 * This test tests the behavior of PortalTest base cleanup after a test finishes while logged into Sugar
	 * as a Regular user.
	 *
	 * @throws Exception
	 */
	@Test
	public void portalTestFinishesWhileLoggedInAsRegularUser() throws Exception {
		VoodooUtils.voodoo.log.info("Running portalTestFinishesWhileLoggedInAsRegularUser()...");

		// Log into Portal
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		VoodooUtils.voodoo.log.info("If this message appears, that means we logged in successfully without error.");
		portal.navbar.userAction.assertVisible(true);

		sugar().loginScreen.navigateToSugar();
		sugar().alerts.waitForLoadingExpiration(30000);
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		VoodooUtils.voodoo.log.info("portalTestFinishesWhileLoggedInAsRegularUser() completed.");
	}

	/**
	 * NOTE:
	 * This test is to simulate a failure in a test execute that triggers Cleanup to occur.
	 *
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void portalTestBaseCleanupAfterFailure() throws Exception {
		VoodooUtils.voodoo.log.info("Running portalTestBaseCleanupAfterFailure()...");

		// Log into Portal
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);

		portal.loginScreen.getControl("login").click(); // This will fail, but the @Test expects this

		VoodooUtils.voodoo.log.info("portalTestBaseCleanupAfterFailure() completed.");
	}

	public void cleanup() throws Exception {}
}