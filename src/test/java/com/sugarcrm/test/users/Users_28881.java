package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_28881 extends SugarTest { 
	public void setup() throws Exception {
		// Login as Admin user
		sugar().login();
	}

	/**
	 * "Go back to previous page" link should display while passing bad request
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_28881_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// For Side car Modules
		// Navigate to any side car module
		sugar().contacts.navToListView();

		// Add any bad request or url at the end of sugar link (e.g AND IF(version() like ‘5%’, sleep(10), ‘false’))--) and click enter
		String newUrlForSideCarModules = VoodooUtils.getUrl() + customData.get("badRequestURL");
		VoodooUtils.go(newUrlForSideCarModules);

		// Verify that the Error: Invalid Request, Please contact your Sugar Administrator for more details. Go back to previous page.
		// TODO: VOOD-2036
		new VoodooControl("h1", "css", "#content .error-message h1").assertEquals(customData.get("dataNotAvailable"), true);
		new VoodooControl("p", "css", "#content .error-message p").assertEquals(customData.get("doNotHavePermissionMsg"), true);
		new VoodooControl("p", "css", "#content .error-message p:nth-child(3)").assertEquals(customData.get("pleaseTryAgainMsg"), true);
		VoodooControl goBackToPreviousPageCtrl = new VoodooControl("a", "css", "#content .error-message a");
		goBackToPreviousPageCtrl.assertEquals(customData.get("goBackToPreviousPageMsg"), true);

		// Click on "Go back to previous page." link
		goBackToPreviousPageCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the instance navigates to Contacts list view
		// TODO: VOOD-1887 - Once resolved uncomment the commented lines
		// sugar().contacts.listView.assertVisible(true);
		// sugar().contacts.listView.getControl("createButton").assertVisible(true);
		sugar().contacts.listView.getControl("moduleTitle").assertEquals(sugar().contacts.moduleNamePlural, true);

		// For BWC module
		// Navigate to any BWC module
		sugar().documents.navToListView();

		// Add any bad request or url at the end of sugar link (e.g AND IF(version() like ‘5%’, sleep(10), ‘false’))--) and click enter
		String newUrlForBWCModules = VoodooUtils.getUrl() + customData.get("badRequestURL");
		VoodooUtils.go(newUrlForBWCModules);

		// Verify that the 'Bad data passed in; Return to Home'
		// TODO: VOOD-2036
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("body", "css", "body").assertContains(customData.get("badDataMsg"), true);
		VoodooControl linkToHome = new VoodooControl("a", "css", "body a");
		linkToHome.assertContains(customData.get("returnToHomeMsg"), true);

		// Click on the "Return to Home" link
		linkToHome.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify that Home page is displayed
		// TODO: VOOD-1887 -  Once resolved uncomment the commented line
		// sugar().home.dashboard.assertVisible(true);
		sugar().home.dashboard.getControl("dashboardTitle").assertEquals(customData.get("myDashboard"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}