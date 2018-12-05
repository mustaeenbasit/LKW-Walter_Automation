package com.sugarcrm.sugar.modules;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BWCRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.BWCDetailView;
import com.sugarcrm.sugar.views.BWCEditView;
import com.sugarcrm.sugar.views.BWCListView;
import com.sugarcrm.sugar.views.View;

/**
 * Base class from which all BWC modules with records extend. Methods and data
 * which are common to BWC modules are stored here.
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 */
public abstract class BWCModule extends RecordsModule {
	public BWCListView listView = new BWCListView(BWCModule.this);
	public BWCEditView editView = new BWCEditView(BWCModule.this);
	public BWCDetailView detailView = new BWCDetailView(BWCModule.this);
	public View popupSearch = new View(BWCModule.this);

	/**
	 * Deletes all records in the current module using the UI.
	 * 
	 * @throws Exception
	 */
	public BWCModule() throws Exception {
		bwc = true;
	}

	public void init() throws Exception {
		searchSelect.setModule(BWCModule.this);
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
		listView.delete();
		listView.confirmDelete();
	}

	/**
	 * Creates a single record record via the UI from the data in a FieldSet.
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
		sugar().navbar.navToModule(moduleNamePlural);
		sugar().navbar.selectMenuItem(this, "create" + moduleNameSingular);

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

		VoodooUtils.focusDefault();
		editView.save();
		String href = VoodooUtils.getUrl();
		int recordEqualsPos = href.lastIndexOf("record=");
		href = href.substring(recordEqualsPos + 7);
		String guid = href.substring(0, href.indexOf('&'));

		BWCRecord toReturn = (BWCRecord)Class.forName(BWCModule.this.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
		toReturn.setGuid(guid);

		VoodooUtils.voodoo.log.fine("Record created.");

		return toReturn;
	}	
}