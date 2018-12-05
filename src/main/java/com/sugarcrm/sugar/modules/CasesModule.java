package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Cases module object which contains tasks associated with the Cases module
 * like create/deleteAll
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class CasesModule extends StandardModule {
	protected static CasesModule module;

	public static CasesModule getInstance() throws Exception {
		if (module == null)
			module = new CasesModule();
		return module;
	}

	private CasesModule() throws Exception {
		moduleNameSingular = "Case";
		moduleNamePlural = "Cases";
		bwcSubpanelName = "Cases";
		recordClassName = CaseRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Fields
		relatedModulesOne.put("accountName", "Account");
		relatedModulesOne.put("assignedUserName", "User");
		relatedModulesOne.put("teamName", "Team");

		// Cases Module Menu Items
		// TODO: When JIRA story VOOD-451 is un-blocked by SFA-1287, please
		// update element def's
		menu = new Menu(this);
		menu.addControl("createCase", "a", "css", "li[data-module='Cases'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_CASE']");
		menu.addControl("viewCases", "a", "css", "li[data-module='Cases'] ul[role='menu'] a[data-navbar-menu-item='LNK_CASE_LIST']");
		menu.addControl("viewCaseReports", "a", "css", "li[data-module='Cases'] ul[role='menu'] a[data-navbar-menu-item='LNK_CASE_REPORTS']");
		menu.addControl("importCases", "a", "css", "li[data-module='Cases'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_CASES']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Cases...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("case_number");
		listView.addHeader("name");
		listView.addHeader("account_name");
		listView.addHeader("priority");
		listView.addHeader("status");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_entered");
		listView.addHeader("date_modified");
		listView.addHeader("team_name");
		
		
		// Related Subpanels
		relatedModulesMany.put("case_calls", sugar().calls);
		relatedModulesMany.put("case_meetings", sugar().meetings);
		relatedModulesMany.put("case_tasks", sugar().tasks);
		relatedModulesMany.put("case_notes", sugar().notes);
		relatedModulesMany.put("documents_cases", sugar().documents);
		relatedModulesMany.put("contacts_cases", sugar().contacts);
		relatedModulesMany.put("cases_bugs", sugar().bugs);
		relatedModulesMany.put("case_emails", sugar().emails);
		relatedModulesMany.put("relcases_kbcontents", sugar().knowledgeBase);
		
		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("case_number");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("account_name");
		standardsubpanel.addHeader("priority");
		standardsubpanel.addHeader("date_entered");
		standardsubpanel.addHeader("assigned_user_name");

		// Cases Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // Cases