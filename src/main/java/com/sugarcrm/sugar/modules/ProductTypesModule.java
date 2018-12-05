package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BWCRecord;
import com.sugarcrm.sugar.records.ProductTypeRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.ProductTypesEditView;
import com.sugarcrm.sugar.views.ProductTypesListView;

/**
 * Contains data and tasks associated with the ProductTypes module, such as field
 * data.
 * 
 * @author Mohd. Shariq <mshariq@sugarcrm.com>
 */
public class ProductTypesModule extends BWCModule {
	protected static ProductTypesModule module;
	public ProductTypesEditView editView = new ProductTypesEditView(ProductTypesModule.this);
	public ProductTypesListView listView = new ProductTypesListView(ProductTypesModule.this);

	public static ProductTypesModule getInstance() throws Exception{
		if(module == null) 
			module = new ProductTypesModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ProductTypesModule() throws Exception {
		moduleNameSingular = "ProductType";
		moduleNamePlural = "ProductTypes";
		recordClassName = ProductTypeRecord.class.getName();
		listView.setLinkColumn(1);
		// Load field defs from CSV
		loadFields();

		// ProductTypes Module Menu items
		menu = new Menu(this);
		menu.addControl("createProductType", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_TYPE']");
		menu.addControl("viewProductTypes", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_VIEW_PRODUCT_TYPES']");
		menu.addControl("createProduct", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT']");
		menu.addControl("viewProductCatalog", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_PRODUCT_LIST']");
		menu.addControl("viewManufacturers", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_MANUFACTURER']");
		menu.addControl("viewProductCategories", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_CATEGORY']");
		menu.addControl("importProductTypes", "a", "css", "li[data-module='ProductTypes'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_PRODUCT_TYPES']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init ProductTypes...");
		super.init();
		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("description");
		listView.addHeader("list_order");
	}

	/**
	 * Navigates to the list view of the current module.
	 * 
	 * @throws Exception
	 */
	@Override
	public void navToListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating list view ...");
		// navigate to ProductType
		sugar().admin.navToAdminPanelLink("productType");
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * This method overrides the BWCModule method because the
	 * ProductType module has non-standard create/edit/detail views.
	 * 
	 * @param	testData	A FieldSet containing the data passed from the test.
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
		listView.clickCreate();
		VoodooUtils.focusFrame("bwc-frame");
		// Iterate over the field data and set field values.
		for(String controlName : recordData.keySet()) {
			if(recordData.get(controlName) != null) {
				String toSet = recordData.get(controlName);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " to "
						+ toSet);
				editView.getEditField(controlName).set(toSet);
				VoodooUtils.pause(300);
			} else {
				throw new Exception("Tried to set field " + controlName + " to a" +
						" null value!");
			}
		}
		editView.getControl("save").click();
		VoodooUtils.focusDefault();

		String href = VoodooUtils.getUrl();
		int recordEqualsPos = href.lastIndexOf("record=");
		String guid = href.substring(recordEqualsPos + 7);
		BWCRecord toReturn = (BWCRecord)Class.forName(ProductTypesModule.this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
} // Product Type
