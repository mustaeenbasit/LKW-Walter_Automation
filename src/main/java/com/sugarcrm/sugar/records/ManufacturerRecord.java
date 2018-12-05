package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ManufacturerRecord extends BWCRecord {
	public ManufacturerRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().manufacturers;
	}

	/**
	 * Navigate to this manufacturers record in the Manufacturers Module.
	 * <p>
	 * When used, you will be left in the Manufacturers Module with this Manufacturers record details displayed.
	 * <p>
	 * Note:<br>
	 * This record must exist in the Manufacturers Module to use.
	 */
	@Override
	public void navToRecord() throws Exception {
		module.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "xpath", "//*[@id='contentTable']//*[contains(@class,'list view')]//*[text()[contains(.,'"+ this.getRecordIdentifier() +"')]]").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Navigates to the record's detailView and deletes the record
	 */
	@Override
	public void delete() throws Exception {
		VoodooUtils.voodoo.log.info("Deleting record " + getRecordIdentifier() +".");

		navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "xpath", "//*[@id='contentTable']//*[contains(@class,'list view')]//tr[contains(.,'"+ this.getRecordIdentifier() +"')]//*[text()[contains(.,'Delete')]]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Edit the calling Record with the supplied FieldSet data.
	 * 
	 * @param editedData	Data to edit in the record.
	 * @throws Exception
	 */
	@Override
	public void edit(FieldSet editedData) throws Exception {
		navToRecord();
		sugar().manufacturers.editView.setFields(editedData);
		putAll(editedData);
		sugar().manufacturers.editView.save();
		VoodooUtils.waitForReady();
	} // edit
	
} // ManufacturersRecord