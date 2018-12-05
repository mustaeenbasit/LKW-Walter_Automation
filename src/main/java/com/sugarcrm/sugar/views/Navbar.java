package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.records.Record;

import java.util.ArrayList;
import java.util.HashMap;


// TODO: Rewrite this whole thing to actually model the navbar.
/**
 * Models the SugarCRM navbar.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class Navbar extends View {
	protected static Navbar navbar;

	public View userAction;
	public View quickCreate;
	public View search;

	HashMap<Module, Menu> menus = new HashMap<Module, Menu>();

	public static Navbar getInstance() throws Exception {
		if (navbar == null) navbar = new Navbar();
		return navbar;
	}

	/**
	 * Initialize the navbar.
	 * @throws Exception
	 */
	private Navbar() throws Exception {
		super("div", "css", ".navbar");

		addControl("showAllModules", "a", "css", getHookString() + " *[data-container='module-list'] *[data-action='more-modules']");
		addControl("globalSearch", "input", "css", ".navbar div[data-voodoo-name='quicksearch-bar'] input");

		userAction = new View("li", "css", getHookString() + " li[data-menu='user-actions']");
		userAction.addControl("userActions", "button", "css", userAction.getHookString() + " button[data-toggle='dropdown']");
		userAction.addControl("admin", "a", "css", userAction.getHookString() + " li.administration a");
		userAction.addControl("profile", "a", "css", userAction.getHookString() + " li.profileactions-profile a");
		userAction.addControl("logout", "a", "css", userAction.getHookString() + " li.profileactions-logout a");

		quickCreate = new View("div", "css", getHookString() + " [data-voodoo-name='quickcreate']");
		quickCreate.addControl("quickCreateActions", "button", "css", quickCreate.getHookString() + " .dropdown-toggle");
		quickCreate.addControl("actionMenu", "div", "css", quickCreate.getHookString() + " [role='menu']");
		quickCreate.addControl("Accounts", "button", "css", quickCreate.getHookString() + " [data-module='Accounts']");
		quickCreate.addControl("Contacts", "button", "css", quickCreate.getHookString() + " [data-module='Contacts']");
		quickCreate.addControl("Opportunities", "button", "css", quickCreate.getHookString() + " [data-module='Opportunities']");
		quickCreate.addControl("Leads", "button", "css", quickCreate.getHookString() + " [data-module='Leads']");
		quickCreate.addControl("Documents", "button", "css", quickCreate.getHookString() + " [data-module='Documents']");
		quickCreate.addControl("Emails", "button", "css", quickCreate.getHookString() + " [data-action='email']");
		quickCreate.addControl("Calls", "button", "css", quickCreate.getHookString() + " [data-module='Calls']");
		quickCreate.addControl("Meetings", "button", "css", quickCreate.getHookString() + " [data-module='Meetings']");
		quickCreate.addControl("Tasks", "button", "css", quickCreate.getHookString() + " [data-module='Tasks']");
		quickCreate.addControl("Notes", "button", "css", quickCreate.getHookString() + " [data-module='Notes']");
		quickCreate.addControl("RevenueLineItems", "button", "css", quickCreate.getHookString() + " [data-module='RevenueLineItems']");

		// Modules disabled by default
		quickCreate.addControl("Bugs", "button", "css", quickCreate.getHookString() + " [data-module='Bugs']");
		quickCreate.addControl("Cases", "button", "css", quickCreate.getHookString() + " [data-module='Cases']");
		quickCreate.addControl("Contracts", "button", "css", quickCreate.getHookString() + " [data-module='Contracts']");
		quickCreate.addControl("Projects", "button", "css", quickCreate.getHookString() + " [data-module='Projects']");
		quickCreate.addControl("Targets", "button", "css", quickCreate.getHookString() + " [data-module='Targets']");

		search = new View("div", "css", ".navbar .search");
		String searchHookString = search.getHookString();
		search.addControl("searchTitle", "div", "css", searchHookString + " .search-headerpane.fld_title div");
		search.addControl("cancelSearch", "button", "css", searchHookString + " div[data-voodoo-name='quicksearch-button'] button");
		search.addControl("searchModuleDropdown", "button", "css", searchHookString + " [track='click:globalsearch-dropdown']");
		search.addControl("searchModuleList", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list");
		search.addControl("searchModuleIcons", "span", "css", searchHookString + " [data-label='module-icons']");
		search.addControl("searchAll", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-action='select-all']");
		search.addControl("searchContacts", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Contacts']");
		search.addControl("searchAccounts", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Accounts']");
		search.addControl("searchOpportunities", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Opportunities']");
		search.addControl("searchCases", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Cases']");
		search.addControl("searchNotes", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Notes']");
		search.addControl("searchCalls", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Calls']");
		search.addControl("searchEmails", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Emails']");
		search.addControl("searchMeetings", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Meetings']");
		search.addControl("searchTasks", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Tasks']");
		search.addControl("searchLeads", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Leads']");
		search.addControl("searchProjects", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Project']");
		search.addControl("searchContracts", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Contracts']");
		search.addControl("searchQuotes", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Quotes']");
		search.addControl("searchQuotedLineItems", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Products']");
		search.addControl("searchProductCategories", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='ProductCategories']");
		search.addControl("searchManufacturers", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Manufacturers']");
		search.addControl("searchBugs", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Bugs']");
		search.addControl("searchProjectTasks", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='ProjectTask']");
		search.addControl("searchCampaigns", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Campaigns']");
		search.addControl("searchDocuments", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Documents']");
		search.addControl("searchEmployees", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Employees']");
		search.addControl("searchProspects", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Prospects']");
		search.addControl("searchProspectLists", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='ProspectLists']");
		search.addControl("searchTags", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='Tags']");
		search.addControl("searchKnowledgeBase", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='KBContents']");
		search.addControl("searchRevenueLineItems", "li", "css", searchHookString + " .dropdown-menu.module-dropdown-list [data-module='RevenueLineItems']");
		search.addControl("viewAllResults", "a", "css", searchHookString + " .view-all-results"); // requires more than 5 results
		search.addControl("searchResults", "ul", "css", searchHookString + " .dropdown-menu.search-results");
	}

	/**
	 * Start a Quick Create action
	 * Leaves you on the create screen for the action desired.
	 * @param selection String of the desired action (e.g. "Accounts")
	 * @throws Exception
	 */
	public void quickCreateAction(String selection) throws Exception {
		openQuickCreateMenu();
		quickCreate.getControl(selection).click();
		VoodooUtils.waitForReady();
	}


	/**
	 * Clicks on the down caret to open the User Actions menu.
	 * The navbar must be visible on the current page.
	 * Leaves you on the same page with the User Actions menu open.
	 * @throws Exception
	 */
	public void toggleUserActionsMenu() throws Exception {
		userAction.getControl("userActions").click();
		VoodooUtils.waitForReady();
		userAction.waitForVisible();
	}

	/**
	 * Clicks on the "+" icon to open the Quick Create Actions menu.
	 * The navbar must be visible on the current page.
	 * Leaves you on the same page with the Quick Create action menu open.
	 * Note: This action opens Quick Create menu.
	 * @throws Exception
	 */
	public void openQuickCreateMenu() throws Exception {
		quickCreate.getControl("quickCreateActions").click();
		quickCreate.getControl("actionMenu").waitForVisible();
	}

	/**
	 * Clicks on the "caret" to activate the dropdown menu for a given module
	 * @param module desired module
	 * @throws Exception
	 */
	public void clickModuleDropdown(Module module) throws Exception {
		new VoodooControl("button", "css", "li[data-module='" + module.moduleNamePlural + "'] button[data-toggle='dropdown']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks on a module action e.g. clicking on Accounts -> Create Account
	 * @param module desired module
	 * @param menuItem menu tab action to click on e.g. createAccount, viewAccounts
	 */
	public void selectMenuItem(Module module, String menuItem) throws Exception {
		navToModule(module.moduleNamePlural);
		clickModuleDropdown(module);
		if(module.isBwc()) {
			// A bit of a hack because when the "Loading..." div closes, so does
			// the menu.
			VoodooUtils.waitForAlertExpiration();
			if(!menus.get(module).getControl(menuItem).queryVisible()) {
				clickModuleDropdown(module);
			}
		}
		menus.get(module).getControl(menuItem).click();
		VoodooUtils.waitForReady(20000);
	} // selectMenuItem

	/**
	 * Navigates to Admin Tools page (note: this method is just an alias for
	 * convenience).
	 * You must be on a page which displays the navbar to use this method.
	 * Leaves you on the Administration page.
	 * @throws Exception
	 */
	public void navToAdminTools() throws Exception {
		selectUserAction("admin");
		// wait a bit for the iframe to render.
		VoodooUtils.waitForReady();
	}

	/**
	 * Navigates to the Users Profile page (note: this method is just an alias for
	 * convenience).
	 * You must be on a page which displays the navbar to use this method.
	 * Leaves you on the Users Profile page.
	 * @throws Exception
	 */
	public void navToProfile() throws Exception {
		selectUserAction("profile");
		// wait a bit for the iframe to render.
		VoodooUtils.waitForReady();
	}

	/**
	 * Navigates to a given module.
	 * You must be on a page which displays the navbar to use this method.
	 * Leaves you on the ListView of the specified module.
	 * @param module	plural name of the module to navigate to
	 */
	public void navToModule(String module) throws Exception {
		showAllModules();
		VoodooControl topNavLink = new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='" + module + "'] a");
		if(!(topNavLink.queryVisible())) {
			// If the link in the top nav is hidden, use the overflow menu instead.
			new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" + module + "']").click();
		} else topNavLink.click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Selects a menu item from the user actions menu.
	 * You must be on a page which displays the navbar to use this method.
	 * Leaves you on the page corresponding to the menu item selected.
	 * @param selection	the Voodoo name for the menu item to click on.
	 */
	public void selectUserAction(String selection) throws Exception {
		toggleUserActionsMenu();
		userAction.getControl(selection).click();
		/* Calling logging out causes a document unload, making waitForReady
		 * fail. Here we only call waitForReady if we are not logging out
		 */
		if (!"logout".equals(selection)) {
			VoodooUtils.waitForReady();
		}
	}

	/**
	 * Expands the overflow menu to display more modules.
	 * You must be on a page which displays the navbar to use this method.
	 * Leaves you on the same page with the overflow menu open.
	 */
	public void showAllModules() throws Exception {
		getControl("showAllModules").click();
	}

	/**
	 * Adds a Menu object
	 * @param module module to add
	 * @param menu menu of the module to add
	 * @throws Exception
	 */
	public void addMenu(Module module, Menu menu) throws Exception {
		menus.put(module, menu);
	}

	/**
	 * This method will click on a recently viewed record using the passed in index
	 * @param module desired module
	 * @param index 1 based index of the record in recently viewed to click on
	 * @throws Exception
	 */
	public void clickRecentlyViewed(Module module, int index) throws Exception {
		clickModuleDropdown(module);

		String moduleTab = "li[data-module='" + module.moduleNamePlural + "']";
		String recordIndex = " ul[role='menu'] .recentContainer li:nth-of-type(" + index + ") a";

		new VoodooControl("a","css", moduleTab + recordIndex).click();
		VoodooUtils.waitForReady();
	}

	/**
	 * This method will click on a recently viewed record using the passed in record name string
	 * @param module desired module
	 * @param record string of the records name
	 * @throws Exception
	 */
	public void clickRecentlyViewed(Module module, String record) throws Exception {
		clickModuleDropdown(module);

		String moduleTab = "//li[@data-module='" + module.moduleNamePlural + "']";
		String recordName = "//ul[@role='menu']//a[.=contains(.,'" + record + "')]";

		new VoodooControl("a","xpath", moduleTab + recordName).click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Set the module to be searched for Global Search
	 * <p>
	 * @param module String Plural Module Name you desire to search
	 * @throws Exception
	 */
	public void searchModule(String module) throws Exception {
		// While used in a loop like searchModules for loop, we need to check for visibility of the dropdown
		if(!search.getControl("searchModuleDropdown").queryVisible()) {
			getControl("globalSearch").click(); // basically set global search input to edit mode
			VoodooUtils.waitForReady();
		}
		// While used in a lopp like searchModules for loop, we need to check for the visibility of the module list dropdown
		if(!search.getControl("searchModuleList").queryVisible()) {
			search.getControl("searchModuleDropdown").click(); // expose the module list dropdown
			VoodooUtils.waitForReady();
		}
		// Scroll into View just in case
		search.getControl("search" + module).scrollIntoView();
		// Click on the module to search
		search.getControl("search" + module).click();
	}

	/**
	 * Set the module to be searched for Global Search
	 * <p>
	 * @param module Module to be searched
	 * @throws Exception
	 */
	public void searchModule(Module module) throws Exception {
		searchModule(module.moduleNamePlural);
	}

	/**
	 * Set the modules to be searched in Global Search
	 * <p>
	 * @param modules ArrayList of Modules to be searched
	 * @throws Exception
	 */
	public void searchModules(ArrayList<Module> modules) throws Exception {
		for(Module module : modules) {
			searchModule(module);
		}
	}

	/**
	 * Set Global Search to search All Modules
	 * <p>
	 * @throws Exception
	 */
	public void searchAll() throws Exception {
		if(!search.getControl("searchModuleList").queryVisible()) {
			search.getControl("searchModuleDropdown").click();
			VoodooUtils.waitForReady();
		}
		search.getControl("searchAll").scrollIntoView();
		search.getControl("searchAll").click();
		search.getControl("searchModuleDropdown").click(); // to close the module dropdown
	}

	/**
	 * Set the Global Search field
	 * <p>
	 * @param searchString String to search
	 * @throws Exception
	 */
	public void setGlobalSearch(String searchString) throws Exception {
		getControl("globalSearch").click();
		getControl("globalSearch").set(searchString);
		VoodooUtils.waitForReady();
	}

	/**
	 * Click on global search result by row.
	 * <p>
	 * Must have results to click on.<br>
	 * When used, you will be taken to the record view of the record clicked.
	 *
	 * @param row int Index of desired records row
	 * @throws Exception
	 */
	public void clickSearchResult(int row) throws Exception {
		String parentCSS = search.getControl("searchResults").getHookString();
		new VoodooControl("a", "css", parentCSS + " li.search-result:nth-of-type(" + row + ") a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click on global search result by Record.
	 * <p>
	 * Must have results to click on.<br>
	 * When used, you will be taken to the record view of the record clicked.
	 *
	 * @param record Record to click on
	 * @throws Exception
	 */
	public void clickSearchResult(Record record) throws Exception {
		String parentCSS = search.getControl("searchResults").getHookString();
		new VoodooControl("a", "css", parentCSS + " [href*='" + record.getGuid() + "']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the View All Results link in Global Search
	 * <p>
	 * Only possible when you have searched for records and the results are more than 5 shown.<br>
	 *
	 * @throws Exception
	 */
	public void viewAllResults() throws Exception {
		search.getControl("viewAllResults").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Get the desired Pill in Global Search Bar.
	 * <p>
	 * Must have the global search bar expanded to use.<br>
	 *
	 * @param pill Int 1 based index of the desired module/title pill
	 * @return VoodooControl of the desired pill
	 * @throws Exception
	 */
	public VoodooControl getGlobalSearchModulePill(int pill) throws Exception {
		return new VoodooControl("span", "css", search.getControl("searchModuleIcons").getHookString() + " span:nth-of-type(" + pill + ")");
	}
} // Navbar