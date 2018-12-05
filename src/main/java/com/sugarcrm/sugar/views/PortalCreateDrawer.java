package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.PortalAppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the CreateDrawer for Portal modules.
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class PortalCreateDrawer extends View {
	/**
	 * Initializes the CreateDrawer and specifies its parent module so that it knows which fields are available.
	 * 
	 * @param parentModule	the module that owns this CreateDrawer, likely passed in using the module's this 
	 * variable when constructing the CreateDrawer.
	 * @throws Exception
	 */
	public PortalCreateDrawer(StandardModule parentModule) throws Exception {
		super(parentModule, "div", "css", ".drawer.active");

		// Common control definitions.
		// saveButton represents both "Save" and "Ignore Duplicate and Save".
		addControl("saveButton", "a", "css", getHookString() + " .fld_save_button a");
		addControl("primaryButtonDropdown", "a", "css", "#drawers .btn.dropdown-toggle.btn-primary");
		addControl("saveAndViewButton", "a", "css", "#drawers .fld_save_view_button a");
		addControl("saveAndCreateNewButton", "a", "css", "#drawers .fld_save_create_button a");
		addControl("cancelButton", "a", "css", "#drawers .fld_cancel_button a");

		addControl("dismissAlert", "a", "css", "#alerts a.close");
		addControl("actionDropDown", "span", "css", "#drawers .headerpane span.fa-caret-down");
		addControl("toggleSidebar", "a", "css", "#drawers .fld_sidebar_toggle a");
	}

	/**
	 * Click the Save button.
	 * 
	 * You must already be on the CreateDrawer in edit mode to use this method.
	 * Takes you to the ListView if successful, remains on the CreateDrawer
	 * otherwise.
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		getControl("saveButton").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Opens the primary button's drop down list to expose alternate actions.
	 * 
	 * You must already be on the CreateDrawer to use this method. Remains on
	 * the CreateDrawer.
	 * 
	 * @throws Exception
	 */
	public void openPrimaryButtonDropdown() throws Exception {
		getControl("primaryButtonDropdown").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Click the Save and View button.
	 * 
	 * You must already be on the CreateDrawer to use this method. Takes you to
	 * the RecordView in Detail mode if successful, remains on the CreateDrawer
	 * otherwise.
	 * 
	 * @throws Exception
	 */
	public void saveAndView() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndViewButton").click();
		portal().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Save and Create New button.
	 * 
	 * You must already be on the CreateDrawer to use this method. Takes you to
	 * a blank CreateDrawer if successful, remains on the current one otherwise.
	 * 
	 * @throws Exception
	 */
	public void saveAndCreateNew() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndCreateNewButton").click();
		portal().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Cancel button.
	 * 
	 * You must already be on the CreateDrawer to use this method. Takes you to
	 * the RecordView in detail mode.
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancelButton").click();
	}

	/**
	 * Retrieve a reference to a field on the createDrawer.
	 * 
	 * @param fieldName
	 *            - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getEditField(String fieldName) throws Exception {
		return ((StandardModule) parentModule).getField(fieldName).portalEditControl;
	}

	/**
	 * Fill the specified fields on the CreateDrawer.
	 * 
	 * You must already be on the CreateDrawer to use this method.
	 * 
	 * This method will leave you on the CreateDrawer with the specified fields
	 * filled.
	 * 
	 * @param editedData
	 *            A FieldSet of fields to fill on the CreateDrawer.
	 * @throws Exception
	 */
	public void setFields(FieldSet editedData) throws Exception {
		for (String controlName : editedData.keySet()) {
			if (editedData.get(controlName) != null && getEditField(controlName) != null) {
				VoodooUtils.voodoo.log.fine("Editing field: " + controlName);
				String toSet = editedData.get(controlName);
				VoodooUtils.pause(100);
				VoodooUtils.voodoo.log.fine("Setting " + controlName + " field to: " + toSet);
				getEditField(controlName).scrollIntoViewIfNeeded(false);;
				getEditField(controlName).set(toSet);
				VoodooUtils.pause(100);
			} else {
				VoodooUtils.voodoo.log.warning("Field " + controlName
						+ " was in the supplied record data, but no value was given or no Edit Control was available.");
			}
		}
	}
}