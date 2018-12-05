package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.PortalAppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;

import java.util.HashMap;

/**
 * Models the SugarCRM Portal navbar.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class PortalNavbar extends View {
	protected static PortalNavbar portalNavbar;

	public View userAction;

	HashMap<Module, Menu> menus = new HashMap<Module, Menu>();

	public static PortalNavbar getInstance() throws Exception {
		if (portalNavbar == null)
			portalNavbar = new PortalNavbar();
		return portalNavbar;
	}

	/**
	 * Initialize the PortalNavbar.
	 * @throws Exception
	 */
	private PortalNavbar() throws Exception {
		super("div", "css", ".navbar.navbar-fixed-top");

		// TODO: VOOD-1031 -- Update Navbar and PortalNavbar classes
		userAction = new View("div", "css", getHookString() + " div[data-voodoo-name='profileactions']");
		userAction.addControl("userActions", "a", "css", userAction.getHookString() + " button[data-toggle='dropdown']");
		userAction.addControl("profile", "a", "css", userAction.getHookString() + " .profileactions-profile a");
		userAction.addControl("logout", "a", "css", userAction.getHookString() + " a[track='click:userAction-LBL_LOGOUT']");

		// Common menu container across all module dropdowns
		addControl("menu", "div", "css", getHookString() + " .btn-group.open .dropdown-menu.scroll");
	}
	
	/**
	 * Clicks on the down caret to open the User Actions menu.
	 * <p>
	 * The portalNavbar must be visible on the current page.<br>
	 * Leaves you on the same page with the User Actions menu open.
	 * 
	 * @throws Exception
	 */
	public void toggleUserActionsMenu() throws Exception {
		userAction.getControl("userActions").click();
		// TODO: VOOD-1030 -- Portal Application timing, control definitions
		new VoodooControl("ul", "css", userAction.getHookString() + " ul[role='menu']").waitForVisible(3000);
	}

	/**
	 * Clicks on the "caret" to activate the dropdown menu for a given module
	 * 
	 * @param module
	 *            desired module
	 * @throws Exception
	 */
	public void clickModuleDropdown(Module module) throws Exception {
		// TODO: VOOD-1031 -- Update Navbar and PortalNavbar classes
		new VoodooControl("button", "css", "li[data-module='" + module.moduleNamePlural + "'] button[data-toggle='dropdown']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks on a module action e.g. clicking on Bugs -> Report Bug
	 * 
	 * @deprecated - Not implemented yet.
	 * @param module
	 *            desired module
	 * @param menuItem
	 *            menu tab action to click on e.g. reportBug, viewBugs
	 */
	public void selectMenuItem(Module module, String menuItem) throws Exception {
//		navToModule(module.moduleNamePlural);
//		clickModuleDropdown(module);
//		if(module.isBwc()) {
//			// A bit of a hack because when the "Loading..." div closes, so does
//			// the menu. 
//			VoodooUtils.waitForAlertExpiration();
//			if(!menus.get(module).getControl(menuItem).queryVisible()) {
//				clickModuleDropdown(module);
//			}
//		}
//		menus.get(module).getControl(menuItem).click();
//		VoodooUtils.pause(2000);
		VoodooUtils.voodoo.log.info("Not yet implemented!");
	}
	
	/**
	 * Navigates to the Users Profile page (note: this method is just an alias for
	 * convenience).
	 * <p>
	 * You must be on a page which displays the portalNavbar to use this method.<br>
	 * Leaves you on the Users Profile page.
	 * @throws Exception
	 */
	public void navToProfile() throws Exception {
		selectUserAction("profile");
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Navigates to a given module.
	 * <p>
	 * The given module MUST be enabled in Portal to use.
	 * <p>
	 * You must be on a page which displays the portalNavbar to use this method.<br>
	 * Leaves you on the ListView of the specified module.
	 * 
	 * @param module
	 *            plural name of the module to navigate to
	 */
	public void navToModule(String module) throws Exception {
		// TODO: VOOD-1031 -- Update Navbar and PortalNavbar classes
		new VoodooControl("li", "css", getHookString() + " li[data-module='" + module + "']:not([class~='hidden'])").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Selects a menu item from the user actions menu.
	 * <p>
	 * You must be on a page which displays the portalNavbar to use this method.<br>
	 * Leaves you on the page corresponding to the menu item selected.
	 * 
	 * @param selection
	 *            the Voodoo name for the menu item to click on.
	 */
	public void selectUserAction(String selection) throws Exception {
		toggleUserActionsMenu();
		userAction.getControl(selection).click();
	}

	/**
	 * Adds a Menu object.
	 * @param module module to add
	 * @param menu menu of the module to add
	 * @throws Exception
	 */
	public void addMenu(Module module, Menu menu) throws Exception {
		menus.put(module, menu);
	}

	/**
	 * This method will click on a recently viewed record using the passed in index.
	 * 
	 * @deprecated - Not implemented yet.
	 * @param module
	 *            desired module
	 * @param index
	 *            1 based index of the record in recently viewed to click on
	 * @throws Exception
	 */
	public void clickRecentlyViewed(Module module, int index) throws Exception {
//		clickModuleDropdown(module);
//
//		String moduleTab = "li[data-module='" + module.moduleNamePlural + "']";
//		String recordIndex = " ul[role='menu'] .recentContainer li:nth-of-type(" + index + ") a";
//
//		new VoodooControl("a","css", moduleTab + recordIndex).click();
//		VoodooUtils.waitForAlertExpiration();
//		VoodooUtils.pause(1000);
		VoodooUtils.voodoo.log.info("Not yet implemented!");
	}

	/**
	 * This method will click on a recently viewed record using the passed in
	 * record name string.
	 * 
	 * @deprecated - Not implemented yet.
	 * @param module
	 *            desired module
	 * @param record
	 *            string of the records name
	 * @throws Exception
	 */
	public void clickRecentlyViewed(Module module, String record) throws Exception {
//		clickModuleDropdown(module);
//
//		String moduleTab = "//li[@data-module='" + module.moduleNamePlural + "']";
//		String recordName = "//ul[@role='menu']//a[.=contains(.,'" + record + "')]";
//
//		new VoodooControl("a","xpath", moduleTab + recordName).click();
//		VoodooUtils.waitForAlertExpiration();
//		VoodooUtils.pause(1000);
		VoodooUtils.voodoo.log.info("Not yet implemented!");
	}
} // PortalNavbar