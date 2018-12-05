package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import org.openqa.selenium.WebDriverException;

/**
 * Models the CreateDrawer for standard SugarCRM modules.
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class CreateDrawer extends View {
	// These will be used to dynamically construct controls
	String dupRecordRow = "div[data-voodoo-name='dupecheck-list-edit'] table tbody tr";
	String dupSelectAndEditButton = "a[data-event='list:dupecheck-list-select-edit:fire']";
	String dupPreviewButton = "a[data-event='list:preview:fire']";

	/**
	 * Initializes the CreateDrawer and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this CreateDrawer, likely passed in using the module's this variable when constructing the CreateDrawer.
	 * @throws Exception
	 */
	public CreateDrawer(StandardModule parentModule) throws Exception {
		super(parentModule);

		// Common control definitions.
		addControl("saveButton", "a", "css", "#drawers .fld_save_button a:not(.hide)");
		addControl("cancelButton", "a", "css", "#drawers .fld_cancel_button a");
		addControl("dismissAlert", "a", "css", "#alerts a.close");
		addControl("showMore", "button", "css", "#drawers .active .main-pane button[data-moreless='more']");
		addControl("showLess", "button", "css", "#drawers .active .main-pane button[data-moreless='less']");
		addControl("toggleSidebar", "button", "css", "#drawers .detail.fld_sidebar_toggle button");

		// Dup check controls
		addControl("resetToOriginalButton", "a", "css", "#drawers .fld_restore_button a");
		addControl("ignoreDuplicateAndSaveButton", "a", "css", "#drawers .create.fld_duplicate_button a");
		addControl("duplicateCount", "span", "css", ".duplicate_count");
		addControl("duplicateHeaderRow", "tr", "css", "div[data-voodoo-name='dupecheck-list-edit'] table thead tr");
	}

	/**
	 * Click the Save button.
	 *
	 * You must already be on the CreateDrawer in edit mode to use this method.
	 * Takes you to the ListView if successful, remains on the CreateDrawer otherwise.
	 *
	 * @throws Exception
	 */
	public void save() throws Exception {
		getControl("saveButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the IgnoreDuplicateAndSave button.
	 *
	 * You must already be on the CreateDrawer in edit mode to use this method.
	 * And should have a record with same data
	 * Takes you to the ListView if successful, remains on the CreateDrawer otherwise.
	 *
	 * @throws Exception
	 */
	public void ignoreDuplicateAndSave() throws Exception {
		getControl("ignoreDuplicateAndSaveButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Cancel button.
	 *
	 * You must already be on the CreateDrawer to use this method.
	 * Takes you to the RecordView in detail mode.
	 *
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancelButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Show More link.
	 *
	 * You must already be on the CreateDrawer to use this method.
	 * Remains on the CreateDrawer and displays the portion of the page hidden behind the "Show More" link.
	 * If the Show More link does not exist, this method does nothing (i.e. does not fail).
	 * @throws Exception
	 */
	public void showMore() throws Exception {
			if(getControl("showMore").queryVisible()) {
				getControl("showMore").click();
				VoodooUtils.waitForReady();
			}
	}

	/**
	 * Click the Show Less link.
	 *
	 * You must already be on the CreateDrawer to use this method.
	 * Remains on the CreateDrawer and hides the portion of the page previously hidden behind the "Show More" link.
	 * If the Show Less link is not present, this method does nothing (i.e. does not fail).
	 * @throws Exception
	 */
	public void showLess() throws Exception {
			if(getControl("showLess").queryExists()) {
				getControl("showLess").click();
				VoodooUtils.waitForReady();
			}
	}

	/**
	 * Retrieve a reference to a field on the RecordView.
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getEditField(String fieldName) throws Exception {
		return ((StandardModule)parentModule).getField(fieldName).editControl;
	}

	/**
	 * Click the Select and Edit button on the specified row of the dup check
	 * panel.
	 * @param row	1-based integer specifying the row on which to click.
	 * @throws Exception
	 */
	public void selectAndEditDuplicate(int row) throws Exception {
		new VoodooControl("a", "css", dupRecordRow + ":nth-child(" + row + ") " +
				dupSelectAndEditButton).click();
	}

	/**
	 * Click the Preview button on the specified row of the dup check panel.
	 * @param row
	 * @throws Exception
	 */
	public void previewDuplicate(int row) throws Exception {
		new VoodooControl("a", "css", dupRecordRow + ":nth-child(" + row + ") " +
				dupPreviewButton).click();
		sugar().previewPane.setModule(parentModule);
		VoodooUtils.pause(500);
	}

	/**
	 * Reset the CreateDrawer fields to their original values before they were
	 * overwritten during dup check.
	 * @throws Exception
	 */
	public void resetToOriginal() throws Exception {
		getControl("resetToOriginalButton").click();
	}

	/**
	 * Fill the specified fields on the CreateDrawer.
	 *
	 * You must already be on the CreateDrawer to use this method.
	 *
	 * This method will leave you on the CreateDrawer with the specified fields filled.
	 * @param editedData	A FieldSet of fields to fill on the CreateDrawer.
	 * @throws Exception
	 */
	public void setFields(FieldSet editedData) throws Exception {
		for (String controlName : editedData.keySet()) {
			setField(controlName, editedData.get(controlName));
		}
	}

	/**
	 * Set a field in this CreateDrawer.
	 * <p>
	 * @param field String of the field name to set.
	 * @param value String value to be set.
	 * @throws Exception
	 */
	public void setField(String field, String value) throws Exception {
		if (field != null && value != null && getEditField(field) != null) {
			VoodooUtils.voodoo.log.fine("Editing field: " + field);
			VoodooUtils.waitForReady();
			VoodooUtils.voodoo.log.fine("Setting " + field + " field to: "
					+ value);
			getEditField(field).scrollIntoViewIfNeeded(false);
			getEditField(field).set(value);
			VoodooUtils.waitForReady();
		} else {
			VoodooUtils.voodoo.log.warning("Field " + field +
					" was in the supplied record data, but no value was given or no Edit Control was available.");
		}
	}
}