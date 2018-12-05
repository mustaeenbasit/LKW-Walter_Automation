package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProjectRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Projects module, such as field
 * data.
 * 
 */
public class ProjectsModule extends BWCModule {
	protected static ProjectsModule module;
	
	public static ProjectsModule getInstance() throws Exception {
		if(module == null) 
			module = new ProjectsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProjectsModule() throws Exception {
		moduleNameSingular = "Project";
		// TODO: TR-6475
		// File Product bug to change "data-module" attribute value from "Project" to "Projects"
		// Once fixed, remove the below code block and uncomment the below line
		moduleNamePlural = "Project";
		// moduleNamePlural = "Projects";
		bwcSubpanelName = "Projects";
		recordClassName = ProjectRecord.class.getName();
		
		// Load field defs from CSV
		loadFields();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("date_start");
		listView.addHeader("date_end");
		listView.addHeader("status");
		listView.addHeader("assigned_user_name");
		
		// Override link Column and regenerate affected part of listView
		listView.setLinkColumn(3);

		// Relate Widget access
		relatedModulesOne.put("assignedTo", "Users");
		relatedModulesOne.put("teams", "Teams");
	
		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		
		// Projects Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createProject", "a", "css", "[data-navbar-menu-item='LNK_NEW_PROJECT']");
		menu.addControl("createProjectTemplate", "a", "css", "[data-navbar-menu-item='LNK_NEW_PROJECT_TEMPLATES']");
		menu.addControl("viewProjects", "a", "css", "[data-navbar-menu-item='LNK_PROJECT_LIST']");
		menu.addControl("viewProjectsTemplates", "a", "css", "[data-navbar-menu-item='LNK_PROJECT_TEMPLATES_LIST']");
		menu.addControl("viewProjectsTasks", "a", "css", "[data-navbar-menu-item='LNK_PROJECT_TASK_LIST']");
		menu.addControl("viewProjectsDashboard", "a", "css", "[data-navbar-menu-item='LNK_PROJECT_DASHBOARD']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Projects...");
		super.init();
		
		// Related Subpanels
		// TODO: Add Project_tasks, Project_holidays, Activities, History
		relatedModulesMany.put("Projects_contacts", sugar().contacts);
		relatedModulesMany.put("Projects_accounts", sugar().accounts);
		relatedModulesMany.put("contacts_opportunities", sugar().opportunities);
		relatedModulesMany.put("Projects_quotes", sugar().quotes);
		relatedModulesMany.put("Project_cases", sugar().cases);
		
		// Add Subpanels
		detailView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("assigned_user_name");
		standardsubpanel.addHeader("estimated_start_date");
		standardsubpanel.addHeader("estimated_end_date");
		
		// Project Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // ProjectsModule
