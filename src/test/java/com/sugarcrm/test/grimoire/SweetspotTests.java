package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class SweetspotTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void assertElementDefinitions() throws Exception {
		VoodooUtils.voodoo.log.info("Running assertElementDefinitions()...");

		sugar().sweetspot.getControl("sweetspotBar").assertExists(true);

		sugar().sweetspot.show();

		// assert search Bar and Configure Icon
		sugar().sweetspot.getControl("searchBox").assertVisible(true);
		sugar().sweetspot.getControl("configureIcon").assertVisible(true);

		// Configure search result controls
		sugar().sweetspot.search("Acc");
		sugar().sweetspot.getControl("searchResult").assertVisible(true);
		sugar().sweetspot.getControl("searchResultKeywords").assertExists(true);
		sugar().sweetspot.getControl("searchResultActions").assertExists(true);
		sugar().sweetspot.getControl("searchResultRecords").assertExists(true);

		// Goto configuration page
		sugar().sweetspot.configure();

		// assert Configuration page controls
		sugar().sweetspot.getControl("titleConfigurationPage").assertVisible(true);
		sugar().sweetspot.getControl("cancelConfiguration").assertVisible(true);
		sugar().sweetspot.getControl("saveConfiguration").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysAction").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysKeyword").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysRemove").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysAdd").assertVisible(true);
		sugar().sweetspot.getControl("theme").assertVisible(true);

		sugar().sweetspot.getControl("hotkeysAction").click();
		VoodooUtils.waitForReady();
		sugar().sweetspot.getControl("hotkeysActionInput").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysActionInput").set("Import process email template");
		VoodooUtils.waitForReady();
		sugar().sweetspot.getControl("hotkeysActionMatch").assertVisible(true);
		sugar().sweetspot.getControl("hotkeysActionMatch").
		assertContains("Import Process Email Template", true);
		sugar().sweetspot.getControl("hotkeysActionMatch").click();
		sugar().sweetspot.cancelConfiguration();

		VoodooUtils.voodoo.log.info("Running assertElementDefinitions() complete.");
	}

	@Test
	public void showAndHideCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running showAndHideCheck()...");

		sugar().sweetspot.getControl("sweetspotBar").assertVisible(false);

		sugar().sweetspot.show();
		sugar().sweetspot.getControl("sweetspotBar").assertVisible(true);
		sugar().sweetspot.hide();
		sugar().sweetspot.getControl("sweetspotBar").assertVisible(false);

		VoodooUtils.voodoo.log.info("Running showAndHideCheck() complete.");
	}

	@Test
	public void searchResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running searchResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Help");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info("Running searchResultCheck() complete.");
	}

	@Test
	public void searchNilResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running searchResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Garbage Garbage");
		sugar().sweetspot.assertResultNotVisible();
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info("Running searchResultCheck() complete.");
	}

	@Test
	public void keywordsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running keywordsResultCheck()...");

		// Additionally, this test also checks methods on Sweetspot configuration page such as
		// addKeyword(), removeKeyword() and saveConfiguration()

		// Add keyword
		sugar().sweetspot.addKeyword("Calls", "myCalls");

		// Goto Home
		sugar().sweetspot.getControl("homeCube").click();
		VoodooUtils.waitForReady();
		sugar().sweetspot.show();

		// Check Keyword search
		sugar().sweetspot.search("myCalls");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertKeywordsResultVisible();
		sugar().sweetspot.assertActionsResultVisible();

		// Delete keyword
		VoodooUtils.waitForReady();
		sugar().sweetspot.removeKeyword("Calls");

		VoodooUtils.voodoo.log.info("Running keywordsResultCheck() complete.");
	}

	@Test
	public void actionsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running actionsResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Leads");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertActionsResultVisible();
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info("Running actionsResultCheck() complete");
	}

	@Test
	public void recordsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running recordsResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Administrator");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertActionsResultVisible();
		sugar().sweetspot.assertRecordsResultVisible();
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info("Running recordsResultCheck() complete.");
	}

	@Test
	public void getAndClickKeywordsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running getAndClickKeywordsResultCheck()...");

		// Additionally, this test also checks methods on Sweetspot configuration page such as
		// addKeyword(), removeKeyword() and saveConfiguration()

		// Add keyword
		sugar().sweetspot.addKeyword("Calls", "myCalls");

		// Goto Home
		sugar().sweetspot.getControl("homeCube").click();
		VoodooUtils.waitForReady();
		sugar().sweetspot.show();

		// Check Keyword search
		sugar().sweetspot.search("myCalls");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertKeywordsResultVisible();
		sugar().sweetspot.getKeywordsResult().assertContains("Calls", true);
		sugar().sweetspot.clickKeywordsResult();

		// Verify Calls ListView is displayed
		sugar().calls.listView.verifyModuleTitle("Calls");
		//sugar().calls.listView.getControl("moduleTitle").assertContains("Calls", true);

		// Delete keyword
		sugar().sweetspot.removeKeyword("Calls");

		VoodooUtils.voodoo.log.info("Running getAndClickKeywordsResultCheck() complete.");
	}

	@Test
	public void getAndClickActionsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running getAndClickActionsResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Accounts");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertActionsResultVisible();
		sugar().sweetspot.getActionsResult().assertContains("Accounts", true);
		sugar().sweetspot.clickActionsResult();

		// Verify Calls ListView is displayed
		sugar().accounts.listView.verifyModuleTitle("Accounts");
		//sugar().calls.listView.getControl("moduleTitle").assertContains("Accounts", true);

		VoodooUtils.voodoo.log.info("Running getAndClickActionsResultCheck() complete.");
	}

	@Test
	public void getAndClickRecordsResultCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running getAndClickRecordsResultCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("Administrator");
		sugar().sweetspot.assertResultVisible();
		sugar().sweetspot.assertRecordsResultVisible();
		sugar().sweetspot.getRecordsResult().assertContains("Administrator", true);
		sugar().sweetspot.clickRecordsResult();

		// Verify Administrator page is displayed
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.detailView.getControl("moduleTitle").assertContains("Administrator", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("Running getAndClickRecordsResultCheck() complete.");
	}

	@Test
	public void cancelConfigurationCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running cancelConfigurationCheck()...");

		sugar().sweetspot.show();
		sugar().sweetspot.configure();
		sugar().sweetspot.setHotkeysActionInput("Calls");
		sugar().sweetspot.setHotkeysKeyword("myCalls");
		sugar().sweetspot.addHotkey();
		sugar().sweetspot.cancelConfiguration();

		sugar().sweetspot.show();

		// Check Keyword search
		sugar().sweetspot.search("myCalls");
		sugar().sweetspot.assertResultVisible();

		// assert that no keyword search result is available
		sugar().sweetspot.getControl("searchResultKeywords").assertVisible(false);

		VoodooUtils.voodoo.log.info("Running cancelConfigurationCheck() complete.");
	}

	@Test
	public void navToModuleCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running navToModuleCheck()...");

		sugar().sweetspot.navToModule(sugar().accounts);
		sugar().accounts.listView.verifyModuleTitle("Accounts");

		sugar().sweetspot.navToModule(sugar().users);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.listView.verifyModuleTitle("Search Users");
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("Running navToModuleCheck() complete.");
	}

	@Test
	public void navToModuleMenuItemCheck() throws Exception {
		VoodooUtils.voodoo.log.info("Running navToModuleMenuItemCheck()...");

		sugar().sweetspot.navToModuleMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.recordView.getControl("moduleIDLabel").assertContains("Ac", true);

		sugar().sweetspot.navToModuleMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.listView.verifyModuleTitle("Create");
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("Running navToModuleMenuItemCheck() complete.");
	}

	public void cleanup() throws Exception {}
}