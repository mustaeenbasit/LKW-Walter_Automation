package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the Mass Update widget for SugarCRM modules.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class MassUpdate extends View {
	Module identityModule = null;
	
	public MassUpdate(Module identityModule) throws Exception {
		this.identityModule = identityModule;
		// Common control definitions. 
		addControl("update", "a", "css", "div[data-voodoo-name='"+ identityModule.moduleNamePlural +"'] .fld_update_button.massupdate a");
		addControl("cancelUpdate", "a", "css", "div[data-voodoo-name='"+ identityModule.moduleNamePlural +"'] .pull-right .massupdate a.cancel_button");
		addControl("acceptBlanks","a","css","#alerts a");
		
		// TODO: Possibly move these to a select2 VoodooControl subclass
		addControl("search", "input", "css", "#select2-drop .select2-search input");
		addControl("result", "span", "css", "#select2-drop .select2-result-label span");
	
		// Add 20 rows of element definitions that correspond to the possible rows in the mass update panel
		// TODO: Make the MassUpdate object know how many rows there are and not pre-make 20 rows of elements 
		for(int i=2; i <= 21; i++){
			String massUpdateField = String.format("massUpdateField%02d", i);
			String massUpdateValue = String.format("massUpdateValue%02d", i);
			String addField = String.format("addField%02d", i);
			String removeField = String.format("removeField%02d", i);
			
			String currentRow = "div[data-voodoo-name='"+ identityModule.moduleNamePlural +"'] .filter-body.clearfix:nth-of-type(" + i + ") ";
			
			addSelect(massUpdateField, "a", "css", currentRow + " .controls.filter-field a");
			addSelect(massUpdateValue, "a", "css", currentRow + " .controls.filter-value a");
			addControl(addField, "button", "css", currentRow + " .filter-actions.btn-group [data-action='add']");
			addControl(removeField, "a", "css", currentRow + " .filter-actions.btn-group [data-action='remove']");
		}
		
	}
	
	/**
	 * Clicks the Update button to perform an update using chosen fields.
	 * 
	 * You must be on the ListView or on a Subpanel with the Mass Update panel open.
	 * This leaves you on the ListView or Subpanel with the Mass Update panel now closed.
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception{
		getControl("update").click();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Clicks on the "x" button to cancel a Mass Update.
	 * 
	 * You must be on the ListView or a Subpanel with the Mass Update panel open.
	 * This leaves you on the ListView or Subpanel with the Mass Update panel now closed.
	 * 
	 * This action will NOT reset the Mass Update panel, all fields that were chosen will remain if Mass Update is attempted again.
	 * 
	 * @throws Exception
	 */
	public void cancelUpdate() throws Exception {
		getControl("cancelUpdate").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Clicks the "+" link on a particular row of the current Mass Update View.
	 * 
	 * You must be on ListView or on a Subpanel and have 1 or more records selected as well as have the Mass Update panel open.
	 * This leaves you on the ListView or RecordView and adds an additional field to use in mass update.
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void addRow(int rowNum) throws Exception{
		getControl(String.format("addField%02d", rowNum)).click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Clicks the "-" link on a particular row of the current Mass Update View.
	 * 
	 * You must be on ListView or on a Subpanel and have 2 or more fields already in the Mass Update panel
	 * This leaves you on the ListView or RecordView and removes the specific Mass Update field
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void removeRow(int rowNum) throws Exception{
		getControl(String.format("removeField%02d", rowNum)).click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Retrieve a reference to the mass update version of a field on the mass update panel. 
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getMassUpdateField(String fieldName) throws Exception{
		return ((StandardModule)parentModule).getField(fieldName).massUpdateControl;
	}
	
	/**
	 * Set fields in the mass update panel to specific values.
	 * @param update - FieldSet of fields to choose and set for mass update.
	 * @throws Exception
	 */
	public void setMassUpdateFields(FieldSet update) throws Exception{
		int current = 2;
		int totalSize = update.size() + 1;
		for(String field : update.keySet()){
			if(update.get(field) != null){
				getControl(String.format("massUpdateField%02d", current)).set(field);
				VoodooUtils.pause(300);
				getControl(String.format("massUpdateValue%02d", current)).set(update.get(field));
				VoodooUtils.pause(300);
				if(current < totalSize){
					addRow(current);
					current++;
				}
			}
		}
	}
	
	/**
	 * This method will perform a mass update using the fields of the passed in FieldSet.
	 * 
	 * This method requires that you are on the ListView of a module.
	 * The Records chosen to be mass updated must all ready be selected on the view you are in.
	 * 
	 * After this action is taken, the record chosen will have the passed in fields updated to the proper values.
	 * You will be left on the previous view before the mass update action was taken.
	 * 
	 * @param update - FieldSet of desired mass update fields and their values
	 * @throws Exception
	 */
	public void performMassUpdate(FieldSet update) throws Exception{
		if(identityModule instanceof StandardModule){
			((StandardModule)identityModule).listView.openActionDropdown();
			((StandardModule)identityModule).listView.massUpdate();
			setMassUpdateFields(update);
			update();
			VoodooUtils.pause(500);
		}
	}
	
	/**
	 * This method will perform a mass update using the fields of the passed in FieldSet.
	 * This action will be performed on mass update panel of a subpanel.
	 * 
	 * This method required that you are on the Subpanel view of a record.
	 * The Records chosen to be mass updated must all ready be selected on the view you are in.
	 * 
	 * After this action is taken, the records chosen will have the passed in fields values updated.
	 * You will be left on the previous view before the mass update action was taken.
	 * 
	 * @param update - FieldSet of desired mass update fields and their values
	 * @throws Exception
	 */
	public void performMassUpdateSubpanel(FieldSet update) throws Exception{
		if(identityModule instanceof StandardModule){
			setMassUpdateFields(update);
			update();
		}
	}
} // MassUpdate