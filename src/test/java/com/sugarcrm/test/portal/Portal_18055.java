package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_18055 extends PortalTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		sugar().login();

		// Enable Bugs & KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable portal in admin, portal contact set up
		sugar().admin.portalSetup.enablePortal();

		// TODO: VOOD-1833
		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
		sugar().logout();
	}

	/**
	 * Verify Portal header action menus
	 * @throws Exception
	 */
	@Test
	public void Portal_18055_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Cases
		portal().navbar.clickModuleDropdown(sugar().cases);

		// Verify that the Cases module drop down list included "Create Case" and "View Cases" option.
		portal().cases.menu.getControl("createCase").assertVisible(true);
		portal().cases.menu.getControl("viewCases").assertVisible(true);

		// Verify Create Case view
		portal().cases.menu.getControl("createCase").click();
		VoodooUtils.waitForReady();

		// Verify "Create Case" page should displayed properly.
		portal().cases.recordView.getEditField("name").assertVisible(true);
		portal().cases.recordView.cancel();

		// Click on "viewCases"
		portal().navbar.clickModuleDropdown(sugar().cases);
		portal().cases.menu.getControl("viewCases").click();
		VoodooUtils.waitForReady();

		// Verifyt "View Cases" page should displayed properly.
		portal().cases.listView.assertIsEmpty();

		// Bugs
		portal().navbar.clickModuleDropdown(sugar().bugs);

		// Verify that the Bugs module drop down list included "Report Bug" and "View Bugs" option.
		portal().bugs.menu.getControl("createBug").assertVisible(true);
		portal().bugs.menu.getControl("viewBugs").assertVisible(true);

		// Click on Report bug
		portal().bugs.menu.getControl("createBug").click();
		VoodooUtils.waitForReady();

		// "Report Bug" page should displayed properly.
		portal().bugs.recordView.getEditField("name").assertVisible(true);
		portal().bugs.recordView.cancel(); // Click on "Cancel" button

		// Click on Bugs view
		portal().navbar.clickModuleDropdown(sugar().bugs);
		portal().bugs.menu.getControl("viewBugs").click();
		VoodooUtils.waitForReady();

		// Verify that the "View Bugs" page should displayed properly.
		portal().bugs.listView.assertIsEmpty();

		// Knowledge Base
		portal().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// Verify Knowledge Base module drop down list included "View Articles" option.
		portal().knowledgeBase.menu.getControl("viewArticles").assertExists(true);

		portal().knowledgeBase.menu.getControl("viewArticles").click();
		VoodooUtils.waitForReady();

		// Verify that "View Articles" page should displayed properly.
		portal().knowledgeBase.listView.assertExists(true);
		portal().knowledgeBase.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}