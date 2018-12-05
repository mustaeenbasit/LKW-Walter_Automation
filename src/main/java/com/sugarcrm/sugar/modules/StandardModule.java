package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.*;

/**
 * Base class from which all standard modules extend. Methods and data which are
 * common to standard modules are stored here.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * @author David Safar <dsafar@sugarcrm.com>
 * @author Mohd Shariq <mshariq@sugarcrm.com>
 */
public abstract class StandardModule extends RecordsModule {
	// Views specific to the StandardModule.
	public CreateDrawer createDrawer;
	public PortalCreateDrawer portalCreateDrawer;
	public RecordView recordView; 
	public ListView listView;

	public StandardModule() throws Exception {
		dashboard = new Dashboard();
	}

	/**
	 * Deletes all records in the current module using the UI.
	 * 
	 * @throws Exception
	 */
	public void deleteAll() throws Exception {
		VoodooUtils.voodoo.log.info("Deleting all " + moduleNameSingular
				+ " records...");
		navToListView();
		listView.toggleSelectAll();
		listView.openActionDropdown();
		listView.delete();
		listView.confirmDelete();
	}

	/**
	 * This method will navigate to the module of the calling record and search
	 * for that record.
	 * 
	 * @throws Exception
	 */
	public void search() throws Exception {
		// TODO: Revise this (including JavaDoc).
		VoodooUtils.voodoo.log.info("Navigating to " + moduleNamePlural
				+ " module");
		navToListView();
		VoodooUtils.voodoo.log.info("Searching for " + moduleNameSingular
				+ " record ");
		listView.getControl("searchFilter").click();
		listView.getControl("searchFilter").set("");
		listView.getControl("searchSuggestion").click();
	}

	public void init() throws Exception {
		listView = new ListView(StandardModule.this);
		createDrawer = new CreateDrawer(StandardModule.this);
		recordView = new RecordView(StandardModule.this);
		portalCreateDrawer = new PortalCreateDrawer(StandardModule.this);
		searchSelect.setModule(StandardModule.this);
	}

	/**
	 * Creates a single portal record via the UI from the data in a FieldSet.
	 * <p>
	 * Notes:<br>
	 * Must be logged into Portal to use.
	 * 
	 * @param	testData	a FieldSet containing the data for the new record.
	 * @return	a Record object representing the record created
	 * @throws Exception
	 */
	public Record portalCreate(FieldSet testData) throws Exception {
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(testData);

		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");
		portal().navbar.navToModule(moduleNamePlural);

		// Move to the CreateDrawer and show hidden fields.
		// TODO: VOOD-1046: Support ListView in Portal
		listView.create(); // HACK HACK HACK... Not yet implemented in portal. This when done will be portalListView.create();

		// Iterate over the field data and set field values.
		portalCreateDrawer.setFields(recordData);

		portalCreateDrawer.save();
		VoodooUtils.pause(1000);
		portal().alerts.waitForLoadingExpiration();
		portal().alerts.getAlert().closeAlert();
		VoodooUtils.voodoo.log.fine("Record created.");

		return (StandardRecord) Class.forName(StandardModule.this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
	}

	/**
	 * Creates a single record record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet testData) throws Exception {
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = getDefaultData();
		recordData.putAll(testData);

		VoodooUtils.voodoo.log.info("Creating a(n) " + moduleNameSingular + " via UI...");
		sugar().navbar.navToModule(moduleNamePlural);

		// Move to the CreateDrawer and show hidden fields.
		listView.create();
		createDrawer.showMore();

		// Iterate over the field data and set field values.
		createDrawer.setFields(recordData);

		createDrawer.getControl("saveButton").click(); // save() would clear the alert we need.
		createDrawer.getControl("saveButton").waitForInvisible();

		// Handle alerts and scrape GUID from success message.
		VoodooControl successAlert = sugar().alerts.getSuccess();
		successAlert.waitForVisible();
		String href = new VoodooControl("a", "css", successAlert.getHookString() + " a").getAttribute("href");
		int lastSlashPos = href.lastIndexOf('/');
		String guid = href.substring(lastSlashPos + 1);
		VoodooUtils.waitForReady();

		// Create the record and set its GUID.
		StandardRecord toReturn = (StandardRecord)Class.forName(StandardModule.this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
}