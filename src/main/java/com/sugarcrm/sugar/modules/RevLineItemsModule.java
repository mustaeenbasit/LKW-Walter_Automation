package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.SugarField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Revenue Line Item module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com> 
 */
public class RevLineItemsModule extends StandardModule {
	protected static RevLineItemsModule module;

	public static RevLineItemsModule getInstance() throws Exception {
		if(module == null) 
			module = new RevLineItemsModule();
		return module;
	}

	private RevLineItemsModule() throws Exception {
		moduleNameSingular = "RevenueLineItem";
		moduleNamePlural = "RevenueLineItems";
		bwcSubpanelName = "RevenueLineItems";
		recordClassName = RevLineItemRecord.class.getName();

		loadFields();

		// Populate default data.
		for(String currentFieldName : fields.keySet()) {
			SugarField currentField = fields.get(currentFieldName);
			String defaultValue = currentField.get("default_value");

			if(defaultValue != null && !(defaultValue.isEmpty()))
				defaultData.put(currentFieldName, defaultValue);
		}

		relatedModulesOne.put("assignedUserName", "Users");
		relatedModulesOne.put("teamName", "Teams");
		relatedModulesOne.put("opportunityName", "Opportunities");
		relatedModulesOne.put("salesStage", "Sales Stage");

		// RevLineItem Menu Items
		menu = new Menu(this);
		menu.addControl("createRevenueLineItem", "a", "css", "li[data-module='RevenueLineItems'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_REVENUELINEITEM']");
		menu.addControl("viewRevenueLineItems", "a", "css", "li[data-module='RevenueLineItems'] ul[role='menu'] a[data-navbar-menu-item='LNK_REVENUELINEITEM_LIST']");
		menu.addControl("importRevenueLineItems", "a", "css", "li[data-module='RevenueLineItems'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_REVENUELINEITEMS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Revenue Line Items...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("opportunity_name");
		listView.addHeader("account_name");
		listView.addHeader("sales_stage");
		listView.addHeader("probability");
		listView.addHeader("date_closed");
		listView.addHeader("product_template_name");
		listView.addHeader("category_name");
		listView.addHeader("quantity");
		listView.addHeader("worst_case");
		listView.addHeader("likely_case");
		listView.addHeader("best_case");
		listView.addHeader("quote_name"); 
		listView.addHeader("assigned_user_name");
		listView.addHeader("date_modified");
		listView.addHeader("date_entered");
		listView.addHeader("commit_stage");
		
		
		// Related Subpanels
		relatedModulesMany.put("revenuelineitem_calls", sugar().calls);
		relatedModulesMany.put("revenuelineitem_meetings", sugar().meetings);
		relatedModulesMany.put("revenuelineitem_tasks", sugar().tasks);
		relatedModulesMany.put("revenuelineitem_notes", sugar().notes);
		relatedModulesMany.put("documents_revenuelineitems", sugar().documents);
		relatedModulesMany.put("revenuelineitems_emails", sugar().emails);

		// Define Actions menu items specific to Revenue Line Items module 
		recordView.addControl("generateQuote", "span", "css", ".fld_convert_to_quote_button");

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("opportunity_name");
		standardsubpanel.addHeader("account_name");
		standardsubpanel.addHeader("sales_stage");
		standardsubpanel.addHeader("date_closed");
		standardsubpanel.addHeader("product_template_name");
		standardsubpanel.addHeader("category_name");
		standardsubpanel.addHeader("worst_case");
		standardsubpanel.addHeader("likely_case");
		standardsubpanel.addHeader("best_case");
		standardsubpanel.addHeader("quote_name");
		standardsubpanel.addHeader("assigned_user_name");

		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // RevLineItemModule