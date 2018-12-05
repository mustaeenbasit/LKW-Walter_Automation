package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class InboundEmailRecord extends BWCRecord {
	public InboundEmailRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().inboundEmail;
	}

	/**
	 * Verifies the current record using the specified data.
	 * 
	 * @param	verifyThis	a FieldSet of field names and expected values.
	 * @throws Exception
	 */
	public void verify(FieldSet verifyThis) throws Exception {
		navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		
		for(String controlName : verifyThis.keySet()) {
			if(verifyThis.get(controlName) != null) {
				if(module.detailView.getDetailField(controlName) == null || controlName.equals("password")) {
					continue;
				}
				VoodooUtils.voodoo.log.info("Verifying field " + controlName);
				String toVerify = verifyThis.get(controlName);
				module.detailView.getDetailField(controlName).assertContains(toVerify, true);
			}
		}
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Navigates to this record by clicking on it by its identifier in the ListView.
	 * <p>
	 * Leaves you on this records detailView.<br>
	 * 
	 * BWC Content: This method will leave you on the detailView but attached
	 * to the default content of sidecar.
	 * 
	 * @throws Exception 
	 */
	public void navToRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating to " + getRecordIdentifier() + '.');
		sugar().inboundEmail.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// new VoodooControl to click on the record by its name
		new VoodooControl("a", "xpath", "//a[contains(.,'" + getRecordIdentifier() + "')]").click();
		VoodooUtils.pause(2000);
	}
	
	/**
	 * Edit the Inbound Email Record with the supplied FieldSet data.
	 * 
	 * @param editedData	Data to edit in the record.
	 * @throws Exception
	 */
	public void edit(FieldSet editedData) throws Exception {
		navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().inboundEmail.detailView.getControl("editButton").click();
		// Iterate over the field data and set field values.
		for(String controlName : editedData.keySet()) {
			if(editedData.get(controlName) != null) {
				String toSet = editedData.get(controlName);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " to "
						+ toSet);
				sugar().inboundEmail.editView.getEditField(controlName).set(toSet);
				VoodooUtils.pause(300);
			} else {
				throw new Exception("Tried to set field " + controlName + " to a" +
						" null value!");
			}
		}

		putAll(editedData);

		sugar().inboundEmail.editView.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(2000);
	} // edit
	
	/**
	 * Delete this record.
	 * <p>
	 * Must be on this records detailView to use.<br>
	 * Confirms the delete action.<br>
	 * Leaves the user on the list view with this record deleted.
	 * <p>
	 * BWC Content: This method will leave you on the listview but attached
	 * to the default content of sidecar.
	 * 
	 */
	public void delete() throws Exception {
		navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().inboundEmail.detailView.getControl("deleteButton").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.pause(2000);
	}
} // end InboundEmailRecord