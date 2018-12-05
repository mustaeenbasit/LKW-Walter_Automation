package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.StandardRecord;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;

import java.util.ArrayList;

public class NavbarTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void navigateModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running navigateModules()...");

		// Nav to Admin Tools
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("moduleTitle").assertEquals("Administration", true);
		VoodooUtils.focusDefault();

		// Nav to Accounts
		sugar().accounts.navToListView();
		sugar().accounts.listView.getControl("moduleTitle").assertEquals(sugar().accounts.moduleNamePlural, true);

		// Nav to Cases
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("moduleTitle").assertEquals(sugar().cases.moduleNamePlural, true);

		// Nav to Quotes
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("moduleTitle").assertEquals("Search Quotes", true);
		VoodooUtils.focusDefault();

		// Nav to Contacts
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.getControl("moduleTitle").assertEquals(sugar().contacts.moduleNamePlural, true);

		// Nav to Documents
		sugar().navbar.navToModule(sugar().documents.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.listView.getControl("moduleTitle").assertEquals("Search Documents", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("navigateModules() complete.");
	}

	@Ignore("SC-3506 and SC-3893 - Unexpected unsaved warning when create record from quick create menu.")
	@Test
	public void navigateQuickCreate() throws Exception {
		VoodooUtils.voodoo.log.info("Running  navigateQuickCreate()...");

		// Quick Create Accounts
		sugar().navbar.quickCreateAction(sugar().accounts.moduleNamePlural);
		sugar().accounts.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().accounts.createDrawer.getEditField("name").assertVisible(true);
		sugar().accounts.createDrawer.cancel();

		// Quick Create Contacts
		sugar().navbar.quickCreateAction(sugar().contacts.moduleNamePlural);
		sugar().contacts.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().contacts.createDrawer.getEditField("firstName").assertVisible(true);
		sugar().contacts.createDrawer.cancel();

		// Quick Create Opportunity
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().opportunities.createDrawer.getEditField("name").assertVisible(true);
		sugar().opportunities.createDrawer.cancel();

		// Quick Create Leads
		sugar().navbar.quickCreateAction(sugar().leads.moduleNamePlural);
		sugar().leads.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().leads.createDrawer.getEditField("firstName").assertVisible(true);
		sugar().leads.createDrawer.cancel();

		// Quick Create Documents
		sugar().navbar.quickCreateAction(sugar().documents.moduleNamePlural);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.editView.getControl("cancelButton").waitForVisible();
		sugar().documents.editView.getEditField("documentName").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().documents.editView.cancel();

		// Quick Create Calls
		sugar().navbar.quickCreateAction(sugar().calls.moduleNamePlural);
		sugar().calls.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().calls.createDrawer.getEditField("name").assertVisible(true);
		sugar().calls.createDrawer.cancel();

		// Quick Create Meetings
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);
		sugar().meetings.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().meetings.createDrawer.getEditField("name").assertVisible(true);
		sugar().meetings.createDrawer.cancel();

		// Quick Create Tasks
		sugar().navbar.quickCreateAction(sugar().tasks.moduleNamePlural);
		sugar().tasks.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().tasks.createDrawer.getEditField("subject").assertVisible(true);
		sugar().tasks.createDrawer.cancel();

		// Quick Create Notes
		sugar().navbar.quickCreateAction(sugar().notes.moduleNamePlural);
		sugar().notes.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().notes.createDrawer.getEditField("subject").assertVisible(true);
		sugar().notes.createDrawer.cancel();

		// Quick Create Revenue Line Items
		sugar().navbar.quickCreateAction(sugar().revLineItems.moduleNamePlural);
		sugar().revLineItems.createDrawer.getControl("cancelButton").waitForVisible();
		sugar().revLineItems.createDrawer.getEditField("name").assertVisible(true);
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("navigateQuickCreate() complete.");
	}

	@Test
	public void setGlobalSearchByString() throws Exception {
		VoodooUtils.voodoo.log.info("Running setGlobalSearchByString()...");

		sugar().navbar.searchModule(sugar().accounts.moduleNamePlural);
		sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".label-" + sugar().accounts.moduleNamePlural).assertExists(true);

		VoodooUtils.voodoo.log.info("setGlobalSearchByString() complete.");
	}

	@Test
	public void setGlobalSearchBySingleModule() throws Exception {
		VoodooUtils.voodoo.log.info("Running setGlobalSearchBySingleModule()...");

		sugar().navbar.searchModule(sugar().contacts);
		sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertExists(true);

		VoodooUtils.voodoo.log.info("setGlobalSearchBySingleModule() complete.");
	}

	@Test
	public void setGlobalSearchByModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running setGlobalSearchByModules()...");

		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().opportunities);
		modules.add(sugar().bugs);
		modules.add(sugar().campaigns);

		sugar().navbar.searchModules(modules);
		for(Module module : modules) {
			sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".label-" + module.moduleNamePlural).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("setGlobalSearchByModules() complete.");
	}

	@Test
	public void setGlobalSearchAllModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running setGlobalSearchAllModules()...");

		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().opportunities);
		modules.add(sugar().bugs);
		modules.add(sugar().campaigns);

		sugar().navbar.searchModules(modules);
		for(Module module : modules) {
			sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".label-" + module.moduleNamePlural).assertExists(true);
		}

		sugar().navbar.searchAll();
		sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".non-module-label").assertExists(true);

		VoodooUtils.voodoo.log.info("setGlobalSearchAllModules() complete.");
	}

	@Test
	public void clickSearchResultRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickSearchResultRecord()...");

		StandardRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().navbar.setGlobalSearch(myAccount.getRecordIdentifier());
		sugar().navbar.clickSearchResult(myAccount);
		sugar().accounts.recordView.getDetailField("name").assertContains(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("clickSearchResultRecord() complete.");
	}

	@Test
	public void clickSearchResultRow() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickSearchResultRow()...");

		StandardRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().navbar.setGlobalSearch(myAccount.getRecordIdentifier());
		sugar().navbar.clickSearchResult(1);
		sugar().accounts.recordView.getDetailField("name").assertContains(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("clickSearchResultRow() complete.");
	}

	@Test
	public void viewAllResults() throws Exception {
		VoodooUtils.voodoo.log.info("Running viewAllResults()...");

		DataSource ds = testData.get(testName);
		sugar().accounts.api.create(ds);
		sugar().navbar.setGlobalSearch(sugar().accounts.moduleNameSingular);
		sugar().navbar.viewAllResults();

		// TODO: VOOD-1848
		new VoodooControl("div", "css", ".fld_reset_button a[name='reset_button']").assertVisible(true);

		VoodooUtils.voodoo.log.info("viewAllResults() complete.");
	}

	@Test
	public void getGlobalSearchModulePillMultipleModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running getGlobalSearchModulePillMultipleModules()...");

		ArrayList<Module> modules = new ArrayList<>();
		modules.add(sugar().accounts);
		modules.add(sugar().calls);
		modules.add(sugar().cases);
		modules.add(sugar().contacts);
		sugar().navbar.searchModules(modules);

		sugar().navbar.getGlobalSearchModulePill(1).assertEquals("Multiple Modules", true);

		sugar().navbar.searchAll();

		VoodooUtils.voodoo.log.info("getGlobalSearchModulePillMultipleModules() complete.");
	}

	@Test
	public void getGlobalSearchModulePillThreeModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running getGlobalSearchModulePillThreeModules()...");

		ArrayList<Module> modules = new ArrayList<>();
		modules.add(sugar().accounts);
		modules.add(sugar().calls);
		modules.add(sugar().cases);
		sugar().navbar.searchModules(modules);

		sugar().navbar.getGlobalSearchModulePill(2).assertEquals("Cl", true);

		sugar().navbar.searchAll();

		VoodooUtils.voodoo.log.info("getGlobalSearchModulePillThreeModules() complete.");
	}

	@Test
	public void getGlobalSearchModulePillAll() throws Exception {
		VoodooUtils.voodoo.log.info("Running getGlobalSearchModulePillAll()...");

		sugar().navbar.getControl("globalSearch").click();

		sugar().navbar.getGlobalSearchModulePill(1).assertEquals("All", true);

		sugar().navbar.searchAll();

		VoodooUtils.voodoo.log.info("getGlobalSearchModulePillAll() complete.");
	}

	public void cleanup() throws Exception {}
}