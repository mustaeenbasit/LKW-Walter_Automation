package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Targets module object exposing controls and tasks for the Targets module
 * 
 * @author Eric Yang <eyang@sugarcrm.com>
 */
public class TargetsModule extends StandardModule {
	protected static TargetsModule module;

	public static TargetsModule getInstance() throws Exception {
		if (module == null) module = new TargetsModule();
		return module;
	}

	private TargetsModule() throws Exception {
		moduleNameSingular = "Prospect";
		moduleNamePlural = "Prospects";
		bwcSubpanelName = "Prospects";
		recordClassName = TargetRecord.class.getName();

		// Load Targets Module element definitions from CSV
		loadFields();

		// Targets Module Menu Items
		menu = new Menu(this);
		menu.addControl("createTarget", "a", "css", "li[data-module='Prospects'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PROSPECT']");
		menu.addControl("createtargetFromVcard", "a", "css", "li[data-module='Prospects'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_VCARD']");
		menu.addControl("viewtargets", "a", "css", "li[data-module='Prospects'] ul[role='menu'] a[data-navbar-menu-item='LNK_PROSPECT_LIST']");
		menu.addControl("importTargets", "a", "css", "li[data-module='Prospects'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PROSPECTS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Targets...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("full_name");
		listView.addHeader("title");
		listView.addHeader("email"); // no sort icon
		listView.addHeader("phone_work");
		listView.addHeader("date_entered");

		// Related Subpanels
		relatedModulesMany.put("prospect_calls", sugar().calls);
		relatedModulesMany.put("prospect_meetings", sugar().meetings);
		relatedModulesMany.put("prospect_tasks", sugar().tasks);
		relatedModulesMany.put("prospect_notes", sugar().notes);
		relatedModulesMany.put("prospect_campaign_log", sugar().campaigns);
		relatedModulesMany.put("prospect_emails", sugar().emails);

		recordView.addControl("selectListCampaignLog", "span", "xpath", "//*[contains(@class,'select2-result-label')]//span[@innerHtml='Campaign Log']");

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("full_name");
		standardsubpanel.addHeader("title");
		standardsubpanel.addHeader("email");
		standardsubpanel.addHeader("phone_work");

		// Special case for Campaign Log
		recordView.subpanels.get(sugar().campaigns.moduleNamePlural).setModuleName("CampaignLog");
	}
} // TargetsModule
