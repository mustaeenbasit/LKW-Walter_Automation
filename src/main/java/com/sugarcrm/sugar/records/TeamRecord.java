package com.sugarcrm.sugar.records;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class TeamRecord extends BWCRecord {
	private static final long serialVersionUID = 1L;

	public TeamRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().teams;
	}

	/**
	 * Navigates to the record's detailView and edit Team record
	 * Overrides the BWC record edit because Teams edit view is a bit different 
	 * @param editedData Data to edit in the record.
	 * @throws Exception
	 */
	public void edit(FieldSet editedData) throws Exception {
		VoodooUtils.voodoo.log.info("Edit record ....");

		navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		module.detailView.getControl("editButton").click();
		module.editView.getEditField("name").set(editedData.get("name"));
		module.editView.getEditField("description").set(editedData.get("description"));
		module.editView.getControl("save").click();
		VoodooUtils.waitForReady();
		putAll(editedData); // it will update Hash map with edited values
		VoodooUtils.focusDefault();
	}

} // end TeamRecord