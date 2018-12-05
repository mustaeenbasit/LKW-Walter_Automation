package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Contracts module, such as field
 * data.
 * 
 */
public class ContractsModule extends BWCModule {
	protected static ContractsModule module;
	
	public static ContractsModule getInstance() throws Exception {
		if(module == null) 
			module = new ContractsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ContractsModule() throws Exception {
		moduleNameSingular = "Contract";
		moduleNamePlural = "Contracts";
		bwcSubpanelName = "Contracts";
		recordClassName = ContractRecord.class.getName();
		
		// Load field defs from CSV
		loadFields();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("account_name");
		listView.addHeader("status");
		listView.addHeader("date_start");
		listView.addHeader("date_end");
		listView.addHeader("assigned_user_name");

		// Relate Widget access
		relatedModulesOne.put("accountName", "Accounts");
		relatedModulesOne.put("opportunityName", "Opportunities");
		relatedModulesOne.put("assignedTo", "Users");
		relatedModulesOne.put("teams", "Teams");
	
		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		
		// Contracts Module Menu items
		menu = new Menu(this);
		// This is a dup of Classic, needed for standard code to work.
		menu.addControl("createContract", "a", "css", "[data-navbar-menu-item='LNK_NEW_CONTRACT']");
		menu.addControl("viewContracts", "a", "css", "[data-navbar-menu-item='LNK_CONTRACT_LIST']");
		menu.addControl("importContracts", "a", "css", "[data-navbar-menu-item='LNK_IMPORT_CONTRACTS']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Contracts...");
		super.init();
		
		// Related Subpanels
		relatedModulesMany.put("contracts_documents", sugar().documents);
		relatedModulesMany.put("contract_notes", sugar().notes);
		relatedModulesMany.put("contracts_contacts", sugar().contacts);
		relatedModulesMany.put("contracts_quotes", sugar().quotes);
		relatedModulesMany.put("contacts_products", sugar().quotedLineItems);
		
		// Add Subpanels
		detailView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("start_date");
		standardsubpanel.addHeader("end_date");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("total_contract_value");
		
		// Contract Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // ContractsModule
