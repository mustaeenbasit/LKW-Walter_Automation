package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the Filter Create widget for SugarCRM modules.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class FilterCreateView extends View {
	StandardModule identityModule = null;

	public FilterCreateView(Module identityModule) throws Exception {
		super(identityModule, "div", "css", "div[data-voodoo-name='" + identityModule.moduleNamePlural + "'] .filter-options.extend");
		this.identityModule = (StandardModule)identityModule;
		// Common control definitions.
		addControl("filterName", "input", "css", getHookString() + " .filter-header input");
		addControl("saveButton", "a", "css", getHookString() + " [data-action='filter-save']");
		addControl("cancelButton", "a", "css", getHookString() + " [data-action='filter-close']");
		addControl("resetButton", "a", "css", getHookString() + " [data-action='filter-reset']");
		addControl("deleteButton", "a", "css", getHookString() + " [data-action='filter-delete']");

		// Add 10 rows of element definitions that correspond to the possible rows in the filter panel
		for(int i=1; i <= 10; i++) {
			String addFilterRow = String.format("addFilterRow%02d", i);
			String removeFilterRow = String.format("removeFilterRow%02d", i);

			String currentRow = "[data-filter='row']:nth-of-type(" + i + ")";

			addControl(addFilterRow, "input", "css", currentRow + " .filter-actions.btn-group [data-action='add']");
			addControl(removeFilterRow, "span", "css", currentRow + " .filter-actions.btn-group [data-action='remove']");
		}
	}

	/**
	 * Click the save button in this filter create view.
	 * <p>
	 * Must be on the filter create view to use.<br>
	 * When used, you will be left on the parentModule listView with this filter create view closed.<br>
	 * If used with all the proper fileds filled in, the new filter will be saved.
	 *
	 * @throws Exception
	 */
	public void save() throws Exception {
		getControl("saveButton").click();
		sugar().alerts.waitForLoadingExpiration(30000);
	}

	/**
	 * Click the cancel button in this filter create view.
	 * <p>
	 * Must be on the filter create view to use.<br>
	 * When used, you will be left on the parentModule listView with this filter create view closed.
	 *
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancelButton").click();
	}

	/**
	 * Reset all fields in this filter create view to their default or blank values.
	 * <p>
	 * Must be on the filter create view to use.<br>
	 * When used, you will left in the filter create view with the fields values blank.
	 *
	 * @throws Exception
	 */
	public void reset() throws Exception {
		getControl("resetButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click the Delete link in this filter.
	 * <p>
	 * Must already have a saved Filter up to use.<br>
	 * When used the user will be prompted to confirm/cancel the delete operation.<br>
	 *
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getControl("deleteButton").click();
	}

	/**
	 * Click Add "+" on a specific row
	 * <p>
	 * Must already have the Filter view up to use.<br>
	 * When used you will have 1 more filter row.
	 *
	 * @param row Index of the row "+" to click on
	 * @throws Exception
	 */
	public void clickAddRow(int row) throws Exception {
		getControl(String.format("addFilterRow%02d", row)).click();
	}

	/**
	 * Click Remove "-" on a specific row
	 * <p>
	 * Must already have the Filter view up to use.<br>
	 * Must have more than 1 additional row added to use.<br>
	 * When used you will have 1 less filter row.
	 *
	 * @param row Index of the row "-" to click on.
	 * @throws Exception
	 */
	public void clickRemoveRow(int row) throws Exception {
		getControl(String.format("removeFilterRow%02d", row)).click();
	}

	/**
	 * Create a new search filter.
	 * <p>
	 * Must already have the Filter view up to use.<br>
	 *
	 * @param filterData FieldSet of data to use to create a new search filter
	 *                   NOTE: Uses the following <key,value> structure -- ("fieldName", "display name,operator,value,row")
	 * @throws Exception
	 */
	public void create(FieldSet filterData) throws Exception {
		// Set the Filter name first, then remove reference to name from FieldSet
		getControl("filterName").set(filterData.get("filterName"));
		FieldSet data = filterData.deepClone();
		data.remove("filterName");


		//		The following loop is required so that there will exist rows prior to setting fields by row. This was needed
		//		specifically because the order in which rows are set is not guaranteed and could cause row 3 to be set when
		//		only 2 rows exist
		// Add a row for each filter needed
		for(String key : data.keySet()) {
			clickAddRow(1);
		}

		// Set field, operator and value for each filter
		for(String key : data.keySet()) {
			String fieldName = key;
			String displayName = data.get(key).split(",")[0];
			String operator = data.get(key).split(",")[1];
			String value = data.get(key).split(",")[2];
			int row = Integer.parseInt(data.get(key).split(",")[3]);

			setFilterFields(fieldName, displayName, operator, value, row);
		}
		save();
	}

	/**
	 * Set fields in this Filter.
	 * <p>
	 * Must already be on the Filter view to use.<br>
	 * When used, you will be left on the Filter View with the desired fields added and values set.
	 * It doesn’t support "is between" and "do not call" yet.
	 *
	 * @param fieldName String field name desired to use
	 * @param displayName String display name of the field to use in this filter
	 * @param operator String operator for to be set
	 * @param value String of the value for this filter field to be set
	 * @param row Int index of the row desired
	 * @throws Exception
	 */
	public void setFilterFields(String fieldName, String displayName, String operator, String value, int row) throws Exception {
		if (fieldName != null && operator != null && value != null) {
			// Capitalize the first letter of each word of the field value
			VoodooUtils.voodoo.log.info("Adding '" + displayName + "' field to filter, setting operator to '" + operator + "' and value to '" + value + "'");
			new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(" + row + ") .fld_filter_row_name.detail").set(displayName);
			VoodooUtils.waitForReady();
			new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(" + row + ") .fld_filter_row_operator.detail").set(operator);
			VoodooUtils.waitForReady();
			VoodooControl fieldValue = new VoodooControl("div", "css", ".filter-definition-container [data-filter='row']:nth-of-type(" + row + ")").
					getChildElement(identityModule.getField(fieldName).filterCreateControl.getTag(),
							identityModule.getField(fieldName).filterCreateControl.getStrategyName(),
							identityModule.getField(fieldName).filterCreateControl.getHookString());
			fieldValue.click();
			fieldValue.set(value);
			// To search and select the record 
			if (identityModule.getField(fieldName).get("type").equals("select") || identityModule.getField(fieldName).get("type").equals("tag")) {
				new VoodooControl("span", "css", ".select2-results .select2-match").click();
			}
			VoodooUtils.waitForReady();
		} else {
			throw new Exception("Field '" + displayName + "' was in the supplied record data, but no value/operator was given or no Control was available.");
		}
	}
} // FilterCreate