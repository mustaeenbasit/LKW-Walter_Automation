package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Product Catalog, such as field
 * data.
 * 
 * @author Mohd Shariq <mshariq@sugarcrm.com>
 */
public class ProductCatalogModule extends StandardModule {
	protected static ProductCatalogModule module;

	public static ProductCatalogModule getInstance() throws Exception {
		if(module == null) 
			module = new ProductCatalogModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProductCatalogModule() throws Exception {
		moduleNameSingular = "ProductTemplate";
		moduleNamePlural = "ProductTemplates";
		bwcSubpanelName = "ProductTemplates";
		recordClassName = ProductCatalogRecord.class.getName();

		// Load field defs from CSV
		loadFields();

		// Product Catalog Module Menu items
		menu = new Menu(this);
		menu.addControl("createProduct", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT']");
		menu.addControl("viewProduct", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_PRODUCT_LIST']");
		menu.addControl("viewManufacturers", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_MANUFACTURER']");
		menu.addControl("viewProductCategories", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_CATEGORY']");
		menu.addControl("viewProductTypes", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_TYPE']");
		menu.addControl("importProductCatalog", "a", "css", "li[data-module='ProductTemplates'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PRODUCT_CATALOG']");
		
		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init ProductCatalog...");
		super.init();

		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("type_name");
		listView.addHeader("category_name");
		listView.addHeader("status");
		listView.addHeader("qty_in_stock");
		listView.addHeader("cost_price");
		listView.addHeader("list_price");
		listView.addHeader("discount_price");

		// Product Catalog Mass Update Panel
		massUpdate = new MassUpdate(this);
	}

	/**
	 * Navigates to the list view of the current module.
	 * 
	 * @throws Exception
	 */
	@Override
	public void navToListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating list view ...");
		// navigate to product catalog
		sugar().admin.navToAdminPanelLink("productCatalog");
	}

	/**
	 * Creates a single user record via the UI from the data in a FieldSet. 
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 *
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	@Override
	public Record create(FieldSet testData) throws Exception {
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(testData);

		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");
		navToListView();

		// Move to the CreateDrawer and show hidden fields.
		listView.create();

		// Iterate over the field data and set field values.
		createDrawer.setFields(recordData);

		createDrawer.getControl("saveButton").click(); // save() would clear the alert we need.
		createDrawer.getControl("saveButton").waitForInvisible();

		// Handle alerts and scrape GUID from success message.
		VoodooControl successAlert = sugar().alerts.getSuccess();
		successAlert.waitForVisible();
		String href = new VoodooControl("a", "css", successAlert.getHookString() + " a").getAttribute("href");
		int lastSlashPosition = href.lastIndexOf('/');
		String guid = href.substring(lastSlashPosition + 1);
		sugar().alerts.waitForLoadingExpiration();

		// Create the record and set its GUID.
		StandardRecord toReturn = (StandardRecord)Class.forName(this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
} // ProductCatalogModule
