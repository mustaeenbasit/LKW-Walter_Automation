package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

/**
 * Models the RecordView for standard SugarCRM modules.
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class BWCEditView extends View {
	public String bottomPaneState = "";
	public ActivityStream activityStream;
	
	/**
	 * Initializes the EditView and specifies its parent module so that it
	 * knows which fields are available.  
	 * @param	parentModule	the module that owns this RecordView
	 * @throws	Exception
	 */
	public BWCEditView(RecordsModule parentModule) throws Exception {
		super(parentModule);
		activityStream = new ActivityStream();
		
		// Common control definitions. 
		addControl("saveButton", "input", "id", "SAVE_HEADER");
		addControl("cancelButton", "input", "id", "CANCEL_HEADER");
		addControl("detailPanel", "div", "css", "#detailpanel_1");
	}
	
	/**
	 * Click the Save button.
	 * 
	 * You must already be on the RecordView in edit mode to use this method.
	 * Takes you to the ListView if successful, remains on the RecordView otherwise.
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("saveButton").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);
	}
	
	/**
	 * Click the Cancel button.
	 * 
	 * You must already be on the RecordView in edit mode to use this method.
	 * Remains on the RecordView, switching it to Detail mode.
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("cancelButton").click();
		VoodooUtils.focusDefault();
	}
	/**
	 * Retrieve a reference to the edit mode version of a field on the RecordView. 
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getEditField(String fieldName) throws Exception {
		return ((RecordsModule)parentModule).getField(fieldName).editControl;
	}

	public void setFields(FieldSet editedData) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		for(String controlName : editedData.keySet()) {
			if (editedData.get(controlName) != null) {
				VoodooUtils.voodoo.log.fine("Editing field: " + controlName);
				String toSet = editedData.get(controlName);
				VoodooUtils.pause(100);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " field to: "
						+ toSet);
				getEditField(controlName).scrollIntoViewIfNeeded(false);
				getEditField(controlName).set(toSet);
				VoodooUtils.pause(100);
			}
		}
		VoodooUtils.focusDefault();
	}
}