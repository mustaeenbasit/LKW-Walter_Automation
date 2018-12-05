package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class PortalNavbarTests extends PortalTest {
	public void setup() throws Exception {
		// Portal contact set up
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().accounts.api.create();
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		sugar().login();

		// Enable portal in admin, portal contact set up
		sugar().admin.portalSetup.enablePortal();

		// Case module enable - link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
		sugar().logout();

		// Navigate to portal URL, login as portal user
		portal.loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal.login(portalUser);
	}

	@Test
	public void portalNavbarTests() throws Exception {
		VoodooUtils.voodoo.log.info("Running  portalNavbarTests()...");

		portal.navbar.navToProfile();

		// TODO: VOOD-1053
		new VoodooControl("span", "css", "span[data-fieldname='full_name']").assertExists(true);

		// Verify Cases module menu options
		portal.navbar.navToModule(portal.cases.moduleNamePlural);
		String expected = portal.cases.moduleNamePlural;
		String found = portal.cases.listView.getModuleTitle();
		assertTrue("Cases getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));		
		portal.navbar.clickModuleDropdown(portal.cases);
		portal.cases.menu.getControl("createCase").assertVisible(true);
		portal.cases.menu.getControl("viewCases").assertVisible(true);
		portal.navbar.clickModuleDropdown(portal.cases);

		// Verify Bugs module menu options
		portal.navbar.navToModule(portal.bugs.moduleNamePlural);
		expected = portal.bugs.moduleNamePlural;
		found = portal.bugs.listView.getModuleTitle();
		assertTrue("Bugs getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));		
		portal.navbar.clickModuleDropdown(portal.bugs);
		portal.bugs.menu.getControl("createBug").assertVisible(true);
		portal.bugs.menu.getControl("viewBugs").assertVisible(true);
		portal.navbar.clickModuleDropdown(portal.bugs);

		// Verify Knowledge Base menu options
		portal.navbar.navToModule(portal.knowledgeBase.moduleNamePlural);
		expected = "Knowledge Base";
		found = portal.knowledgeBase.listView.getModuleTitle();
		assertTrue("Knowledge Base getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));		
		portal.navbar.clickModuleDropdown(portal.knowledgeBase);
		portal.knowledgeBase.menu.getControl("viewArticles").assertVisible(true);
		portal.navbar.clickModuleDropdown(portal.knowledgeBase);

		VoodooUtils.voodoo.log.info("portalNavbarTests() completed...");
	}

	public void cleanup() throws Exception {}
}