package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Accounts module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * @author David Safar <dsafar@sugarcrm.com> 
 */
public class AccountsModule extends StandardModule {
	protected static AccountsModule module;

	public static AccountsModule getInstance() throws Exception {
		if(module == null) 
			module = new AccountsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private AccountsModule() throws Exception {
		moduleNameSingular = "Account";
		moduleNamePlural = "Accounts";
		bwcSubpanelName = "Accounts";
		recordClassName = AccountRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Relate Widget access
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("parentName", "Accounts");
		relatedModulesOne.put("campaignName", "Campaigns");
		relatedModulesOne.put("teamName", "Teams");

		// Account Module Menu items
		menu = new Menu(this);
		menu.addControl("createAccount", "a", "css", "li[data-module='Accounts'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_ACCOUNT']");
		menu.addControl("viewAccounts", "a", "css", "li[data-module='Accounts'] ul[role='menu'] a[data-navbar-menu-item='LNK_ACCOUNT_LIST']");
		menu.addControl("viewAccountReports", "a", "css", "li[data-module='Accounts'] ul[role='menu'] a[data-navbar-menu-item='LNK_ACCOUNT_REPORTS']");
		menu.addControl("importAccounts", "a", "css", "li[data-module='Accounts'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_ACCOUNTS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Accounts...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("billing_address_city");
		listView.addHeader("billing_address_country");
		listView.addHeader("phone_office");
		listView.addHeader("assigned_user_name");
		listView.addHeader("email");
		listView.addHeader("date_entered");
		listView.addHeader("date_modified");

		// TODO: Move the common controls for each view to its View subclass. 
		recordView.addControl("relAssignedUserName", "a", "css", "span.fld_assigned_user_name a");
		recordView.addControl("assignedUserName", "a", "css", "span.fld_assigned_user_name div a");

		// What?
		recordView.addControl("selectListSearchField", "input", "css", "html body div.select2-drop div.select2-search input.select2-input");
		recordView.addControl("selectListFirstItem", "span", "css", "html body div.select2-drop ul.select2-results li.select2-results-dept-0 div.select2-result-label span.select2-match");

		recordView.addControl("accountName", "a", "css", "div.record span.fld_parent_name div a"); // Account relate widget
		recordView.addControl("campaignName", "a", "css", "div.record span.fld_campaign_name div a"); // Campaign relate widget
		recordView.addControl("assignedUserName", "a", "css", "div.record span.fld_assigned_user_name div a"); // Assigned User relate widget

		// Related Subpanels
		relatedModulesMany.put("account_calls", sugar().calls);
		relatedModulesMany.put("accounts_contacts", sugar().contacts);
		relatedModulesMany.put("revenuelineitems_accounts", sugar().revLineItems);
		relatedModulesMany.put("accounts_opportunities", sugar().opportunities);
		relatedModulesMany.put("account_meetings", sugar().meetings);
		relatedModulesMany.put("account_tasks", sugar().tasks);
		relatedModulesMany.put("account_notes", sugar().notes);
		relatedModulesMany.put("member_accounts", sugar().accounts);
		relatedModulesMany.put("account_leads", sugar().leads);
		relatedModulesMany.put("account_cases", sugar().cases);
		relatedModulesMany.put("accounts_bugs", sugar().bugs);
		relatedModulesMany.put("documents_accounts", sugar().documents);
		relatedModulesMany.put("account_contracts", sugar().contracts);
		relatedModulesMany.put("account_emails", sugar().emails);
		relatedModulesMany.put("quotes_billto_accounts", sugar().quotes);
		relatedModulesMany.put("quotes_shipto_accounts", sugar().quotes);
		relatedModulesMany.put("products_accounts", sugar().quotedLineItems);
		relatedModulesMany.put("projects_accounts", sugar().projects);

		// Add Subpanels
		recordView.addSubpanels();
		// Adding custom subpanels
		recordView.addCustomSubpanel(sugar().quotes, "Quotes (Bill To)", "quotesBillToAccounts");
		recordView.addCustomSubpanel(sugar().quotes, "Quotes (Ship To)", "quotesShipToAccounts");


		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("billing_address_city");
		standardsubpanel.addHeader("billing_address_country");
		standardsubpanel.addHeader("phone_office");

		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // AccountsModule
