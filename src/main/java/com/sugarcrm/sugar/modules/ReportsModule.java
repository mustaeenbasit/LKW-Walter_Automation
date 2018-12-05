package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Reports module, such as field
 * data.
 * 
 * @author Alex Nisevich 
 */
public class ReportsModule extends BWCModule {
	protected static ReportsModule module;

	public static ReportsModule getInstance() throws Exception {
		if(module == null) 
			module = new ReportsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ReportsModule() throws Exception {
		moduleNameSingular = "Reports";
		moduleNamePlural = "Reports";

		// Reports Module Menu items
		menu = new Menu(this);
		menu.addControl("createReport", "a", "css", "[data-navbar-menu-item='LBL_CREATE_REPORT']");
		menu.addControl("myFavoriteReports", "a", "css", "[data-navbar-menu-item='LBL_FAVORITE_REPORTS']");
		menu.addControl("viewReports", "a", "css", "[data-navbar-menu-item='LBL_ALL_REPORTS']");
		menu.addControl("manageAdvancedReports", "a", "css", "[data-navbar-menu-item='LNK_ADVANCED_REPORTING']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Quotes...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("module");
		listView.addHeader("report_type");
		listView.addHeader("user");
		listView.addHeader("schedule_report");
		listView.addHeader("last_run_date");
		listView.addHeader("date_created");

		// Override link Column and regenerate affected part of listView
		listView.setLinkColumn(5);

	}
} // ReportsModule
