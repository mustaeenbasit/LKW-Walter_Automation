package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * Contains data and tasks associated with the Campaigns module, such as field
 * data.
 * 
 * @author David Safar <dsafar@sugarcrm.com> 
 */
public class CampaignsModule extends BWCModule {
	protected static CampaignsModule module;
	
	public static CampaignsModule getInstance() throws Exception {
		if(module == null) 
			module = new CampaignsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private CampaignsModule() throws Exception {
		moduleNameSingular = "Campaign";
		moduleNamePlural = "Campaigns";
		bwcSubpanelName = "Campaigns";
		recordClassName = CampaignRecord.class.getName();
		
		// Load field defs from CSV
		loadFields();

		// Define the columns on the ListView.
		listView.addHeader("campaign");
		listView.addHeader("status");
		listView.addHeader("type");
		listView.addHeader("date_end");
		listView.addHeader("user");
		listView.addHeader("date_created");

		// Add control for "View Status" button on Campaigns Listview
		for(int i=1; i <= 99; i++){
			// Build internal Voodoo names for each control in a row.
			String viewStatus = String.format("viewStatus%02d", i);

			// Build a string prefix that represents the current row in each control.
			String currentRow = "table.list.view tbody tr:nth-of-type(" + (i + 2) + ")";

			// Add Voodoo controls for all controls in the row.
			listView.addControl(viewStatus, "a", "css", currentRow + " td:nth-of-type(9) a");
		}

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("parentName", "Accounts");
		relatedModulesOne.put("teamName", "Teams");
	
		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		
		// Campaigns Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createCampaign", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNK_NEW_CAMPAIGN']");
		menu.addControl("createCampaignWizard", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNL_NEW_CAMPAIGN_WIZARD']");
		menu.addControl("createCampaignClassic", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNK_NEW_CAMPAIGN']");
		menu.addControl("viewCampaigns", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNK_CAMPAIGN_LIST']");
		menu.addControl("viewNewsletters", "a", "css", "li.dropdown.active [data-navbar-menu-item='LBL_NEWSLETTERS']");
		menu.addControl("createEmailTemplate", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNK_NEW_EMAIL_TEMPLATE']");
		menu.addControl("viewEmailTemplates", "a", "css", "li.dropdown.active [data-navbar-menu-item='LNK_EMAIL_TEMPLATE_LIST']");
		menu.addControl("setUpEmail", "a", "css", "li.dropdown.active [data-navbar-menu-item='LBL_EMAIL_SETUP_WIZARD']");
		menu.addControl("viewDiagnostics", "a", "css", "li.dropdown.active [data-navbar-menu-item='LBL_DIAGNOSTIC_WIZARD']");
		menu.addControl("createLeadForm", "a", "css", "li.dropdown.active [data-navbar-menu-item='LBL_WEB_TO_LEAD']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Campaigns...");
		super.init();
		
		// Related Subpanels
		relatedModulesMany.put("campaign_accounts", sugar().accounts);
		relatedModulesMany.put("campaign_contacts", sugar().contacts);
		relatedModulesMany.put("campaign_leads", sugar().leads);
		relatedModulesMany.put("campaign_opportunities", sugar().opportunities);
		relatedModulesMany.put("campaign_prospects", sugar().targetlists);
		
		// Add Subpanels
		detailView.addSubpanels();
		
		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}

	/**
	 * Configure the Email Settings in Campaign > setUpEmail.
	 * <p>
	 * Can be called anywhere in the Application.<br>
	 * When used, leaves you at the Email Setup for Campaigns <br> displaying the <b>list of Inbound email settings</b>.
	 *
	 * @param settings FieldSet of Email Settings to use from campaign module
	 * @throws Exception
	 */
	public void setupEmail(FieldSet settings) throws Exception {
		sugar().campaigns.navToListView();
		sugar().navbar.selectMenuItem(sugar().campaigns, "setUpEmail");
		VoodooUtils.focusFrame("bwc-frame");

		if (checkFieldsExists(settings, "userName"))
			new VoodooControl("input", "id", "mail_smtpuser").set(settings.get("userName"));
		if (checkFieldsExists(settings, "password"))
			new VoodooControl("input", "id", "mail_smtppass").set(settings.get("password"));
		if (checkFieldsExists(settings, "fromName"))
			new VoodooControl("input", "id", "notify_fromname").set(settings.get("fromName"));
		if (checkFieldsExists(settings, "fromAddress"))
			new VoodooControl("input", "id", "notify_fromaddress").set(settings.get("fromAddress"));
		if (checkFieldsExists(settings, "smtpServer"))
			new VoodooControl("input", "id", "mail_smtpserver").set(settings.get("smtpServer"));
		if (checkFieldsExists(settings, "smtpAuth"))
			new VoodooControl("checkbox", "id", "mail_smtpauth_req").set(settings.get("smtpAuth"));
		if (checkFieldsExists(settings, "smtpPort"))
			new VoodooControl("input", "id", "mail_smtpport").set(settings.get("smtpPort"));
		if (checkFieldsExists(settings, "smtpssl"))
			new VoodooControl("select", "id", "mail_smtpssl").set(settings.get("smtpssl"));
		if (checkFieldsExists(settings, "emailsPerRun"))
			new VoodooControl("input", "id", "massemailer_campaign_emails_per_run").set(settings.get("emailsPerRun"));
		if (checkFieldsExists(settings, "trackingLocationType"))
			new VoodooControl("input", "id", "massemailer_tracking_entities_location_type").set(settings.get("trackingLocationType"));
		if (checkFieldsExists(settings, "trackingLocation"))
			if (!new VoodooControl("input", "id", "massemailer_tracking_entities_location").isDisabled())
				new VoodooControl("input", "id", "massemailer_tracking_entities_location").set(settings.get("trackingLocation"));
		if (checkFieldsExists(settings, "emailCopy"))
			new VoodooControl("input", "name", "massemailer_email_copy").set(settings.get("emailCopy"));

		new VoodooControl("input", "id", "wiz_next_button").click();
		VoodooUtils.waitForReady();

		if (!(new VoodooControl("checkbox", "id", "create_mbox")).isChecked()) {
			(new VoodooControl("checkbox", "id", "create_mbox")).click();
			VoodooUtils.waitForReady();
		}
		new VoodooControl("input", "id", "ssl").click();
		new VoodooControl("select", "id", "protocol").set(settings.get("protocol"));

		// this hack is added because of the validation failure in the product code , the latency cannot be handle using the wait for ui element
		int timeout = 1;
		while (new VoodooControl("input", "id", "wiz_current_step").getAttribute("value").equals("2") && (timeout < 20)) {
			new VoodooControl("input", "id", "wiz_next_button").click();
			VoodooUtils.pause(100);
		}

		new VoodooControl("input", "id", "wiz_submit_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Check the field exists
	 * <p>
	 * Can be called in the class <br>
	 * When used, checks the key exists and key has value
	 *
	 * @param settings FieldSet of Campaign Module Settings to use for setupEmail .
	 * @param key of the settings field that is needs to be checked.
	 * @throws Exception
	 */
	private boolean checkFieldsExists(FieldSet settings, String key) throws Exception {
		if (settings.containsKey(key)) {
			if ((settings.get(key).isEmpty()) || (settings.get(key).equals("null")))
				return false;
			else
				return true;
		}
		else
			return false;
	}

} // AccountsModule
