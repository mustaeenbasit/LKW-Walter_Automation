package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Quotes module, such as field
 * data.
 * 
 * @author Jessica Cho 
 */
public class QuotesModule extends BWCModule {
	protected static QuotesModule module;

	public static QuotesModule getInstance() throws Exception {
		if(module == null) 
			module = new QuotesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private QuotesModule() throws Exception {
		moduleNameSingular = "Quote";
		moduleNamePlural = "Quotes";
		bwcSubpanelName = "Quotes";
		recordClassName = QuoteRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Related Fields
		relatedModulesOne.put("billingAccountName", "Accounts");
		relatedModulesOne.put("shippingAccountName", "Accounts");
		relatedModulesOne.put("billingContactName", "Contacts");
		relatedModulesOne.put("shippingContactName", "Contacts");
		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("teamName", "Teams");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		subpanels.put("quotesBillToAccounts", new StandardSubpanel(this,"quotes"));
		subpanels.put("quotesShipToAccounts", new StandardSubpanel(this,"quotes_shipto"));
		subpanels.put("quotesBillToContacts", new StandardSubpanel(this,"billing_quotes"));
		subpanels.put("quotesShipToContacts", new StandardSubpanel(this,"quotes"));

		// Quotes Module Menu items
		menu = new Menu(this);
		menu.addControl("createQuote", "a", "css", "[data-navbar-menu-item='LNK_NEW_QUOTE']");
		menu.addControl("viewQuotes", "a", "css", "[data-navbar-menu-item='LNK_QUOTE_LIST']");
		menu.addControl("viewQuoteReports", "a", "css", "[data-navbar-menu-item='LNK_QUOTE_REPORTS']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Quotes...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("number");
		listView.addHeader("subject");
		listView.addHeader("account_name");
		listView.addHeader("stage");
		listView.addHeader("total_amount");
		listView.addHeader("valid_until");
		listView.addHeader("user");
		listView.addHeader("date_created");

		// Override link Column and regenerate affected part of listView
		listView.setLinkColumn(5);

		// Related Subpanels
		// TODO: Figure out how to group related modules for Activities and History
		relatedModulesMany.put("contracts_quotes", sugar().contracts);
		relatedModulesMany.put("documents_quotes", sugar().documents);
		relatedModulesMany.put("quote_tasks", sugar().tasks);
		relatedModulesMany.put("quote_emails", sugar().emails);

		// Add Subpanels
		detailView.addSubpanels();

		// Add Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // QuotesModule
