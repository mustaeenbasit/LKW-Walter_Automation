package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Meetings module, such as field
 * data.
 * 
 * @author Ian Fleming <ifleming@sugarcrm.com>
 */
public class MeetingsModule extends ActivityModule {
	protected static MeetingsModule module;

	public static MeetingsModule getInstance() throws Exception {
		if (module == null)
			module = new MeetingsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * 
	 * @throws Exception
	 */
	private MeetingsModule() throws Exception {
		moduleNameSingular = "Meeting";
		moduleNamePlural = "Meetings";
		bwcSubpanelName = "Activities";
		recordClassName = MeetingRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("teamName", "Teams");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));

		// Campaigns Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createMeeting", "a", "css", "[data-module='Meetings'] [data-navbar-menu-item='LNK_NEW_MEETING']");
		menu.addControl("viewMeetings", "a", "css", "[data-module='Meetings'] [data-navbar-menu-item='LNK_MEETING_LIST']");
		menu.addControl("importMeetings", "a", "css", "[data-module='Meetings'] [data-navbar-menu-item='LNK_IMPORT_MEETINGS']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Campaigns...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("parent_name"); // with no sort icon
		listView.addHeader("date_start");
		listView.addHeader("date_end");
		listView.addHeader("status");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_entered");
		listView.addHeader("team_name");

		// Related Subpanels
		relatedModulesMany.put("meetings_contacts", sugar().contacts);
		relatedModulesMany.put("meetings_users", sugar().users);
		relatedModulesMany.put("meetings_leads", sugar().leads);
		relatedModulesMany.put("meetings_notes", sugar().notes);

		// Add Subpanels
		recordView.addSubpanels();


		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("date_start");
		standardsubpanel.addHeader("date_end");
		standardsubpanel.addHeader("assigned_user_name");

		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // MeetingsModule
