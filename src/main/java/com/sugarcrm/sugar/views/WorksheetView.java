package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;

public class WorksheetView extends ListView {
	public WorksheetView(Module parentModule, String tagIn, String strategyNameIn, String hookStringIn) throws Exception {
		super(parentModule);
		// Controls found on Forecast Module
		addControl("currentForecastUser", "span", "css", ".module-title.pull-left");
		addControl("saveDraftButton", "a", "css", "a[name='save_draft_button']");
		addControl("commitButton", "a", "css", "a[name='commit_button']");
		addControl("actionDropdown", "a", "css", ".actions.btn-group.list-headerpane a[data-toggle='dropdown']");
		addControl("exportCSV", "a", "css", "ul[data-menu='dropdown'] a[name='export_button']");
		addControl("settingsButton", "a", "css", "ul[data-menu='dropdown'] a[name='settings_button']");
		addSelect("timePeriod", "a", "css", ".forecastsTimeperiod.fld_selectedTimePeriod.edit a");
		addControl("likelyTotal", "td", "css", "#forecastsWorksheetTotalsOverallTotals td:nth-of-type(2)");
		addControl("bestTotal", "td", "css", "#forecastsWorksheetTotalsOverallTotals td:nth-of-type(3)");
		
		for (int i = 1; i <= 99; i++) {
			// Build internal Voodoo names for each field in a row.
			String preview = String.format("preview%02d", i);
			String rliName = String.format("rliName%02d", i);
			String oppName = String.format("oppName%02d", i);
			String acctName = String.format("acctName%02d", i);
			String expectedClose = String.format("expectedClose%02d", i);
			String stage = String.format("stage%02d", i);
			String probability = String.format("probability%02d", i);
			String likelyCase = String.format("likelyCase%02d", i);
			String bestCase = String.format("bestCase%02d", i);

			// Build a string prefix that represents the current row in each
			// control.
			String currentRow = "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(" + i + ")";

			// Add Voodoo controls for all fields in the row.
			addControl(preview, "a", "css", currentRow + " a[data-event='list:preview:fire']");
			addControl(rliName, "a", "css", currentRow + " td[data-field-name='parent_name'] a");
			addControl(oppName, "a", "css", currentRow + " td[data-field-name='opportunity_name'] a");
			addControl(acctName, "a", "css", currentRow + " td[data-field-name='account_name'] a");
			addControl(expectedClose, "span", "css", currentRow + " td[data-field-name='date_closed'] span");
			addSelect(stage, "span", "css", currentRow + " td[data-field-name='sales_stage'] span");
			addControl(probability, "span", "css", currentRow + " td[data-field-name='probability'] span");
			addControl(likelyCase, "span", "css", currentRow + " td[data-field-name='likely_case'] span");
			addControl(bestCase, "span", "css", currentRow + " td[data-field-name='best_case'] span");
		}
		
		// Add headers for Forecast worksheet
		addHeader("commit_stage");
		addHeader("parent_name");
		addHeader("opportunity_name");
		addHeader("account_name");
		addHeader("date_closed");
		addHeader("product_template_name");
		addHeader("sales_stage");
		addHeader("probability");
		addHeader("likely_case");
		addHeader("best_case");
	}

	/**
	 * Click the action dropdown to expose worksheet actions.
	 * <p>
	 * Must be in the forecasts module to use.<br>
	 * Leaves you on the Forecast module with the actions dropdown exposed.
	 * 
	 * @throws Exception
	 */
	public void openActionDropdown() throws Exception {
		getControl("actionDropdown").click();
		new VoodooControl("ul", "css", ".dropdown-menu").waitForVisible(); // Dropdown menu container
	}
	
	/**
	 * Click the commit button.
	 * <p>
	 * Must be in the forecasts module to use.<br>
	 * Must have changes in the forecast worksheet to use.<br>
	 * Leaves you on the forecast module.
	 * 
	 * @throws Exception
	 */
	public void commit() throws Exception {
		getControl("commitButton").click();
		VoodooUtils.pause(2000);
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Export forecast worksheet.
	 * <p>
	 * Must be in the forecasts module to use.<br>
	 * Starts the export process of the current worksheet data.
	 * <p>
	 * Note:<br>
	 * Using this action will trigger a download of the current sheet data to the local file system.
	 * 
	 * @throws Exception
	 */
	public void export() throws Exception {
		openActionDropdown();
		getControl("exportCSV").click();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Click on the setting action in action dropdown menu.
	 * <p>
	 * Must be on the forecasts module to use.<br>
	 * When clicked, you will be taken to the forecast module setting page.<br>
	 * 
	 * @throws Exception
	 */
	public void settings() throws Exception {
		openActionDropdown();
		getControl("settingsButton").click();
		sugar().forecasts.setup.getControl("worksheetSettings").waitForVisible();
	}
	
	/**
	 * Click on the save draft button.
	 * <p>
	 * Must be on the forecasts module to use.<br>
	 * Must have changes to the forecast worksheet to use.<br>
	 * When used, the current worksheet data will be saved.
	 * 
	 * @throws Exception
	 */
	public void saveDraft() throws Exception {
		getControl("saveDraftButton").click();
		VoodooUtils.pause(2000);
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Set the current forecast worksheet time period.
	 * <p>
	 * Must be in the forecast module to use.<br>
	 * Must have time periods defined in Admin Tools.<br>
	 * When used, and the time period has changed, the data in the worksheet will
	 * change to display RLI/Amounts/etc in the new time period.
	 * 
	 * @param timePeriodIn	String representing the time period to change to.
	 * @throws Exception
	 */
	public void setTimePeriod(String timePeriodIn) throws Exception {
		getControl("timePeriod").set(timePeriodIn);
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Sets fields of a record on the forecasts worksheet per row
	 * <p>
	 * Must be on the worksheet view in the forecast module.
	 * Once complete this method will require more action, save draft,
	 * commit or do nothing.
	 * 
	 * @param rowNum	1-based index of row to set fields in
	 * @param data	FieldSet of data to use for setting
	 * @throws Exception
	 */
	public void setEditFields(int rowNum, FieldSet data) throws Exception {
		for(String field : data.keySet()){
			if(field != null && data.get(field) != null) {
				((StandardModule)parentModule).getField(field).getListViewDetailControl(rowNum).click(); // Changes field into its editable version
				((StandardModule)parentModule).getField(field).getListViewEditControl(rowNum).set(data.get(field));
			}
		}	
	}
	
	/**
	 * Update a record by row in the List View.
	 * <p>
	 * Must be on the List View of a module.
	 * Once complete this method will save the changes and return 
	 * user back to list view detail mode.
	 * 
	 * @param rowNum 1-based index of row to edit
	 * @param data FieldSet of data to use for editing
	 * @throws Exception
	 */
	public void updateRecord(int rowNum, FieldSet data) throws Exception {
		setEditFields(rowNum, data);
		saveDraft();
	}
	
	/**
	 * Clicks on the Revenue Line Item link.
	 * <p>
	 * Leaves you on the RecordView of the Revenue Line Item clicked.
	 * 
	 * @param rowNum	one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void clickRevenuelineitem(int rowNum) throws Exception {
		getControl(String.format("rliName%02d", rowNum)).click();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Clicks on the Opportunity Name link.
	 * <p>
	 * Leaves you on the RecordView of the Opportunity clicked.
	 * 
	 * @param rowNum	one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void clickOpportunity(int rowNum) throws Exception {
		getControl(String.format("oppName%02d", rowNum)).click();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Clicks on the Account Name link.
	 * <p>
	 * Leaves you on the RecordView of the Account clicked.
	 * 
	 * @param rowNum	one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void clickAccount(int rowNum) throws Exception {
		getControl(String.format("accName%02d", rowNum)).click();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * @deprecated
	 */
	public void create() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void toggleSelectAll() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void massUpdate() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void delete() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void confirmDelete() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void cancelDelete() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void openFilterDropdown() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void selectFilterAll() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void selectFilterAssignedToMe() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void selectFilterMyFavorites() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void selectFilterRecentlyViewed() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void selectFilterRecentlyCreated() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void selectFilterCreateNew() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void setSearchString(String toSearch) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void checkRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void uncheckRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void toggleRecordCheckbox(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void toggleFavorite(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	/**
	 * @deprecated
	 */
	public void clickSelectAllRecordsLink() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void clickClearSelectionsLink() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void clickRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void saveRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void cancelRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void openRowActionDropdown(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void editRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void toggleFollow(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void deleteRecord(int rowNum) throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void showActivityStream() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}

	/**
	 * @deprecated
	 */
	public void showListView() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
	
	
	/**
	 * @deprecated
	 */
	public void showMore() throws Exception {
		VoodooUtils.voodoo.log.severe("This is not functionality available to Forecast Module.");
	}
}