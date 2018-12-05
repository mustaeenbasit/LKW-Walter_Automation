package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

import java.util.HashMap;

/**
 * Models the DetailView for backwards-compatibility SugarCRM modules.
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class BWCDetailView extends View {
	public HashMap<String, BWCSubpanel> subpanels = new HashMap<String, BWCSubpanel>();
	public BWCComposeEmail composeEmail = new BWCComposeEmail();
	
	/**
	 * Initializes the RecordView and specifies its parent module so that it knows which fields are available.  
	 * @param parentModule - the module that owns this RecordView, likely passed in using the module's this variable when constructing the RecordView.
	 * @throws Exception
	 */
	public BWCDetailView(RecordsModule parentModule) throws Exception {
		super(parentModule, "div", "id" , "content");
		
		// Common control definitions. 
		addControl("primaryButtonDropdown", "span", "css", "span.ab");

		addControl("editButton", "a", "id", "edit_button");
		addControl("deleteButton", "a", "id", "delete_button");
		addControl("copyButton", "a", "id", "duplicate_button");
		
		addControl("moduleTitle", "div", "css", ".moduleTitle");
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
		VoodooUtils.focusFrame("bwc-frame");
		openPrimaryButtonDropdown();
		getControl("editButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Click the Delete button.
	 * 
	 * You must already be on the DetailView to use this method.
	 * Remains on the RecordView and displays the delete confirmation alert.
	 * 
	 * NOTE: Unlike most BWC methods, this method does NOT return focus to the
	 * default window.  Focus REMAINS in the BWC frame.  This is because focus
	 * cannot be changed while a JavaScript alert (such as the delete
	 * confirmation dialog) is active.  After calling this method, the client
	 * must accept or decline the alert using the appropriate method from
	 * VoodooUtils, then move focus back to the default before interacting with
	 * anything outside the BWC frame.
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		openPrimaryButtonDropdown();
		getControl("deleteButton").click();
	}
	
	/**
	 * Click the Copy button.
	 * 
	 * You must already be on the DetailView to use this method.
	 * Takes you to an EditView prepopulated with the current record's data.
	 * 
	 * @throws Exception
	 */
	public void copy() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		openPrimaryButtonDropdown();
		getControl("copyButton").click();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click the down caret on the primary action button to open the dropdown.
	 * 
	 * You must be on a BWC DetailView to use this method.
	 * 
	 * Note that you must give focus to the BWC frame *before* calling this
	 * method.
	 * 
	 * Leaves you on the same DetailView with the primary action button's
	 * dropdown open. 
	 */
	public void openPrimaryButtonDropdown() throws Exception {
		getControl("primaryButtonDropdown").click();
		VoodooUtils.pause(250);
	}

	/**
	 * Retrieve a reference to a field on the DetailView. 
	 * @param fieldName	The VoodooGrimoire name for the desired control.
	 * @return a reference to the element.
	 * @throws Exception
	 */
	public VoodooControl getDetailField(String fieldName) throws Exception {
		return((RecordsModule)parentModule).getField(fieldName).detailControl;
	}

	/**
	 * Add all subpanels to the HashMap of available related subpanels displayed on this DetailView
	 * @throws Exception
	 */
	public void addSubpanels() throws Exception {
		for(RecordsModule relatedModule : ((RecordsModule)parentModule).relatedModulesMany.values()){
			subpanels.put(relatedModule.moduleNamePlural, (BWCSubpanel) relatedModule.getSubpanel("bwc"));
		}
	}

	// TODO: Add getSubpanel method to return a specific subpanel
}