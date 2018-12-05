package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DS;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.WsRestV10;
import com.sugarcrm.sugar.views.PortalEditor;
import com.sugarcrm.sugar.views.StudioView;
import com.sugarcrm.sugar.views.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Admin module object which contains tasks associated with the Admin Tools
 * module like user management, roles management, teams management etc..
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class AdminModule extends Module {
	protected static AdminModule module;
	public StudioView studio;
	public View adminTools, passwordManagement, configureTabs, emailSettings, inboundEmail, renameModule, systemSettings, currencySettings, oppViewSettings;
	public PortalEditor portalSetup;
	public Api api = new Api();

	public static AdminModule getInstance() throws Exception {
		if (module == null)
			module = new AdminModule();
		return module;
	}

	private AdminModule() throws Exception {
		moduleNameSingular = "Admin";
		moduleNamePlural = "Admin"; // We don't pluralize this one.
		adminTools = new View();
		passwordManagement = new View();
		configureTabs = new View();
		emailSettings = new View();
		studio = new StudioView();
		portalSetup = new PortalEditor();
		inboundEmail = new View();
		renameModule = new View();
		currencySettings = new View();
		systemSettings = new View();
		oppViewSettings = new View("div", "css", ".layout_Opportunities.drawer.active");

		// TODO: Replace with loading data from disk.

		// Sugar7 UI elements
		adminTools.addControl("moduleTitle", "div", "css", ".moduleTitle");

		// Users
		adminTools.addControl("userManagement", "a", "id", "user_management");
		adminTools.addControl("rolesManagement", "a", "id", "roles_management");
		adminTools.addControl("teamsManagement", "a", "id", "teams_management");
		adminTools.addControl("passwordManagement", "a", "id", "password_management");
		adminTools.addControl("teamsPermissions", "a", "id", "tba_management");

		// Sugar connect
		adminTools.addControl("licenseManagement", "a", "id", "license_management");
		adminTools.addControl("sugarUpdates", "a", "id", "update");
		adminTools.addControl("onlineDocumentation", "a", "id", "documentation");

		// System
		adminTools.addControl("systemSettings", "a", "id", "configphp_settings");
		adminTools.addControl("importWizard", "a", "id", "import");
		adminTools.addControl("locale", "a", "id", "locale");
		adminTools.addControl("upgradeWizard", "a", "id", "upgrade_wizard");
		adminTools.addControl("currencyManagement", "a", "id", "currencies_management");
		adminTools.addControl("backupManagement", "a", "id", "backup_management");
		adminTools.addControl("language", "a", "id", "languages");
		adminTools.addControl("repair", "a", "id", "repair");
		adminTools.addControl("globalSearch", "a", "id", "global_search");
		adminTools.addControl("diagnosticTool", "a", "id", "diagnostic");
		adminTools.addControl("connector", "a", "id", "connector_settings");
		adminTools.addControl("tracker", "a", "id", "tracker_settings");
		adminTools.addControl("scheduler", "a", "id", "scheduler");
		adminTools.addControl("pdfManager", "a", "id", "pdfmanager");
		adminTools.addControl("mobile", "a", "id", "enable_wireless_modules");
		adminTools.addControl("webLogicHook", "a", "id", "web_logic_hooks");
		adminTools.addControl("oauth", "a", "id", "oauth");

		// Email
		adminTools.addControl("emailSettings", "a", "id", "mass_Email_config");
		adminTools.addControl("inboundEmail", "a", "id", "mailboxes");
		adminTools.addControl("relatedContactEmails", "a", "id", "history_contacts_emails");
		adminTools.addControl("campaign", "a", "id", "campaignconfig");
		adminTools.addControl("emailQueue", "a", "id", "mass_Email");
		adminTools.addControl("emailArchive", "a", "id", "register_snip");

		// Developer Tools
		adminTools.addControl("studio", "a", "id", "studio");
		adminTools.addControl("renameModules", "a", "id", "rename_tabs");
		adminTools.addControl("moduleBuilder", "a", "id", "moduleBuilder");
		adminTools.addControl("configureTabs", "a", "id", "configure_tabs");
		adminTools.addControl("moduleLoader", "a", "id", "module_loader");
		adminTools.addControl("configurationShortcutBar", "a", "id", "config_prod_bar");
		adminTools.addControl("portalSettings", "a", "id", "sugarportal");
		adminTools.addControl("styleGuide", "a", "id", "styleguide");
		adminTools.addControl("dropdownEditor", "a", "id", "dropdowneditor");
		adminTools.addControl("workflowManagement", "a", "id", "workflow_management");

		// Product and Quotes
		adminTools.addControl("productCatalog", "a", "id", "product_catalog");
		adminTools.addControl("manufacturer", "a", "id", "manufacturers");
		adminTools.addControl("productCategories", "a", "id", "product_categories");
		adminTools.addControl("shippingProvider", "a", "id", "shipping_providers");
		adminTools.addControl("productType", "a", "id", "product_types");
		adminTools.addControl("taxRate", "a", "id", "tax_rates");

		// Bugs
		adminTools.addControl("release", "a", "id", "bug_tracker");

		// Forecast
		adminTools.addControl("forecastManagement", "a", "id", "forecast_setup");

		// Opportunities
		adminTools.addControl("opportunityManagement", "a", "id", "opportunities_setup");

		// Contracts
		adminTools.addControl("contractManagement", "a", "id", "contract_type_management");

		// Process Author
		adminTools.addControl("processSettings", "a", "id", "Settings");
		adminTools.addControl("processList", "a", "id", "CasesList");
		adminTools.addControl("logViewer", "a", "id", "EngineLogs");

		// password management
		passwordManagement.addControl("passwordManagementLink", "a", "id", "password_management");
		passwordManagement.addControl("passwordMinLength", "input", "id", "passwordsetting_minpwdlength");
		passwordManagement.addControl("passwordMaxLength", "input", "id", "passwordsetting_maxpwdlength");
		passwordManagement.addControl("passwordSettingOneUpper", "input", "id", "passwordsetting_oneupper");
		passwordManagement.addControl("passwordSettingOneNumber", "input", "id", "passwordsetting_onenumber");
		passwordManagement.addControl("passwordSettingOneLower", "input", "id", "passwordsetting_onelower");
		passwordManagement.addControl("passwordSettingOneSpecial", "input", "id", "passwordsetting_onespecial");
		passwordManagement.addControl("SystemGeneratedPasswordCheckbox", "input", "id", "SystemGeneratedPassword_checkbox");
		passwordManagement.addControl("save", "input", "id", "btn_save");

		// Module tabs to display/hide
		// TODO: The following controls need to be uniquely identified, please perform JIRA task VOOD-466 for this
		configureTabs.addControl("AccountsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Accounts']");
		configureTabs.addControl("BugsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Bugs']");
		configureTabs.addControl("ContactsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Contacts']");
		configureTabs.addControl("LeadsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Leads']");
		configureTabs.addControl("ProductsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Revenue Line Items']");
		configureTabs.addControl("ProjectsModule", "div", "xpath", "//*[contains(@class,'add_table')]//div[.='Projects']");

		// Subpanel tabs to display/hide
		// TODO: The following controls need to be uniquely identified, please perform JIRA task VOOD-466 for this
		configureTabs.addControl("AccountsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Accounts']");
		configureTabs.addControl("BugsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Bugs']");
		configureTabs.addControl("ContactsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Contacts']");
		configureTabs.addControl("LeadsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Leads']");
		configureTabs.addControl("ProductsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Revenue Line Items']");
		configureTabs.addControl("ProjectsSubpanel", "div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='Projects']");

		// Module and Subpanel columns to drag to
		configureTabs.addControl("displayedModules", "tbody", "css", "#enabled_div tbody[tabindex='0']");
		configureTabs.addControl("displayedSubpanels", "tbody", "css", "#enabled_subpanels_div tbody[tabindex='0']");
		configureTabs.addControl("hiddenModules", "tbody", "css", "#disabled_div tbody[tabindex='0']");
		configureTabs.addControl("hiddenSubpanels", "tbody", "css", "#disabled_subpanels_div tbody[tabindex='0']");

		configureTabs.addControl("save", "input", "css", "input[value='Save']");

		// email settings
		emailSettings.addControl("save", "input", "id", "btn_save");
		emailSettings.addControl("cancel", "input", "css", "input[title='Cancel']");
		emailSettings.addControl("gmailButton", "button", "id", "gmail-button");
		emailSettings.addControl("userName", "input", "id", "mail_smtpuser");
		emailSettings.addControl("password", "input", "id", "mail_smtppass");
		emailSettings.addControl("passwordLink", "a", "id", "mail_smtppass_link");
		emailSettings.addControl("allowAllUsers", "input", "id", "notify_allow_default_outbound");

		// Rename Module
		renameModule.addControl("saveButton", "input", "id", "renameSaveBttn");
		renameModule.addControl("cancelButton", "input", "id", "renameCancelBttn");

		// System settings
		systemSettings.addControl("save", "input", "id", "ConfigureSettings_save_button");
		systemSettings.addControl("cancel", "input", "id", "ConfigureSettings_cancel_button");

		// System settings (User Interface controls)
		systemSettings.addControl("maxEntriesPerPage", "input", "id", "ConfigureSettings_list_max_entries_per_page");
		systemSettings.addControl("maxEntriesPerSubPanel", "input", "id", "ConfigureSettings_list_max_entries_per_subpanel");
		systemSettings.addControl("displayServerResponseTime", "input", "css", ".edit.view [name='calculate_response_time'][value='true']");
		systemSettings.addControl("defaultModuleFavicon", "input", "css", ".edit.view [name='default_module_favicon'][value='true']");
		systemSettings.addControl("systemName", "input", "css", ".edit.view [name='system_name']");
		systemSettings.addControl("showFullNames", "input", "css", ".edit.view [name='use_real_names'][value='true']");
		systemSettings.addControl("showDownloadTab", "input", "css", ".edit.view [name='show_download_tab'][value='true']");
		systemSettings.addControl("enableActionMenu", "input", "css", ".edit.view [name='enable_action_menu'][value='true']");
		systemSettings.addControl("lockSubpanels", "input", "css", ".edit.view [name='lock_subpanels'][value='true']");

		// System settings (Proxy Settings controls)
		systemSettings.addControl("proxyOn", "input", "id", "proxy_on");
		systemSettings.addControl("proxyHost", "input", "id", "proxy_host");
		systemSettings.addControl("proxyPort", "input", "id", "proxy_port");
		systemSettings.addControl("proxyAuth", "input", "id", "proxy_auth");
		systemSettings.addControl("proxyUsername", "input", "id", "proxy_username");
		systemSettings.addControl("proxyPassword", "input", "id", "proxy_password");

		// System settings (SkypeOut controls)
		systemSettings.addControl("skypeOut", "input", "css", ".edit.view [name='system_skypeout_on'][type='checkbox']");

		// System settings (Tweet to case controls)
		systemSettings.addControl("tweetTocase", "input", "css", ".edit.view [name='system_tweettocase_on'][type='checkbox']");

		// System settings (Advanced controls)
		systemSettings.addControl("clientIp", "input", "css", ".edit.view [name='verify_client_ip'][type='checkbox']");
		systemSettings.addControl("logMemoryUsage", "input", "css", ".edit.view [name='log_memory_usage'][type='checkbox']");
		systemSettings.addControl("dumpSlowQueries", "input", "css", ".edit.view [name='dump_slow_queries'][type='checkbox']");
		systemSettings.addControl("slowQueryTime", "input", "css", ".edit.view [name='slow_query_time_msec']");
		systemSettings.addControl("uploadMaxsize", "input", "css", ".edit.view [name='upload_maxsize']");
		systemSettings.addControl("stackTraceErrors", "input", "css", ".edit.view [name='stack_trace_errors'][type='checkbox']");
		systemSettings.addControl("systemSessionTimeout", "input", "css", ".edit.view [name='system_session_timeout']");
		systemSettings.addControl("developerMode", "input", "css", ".edit.view [name='developerMode'][type='checkbox']");
		systemSettings.addControl("vcalTime", "input", "css", ".edit.view [name='vcal_time']");
		systemSettings.addControl("maxRecordsLimit", "input", "css", ".edit.view [name='import_max_records_total_limit']");
		systemSettings.addControl("noPrivateTeamUpdate", "input", "css", ".edit.view [name='noPrivateTeamUpdate'][type='checkbox']");

		// System settings (Logger Settings controls)
		systemSettings.addControl("loggerFileName", "input", "css", ".edit.view [name='logger_file_name']");
		systemSettings.addControl("loggerFileExt", "input", "css", ".edit.view [name='logger_file_ext']");
		systemSettings.addControl("loggerFileSuffix", "select", "css", ".edit.view [name='logger_file_suffix']");
		systemSettings.addControl("loggerFileMaxSize", "input", "css", ".edit.view [name='logger_file_maxSize']");
		systemSettings.addControl("loggerFileDateFormat", "input", "css", ".edit.view [name='logger_file_dateFormat']");
		systemSettings.addControl("loggerLevel", "select", "css", ".edit.view [name='logger_level']");
		systemSettings.addControl("loggerFileMaxLogs", "input", "css", ".edit.view [name='logger_file_maxLogs']");

		// Opportunities view vs. Revenue Line Items View Controls
		oppViewSettings.addControl("saveButton", "a", "css", oppViewSettings.getHookString() + " .fld_save_button a");
		oppViewSettings.addControl("cancelButton", "a", "css", oppViewSettings.getHookString() + " .fld_cancel_button a");
		oppViewSettings.addControl("oppView", "a", "css", oppViewSettings.getHookString() + " .fld_opps_view_by input[value='Opportunities']");
		oppViewSettings.addControl("rliView", "a", "css", oppViewSettings.getHookString() + " .fld_opps_view_by input[value='RevenueLineItems']");
		oppViewSettings.addControl("latestCloseDate", "a", "css", oppViewSettings.getHookString() + " .fld_opps_closedate_rollup input[value='latest']");
		oppViewSettings.addControl("earliestCloseDate", "a", "css", oppViewSettings.getHookString() + " .fld_opps_closedate_rollup input[value='earliest']");

		// Currency Settings
		currencySettings.addControl("currencyName", "input", "css", "table.edit.view input[name='name']");
		currencySettings.addControl("conversionRate", "input", "css", "table.edit.view input[name='conversion_rate']");
		currencySettings.addControl("currencySymbol", "input", "css", "table.edit.view input[name='symbol']");
		currencySettings.addControl("ISOcode", "input", "css", "table.edit.view input[name='iso4217']");
		currencySettings.addControl("status", "select", "css", "#contentTable .edit.view slot select");
		currencySettings.addControl("save", "input", "css", "input[value='Save']");
		currencySettings.addControl("cancel", "input", "css", "input[value='Cancel']");
	}

	/**
	 * Perform setup which depends on other modules or views already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Admin Module...");

		portalSetup.setControls();
	}

	/**
	 * Method to manage password settings.
	 *
	 * @param passSetting
	 *            FieldSet of fields and values for fields to be set
	 * @throws Exception
	 */
	public void passwordSettings(FieldSet passSetting) throws Exception {
		navToAdminPanelLink("passwordManagement");
		VoodooUtils.focusFrame("bwc-frame");

		for (String controlName : passSetting.keySet()) {
			VoodooUtils.voodoo.log.info(controlName);
			if (passSetting.get(controlName) != null) {
				String toSet = passSetting.get(controlName);
				VoodooUtils.voodoo.log.info("Setting " + controlName + " to " + toSet);
				passwordManagement.getControl(controlName).set(toSet);
				VoodooUtils.pause(300);
			}
		}
		passwordManagement.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Navigate to Currency Settings in Admin Tools. This navigation method will leave you
	 * focused on the default content.
	 *
	 * @throws Exception
	 */
	public void navToCurrencySettings() throws Exception {
		navToAdminPanelLink("currencyManagement");
	}

	/**
	 * Method to create new currency using Fieldset which is given as parameter. This method leave you on the
	 * same page (i.e. Currency Management page) after creating currency.
	 *
	 * @param currencyData FieldSet of currency settings to use
	 * @throws Exception
	 */
	public void setCurrency(FieldSet currencyData) throws Exception {
		navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");
		for(String controlName : currencyData.keySet()){
			VoodooUtils.voodoo.log.info(controlName);
			if(currencyData.get(controlName) != null) {
				String toSet = currencyData.get(controlName);
				VoodooUtils.voodoo.log.info("Setting " + controlName + " to " + toSet);
				currencySettings.getControl(controlName).set(toSet);
			}
		}
		currencySettings.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Method to edit currency. This method navigates to Currency Management page in Admin Tools and
	 * edit fields of currency. At the end of this method, you will be on the same page.
	 *
	 * @param currencyName
	 *  		Name of the currency to edit
	 * @param currencyData
	 * 			FieldSet for currency settings
	 * @throws Exception
	 */
	public void editCurrency(String currencyName, FieldSet currencyData) throws Exception {
		navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Select existing currency to edit
		new VoodooControl("a","xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+currencyName+"')]/td/slot/a").click();
		VoodooUtils.focusFrame("bwc-frame");

		for(String controlName : currencyData.keySet()){
			VoodooUtils.voodoo.log.info(controlName);
			if(currencyData.get(controlName) != null) {
				String toSet = currencyData.get(controlName);
				VoodooUtils.voodoo.log.info("Setting " + controlName + " to " + toSet);
				currencySettings.getControl(controlName).set(toSet);
			}
		}
		currencySettings.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Set currency inactive. This method navigate to Currency Management page in Admin Tools and set
	 * status of selected currency to 'Inactive'. And leaves you on the same page (i.e. Currency Management page).
	 *
	 * @param currencyName String
	 * @throws Exception
	 */
	public void inactiveCurrency(String currencyName) throws Exception {
		navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Xpath used to find currency by name
		new VoodooControl("a","xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+currencyName+"')]/td/slot/a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		currencySettings.getControl("status").set("Inactive");
		currencySettings.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Rename module.
	 * @param sugarModule
	 *            Name of Sugar Module to rename
	 * @param singularLabel
	 *            Singular Label to replace with old one
	 * @param pluralLabel
	 *            Plural Label to replace with old one
	 *
	 * @throws Exception
	 */
	public void renameModule(Module sugarModule, String singularLabel, String pluralLabel) throws Exception {
		// Navigate to Rename Modules in Admin Tools
		navToAdminPanelLink("renameModules");
		VoodooUtils.focusFrame("bwc-frame");
		String moduleName = sugarModule.moduleNamePlural.toLowerCase();
		new VoodooControl("span", "css", ".module-" + moduleName +"-title.rename-slot-title").click();

		// Replace 'Singular Label' and 'Plural Label' with new one, click save
		new VoodooControl("input", "css", ".module-" + moduleName +"-singular.rename-label-singular").set(singularLabel);
		new VoodooControl("input", "css", ".module-" + moduleName +"-plural.rename-label-plural").set(pluralLabel);
		renameModule.getControl("saveButton").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(60000);
	}

	/**
	 * Enable a single module.
	 *
	 * @param	module	Module to enable.
	 * @throws Exception
	 */
	public void enableModuleDisplay(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		enableModuleDisplay(modules);
	}

	/**
	 * Enables multiple modules
	 *
	 * @param	modules	ArrayList of Modules to enable
	 * @throws Exception
	 */
	public void enableModuleDisplay(ArrayList<Module> modules) throws Exception {
		navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		for(Module module : modules) {
			// Drag and drop module to Displayed Modules column
			// TODO: TR-6475
			// File Product bug to change "data-module" attribute value from "Project" to "Projects"
			// Once fixed, remove the below code block and replace moduleName variable accordingly
			String moduleName;
			if (module.moduleNamePlural == "Project")
				moduleName = "Projects";
			else
				moduleName = module.moduleNamePlural;

			new VoodooControl("div", "xpath", "//*[contains(@class,'add_table')]//div[.='" + moduleName + "']")
			.dragNDrop(configureTabs.getControl("displayedModules"));
			new VoodooControl("div", "xpath", "//*[@id='enabled_div']//div[.='" + moduleName + "']")
			.waitForVisible();
		}
		configureTabs.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Disable a single module
	 *
	 * @param	module	single module to disable
	 * @throws Exception
	 */
	public void disableModuleDisplay(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		disableModuleDisplay(modules);
	}

	/**
	 * Disables multiple modules
	 *
	 * @param	modules	ArrayList of Modules to hide
	 * @throws Exception
	 */
	public void disableModuleDisplay(ArrayList<Module> modules) throws Exception {
		navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		for(Module module : modules) {
			// Drag and drop module to Hidden Module column
			// TODO: TR-6475
			// File Product bug to change "data-module" attribute value from "Project" to "Projects"
			// Once fixed, remove the below code block and replace moduleName variable accordingly
			String moduleName;
			if (module.moduleNamePlural == "Project")
				moduleName = "Projects";
			else
				moduleName = module.moduleNamePlural;

			new VoodooControl("div", "xpath", "//*[contains(@class,'add_table')]//div[.='" + moduleName + "']")
			.dragNDrop(configureTabs.getControl("hiddenModules"));
			new VoodooControl("div", "xpath", "//*[@id='disabled_div']//div[.='" + moduleName + "']")
			.waitForVisible();
		}
		configureTabs.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Enable a single module via Javascript
	 *
	 * @param	module	Single module to enable.
	 * @throws Exception
	 */
	public void enableModuleDisplayViaJs(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		enableModuleDisplayViaJs(modules);
	}

	/**
	 * Enables multiple modules via Javascript
	 *
	 * @param	modules	ArrayList of modules to enable.
	 * @throws Exception
	 */
	public void enableModuleDisplayViaJs(ArrayList<Module> modules) throws Exception {
		toggleDisplayViaJs("modules", modules, true);
	}

	/**
	 * Disable a single module via Javascript
	 *
	 * @param	module	Single module to disable.
	 * @throws Exception
	 */
	public void disableModuleDisplayViaJs(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		disableModuleDisplayViaJs(modules);
	}

	/**
	 * Disables multiple modules via Javascript
	 *
	 * @param	modules	ArrayList of modules to disable.
	 * @throws Exception
	 */
	public void disableModuleDisplayViaJs(ArrayList<Module> modules) throws Exception {
		toggleDisplayViaJs("modules", modules, false);
	}

	/**
	 * Enable a single subpanel via Javascript
	 *
	 * @param	module	Subpanel to enable.
	 * @throws Exception
	 */
	public void enableSubpanelDisplayViaJs(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		enableSubpanelDisplayViaJs(modules);
	}

	/**
	 * Enables multiple subpanels via Javascript
	 *
	 * @param	modules	ArrayList of subpanels to enable
	 * @throws Exception
	 */
	public void enableSubpanelDisplayViaJs(ArrayList<Module> modules) throws Exception {
		toggleDisplayViaJs("subpanels", modules, true);
	}

	/**
	 * Disable a single subpanel via Javascript
	 *
	 * @param	module	Single subpanel to disable
	 * @throws Exception
	 */
	public void disableSubpanelDisplayViaJs(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		disableSubpanelDisplayViaJs(modules);
	}

	/**
	 * Disables multiple subpanels via Javascript
	 *
	 * @param	modules	ArrayList of Modules to disable
	 * @throws Exception
	 */
	public void disableSubpanelDisplayViaJs(ArrayList<Module> modules) throws Exception {
		toggleDisplayViaJs("subpanels", modules, false);
	}

	/**
	 * Toggles module or subpanel visibility via JavaScript.
	 *
	 * The ability to do this is temporary, to be removed after resolution of CB-245.
	 *
	 * This is not the case of simple dragNDrop. the associated Javascript script modifies the
	 * DOM to store the enabled modules and the disabled subpanels (note: not disabled modules)
	 * through Javascript function SUGAR.saveConfigureTabs() which is the value of onclick()
	 * attribute of the "save" button. Then a call to server updates the enabled/disabled
	 * modules/subpanels.
	 *
	 * Each time, this page loads, a fresh list of enabled/disabled modules/subpanels gets
	 * stored in Javascript variables -  enabled_modules, disabled_modules, sub_enabled_modules
	 * and sub_disabled_modules. We have used these variables to fetch the current list.
	 *
	 * SUGAR.saveConfigureTabs() is a common function for enabling/disabling both modules and
	 * subpanels. Hence we need to check and use list of both enabled/disabled modules/subpanels
	 * in all enable module method and disable module method.
	 *
	 * A bare minimum stripped down version of SUGAR.saveConfigureTabs() has been used as we do
	 * not need the full version here. Also, as the page reloads, the default version of
	 * SUGAR.saveConfigureTabs() gets loaded each time.
	 *
	 * @param	modulesOrSubpanels	String "modules" or "subpanels" determines which visibility lists
	 *                              are to be modified.
	 * @param	modules	ArrayList of Modules to disable
	 * @param	enable	boolean true to enable the listed modules, false to disable.
	 * @throws Exception
	 */
	private void toggleDisplayViaJs(String modulesOrSubpanels, ArrayList<Module> modules,
									boolean enable) throws Exception {
		navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		// Fetch the current list of enabled/disabled modules.
		// As we may call this method multiple times in a page, it is mandatory to fetch the most
		// current list of enabled/disabled modules.
		String enabledModulesList = getEnabledModules();
		String disabledSubpanelsList = getDisabledSubpanels();

		//
		ArrayList<String> enabledModules = convertDisplayListStringToList(enabledModulesList);
		ArrayList<String> disabledSubpanels = convertDisplayListStringToList(disabledSubpanelsList);

		for(Module module : modules) {
			String moduleName = module.moduleNamePlural;

			// enabled_tabs determines which *modules* will be *enabled*
			// disabled_tabs determined which *subpanels* will be *disabled*.
			// Therefore we have to perform opposite operations to perform the same action in
			// modules vs. subpanels: for modules we must ADD a module to the ENABLED list to
			// enable it; for subpanels we must REMOVE it from the DISABLED list.
			if("modules".equals(modulesOrSubpanels)) {
				if(enabledModules.contains(moduleName)) {
					if(!enable)
						enabledModules.remove(moduleName);
				} else {
					if(enable)
						enabledModules.add(moduleName);
				}
			} else if("subpanels".equals(modulesOrSubpanels)) {
				if(disabledSubpanels.contains(moduleName.toLowerCase())) {
					if(enable) {
						disabledSubpanels.remove(moduleName.toLowerCase());
					}
				} else {
					if(!enable) {
						disabledSubpanels.add(moduleName.toLowerCase());
					}
				}
			}
		}

		String enabledModulesString = convertDisplayListListToString(enabledModules);
		String disabledSubpanelsString = convertDisplayListListToString(disabledSubpanels);
		String newDisplayListJs = getNewDisplayListJs(enabledModulesString, disabledSubpanelsString);
		VoodooUtils.executeJS(newDisplayListJs);

		configureTabs.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(60000);
	}

	private static String getEnabledModules() throws Exception {
		return getDisplayList("modules", true);
	}

	private static String getDisabledModules() throws Exception {
		return getDisplayList("modules", false);
	}

	private static String getEnabledSubpanels() throws Exception {
		return getDisplayList("subpanels", true);
	}

	private static String getDisabledSubpanels() throws Exception {
		return getDisplayList("subpanels", false);
	}

	private static String getDisplayList(String modulesOrSubpanels, boolean enabled) throws Exception {
		String jsTemplate =
			"var modules = [];" +
			"for(var i=0; i < :::LIST_NAME:::.length; i++) {" +
			"	var data = :::LIST_NAME:::[i];" +
			"	if (data.module && data.module != '') {" +
			"		modules[i] = data.module;" +
			"	}" +
			"}" +
			"return JSON.stringify(modules);";
		String listName = "modules";

		if(enabled)
			listName = "enabled_" + listName;
		else
			listName = "disabled_" + listName;

		if("subpanels".equals(modulesOrSubpanels))
			listName = "sub_" + listName;
		else if("modules".equals(modulesOrSubpanels)) {
			// no prefix needed, so do nothing.
		} else {
			throw new Exception("Incorrect argument to AdminModule.getDisplayList()." +
				"First arg can only be 'modules' or 'subpanels'; found '" + modulesOrSubpanels +
				"'");
		}

		String toExecute = jsTemplate.replaceAll(":::LIST_NAME:::", listName);

		String toReturn = ((String)VoodooUtils.executeJS(toExecute));

		return toReturn;
	}

	private static ArrayList<String> convertDisplayListStringToList(String toConvert) {
		toConvert = toConvert.replace("[", "").replace("]", "").replace("\"", "");
		String[] partiallyConverted = toConvert.split(",");
		ArrayList<String> toReturn = new ArrayList<String>(Arrays.asList(partiallyConverted));
		return toReturn;
	}

	private String convertDisplayListListToString(ArrayList<String> enabledModules) {
		// TODO: Any simple method to convert an ArrayList to ["xx","yy","zz"] format ?
		String enabledModulesString = "'[";
		for (int i = 0; i < enabledModules.size(); i++) {
			enabledModulesString = enabledModulesString + "\"" + enabledModules.get(i) + "\"";
			if (!(i == enabledModules.size() -1))
				enabledModulesString += ",";
		}
		enabledModulesString = enabledModulesString + "]'";

		return enabledModulesString;
	}

	private static String getNewDisplayListJs(String modules, String subpanels) {
		// enabled_tabs determines which *modules* will be *enabled*
		// disabled_tabs determined which *subpanels* will be *disabled*.
		return
				"SUGAR.saveConfigureTabs = function() " +
				"{" +
				"	YAHOO.util.Dom.get('enabled_tabs').value = " + modules + ";" +
				"	YAHOO.util.Dom.get('disabled_tabs').value = " + subpanels+ ";" +
				"} ";
	}

	/**
	 * Enable a single subpanel
	 *
	 * @param 	module	single subpanel to enable
	 * @throws Exception
	 */
	public void enableSubpanel(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		enableSubpanel(modules);
	}

	/**
	 * Enables multiple subpanels to be displayed
	 *
	 * @param	modules	ArrayList of subpanels to enable
	 * @throws Exception
	 */
	public void enableSubpanel(ArrayList<Module> modules) throws Exception {
		navToConfigureTabs();

		// Drag and drop subpanels to Displayed Subpanels column
		for (Module module : modules) {
			new VoodooControl("div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='" + module.moduleNamePlural + "']")
			.dragNDrop(configureTabs.getControl("displayedSubpanels"));
			new VoodooControl("div", "xpath", "//*[@id='enabled_subpanels_div']//div[.='" + module.moduleNamePlural + "']")
			.waitForVisible();
		}
		configureTabs.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Disable a single subpanel
	 *
	 * @param	module	single subpanel to disable
	 * @throws Exception
	 */
	public void disableSubpanel(Module module) throws Exception {
		ArrayList<Module>modules = new ArrayList<Module>();
		modules.add(module);
		disableSubpanel(modules);
	}

	/**
	 * Disables multiple subpanels
	 *
	 * @param	modules	ArrayList of subpanels to disable
	 * @throws Exception
	 */
	public void disableSubpanel(ArrayList<Module> modules) throws Exception {
		navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		// Drag and drop subpanels to Displayed Subpanels column
		for (Module module : modules) {
			new VoodooControl("div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='" + module.moduleNamePlural + "']")
			.dragNDrop(configureTabs.getControl("hiddenSubpanels"));
			new VoodooControl("div", "xpath", "//*[@id='disabled_subpanels_div']//div[.='" + module.moduleNamePlural + "']")
			.waitForVisible();
		}
		configureTabs.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Method to navigate to Display Modules and Subpanels in Admin Tools. This
	 * navigation method will leave you focused on the default, sidecar content.
	 *
	 * @throws Exception
	 */
	public void navToConfigureTabs() throws Exception {
		navToAdminPanelLink("configureTabs");
	}

	/**
	 * This method will click the save button, focus back to the default context, and wait for the Loading... alert to disappear.
	 * You must be on an Admin page that has a save button. This will leave you on the same page.
	 *
	 * @deprecated This method is no longer needed and exists only for legacy purposes.
	 * @throws Exception
	 */
	public void saveModulesAndSubpanels() throws Exception {
		// TODO: Eliminate all uses of this method (only QLI uses it now) and delete it.
		configureTabs.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * This method will setup and configure the Google Connector in the user's
	 * profile
	 *
	 * @throws Exception
	 */
	public static void enableGoogledocsConnector() throws Exception {
		// TODO - VOOD-637 LIB for Connectors
		Configuration grimoireConfig = VoodooUtils.getGrimoireConfig();
		sugar().navbar.navToProfile();
		// This pause is needed to allow the page to render
		VoodooUtils.pause(3000);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "edit_button").click();
		VoodooUtils.pause(2000);
		// Tab5 is External Accounts
		new VoodooControl("a", "id", "tab5").click();
		VoodooUtils.pause(2000);
		new VoodooControl("a", "id", "eapm_assigned_user_create_button")
		.click();
		VoodooUtils.pause(2000);
		VoodooUtils.getGrimoireConfig();
		new VoodooControl("select", "id", "application").set("Google");
		new VoodooControl("input", "id", "name").set(grimoireConfig
				.getValue("googledocs_user"));
		new VoodooControl("input", "id", "password").set(grimoireConfig
				.getValue("googledocs_pass"));
		new VoodooControl("input", "id", "EditViewSave").click();
		VoodooUtils.pause(2000);
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.pause(5000);
	}

	/**
	 * This method will remove the Google Connector in the user's profile
	 *
	 * @throws Exception
	 */
	public static void disableGoogledocsConnector() throws Exception {
		// TODO - VOOD-637 LIB for Connectors
		sugar().navbar.navToProfile();
		// This pause is needed to allow the page to render
		VoodooUtils.pause(3000);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "edit_button").click();
		VoodooUtils.pause(2000);
		// Tab5 is External Accounts
		new VoodooControl("a", "id", "tab5").click();
		// The first row in the External Apps area will be the only External App
		// currently active
		new VoodooControl("a", "css", "div#eapm_area tr.oddListRowS1 a")
		.click();
		new VoodooControl("input", "id", "delete_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(5000);
	}

	/**
	 * This method will Create a Role via the Admin Roles - Create Role link
	 * The tester will be taken to the ACL Matrix for the created Role, when this exits
	 *
	 * @throws Exception
	 */
	public static void createRole(FieldSet role) throws Exception {
		// TODO - This will be replaced by VOOD-580 - Create a Roles (ACL)
		// Module LIB
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();

		VoodooUtils.focusDefault();

		// Create a Role
		new VoodooControl("button", "css",
				"li[data-module='ACLRoles'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "css",
				"li[data-module='ACLRoles'] ul[role='menu'] a").click();
		VoodooUtils.waitForReady();
		// Need to reset bwc-frame focus when this page is opened
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(role.get("roleName"));
		new VoodooControl("textarea", "css", ".edit.view textarea").set(role
				.get("roleDescription"));
		new VoodooControl("input", "id", "save_button").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * This method will assign a User to a Role. The Tester needs to be on the
	 * ACL Matrix and is taken back to the ACL Matrix with a user having been
	 * assigned to the role
	 *
	 * @throws Exception
	 */
	public static void assignUserToRole(FieldSet role) throws Exception {
		// TODO - This will be replaced by VOOD-580 - Create a Roles (ACL)
		// Module LIB
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "user_name_advanced").set(role
				.get("userName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "css", "tr.oddListRowS1 input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		// Need to reset bwc-frame focus when this page is opened
		VoodooUtils.focusFrame("bwc-frame");
		//TODO TR-1462 After Role Save still shows the confirm cancel when navigating away from the screen
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * This method will delete a Role that is given as a parameter. The Tester
	 * is left on the List Roles (empty) View.
	 *
	 * @throws Exception
	 */
	public static void deleteRole(FieldSet role) throws Exception {
		// TODO - This will be replaced by VOOD-580 - Create a Roles (ACL)
		// Module LIB
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();

		VoodooUtils.focusDefault();

		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(role.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		new VoodooControl("a", "id", "massall_top").click();
		new VoodooControl("a", "id", "delete_listview_top").click();
		VoodooUtils.acceptDialog();
		//Clear the search data
		new VoodooControl("a", "id", "search_form_clear").click();
		new VoodooControl("a", "id", "search_form_submit").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Navigate to Email Settings in Admin Tools
	 * @throws Exception
	 */
	public void navToEmailSettings() throws Exception {
		navToAdminPanelLink("emailSettings");
	}

	/**
	 * Configure the Email Settings in Admin Tools > Email Settings.
	 * <Currently uses Gmail only>
	 *
	 * @param settings FieldSet of Email Settings to use
	 * @throws Exception
	 */
	public void setEmailServer(FieldSet settings) throws Exception {
		navToEmailSettings();
		VoodooUtils.focusFrame("bwc-frame");
		emailSettings.getControl("gmailButton").click(); // Click gmail button
		for(String controlName : settings.keySet()){
			VoodooUtils.voodoo.log.info(controlName);
			if(settings.get(controlName) != null) {
				String toSet = settings.get(controlName);
				VoodooUtils.voodoo.log.info("Setting " + controlName + " to " + toSet);
				if(controlName.equals("password")) {
					if(emailSettings.getControl("passwordLink").queryVisible()){
						emailSettings.getControl("passwordLink").click();
					}
					emailSettings.getControl(controlName).set(toSet);
				} else {
					emailSettings.getControl(controlName).set(toSet);
				}
				VoodooUtils.pause(300);
			}
		}
		emailSettings.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(40000);
	}

	/**
	 * Disable Configuration of the Email Settings in Admin Tools > Email Settings.
	 * <p>
	 * <Currently uses Gmail only>
	 * This is accomplished by passing incorrect strings to various credential values.<br>
	 *
	 * @throws Exception
	 */
	public void disableEmailServer() throws Exception {
		DataSource fieldDefinitions = null;
		DS dsWrapper = new DS("env_email_setup");
		String propNameCsvBaseDir = "datasource.csv.baseDir";
		String propValueCsvBaseDir = "src/test/resources/data";
		dsWrapper.init(DS.DataType.CSV, propNameCsvBaseDir, propValueCsvBaseDir);
		fieldDefinitions = dsWrapper.getDataSource("env_email_setup");

		// Use line 1 of env_email_setup.csv file to pass incorrect credentials
		setEmailServer(fieldDefinitions.get(1));
	}

	/**
	 * Navigate to Sugar Portal settings to configure portal application.
	 * <p>
	 * Leaves user on the Sugar Portal settings main view, attached to the default sidecar content
	 * @throws Exception
	 */
	public void navToPortalSettings() throws Exception {
		navToAdminPanelLink("portalSettings");
	}

	/**
	 * Navigate to System Settings in Admin Tools to configure system settings.
	 * Leaves user on the System settings main view, attached to default sidecar content
	 *
	 * @throws Exception
	 */
	public void navToSystemSettings() throws Exception {
		navToAdminPanelLink("systemSettings");
	}

	/**
	 * Configure the System Settings in Admin Tools > System Settings.
	 *
	 * @param settings FieldSet of System Settings to use
	 * @throws Exception
	 */
	public void setSystemSettings(FieldSet settings) throws Exception {
		navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		for(String controlName : settings.keySet()){
			VoodooUtils.voodoo.log.info(controlName);
			if(settings.get(controlName) != null) {
				String toSet = settings.get(controlName);
				VoodooUtils.voodoo.log.info("Setting " + controlName + " to " + toSet);
				systemSettings.getControl(controlName).set(toSet);
				VoodooUtils.pause(300);
			}
		}
		systemSettings.getControl("save").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault(); // focus back to the default content
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Navigate to the Opportunity Settings UI page.
	 *
	 * @throws Exception
	 */
	public void navToOpportunitySettings() throws Exception {
		// 60000ms time required if you navigate to this setting page after making a change via API
		navToAdminPanelLink("opportunityManagement", 60000);
	}

	/**
	 * Navigates to the desired settings page inside admin tools
	 * <p>
	 * @param linkName  desired  page name. This name should exactly match the first parameter of the addControl definition define in admin tools.
	 * @throws Exception
	 */
	public void navToAdminPanelLink(String linkName) throws Exception {
		navToAdminPanelLink(linkName, 15000);
	}

	/**
	 * Navigates to the desired settings page inside admin tools
	 * <p>
	 * @param linkName  desired  page name. This name should exactly match the first parameter of the addControl definition define in admin tools.
	 * @param maxTime maximum allowable time to wait.
	 * @throws Exception
	 */
	public void navToAdminPanelLink(String linkName, int maxTime) throws Exception {
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		adminTools.getControl(linkName).click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(maxTime);
	}

	/**
	 * Switch Sugar Opportunity Module View.
	 * <p>
	 * When used, you will be left on the Administration page.<br>
	 *
	 * @param viewType FieldSet of desired view type for Opportunity vs. RLI views and Roll Up choice.<br>
	 *                 Expected <Key,Values>:<br>
	 *                     "desiredView" => "Opportunities" or "RevenueLineItems" -- desired view
	 *                     "rollUp" => "latestCloseDate" or "earliestCloseDate" -- required when switching to Opportunities View
	 * @throws Exception
	 */
	public void switchOpportunityView(FieldSet viewType) throws Exception {
		navToOpportunitySettings();
		switch(viewType.get("desiredView")) {
		case "Opportunities": {
			oppViewSettings.getControl("oppView").set("true");
			oppViewSettings.getControl(viewType.get("rollUp")).set("true");
			break;
		}
		case "RevenueLineItems": {
			oppViewSettings.getControl("rliView").set("true");
			break;
		}
		default:
			throw new Exception("No valid desiredView option was given!");
		}
		VoodooUtils.voodoo.log.info("Swtiching to " + viewType.get("desiredView") + " View.");
		oppViewSettings.getControl("saveButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(90000); // For the first Loading after save
		VoodooUtils.waitForReady(30000); // For the second loading once we are taken to Opp Module
	}

	public class Api {
		private WsRestV10 rest = new WsRestV10();

		public boolean isOpportunitiesView() throws Exception {
			WsRestV10 rest = new WsRestV10();
			return rest.isOpportunitiesView();
		}

		public void switchToOpportunitiesView() throws Exception {
			WsRestV10 rest = new WsRestV10();
			rest.switchToOpportunitiesView();
			VoodooUtils.refresh();
		}

		public void switchToRevenueLineItemsView() throws Exception {
			WsRestV10 rest = new WsRestV10();
			rest.switchToRevenueLineItemsView();
			VoodooUtils.refresh();
		}
	} // Api
} // AdminModule
