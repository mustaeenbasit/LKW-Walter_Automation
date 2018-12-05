package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Calls module, such as field
 * data.
 * 
 * @author Ian Fleming <ifleming@sugarcrm.com> 
 */
public class CallsModule extends ActivityModule {
	protected static CallsModule module;

	public static CallsModule getInstance() throws Exception {
		if(module == null) 
			module = new CallsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private CallsModule() throws Exception {
		moduleNameSingular = "Call";
		moduleNamePlural = "Calls";
		bwcSubpanelName = "Activities";
		recordClassName = CallRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("teamName", "Teams");	

		// Calls Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createCall", "a", "css", "[data-module='Calls'] [data-navbar-menu-item='LNK_NEW_CALL']");
		menu.addControl("viewCalls", "a", "css", "[data-module='Calls'] [data-navbar-menu-item='LNK_CALL_LIST']");
		menu.addControl("importCalls", "a", "css", "[data-module='Calls'] [data-navbar-menu-item='LNK_IMPORT_CALLS']");
		menu.addControl("activitiesReport", "a", "css", "[data-module='Calls'] [data-navbar-menu-item='LBL_ACTIVITIES_REPORTS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Calls...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("parent_name"); // with no sort icon
		listView.addHeader("date_start");
		listView.addHeader("date_end");
		listView.addHeader("status");
		listView.addHeader("direction");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_entered");

		// Related Subpanels
		relatedModulesMany.put("calls_contacts", sugar().contacts);
		relatedModulesMany.put("calls_leads", sugar().leads);
		relatedModulesMany.put("calls_users", sugar().users);
		relatedModulesMany.put("calls_notes", sugar().notes);

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("date_start");
		standardsubpanel.addHeader("date_end");
		standardsubpanel.addHeader("assigned_user_name");


		// Call Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // CallsModule
