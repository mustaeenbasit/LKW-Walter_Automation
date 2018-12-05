package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the ActivityListView for Activity type Modules in SugarCRM.  
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 *
 */
public class ActivityListView extends ListView {
	/**
	 * Initializes the ActivityListView and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this ActivityListView, likely passed in using the module's this
	 *                        variable when constructing the ListView.
	 * @throws Exception
	 */
	public ActivityListView(StandardModule parentModule) throws Exception {
		super(parentModule);
		
		// Calls and Meetings specific controls
		addControl("filterMySchedule", "a", "css", ".search-filter-dropdown [data-id='my_scheduled_" + parentModule.moduleNamePlural.toLowerCase() + "']");
	}

	/**
	 * Selects the My Schedule filter from the filters dropdown. You must
	 * already be on the desired ListView with the filter dropdown open. Leaves
	 * you on the ListView with the All filter selected.
	 * 
	 * @throws Exception
	 */
	public void selectFilterMySchedule() throws Exception {
		openFilterDropdown();
		getControl("filterMySchedule").click();
		sugar().alerts.waitForLoadingExpiration();
	}
	
}
