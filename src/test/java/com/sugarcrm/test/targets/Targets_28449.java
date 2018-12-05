package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_28449 extends SugarTest {
	FieldSet targetsData = new FieldSet();

	public void setup() throws Exception {
		targetsData = testData.get(testName).get(0);

		// Login as an Admin user
		sugar().login();

		// Add a target records, such as having first name, last name  
		// TODO: VOOD-444
		sugar().targets.navToListView();
		sugar().targets.listView.create();
		sugar().targets.createDrawer.showMore();
		sugar().targets.createDrawer.getEditField("firstName").set(targetsData.get("firstName"));
		sugar().targets.createDrawer.getEditField("lastName").set(testName);
		// TODO: VOOD-1005		
		VoodooControl addBtnCtrl = new VoodooControl("a", "css", ".btn.addEmail");
		VoodooControl emailAddressFieldCtrl = sugar().targets.createDrawer.getEditField("emailAddress");

		// Add 3 Email addresses (such as steven@cnn.com as primary email, tim@cnn.com as invalid email, neil@cnn.com as Optout email)
		emailAddressFieldCtrl.set(targetsData.get("firstEmailAddress"));
		addBtnCtrl.click();
		emailAddressFieldCtrl.set(targetsData.get("secondEmailAddress"));
		addBtnCtrl.click();
		new VoodooControl("button", "css", ".control-group.email:nth-child(2) button[data-emailproperty='invalid_email']").click();
		emailAddressFieldCtrl.set(targetsData.get("thirdEmailAddress"));
		addBtnCtrl.click();
		new VoodooControl("button", "css", ".control-group.email:nth-child(3) button[data-emailproperty='opt_out']").click();

		// Add office phone and save the target record
		sugar().targets.createDrawer.getEditField("phoneWork").set(targetsData.get("phoneWork"));
		sugar().targets.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that OOB Default fields that display for Target Search Result
	 * 
	 * @throws Exception
	 */
	@Test
	public void Targets_28449_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for Global Search
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		VoodooControl moduleIconCtrl = searchResultCtrl.getChildElement("span", "css", ".label-" + sugar().targets.moduleNamePlural);

		// In global search enter a query string to search for First Name in Target record
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		globalSearchCtrl.set(targetsData.get("firstName"));
		VoodooUtils.waitForReady();

		// Verify that the Target icon appear. Only First Name is highlighted 
		moduleIconCtrl.assertExists(true);
		searchResultCtrl.assertContains(testName, true);
		// TODO: VOOD-1951
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " h3 strong").assertEquals(targetsData.get("firstName"), true);

		// In global search enter a query string to search for Last Name in Target record
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		globalSearchCtrl.set(testName);
		VoodooUtils.waitForReady();

		// Verify that the Target icon appear. Only Last Name Name is highlighted 
		moduleIconCtrl.assertExists(true);
		searchResultCtrl.assertContains(targetsData.get("firstName"), true);
		// TODO: VOOD-1951
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " h3 strong").assertEquals(testName, true);

		// Enter a query to search for office number
		globalSearchCtrl.set(targetsData.get("phoneWork"));
		VoodooUtils.waitForReady();

		// Verify that the Target icon appear and the found string or number are highlighted
		moduleIconCtrl.assertExists(true);
		searchResultCtrl.assertContains(targetsData.get("firstName") + " " + testName, true);
		// TODO: VOOD-1951
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " .secondary span:nth-child(2) strong").assertEquals(targetsData.get("phoneWork").substring(1), true);

		// Enter a query to search for emails. Such as query = cnn.com
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		globalSearchCtrl.set(targetsData.get("firstEmailAddress").substring(7));
		VoodooUtils.waitForReady();

		// Verify that the Target icon appear and all the email addresses should be highlighted
		// TODO: VOOD-1951
		new VoodooControl("span", "css", searchResultCtrl.getHookString() + " .secondary span").assertEquals(targetsData.get("primaryEmail"), true);
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " .secondary span strong").assertEquals(targetsData.get("firstEmailAddress"), true);
		new VoodooControl("span", "css", searchResultCtrl.getHookString() + " .secondary span:nth-child(3)").assertEquals(targetsData.get("email"), true);
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " .secondary span:nth-child(4) strong").assertEquals(targetsData.get("secondEmailAddress"), true);
		new VoodooControl("strong", "css", searchResultCtrl.getHookString() + " .secondary span:nth-child(4) strong:nth-child(2)").assertEquals(targetsData.get("thirdEmailAddress"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}