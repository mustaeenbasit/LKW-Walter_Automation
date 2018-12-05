package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Lead module object exposing controls and tasks for the Leads module
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class LeadsModule extends StandardModule {
	protected static LeadsModule module;

	public static LeadsModule getInstance() throws Exception {
		if (module == null) module = new LeadsModule();
		return module;
	}

	private LeadsModule() throws Exception {
		moduleNameSingular = "Lead";
		moduleNamePlural = "Leads";
		bwcSubpanelName = "Leads";
		recordClassName = LeadRecord.class.getName();

		//Load Leads Module element definitions from CSV
		loadFields();

		// Leads Module Menu Items
		menu = new Menu(this);
		menu.addControl("createLead", "a", "css", "li[data-module='Leads'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_LEAD']");
		menu.addControl("createLeadFromVcard", "a", "css", "li[data-module='Leads'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_VCARD']");
		menu.addControl("viewLeads", "a", "css", "li[data-module='Leads'] ul[role='menu'] a[data-navbar-menu-item='LNK_LEAD_LIST']");
		menu.addControl("viewLeadReports", "a", "css", "li[data-module='Leads'] ul[role='menu'] a[data-navbar-menu-item='LNK_LEAD_REPORTS']");
		menu.addControl("importLeads", "a", "css", "li[data-module='Leads'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_LEADS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Leads...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("full_name");
		listView.addHeader("status");
		listView.addHeader("phone_work");
		listView.addHeader("date_entered");
		listView.addHeader("account_name");
		listView.addHeader("email");
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		
		// Related Subpanels
		relatedModulesMany.put("calls_leads", sugar().calls);
		relatedModulesMany.put("lead_meetings", sugar().meetings);
		relatedModulesMany.put("lead_tasks", sugar().tasks);
		relatedModulesMany.put("lead_notes", sugar().notes);
		relatedModulesMany.put("campaign_leads", sugar().campaigns);
		relatedModulesMany.put("lead_emails", sugar().emails);

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("full_name");
		standardsubpanel.addHeader("refered_by");
		standardsubpanel.addHeader("lead_source");
		standardsubpanel.addHeader("phone_work");
		standardsubpanel.addHeader("email");
		standardsubpanel.addHeader("lead_source_description");
		standardsubpanel.addHeader("assigned_user_name");


		// Add Subpanels
		recordView.addSubpanels();
		recordView.subpanels.get("Campaigns").setModuleName("CampaignLog");

		// Leads Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // end LeadsModule
