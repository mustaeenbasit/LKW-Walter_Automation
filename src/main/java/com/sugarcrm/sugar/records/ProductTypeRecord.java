package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ProductTypeRecord extends BWCRecord{

	public ProductTypeRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().productTypes;
	}
	/**
	 * Navigate to this productTypes record in the productTypes Module.
	 * <p>
	 * When used, you will be left in the productTypes Module with this productTypes record details displayed.
	 * <p>
	 * Note:<br>
	 * This record must exist in the productTypes Module to use.
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
		sugar().productTypes.editView.setFields(editedData);
		putAll(editedData);
		sugar().productTypes.editView.save();
		VoodooUtils.waitForReady();
	} // edit
	
} // productTypesRecord