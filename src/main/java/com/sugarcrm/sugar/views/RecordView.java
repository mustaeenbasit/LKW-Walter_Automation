package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

import java.util.HashMap;

/**
 * Models the RecordView for standard SugarCRM modules.
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class RecordView extends View {
	public String bottomPaneState = "";
	public HashMap<String, StandardSubpanel> subpanels = new HashMap<String, StandardSubpanel>();
	public ActivityStream activityStream;
	public StandardComposeEmail composeEmail = new StandardComposeEmail();
	
	/**
	 * Initializes the RecordView and specifies its parent module so that it knows which fields are available.  
	 * @param parentModule - the module that owns this RecordView, likely passed in using the module's this variable when constructing the RecordView.
	 * @throws Exception
	 */
	public RecordView(RecordsModule parentModule) throws Exception {
		super(parentModule, "div", "css", ".layout_" + parentModule.moduleNamePlural + " [data-voodoo-name='" + parentModule.moduleNamePlural + "']");
		activityStream = new ActivityStream();
		
		// Common control definitions. 
		addControl("saveButton", "a", "css", ".btn-toolbar.pull-right .fld_save_button a");
		addControl("primaryButtonDropdown", "a", "css", ".fld_main_dropdown.detail .btn.dropdown-toggle.btn-primary");
		addControl("editButton", "a", "css", ".fld_edit_button a");
		addControl("deleteButton", "a", "css", ".fld_delete_button a");
		addControl("copyButton", "a", "css", ".fld_duplicate_button a");
		addControl("cancelButton", "a", "css", ".fld_cancel_button a");
		addControl("favoriteButton", "button", "css", "[data-voodoo-name='my_favorite'] button"); // Favorite Icon ONLY on recordView

		addControl("dismissAlert", "a", "css", "#alerts a.close");
		addControl("actionDropDown", "span", "css", ".headerpane span.fa-caret-down");
		addControl("confirmDelete", "a", "css", ".confirm");
		addControl("showMore", "button", "css", "div.record-cell button[data-moreless='more']");
		addControl("showLess", "button", "css", "div.record-cell button[data-moreless='less']");
		addControl("toggleSidebar", "i", "css", ".fld_sidebar_toggle i");
		addControl("chevronRight", "i", "css", ".fa-chevron-right");
		addControl("chevronLeft",  "i", "css", ".fa-chevron-left");

		addControl("moduleIDLabel", "span", "css", ".fld_picture.detail .label-module");
		addControl("verticalScrollBar", "div", "css", "div.main-pane");

		// Subpanel and Activity Stream buttons
		addControl("dataViewButton", "button", "css", "button[data-view='subpanels']");
		addControl("activityStreamButton", "button", "css", "button[data-view='activitystream']");
		
		// Search Subpanels
		addControl("searchFilter", "input", "css", "div.filter-view.search input.search-name");
		
		// Subpanel Related Selector
		addSelect("relatedSubpanelFilter","a","css","span[data-voodoo-name='filter-module-dropdown'] a");
		addControl("relatedSubpanelChoice", "div", "css", "span[data-voodoo-name='filter-module-dropdown'] .choice-related");
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
		getControl("saveButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}
	
	/**
	 * Opens the primary button's drop down list to expose alternate actions.
	 * 
	 * You must already be on the RecordView to use this method.
	 * Remains on the RecordView.
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
	 * You must already be on the RecordView in edit mode to use this method.
	 * Remains on the RecordView, switching it to Detail mode if successful, remains in Edit mode otherwise.
	 * 
	 * @throws Exception
	 */
	public void saveAndView() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndViewButton").click();
		VoodooUtils.pause(3000); // Added to handle dual alerts
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Click the Save and Create New button.
	 * 
	 * You must already be on the RecordView in edit mode to use this method.
	 * Takes you to a blank RecordView if successful, remains on the current one otherwise.
	 * 
	 * @throws Exception
	 */
	public void saveAndCreateNew() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndCreateNewButton").click();
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
		getControl("cancelButton").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the Edit button.
	 * 
	 * You must already be on the RecordView in detail mode to use this method.
	 * Remains on the RecordView, switching it to edit mode.
	 * 
	 * @throws Exception
	 */
	public void edit() throws Exception {
		openPrimaryButtonDropdown();
		getControl("editButton").click();
	}
	
	/**
	 * Click the Delete button.
	 * 
	 * You must already be on the RecordView in detail mode to use this method.
	 * Remains on the RecordView and displays the delete confirmation alert.
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		openPrimaryButtonDropdown();
		getControl("deleteButton").click();
	}
	
	/**
	 * Click the Copy button.
	 * 
	 * You must already be on the RecordView in detail mode to use this method.
	 * Takes you to a RecordView in edit mode, prepopulated with the current record's data.
	 * 
	 * @throws Exception
	 */
	public void copy() throws Exception {
		openPrimaryButtonDropdown();
		getControl("copyButton").click();
	}
	
	/**
	 * Click the Show More link.
	 * 
	 * You must already be on the RecordView to use this method.
	 * Remains on the RecordView and displays the portion of the page hidden behind the "Show More" link.
	 * If the Show More link does not exist, this method does nothing (i.e. does not fail).
	 * If the Show More link is present but invisible (e.g. because it has already been clicked), an error will occur.    This behavior is likely to change in the future.
	 * 
	 * @throws Exception
	 */
	public void showMore() throws Exception {
		if(getControl("showMore").queryVisible()) {
			getControl("showMore").click();
		}
	}
	
	/**
	 * Click the Show Less link.
	 * 
	 * You must already be on the RecordView to use this method.
	 * Remains on the RecordView and hides the portion of the page previously hidden behind the "Show More" link.
	 * If the Show Less link is not present, this method does nothing (i.e. does not fail).
	 * If the Show Less link is present but invisible (e.g. because it has already been clicked), an error will occur.  This behavior is likely to change in the future.
	 * 
	 * @throws Exception
	 */
	public void showLess() throws Exception {
		if(getControl("showLess").queryVisible()) {
			getControl("showLess").click();
		}
	}
	
	/**
	 * Retrieve a reference to the detail mode version of a field on the RecordView. 
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getDetailField(String fieldName) throws Exception {
		return ((RecordsModule)parentModule).getField(fieldName).detailControl;
	}

	/**
	 * Retrieve a reference to the edit mode version of a field on the RecordView. 
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getEditField(String fieldName) throws Exception {
		return ((RecordsModule)parentModule).getField(fieldName).editControl;
	}
	
	/**
	 * Click the Activity Stream button will toggle the RecordView bottom pane state into the Activity Stream 
	 * @throws Exception
	 */
	public void showActivityStream() throws Exception {
		getControl("activityStreamButton").click();
		bottomPaneState = "activityStream";
		VoodooUtils.pause(2000);
	}
	
	/**
	 * Click the Data View (Subpanel) button will toggle the RecordView bottom pane state into the Data View
	 * @throws Exception
	 */
	public void showDataView() throws Exception {
		getControl("dataViewButton").click();
		bottomPaneState = "dataView";
		VoodooUtils.pause(3000);
	}
	
	/**
	 * Add all subpanels to the HashMap of available related subpanels displayed on this RecordView
	 * @throws Exception
	 */
	public void addSubpanels() throws Exception {
		for(RecordsModule relatedModule : ((RecordsModule)parentModule).relatedModulesMany.values()){
			subpanels.put(relatedModule.moduleNamePlural, (StandardSubpanel) relatedModule.getSubpanel("standard"));
		}
	}

	/**
	 * Add custom subpanels to the HashMap  displayed on the RecordView
	 * @param relatedModule RecordModule of the Subpanel
	 * @param moduleName module name displayed on the Subpanel UI
	 * @param subpanelKey Custom standard subpanel key defined in the Module
	 * @throws Exception
	 */
	public void addCustomSubpanel(RecordsModule relatedModule, String moduleName, String subpanelKey) throws Exception {
		subpanels.put(moduleName, (StandardSubpanel) relatedModule.getSubpanel(subpanelKey));
	}

	/**
	 * Displays or shows the right-hand sidebar.
	 * 
	 * You must be on the RecordView. Leaves you on the RecordView with the sidebar
	 * in the opposite open/closed state.
	 * 
	 * @throws Exception
	 */
	public void toggleSidebar() throws Exception {
		getControl("toggleSidebar").click();
		VoodooUtils.pause(500); // TODO: VOOD-743
	}	

	/**
	 * Access next record without leaving record view mode
	 * 
	 * You must be on the RecordView. Next record should exist.    
	 * 
	 * @throws Exception
	 */
	public void gotoNextRecord() throws Exception {
		getControl("chevronRight").click();
		sugar().alerts.waitForLoadingExpiration();
	}	

	/**
	 * Access previous record without leaving record view mode
	 * 
	 * You must be on the RecordView. Previous record should exist.    
	 * 
	 * @throws Exception
	 */
	public void gotoPreviousRecord() throws Exception {
		getControl("chevronLeft").click();
		sugar().alerts.waitForLoadingExpiration();
	}	

	
	/**
	 * Assume you are in RecordView edit mode.  It will loop through the fields to enter FieldSet data.
	 * @param editedData
	 * @throws Exception
	 */
	public void setFields(FieldSet editedData) throws Exception {
		for (String controlName : editedData.keySet()) {
			setField(controlName, editedData.get(controlName));
		}
	}
	
	// TODO: Add getSubpanel method to return a specific subpanel
	
	/**
	 * Search all subpanels for a record
	 * @param	searchString	the String to enter in the search box
	 * @throws Exception
	 */
	public void setSearchString(String searchString) throws Exception {
		getControl("searchFilter").set(searchString);
		VoodooUtils.pause(2000);
	}
	
	/**
	 * Toggle the Favorite icon.
	 * 
	 * Must be on the RecordView to use.
	 * 
	 * Leaves you on the RecordView with the Favorite icon toggled to the opposite of what it was before toggle.
	 * 
	 * @throws Exception
	 */
	public void toggleFavorite() throws Exception {
		getControl("favoriteButton").click();
		VoodooUtils.pause(200);
	}
	
	/**
	 * Set the Related Subpanel list to display desired modules subpanel.
	 * <p>
	 * @param subpanel String of related module to search against
	 * @throws Exception
	 */
	public void setRelatedSubpanelFilter(String subpanel) throws Exception {
		getControl("relatedSubpanelFilter").set(subpanel);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Set fields in this CreateDrawer.
	 * <p>
	 * @param field String of the field name to set.
	 * @param value String value to be set.
	 * @throws Exception
	 */
	public void setField(String field, String value) throws Exception {
		if (field != null && getEditField(field) != null) {
			VoodooUtils.voodoo.log.fine("Editing field: " + field);
			VoodooUtils.pause(100);
			VoodooUtils.voodoo.log.fine("Setting " + field + " field to: "
					+ value);
			getEditField(field).scrollIntoViewIfNeeded(false);
			getEditField(field).set(value);
			VoodooUtils.pause(100);
		} else {
			VoodooUtils.voodoo.log.warning("Field " + field +
					" was in the supplied record data, but no value was given or no Edit Control was available.");
		}
	}
}