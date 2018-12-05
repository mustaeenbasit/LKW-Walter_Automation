package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BWCRecord;
import com.sugarcrm.sugar.records.ManufacturerRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.ManufacturersListView;
import com.sugarcrm.sugar.views.ManufacturersEditView;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Manufacturers module, such as field
 * data.
 * 
 * @author Mohd. Shariq
 */
public class ManufacturersModule extends BWCModule {
	protected static ManufacturersModule module;
	public ManufacturersEditView editView = new ManufacturersEditView(ManufacturersModule.this);
	public ManufacturersListView listView = new ManufacturersListView(ManufacturersModule.this);
	
	public static ManufacturersModule getInstance() throws Exception {
		if(module == null) 
			module = new ManufacturersModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private ManufacturersModule() throws Exception {
		moduleNameSingular = "Manufacturer";
		moduleNamePlural = "Manufacturers";
		recordClassName = ManufacturerRecord.class.getName();
		listView.setLinkColumn(1);
		// Load field defs from CSV
		loadFields();

		// Manufacturers Module Menu items
		menu = new Menu(this);
		menu.addControl("createProduct", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT']");
		menu.addControl("viewProductCatalog", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_PRODUCT_LIST']");
		menu.addControl("viewManufacturers", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_MANUFACTURER']");
		menu.addControl("viewProductCategories", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_CATEGORY']");
		menu.addControl("viewProductTypes", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_NEW_PRODUCT_TYPE']");
		menu.addControl("importManufacturers", "a", "css", "li[data-module='Manufacturers'] ul[role='menu'] a[data-navbar-menu-item='LNK_IMPORT_MANUFACTURERS']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Manufacturers...");
		super.init();
		// Define the columns on the ListView.
		listView.addHeader("name");
		listView.addHeader("status");
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
		// navigate to manufacturer
		sugar().admin.navToAdminPanelLink(sugar().manufacturers.moduleNameSingular.toLowerCase());
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * This method overrides the BWCModule method because the
	 * Manufacturer module has non-standard create/edit/detail views.
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
		sugar().manufacturers.listView.clickCreate();
		VoodooUtils.focusFrame("bwc-frame");
		// Iterate over the field data and set field values.
		for(String controlName : recordData.keySet()) {
			if(recordData.get(controlName) != null) {
				String toSet = recordData.get(controlName);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " to "
						+ toSet);
				VoodooControl editValue = editView.getEditField(controlName);
				editValue.set(toSet);
				VoodooUtils.pause(300);
			} else {
				throw new Exception("Tried to set field " + controlName + " to a" +
						" null value!");
			}
		}
		editView.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		String href = VoodooUtils.getUrl();
		int recordEqualsPos = href.lastIndexOf("record=");
		href = href.substring(recordEqualsPos + 7);
		String guid = href.substring(0, href.indexOf('&'));

		BWCRecord toReturn = (BWCRecord)Class.forName(ManufacturersModule.this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
} // Manufacturer