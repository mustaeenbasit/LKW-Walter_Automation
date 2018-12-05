package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * TargetLists module object exposing controls and tasks for the TargetLists module
 * 
 * @author Eric Yang <eyang@sugarcrm.com>
 */
public class TargetListsModule extends StandardModule {
	protected static TargetListsModule module;

	public static TargetListsModule getInstance() throws Exception {
		if (module == null) module = new TargetListsModule();
		return module;
	}

	private TargetListsModule() throws Exception {
		moduleNameSingular = "ProspectList";
		moduleNamePlural = "ProspectLists";
		bwcSubpanelName = "ProspectLists";
		recordClassName = TargetListRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));

		// Target Lists Menu items
		menu = new Menu(this);
		menu.addControl("createTargetList", "a", "css", "li[data-module='ProspectLists'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PROSPECT_LIST']");
		menu.addControl("viewTargetLists", "a", "css", "li[data-module='ProspectLists'] ul[role='menu'] a[data-navbar-menu-item='LNK_PROSPECT_LIST_LIST']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	  
	 * @throws Exception 
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init TargetLists...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("list_type");
		listView.addHeader("description");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_entered");

		relatedModulesMany.put("target_list_targets", sugar().targets);
		relatedModulesMany.put("target_list_contacts", sugar().contacts);
		relatedModulesMany.put("target_list_leads", sugar().leads);
		relatedModulesMany.put("target_list_users", sugar().users);
		relatedModulesMany.put("target_list_accounts", sugar().accounts);
		relatedModulesMany.put("target_list_campaigns", sugar().campaigns);

		// Add Subpanels
		recordView.addSubpanels();
	}
} // end TargetListsModule
