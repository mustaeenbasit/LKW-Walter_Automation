package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.modules.Module;

import static org.junit.Assert.*;

/**
 * Models the Sweetspot in SugarCRM.
 * 
 */
public class Sweetspot extends View {
	protected static Sweetspot sweetspot;

	public static Sweetspot getInstance() throws Exception {
		if (sweetspot == null)
			sweetspot = new Sweetspot();
		return sweetspot;
	}

	public Sweetspot() throws Exception {

		// Home
		addControl("homeCube", "a", "css", "a.cube.btn");

		// Sweetspot Bar
		addControl("sweetspotBar", "div", "css", "#sweetspot");
		addControl("searchBox", "input", "css", "#sweetspot input[name='sweetspot-searchinput']");
		addControl("searchResult", "div", "css", "#sweetspot ul.sweetspot-results");
		addControl("searchResultKeywords", "div", "css", "#sweetspot ul.sweetspot-results > li[data-section='keywords']");
		addControl("searchResultActions", "div", "css", "#sweetspot ul.sweetspot-results > li[data-section='actions']");
		addControl("searchResultRecords", "div", "css", "#sweetspot ul.sweetspot-results > li[data-section='records']");

		// Configure Sweetspot
		addControl("configureIcon", "i", "css", "div.sweetspot-searchbar i[data-action='configure']");
		addControl("titleConfigurationPage", "span", "css", "span.sweetspot-config-headerpane.fld_title");
		addControl("cancelConfiguration", "a", "css", "span.sweetspot-config-headerpane.fld_cancel_button a");
		addControl("saveConfiguration", "a", "css", "span.sweetspot-config-headerpane.fld_save_button a");

		// Edit Hotkeys
		addSelect("hotkeysAction", "span", "css", "span.edit.fld_action");
		addControl("hotkeysKeyword", "input", "css", "span.edit.fld_keyword input");
		addControl("hotkeysRemove", "button", "css", "button[data-action='remove']");
		addControl("hotkeysAdd", "button", "css", "button[data-action='add']");

		addControl("hotkeysActionInput", "input", "css", "div#select2-drop input");
		addControl("hotkeysActionMatch", "span", "css", "div#select2-drop ul.select2-results li span.select2-match");

		// Edit Theme
		addSelect("theme", "span", "css", "span.edit.fld_theme");
	}

	/**
	 * Show Sweetspot
	 * 
	 * @throws Exception
	 */
	public void show() throws Exception {
		// Verify that sweetspot can be invoked via mod+shift+space
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		String showSweetspotJS = "SUGAR.App.additionalComponents.sweetspot.show()";
		// SweetSpot Bar is part of DOM but remains hidden until activated 
		VoodooUtils.executeJS(showSweetspotJS);
		VoodooUtils.waitForReady();
	}

	/**
	 * Hide Sweetspot
	 * 
	 * @throws Exception
	 */
	public void hide() throws Exception {
		// Verify that a open Seetspot Bar can be hidden via pressing ESC key
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		String hideSweetspotJS = "SUGAR.App.additionalComponents.sweetspot.hide()";  
		VoodooUtils.executeJS(hideSweetspotJS);
		VoodooUtils.waitForReady();
	}

	/**
	 * Configure Sweetspot
	 * 
	 * Sweetspot must be open
	 * 
	 * After performing action, leaves the user on the same page with the Sweet Spot Configuration Panel open.
	 * 
	 * @throws Exception
	 */
	public void configure() throws Exception {
		getControl("configureIcon").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Search Sweetspot
	 * 
	 * Sweetspot must be open
	 * 
	 * If search is successful, result(s) are displayed in the result panel, otherwise no result panel is shown
	 * 
	 * @param searchString String to be searched
	 * 
	 * @throws Exception
	 */
	public void search(String searchString) throws Exception {
		getControl("searchBox").set(searchString);
		VoodooUtils.waitForReady();
	}

	/**
	 * Confirm result from search Sweetspot
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public void assertResultVisible() throws Exception {
		getControl("searchResult").assertVisible(true);
	}

	/**
	 * Confirm nil result from search Sweetspot
	 * 
	 * Sweetspot must be open and a unsuccessful search must have been performed
	 * 
	 * @throws Exception
	 */
	public void assertResultNotVisible() throws Exception {
		// getControl("searchResult").assertVisible(false) is not working as this is an "ul" and has child "li" elements
		// with class="hide". Hence below code.
		assertTrue("Error: result-set is not empty!", getControl("searchResult").getText().trim().isEmpty());
	}

	/**
	 * Confirm "Keywords" result from search Sweetspot
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public void assertKeywordsResultVisible() throws Exception {
		getControl("searchResultKeywords").assertVisible(true);
	}

	/**
	 * Confirm "Actions" result from search Sweetspot
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public void assertActionsResultVisible() throws Exception {
		getControl("searchResultActions").assertVisible(true);
	}

	/**
	 * Confirm "Records" result from search Sweetspot
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public void assertRecordsResultVisible() throws Exception {
		getControl("searchResultRecords").assertVisible(true);
	}

	/**
	 * Get first control of the Keywords result
	 * 
	 * Sweetspot must be open and a search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getKeywordsResult() throws Exception {
		return getKeywordsResult(1);
	}
	
	/**
	 * Get nth control of the Keywords result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getKeywordsResult(int index) throws Exception {
		return new VoodooControl("div", "css", getControl("searchResultKeywords").getHookString() + 
				" > ul > li:nth-of-type(" + index + ") div.result-text");
	}
	
	/**
	 * Click first control of the Keywords result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickKeywordsResult() throws Exception {
		clickKeywordsResult(1);
	}
	
	/**
	 * Click nth control of the Keywords result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickKeywordsResult(int index) throws Exception {
		// We cannot use identical CSS for getKeywordsResult(index) and this methods as getKeywordsResult() needs to fetch
		// results text and below CSS gives garbage text due to presence of other child elements such as icon etc.
		new VoodooControl("div", "css", getControl("searchResultKeywords").getHookString() + 
				" > ul > li:nth-of-type(" + index + ")").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Get first control of the Actions result
	 * 
	 * Sweetspot must be open and a search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getActionsResult() throws Exception {
		return getActionsResult(1);
	}
	
	/**
	 * Get nth control of the Actions result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getActionsResult(int index) throws Exception {
		return new VoodooControl("div", "css", getControl("searchResultActions").getHookString() + 
				" > ul > li:nth-of-type(" + index + ") div.result-text");
	}
	
	/**
	 * Click first control of the Actions result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickActionsResult() throws Exception {
		clickActionsResult(1);
	}
	
	/**
	 * Click nth control of the Actions result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickActionsResult(int index) throws Exception {
		// We cannot use identical CSS for getActionsResult(index) and this methods as getActionsResult() needs to fetch
		// results text and below CSS gives garbage text due to presence of other child elements such as icon etc.
		new VoodooControl("div", "css", getControl("searchResultActions").getHookString() + 
				" > ul > li:nth-of-type(" + index + ")").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Get first control of the Records result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getRecordsResult() throws Exception {
		return getRecordsResult(1);
	}
	
	/**
	 * Get nth control of the Records result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * @throws Exception
	 */
	public VoodooControl getRecordsResult(int index) throws Exception {
		return new VoodooControl("div", "css", getControl("searchResultRecords").getHookString() + 
				" > ul > li:nth-of-type(" + index + ") div.result-text");
	}
	
	/**
	 * Click first control of the Records result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickRecordsResult() throws Exception {
		clickRecordsResult(1);
	}
	
	/**
	 * Click nth control of the Records result
	 * 
	 * Sweetspot must be open and a successful search must have been performed
	 * 
	 * This will land you on the page selected
	 * 
	 * @throws Exception
	 */
	public void clickRecordsResult(int index) throws Exception {
		// We cannot use identical CSS for getRecordsResult(index) and this methods as getRecordsResult() needs to fetch
		// results text and below CSS gives garbage text due to presence of other child elements such as icon etc.
		new VoodooControl("div", "css", getControl("searchResultRecords").getHookString() + 
				" > ul > li:nth-of-type(" + index + ")").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Click hotkeysAction
	 * 
	 * SweetSpot Configuration page should be open
	 * 
	 * @throws Exception
	 */
	public void clickHotkeysAction() throws Exception {
		getControl("hotkeysAction").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Set hotkeysAction Input text box
	 * 
	 * SweetSpot Configuration page should be open, hotkeysActionInput is visible
	 * 
	 * @throws Exception
	 */
	public void setHotkeysActionInput(String toSet) throws Exception {
		sugar().sweetspot.getControl("hotkeysAction").set(toSet);
		VoodooUtils.waitForReady();	
	}

	/**
	 * Click hotkeysAction Search result
	 * 
	 * SweetSpot Configuration page should be open, hotkeysActionInput is set to a value
	 * and a match is found and displayed
	 * 
	 * @throws Exception
	 */
	public void showHotkeysDropDownOptions(String toSet) throws Exception {
		sugar().sweetspot.getControl("hotkeysActionInput").set(toSet);
		VoodooUtils.waitForReady();	
	}

	/**
	 * Click first option from hotkeysAction Search result
	 * 
	 * SweetSpot Configuration page should be open, hotkeysActionInput is set to a value
	 * and a match is found and displayed
	 * 
	 * @throws Exception
	 */
	public void clickHotkeysDropDownOption() throws Exception {
		sugar().sweetspot.getControl("hotkeysActionMatch").click();
		VoodooUtils.waitForReady();	
	}

	/**
	 * Set hotkeys Keyword
	 * 
	 * SweetSpot Configuration page should be open, hotkeys Keyword Input is visible
	 * 
	 * @throws Exception
	 */
	public void setHotkeysKeyword(String toSet) throws Exception {
		sugar().sweetspot.getControl("hotkeysKeyword").set(toSet);
		VoodooUtils.waitForReady();	
	}

	/**
	 * Click hotkeys Add 
	 * 
	 * SweetSpot Configuration page should be open, hotkeysActionInput is set to a value
	 * and hotkeys Keyword Input is set to a value
	 * 
	 * @throws Exception
	 */
	public void addHotkey() throws Exception {
		sugar().sweetspot.getControl("hotkeysAdd").click();
		VoodooUtils.waitForReady();	
	}

	/**
	 * Click hotkeys remove
	 * 
	 * SweetSpot Configuration page should be open, hotkeysActionInput is set to a value
	 * 
	 * @throws Exception
	 */
	public void removeHotkey() throws Exception {
		sugar().sweetspot.getControl("hotkeysRemove").click();
		VoodooUtils.waitForReady();	
	}

	/**
	 * Confirm that SweetSpot Configuration page is open
	 * 
	 * @throws Exception
	 */
	public void assertConfigurePage() throws Exception {
		getControl("titleConfigurationPage").assertVisible(true);
	}
	
	/**
	 * Add a Keyword for an action for Sweetspot
	 * 
	 * Must be on Sweetspot configuration page. After action is performed, takes you back to the page 
	 * from which Sweetspot was called.
	 * 
	 * @param action Select action to file keyword for 
	 * @param keyword 
	 * @throws Exception
	 */
	public void addKeyword(String action, String keyword) throws Exception {
		show();
		configure();
		setHotkeysActionInput(action);
		setHotkeysKeyword(keyword);
		addHotkey();
		saveConfiguration();
	}

	/**
	 * Remove the Keyword for an action for Sweetspot
	 * 
	 * Must be on Sweetspot configuration page. After action is performed, takes you back to the page 
	 * from which Sweetspot was called.
	 * 
	 * @param action Select action to remove keyword for 
	 * @throws Exception
	 */
	public void removeKeyword(String action) throws Exception {
		show();
		configure();
		setHotkeysActionInput(action);
		removeHotkey();
		saveConfiguration();
	}

	/**
	 * Select Theme option for Sweetspot
	 * 
	 * Must be on Sweetspot configuration page. After action is performed, takes you back to the page 
	 * from which Sweetspot was called.
	 * 
	 * @param theme 
	 * @throws Exception
	 */
	public void configureTheme(String theme) throws Exception {
		getControl("theme").set(theme);
		VoodooUtils.waitForReady();		
	}
	
	
	/**
	 * Save Sweetspot configuration
	 * 
	 * Must be on Sweetspot configuration page. After action is performed, takes you back to the page 
	 * from which Sweetspot was called.
	 * 
	 * @throws Exception
	 */
	public void saveConfiguration() throws Exception {
		getControl("saveConfiguration").click();
		VoodooUtils.waitForReady();		
	}

	/**
	 * Cancel Sweetspot configuration
	 * 
	 * Must be on Sweetspot configuration page. After action is performed, takes you back to the page 
	 * from which Sweetspot was called.
	 * 
	 * @throws Exception
	 */
	public void cancelConfiguration() throws Exception {
		getControl("cancelConfiguration").click();
		VoodooUtils.waitForReady();				
	}
	
	/**
	 * Navigate to Module via SweetSpot Search Bar
	 * 
	 * @param module a valid Sugar Module
	 * @throws Exception
	 */
	public void navToModule(Module destination) throws Exception {
		sugar().sweetspot.show();
		sugar().sweetspot.search(destination.moduleNamePlural);
		sugar().sweetspot.clickActionsResult();
	}

	/**
	 * Clicks on a module action e.g. clicking on Accounts -> Create Account
	 * @param destination desired module
	 * @param menuItem menu tab action to click on e.g. createAccount, viewAccounts
	 */
	public void navToModuleMenuItem(Module destination, String menuItem) throws Exception {
		navToModule(destination);
		sugar().navbar.clickModuleDropdown(destination);
		sugar().navbar.menus.get(destination).getControl(menuItem).click();
		VoodooUtils.waitForReady(20000);
	}
} // Sweetspot