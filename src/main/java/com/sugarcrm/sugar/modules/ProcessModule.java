package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.ProcessCreateDrawer;
import com.sugarcrm.sugar.views.ProcessRecordView;

/**
 * Extended class from which all process type modules extend. Methods and data which are
 * common to process modules are stored here. (i.e. Process Business Rules, Process Email Templates etc.)
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public abstract class ProcessModule extends StandardModule {
	// Views specific to the ProcessModule.
	public ProcessRecordView recordView;
	public ProcessCreateDrawer createDrawer;

	public ProcessModule() throws Exception {
	}

	public void init() throws Exception {
		super.init();
		recordView = new ProcessRecordView(ProcessModule.this);
		createDrawer = new ProcessCreateDrawer(ProcessModule.this);
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet testData) throws Exception {
		return create(testData, ProcessCreateDrawer.Save.SAVE);
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @param	howToSave	an ENUM to control whether to click Save or Save and Design.
	 *                      (SAVE and SAVE_AND_DESIGN)
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet testData, ProcessCreateDrawer.Save howToSave) throws Exception {
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

		createDrawer.openPrimaryButtonDropdown();
		// Choose the save process to follow
		switch (howToSave) {
			case SAVE:
				createDrawer.getControl("saveButton").click(); // save() would clear the alert we need.
				createDrawer.getControl("saveButton").waitForInvisible();
				break;
			case SAVE_AND_DESIGN:
				createDrawer.getControl("saveAndDesignButton").click();
				createDrawer.getControl("saveAndDesignButton").waitForInvisible();
				break;
			default:
				throw new Exception("There are only 2 types of saves that occur and you didn't use one of them." +
						"They are SAVE and SAVE_AND_DESIGN");
		}

		// Handle alerts and scrape GUID from success message.
		VoodooControl successAlert = sugar().alerts.getSuccess();
		successAlert.waitForVisible();
		String href = new VoodooControl("a", "css", successAlert.getHookString() + " a").getAttribute("href");
		int lastSlashPos = href.lastIndexOf('/');
		String guid = href.substring(lastSlashPos + 1);
		VoodooUtils.focusDefault();
		successAlert.waitForInvisible();

		// Create the record and set its GUID.
		StandardRecord toReturn = (StandardRecord)Class.forName(this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
} // ProcessModule
