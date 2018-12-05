package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.SugarField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Quoted Line Item module, such as field
 * data.
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class QuotedLineItemsModule extends StandardModule {
	protected static QuotedLineItemsModule module;

	public static QuotedLineItemsModule getInstance() throws Exception {
		if(module == null)
			module = new QuotedLineItemsModule();
		return module;
	}

	private QuotedLineItemsModule() throws Exception {
		moduleNameSingular = "Product";
		moduleNamePlural = "Products";
		bwcSubpanelName = "Products";
		recordClassName = QuotedLineItemRecord.class.getName();

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

		// Quoted Line Item Menu Items
		menu = new Menu(this);
		menu.addControl("createQuotedLineItem", "a", "css", "li[data-module='Products'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT']");
		menu.addControl("viewQuotedLineItems", "a", "css", "li[data-module='Products'] ul[role='menu'] a[data-navbar-menu-item='LNK_PRODUCT_LIST']");
		menu.addControl("importQuotedLineItems", "a", "css", "li[data-module='Products'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PRODUCTS']");

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed.
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Quoted Line Items...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("account_name");
		listView.addHeader("status");
		listView.addHeader("quote_name");
		listView.addHeader("quantity");
		listView.addHeader("discount_price");
		listView.addHeader("cost_price");
		listView.addHeader("discount_amount");
		listView.addHeader("assigned_user_name");

		// Related Subpanels
		relatedModulesMany.put("contact_products", sugar().contacts);
		relatedModulesMany.put("documents_products", sugar().documents);
		relatedModulesMany.put("product_notes", sugar().notes);

		// Add Subpanels
		recordView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("account_name");
		standardsubpanel.addHeader("contact_name");
		standardsubpanel.addHeader("date_purchased");
		standardsubpanel.addHeader("discount_price");
		standardsubpanel.addHeader("date_support_expires");


		// Account Mass Update Panel
		massUpdate = new MassUpdate(this);
	}
} // QuotedLineItemsModule