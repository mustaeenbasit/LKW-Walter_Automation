package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.ProcessesListView;

/**
 * Contains data and tasks associated with the ProcessesModule module
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class ProcessesModule extends StandardModule {
	protected static ProcessesModule module;
	public ProcessesListView myProcessesListView, processManagementListView, unattendedProcessesListView;

	public static ProcessesModule getInstance() throws Exception {
		if(module == null)
			module = new ProcessesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProcessesModule() throws Exception {
		moduleNameSingular = "pmse_Inbox";
		moduleNamePlural = "pmse_Inbox";

		// Load field defs from CSV
		loadFields();
		
		// Processes Module Menu items
		menu = new Menu(this);
		menu.addControl("viewProcesses", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_LIST']");
		menu.addControl("processManagement", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_PMSE_INBOX_PROCESS_MANAGEMENT']");
		menu.addControl("unattendedProcesses", "a", "css", "li[data-module='" + moduleNamePlural + "'] ul[role='menu'] a[data-navbar-menu-item='LNK_PMSE_INBOX_UNATTENDED_PROCESSES']");
	}

	/**
	 * Perform setup which depends on other modules or views already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Processes Module...");
		super.init();

		myProcessesListView = new ProcessesListView(this);
		processManagementListView  = new ProcessesListView(this);
		unattendedProcessesListView = new ProcessesListView(this);
		
		myProcessesListView.addHeader("task_name");
		myProcessesListView.addHeader("cas_user_id_full_name");
		myProcessesListView.addHeader("date_entered");
		myProcessesListView.addHeader("date_modified");  
		
		processManagementListView.addHeader("cas_status");
		processManagementListView.addHeader("cas_create_date");
		processManagementListView.addHeader("cas_user_id_full_name");
		processManagementListView.addHeader("assigned_user_name");
		
		unattendedProcessesListView.addHeader("cas_user_full_name");
		unattendedProcessesListView.addHeader("date_entered");
		unattendedProcessesListView.addHeader("assigned_user_name");

		// Specific control for save and design
		createDrawer.addControl("saveAndDesignButton", "a", "css", ".create.fld_save_open_design a");
	}

	/**
	 * Navigates to the list view of the current module.
	 * <p>
	 * NOTE: This method is reimplemented here because we extend Module and not RecordsModule
	 *
	 * @throws Exception
	 */
	public void navToListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating to " + moduleNamePlural + " module ListView...");
		sugar().navbar.navToModule(moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration(30000);
	}
} // ProcessesModule
