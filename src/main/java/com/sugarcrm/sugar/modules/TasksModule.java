package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Task module object exposing controls and tasks for the Tasks module
 * 
 * @author Jessica Cho
 */
public class TasksModule extends StandardModule {
	protected static TasksModule module;

	public static TasksModule getInstance() throws Exception {
		if (module == null) module = new TasksModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private TasksModule() throws Exception {
		moduleNameSingular = "Task";
		moduleNamePlural = "Tasks";
		bwcSubpanelName = "Activities";
		recordClassName = TaskRecord.class.getName();

		//Load Tasks Module element definitions from CSV
		loadFields();

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));

		// Tasks Menu
		menu = new Menu(this);
		menu.addControl("createTask", "a", "css", "li[data-module='Tasks'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_TASK']");
		menu.addControl("viewTasks", "a", "css", "li[data-module='Tasks'] ul[role='menu'] a[data-navbar-menu-item='LNK_TASK_LIST']");
		menu.addControl("importTasks", "a", "css", "li[data-module='Tasks'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_TASKS']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Tasks...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("contact_name");
		listView.addHeader("parent_name");
		listView.addHeader("date_due");
		listView.addHeader("team_name");
		listView.addHeader("date_start");
		listView.addHeader("status");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_entered");
		listView.addHeader("date_modified");
		
		// Related Subpanels
		relatedModulesMany.put("tasks_notes", sugar().notes);

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("contact_name");
		standardsubpanel.addHeader("date_start");
		standardsubpanel.addHeader("date_due");
		standardsubpanel.addHeader("assigned_user_name");


		// Tasks Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // end TasksModule
