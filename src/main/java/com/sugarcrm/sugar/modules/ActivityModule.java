package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.ActivityCreateDrawer;
import com.sugarcrm.sugar.views.ActivityListView;
import com.sugarcrm.sugar.views.ActivityRecordView;

/**
 * Extended class from which all activity modules extend. Methods and data which are
 * common to activity modules are stored here. (i.e. Calls and Meetings)
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public abstract class ActivityModule extends StandardModule {
	// Views specific to the ActivityModule.
	public ActivityRecordView recordView;
	public ActivityCreateDrawer createDrawer;
	public ActivityListView listView;
	
	public ActivityModule() throws Exception {
		super();
	}

	public void init() throws Exception {
		super.init();
		recordView = new ActivityRecordView(ActivityModule.this);
		createDrawer = new ActivityCreateDrawer(ActivityModule.this);
		listView = new ActivityListView(ActivityModule.this);
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet testData) throws Exception {
		return create(testData, ActivityCreateDrawer.Save.SAVE);
	}

	/**
	 * Creates a single record via the UI from the data in a FieldSet.
	 * <p>
	 * @param	testData	a FieldSet of data passed from the test for the record.
	 * @param	howToSave	an ENUM to control whether to click Save or Save and Send Invites.
	 *                      (SAVE and SAVE_AND_SEND_INVITES)
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create(FieldSet testData, ActivityCreateDrawer.Save howToSave) throws Exception {
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
			case SAVE_AND_SEND_INVITES:
				createDrawer.getControl("saveAndSendInvites").click();
				createDrawer.getControl("saveAndSendInvites").waitForInvisible();
				break;
			default:
				throw new Exception("There are only 2 types of saves that occur and you didn't use one of them." +
						"They are SAVE and SAVE_AND_SEND_INVITES");
		}

		// Handle alerts and scrape GUID from success message.
		VoodooControl successAlert = sugar().alerts.getSuccess();
		successAlert.waitForVisible();
		String href = new VoodooControl("a", "css", successAlert.getHookString() + " a").getAttribute("href");
		int lastSlashPos = href.lastIndexOf('/');
		String guid = href.substring(lastSlashPos + 1);
		VoodooUtils.waitForReady();

		// Create the record and set its GUID.
		StandardRecord toReturn = (StandardRecord)Class.forName(this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}
} // ActivityModule
