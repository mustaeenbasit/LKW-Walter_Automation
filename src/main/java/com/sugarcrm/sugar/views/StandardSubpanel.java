package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.SortFailedException;
import com.sugarcrm.sugar.modules.BWCModule;
import com.sugarcrm.sugar.modules.RecordsModule;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.StandardRecord;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Models the subpanel for standard SugarCRM modules.
 * <p>
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class StandardSubpanel extends Subpanel {

	private Set<String> headers = new HashSet<String>();

	public StandardSubpanel(RecordsModule identityModule) throws Exception {
		super(identityModule, "div", "css", ".filtered.layout_" + identityModule.moduleNamePlural);
		isBWC = false;

		setControls();
	}

	/**
	 * Constructor
	 * <p>
	 * @param identityModule  Module representing this subpanel (eg . "sugar().accounts")
	 * @param custom	String value of the [data-subpanel-link] attribute in the DOM <br> on the recordview that this subpanel is visible
	 * @throws Exception
     */
	public StandardSubpanel(RecordsModule identityModule, String custom) throws Exception {
		super(identityModule, "div", "css", " div[data-subpanel-link='" + custom + "']");
		isBWC = false;

		setControls();
	}

	/**
	 * Click on a record in this subpanel by index.
	 * <p>
	 * Must have the subpanel expanded.<br>
	 * Must have a record in this subpanel to click on.<br>
	 * When used, you will be on the recordView/detailView of the record.<br>
	 * You will no longer be on the record view of the parent record.
	 *
	 * @param row	1-based index of the row where a record exists.
	 * @throws Exception
	 */
	@Override
	public void clickRecord(int row) throws Exception {
		scrollIntoView();
		getControl(String.format("nameRow%02d", row)).click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Click on the preview button by row
	 *
	 * Must be on the RecordView of the parent record.
	 * Must have subpanel expanded.
	 *
	 * This action will trigger the preview button to be clicked and expose
	 * the right hand preview pane to display.
	 *
	 * This will leave you on the record view with the preview pane displayed.
	 *
	 * @param row
	 * 			- Int that represents the row # to click
	 * @throws Exception
	 */
	public void clickPreview(int row) throws Exception {
		expandSubpanel();
		getControl(String.format("previewRow%02d", row)).click();
		VoodooUtils.pause(2000);
	}

	/**
	 * Toggle the favorite icon by row
	 *
	 * Must be on the RecordView of the parent record.
	 * Must have subpanel expanded.
	 *
	 * This action will trigger the preview icon to be clicked mark a record
	 * as a favorite.
	 *
	 * This will leave you on the record view with the favorite icon
	 *
	 * @param row
	 * 			- Int that represents the row # toggle
	 * @throws Exception
	 */
	public void toggleFavorite(int row) throws Exception {
		scrollIntoView();
		getControl(String.format("favoriteRow%02d", row)).click();
		VoodooUtils.pause(2000);
	}

	/**
	 * Clicks on the "+" icon to add a Record of subpanel type.
	 *
	 * You must be on the RecordView to click on this icon. The subpanel does
	 * not need to be expanded to use this method.
	 *
	 * This action will trigger the createDrawer to come into view while still
	 * on the parent's RecordView
	 *
	 * @throws Exception
	 */
	public void addRecord() throws Exception {
		scrollIntoView();
		getControl("addRecord").click();
		if(!identityModule.isBwc()){
			((StandardModule)identityModule).createDrawer.getControl("cancelButton").waitForVisible();
		} else {
			VoodooUtils.pause(2000);
		}
	}

	/**
	 * Toggle the subpanel between collapsed view and expanded view.
	 *
	 * You must be on the RecordView of the parent record to toggle subpanel.
	 *
	 * Leaves you on the RecordView of the parent record with the subpanel in
	 * the opposite open/closed state.
	 *
	 * @throws Exception
	 */
	public void toggleSubpanel() throws Exception {
		getControl("toggleSubpanel").scrollIntoViewIfNeeded(true); // scroll + click event
		VoodooUtils.waitForReady();
	}

	/**
	 * Expand subpanel if its current status is collapsed
	 *
	 * Must be on the record view to use.
	 *
	 * Leaves you on the record view with this subpanel expanded
	 *
	 * @throws Exception
	 */
	public void expandSubpanel() throws Exception {
		scrollIntoView();
		if(getControl("subpanelStatus").getAttribute("class").contains("closed")){
			toggleSubpanel();
			new VoodooControl("div", "css", getHookString() + " .flex-list-view.left-actions.right-actions").waitForVisible();
			sugar().alerts.waitForLoadingExpiration();
		}
	}

	/**
	 * Collapse subpanel if its current status is expanded.
	 *
	 * Must be on the record view to use.
	 *
	 * Leaves you on the record view with this subpanel collapsed
	 *
	 * @throws Exception
	 */
	public void collapseSubpanel() throws Exception {
		scrollIntoView();
		if(!(getControl("subpanelStatus").getAttribute("class").contains("closed"))){
			toggleSubpanel();
		}
	}

	/**
	 * Edit a record in this subpanel.
	 * <p>
	 * You must be on the RecordView.<br>
	 * Must have subpanel expanded to use this method.<br>
	 * This method triggers the inline edit of a single specified record in this subpanel.<br>
	 *
	 * @param row	1-based index of the row you want to edit.
	 * @throws Exception
	 */
	@Override
	public void editRecord(int row) throws Exception {
		if(!(identityModule.isBwc())) {
			expandSubpanelRowActions(row);
		}
		getControl(String.format("editActionRow%02d", row)).click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Unlink a record in this subpanel.
	 * <p>
	 * You must be on the RecordView.<br>
	 * This subpanel must be expanded to use this method.<br>
	 * This method will trigger the unlink action for the specified row in this
	 * subpanel and prompt you to confirm the action while leaving you on the
	 * RecordView.
	 *
	 * @param row	1-based index of the row you want to unlink.
	 * @throws Exception
	 */
	@Override
	public void unlinkRecord(int row) throws Exception {
		expandSubpanelRowActions(row);
		getControl(String.format("unlinkActionRow%02d", row)).click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
	}

	/**
	 * Starts the process to link an existing record to relate to the parent
	 * Record.
	 * <p>
	 * You must be on the RecordView.<br>
	 * <p>
	 * This method will bring a drawer down to search for an existing record of
	 * this subpanel type while still on the RecordView of the parent module.
	 *
	 * @throws Exception
	 */
	public void clickLinkExisting() throws Exception {
		scrollIntoView();
		getControl("expandSubpanelActions").click();
		// TODO: Find a way to remove this hard coded wait
		VoodooUtils.pause(2000); // It takes time for menu to display
		getControl("linkExistingRecord").click();
		sugar().alerts.waitForLoadingExpiration(30000);
		identityModule.searchSelect.getControl("cancel").waitForVisible();
	}

	/**
	 * Link existing records to relate to the parent Record.
	 * <p>
	 * You must be on the RecordView.<br>
	 * <p>
	 * This method will bring a drawer down to search for an existing record of
	 * this subpanel type while still on the RecordView of the parent module.
	 *
	 * @param linkRecords ArrayList of Records to link in this subpanel.
	 * @throws Exception
	 */

	public void linkExistingRecords(ArrayList<Record> linkRecords) throws Exception {
		clickLinkExisting();
		for(Record record : linkRecords) {
			identityModule.searchSelect.selectRecord(record);
		}
		identityModule.searchSelect.link();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Link an existing record to relate to the parent Record.
	 * <p>
	 * You must be on the RecordView.<br>
	 * <p>
	 * This method will bring a drawer down to search for an existing record of
	 * this subpanel type while still on the RecordView of the parent module.
	 *
	 * @param	record	Record to link in this subpanel.
	 * @throws Exception
	 */
	public void linkExistingRecord(Record record) throws Exception {
		clickLinkExisting();
		identityModule.searchSelect.selectRecord(record);
		identityModule.searchSelect.link();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Starts the process to select reports, you must be on the RecordView.
	 *
	 * This method will bring a drop down with option "select from reports".
	 *
	 * @throws Exception
	 */
	public void clickOnSelectFromReport() throws Exception {
		scrollIntoView();
		getControl("expandSubpanelActions").click();
		VoodooUtils.waitForReady(); // It takes time for menu to display
		getControl("selectFromReports").click();
		sugar().alerts.waitForLoadingExpiration(30000);
		sugar().reports.searchSelect.getControl("cancel").waitForVisible();
	}

	/**
	 * Link Select from Reports, you must be on the RecordView.
	 *
	 * This method will bring a Search and Select Drawer up and the desired record can be selected (radio button clicked).
	 *
	 * @param rowNum Int index of the record to select.
	 * @throws Exception
	 */
	public void selectFromReports(int rowNum) throws Exception {
		clickOnSelectFromReport();
		sugar().reports.searchSelect.selectRecord(rowNum);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks the save button on a particular row of the current Subpanel View.
	 *
	 * You must be on the Subpanel View to use this method. Leaves you on the
	 * Subpanel View with the original record displayed.
	 *
	 * @param rowNum
	 *            1-based index of the row you want to save.
	 * @throws Exception
	 */
	public void saveAction(int rowNum) throws Exception {
		getControl(String.format("saveActionRow%02d", rowNum)).click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks the cancel button on a particular row of the current Subpanel
	 * View.
	 * <p>
	 * You must be on the Subpanel View to use this method. Leaves you on the
	 * Subpanel View with the original record displayed.
	 *
	 * @param rowNum
	 *            1-based index of the row you want to cancel.
	 * @throws Exception
	 */
	public void cancelAction(int rowNum) throws Exception {
		getControl(String.format("cancelActionRow%02d", rowNum)).click();
	}

	/**
	 * Create a Record of this Subpanels module type via UI.
	 *
	 * @param data	FieldSet of data to use to create a record of this subpanels module type.<br>
	 * @return		Record of this subpanels module type.
	 * @throws Exception
	 */
	@Override
	public Record create(FieldSet data) throws Exception {
		StandardModule module = (StandardModule) identityModule;
		addRecord();
		VoodooUtils.voodoo.log.fine("Reconciling record data.");

		// Merge default data and user-specified data.
		FieldSet recordData = this.identityModule.getDefaultData();
		recordData.putAll(data);

		VoodooUtils.voodoo.log.info("Creating a " + identityModule.moduleNameSingular + " via subpanel UI...");

		if(module.createDrawer.getControl("showMore").queryVisible() == true){
			module.createDrawer.showMore();
		}

		// Iterate over the field data and set field values.
		module.createDrawer.setFields(recordData);

		module.createDrawer.save();
		VoodooUtils.pause(1000);
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.voodoo.log.fine("Record created.");
		return (StandardRecord) Class.forName(identityModule.recordClassName)
				.getConstructor(FieldSet.class).newInstance(recordData);
	}

	/**
	 * Expand the actions menu in this subpanel.
	 * <p>
	 * There must be more than 1 action available to the user for this method to work.<br>
	 *
	 * @param row	1-based index of the row you want to expand actions menu on.
	 * @throws Exception
	 */
	@Override
	public void expandSubpanelRowActions(int row) throws Exception {
		getControl(String.format("expandActionRow%02d", row)).scrollIntoView();
		getControl(String.format("expandActionRow%02d", row)).click();
		new VoodooControl("ul", "css", getHookString() + " .actions.btn-group.list.open .dropdown-menu").waitForVisible();
	}

	/**
	 * Close an activity by row in this Subpanel.
	 * <p>
	 * NOTE:<br>
	 * Only works on Calls and Meetings Subpanels!
	 * <p>
	 * Must have an Open Call or Meeting to close.<br>
	 * When used, you are left on the recordView of the parentRecord
	 * with the desired Call or Meeting status updated to "Held".
	 *
	 * @param rowNum	1-based index of the row you want perform the close action on.
	 * @throws Exception
	 */
	public void closeRecord(int rowNum) throws Exception {
		expandSubpanelRowActions(rowNum);
		getControl(String.format("closeActionRow%02d", rowNum)).click();
	}

	/**
	 * Verify data is or is not in this Subpanel.
	 * <p>
	 * Must have this Subpanel expanded to use.<br>
	 *
	 * @param rowNum	1-based index of the row you want verify in this Subpanel.
	 * @param verifyThis	FieldSet of data you want to verify in this subpanel.
	 * @param shouldExist	true if this subpanel should have the data, false if it should not.
	 * @throws Exception
	 */
	public void verify(int rowNum, FieldSet verifyThis, boolean shouldExist) throws Exception {
		this.scrollIntoViewIfNeeded(false);
		for(String controlName : verifyThis.keySet()) {
			VoodooControl row = new VoodooControl("tr", "css" , getHookString() + " tbody tr:nth-of-type(" + rowNum + ")");
			row.assertContains(verifyThis.get(controlName), shouldExist);
		}
	}

	/**
	 * Set control definitions for this Subpanel
	 * @throws Exception
	 */
	public void setControls() throws Exception {
		// Common control definitions.
		addControl("count", "span", "css", getHookString() + " .fld_collection-count");
		addControl("subpanelName", "h4", "css", getHookString() + " h4");
		addControl("toggleSubpanel", "div", "css", getHookString() + " .subpanel-header");
		addControl("addRecord", "a", "css", getHookString() + " .fld_create_button a");
		addControl("expandSubpanelActions", "a", "css", getHookString() + " .dropdown-toggle");
		addControl("linkExistingRecord", "a", "css", getHookString() + " .fld_select_button a");
		addControl("selectFromReports", "a", "css", getHookString() + " .fld_panel_dropdown ul.dropdown-menu li:nth-of-type(2)");
		addControl("subpanelStatus", "li", "css", getHookString() + " li");
		addControl("moreLink", "button", "css", getHookString() + " div[data-voodoo-name='list-bottom'] [data-action='show-more']");
		addControl("selectAllCheckbox", "input", "css", getHookString() + " .toggle-all");
		addControl("actionDropdown", "a", "css", getHookString() + " span.actionmenu.btn-group [data-original-title='Actions']");
		addControl("deleteButton", "a", "css", ".fld_delete_button a");
		addControl("quoteButton", "a", "css", ".fld_quote_button a");
		addControl("selectedRecordsAlert", "span", "css", getHookString() + " .alert.alert-warning span");
		addControl("selectAllRecordsLink", "button", "css", getHookString() + " .alert.alert-warning .btn-inline");
		addControl("clearSelectionsLink", "button", "css", getHookString() + " .alert.alert-warning .btn-inline");

		// Add 99 rows of element definitions that correspond to the possible
		// rows in a subpanel
		for (int i = 1; i <= 99; i++) {
			String checkbox = String.format("checkbox%02d", i);
			String name = String.format("nameRow%02d", i);
			String favorite = String.format("favoriteRow%02d", i);
			String preview = String.format("previewRow%02d", i);
			String expandAction = String.format("expandActionRow%02d", i);
			String editAction = String.format("editActionRow%02d", i);
			String unlinkAction = String.format("unlinkActionRow%02d", i);
			String cancelAction = String.format("cancelActionRow%02d", i);
			String saveAction = String.format("saveActionRow%02d", i);
			String closeAction = String.format("closeActionRow%02d", i);
			String currentRow = getHookString() + " tbody tr:nth-of-type(" + i + ")";

			// TODO: As pointed out in a PR review. Name is specific to Person Type records. We will need to either provide a generic
			// element definition or add additional vars specific to each Module Type.
			addControl(checkbox, "input", "css", currentRow + " input[name='check']");
			addControl(name, "a", "css", currentRow + " span[class*='name'] a");
			addControl(favorite, "i", "css", currentRow + " i[data-original-title='Favorite']");
			addControl(preview, "a", "css", currentRow + " a[data-original-title='Preview']");
			addControl(expandAction, "a", "css", currentRow + " .fa-caret-down");
			addControl(editAction, "a", "css", currentRow + " .fld_edit_button a");
			addControl(unlinkAction, "a", "css", currentRow + " a[data-event='list:unlinkrow:fire']");
			addControl(cancelAction, "a", "css", currentRow + " .fld_inline-cancel.edit a");
			addControl(saveAction, "a", "css", currentRow + " .fld_inline-save.edit a.btn.btn-primary");
			addControl(closeAction, "a", "css", currentRow + " a[name='record-close']");
		}

		// Email Subpanel only controls
		addControl("composeEmail", "a", "css", getHookString() + " .fld_email_compose_button a");
	}

	/**
	 * Set module name for this subpanel.
	 * <p>
	 * NOTE: This method is typically used for resetting moduleNamePlural to something
	 * different than the default. This method also rebuilds the control definitions using
	 * the new module name.
	 * @param moduleNameIn String of module name to use.
	 * @throws Exception
	 */
	public void setModuleName(String moduleNameIn) throws Exception {
		setHookString(".filtered.layout_" + moduleNameIn);
		setControls();
	}

	/**
	 * Determine if this subpanl is empty.
	 *
	 * @return true if this Subpanel is empty, false otherwise.
	 * @throws Exception
	 */
	public boolean isEmpty() throws Exception {
		return getControl("subpanelStatus").queryAttributeContains("class", "empty");
	}

	/**
	 * Click the compose email button.
	 * <p>
	 * Can only be used in the Email subpanel in sidecar enabled modules.<br>
	 * When used, the compose email view will be visible.
	 *
	 * @throws Exception
	 */
	public void composeEmail() throws Exception {
		getControl("composeEmail").click();
		new VoodooControl("table", "css", "#mce_0_tbl").waitForVisible();
	}

	/**
	 * Show more records in this subpanel.
	 * <p>
	 * Must have more records than can be displayed in the default sized subpanel.<br>
	 * When used, this subpanel will display more records.<br>
	 *
	 * @throws Exception
	 */
	public void showMore() throws Exception {
		getControl("moreLink").click();
	}

	/**
	 * Click on a link by Text in this Subpanel.
	 * <p>
	 * Must have this subpanel exposed.<br>
	 * Must have a record in this subpanel with a link.<br>
	 *
	 * @param linkText String of the link to click on
	 * @param row Int row that contains the link to click on
	 * @throws Exception
	 */
	public void clickLink(String linkText, int row) throws Exception {
		new VoodooControl("a", "css", getHookString() + " tr.single:nth-of-type(" + row + ") [data-original-title='" + linkText + "'] a").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Returns a count of rows
	 * <p>
	 * You must be subpanels to use.<br>
	 *
	 * @return an Integer containing row count
	 * @throws Exception
	 */
	public int countRows() throws Exception {
		// TODO: VOOD-915
		VoodooControl row = new VoodooControl("div", "id", "content");
		return Integer.parseInt(row.waitForElement().executeJavascript("return jQuery('.layout_" + identityModule.moduleNamePlural + " tr.single').length;").toString());
	}

	/**
	 * Edit a record in this subpanel.
	 * <p>
	 * You must be on the RecordView.<br>
	 * Must have subpanel expanded to use this method.<br>
	 * This method triggers the inline edit of a single specified record in this subpanel.<br>
	 *
	 * @param row	1-based index of the row you want to edit.
	 * @param editData
	 * @throws Exception
	 */
	public void editRecord(int row, FieldSet editData) throws Exception {
		editRecord(row);
		if(!identityModule.isBwc()){
			getControl(String.format("saveActionRow%02d", row)).waitForVisible();
			for(String fieldName : editData.keySet()) {
				getEditField(row, fieldName).set(editData.get(fieldName));
				VoodooUtils.pause(250);
			}
			getControl(String.format("saveActionRow%02d", row)).click();
			sugar().alerts.waitForLoadingExpiration();
		} else {
			VoodooUtils.waitForReady();
			VoodooUtils.focusFrame("bwc-frame");
			((BWCModule)identityModule).editView.getControl("cancelButton").waitForVisible();
			((BWCModule)identityModule).editView.setFields(editData);
			VoodooUtils.focusDefault();
			((BWCModule)identityModule).editView.save();
		}
	}

	/**
	 * Clicks the select all checkbox to select or deselect all records on the
	 * selected subpanel.
	 *
	 * @throws Exception
	 */
	public void toggleSelectAll() throws Exception {
		getControl("selectAllCheckbox").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Opens the action dropdown next to the massall checkbox. You must already
	 * be on the subpanel of the desired module and some records must already be
	 * checked. Leaves you on the subpanel with the action dropdown open.
	 *
	 * @throws Exception
	 */
	public void openActionDropdown() throws Exception {
		getControl("actionDropdown").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the "select all records" link in the selected records alert row.
	 *
	 * You must already be on the subpanel with the select all records link visible.<br />
	 * Leaves you on the subpanel with all records (not just the first page) selected.
	 *
	 * @throws Exception
	 */
	public void clickSelectAllRecordsLink() throws Exception {
		getControl("selectAllRecordsLink").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the "clear selections" link in the selected records alert row.
	 *
	 * You must already be on the subpanel with the select all records link visible.<br />
	 * Leaves you on the subpanel with all records (not just the first page) unselected.
	 *
	 * @throws Exception
	 */
	public void clickClearSelectionsLink() throws Exception {
		getControl("clearSelectionsLink").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the delete action in the actions dropdown. You must already be on
	 * the subpanel with some records checked and the actions dropdown open.
	 * Leaves you on the subpanel with records checked and the delete
	 * confirmation alert open.
	 *
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getControl("deleteButton").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the Generate Quote action in the actions dropdown. You must already be on
	 * the subpanel with some records checked and the actions dropdown open.
	 * Leaves you on the Quotes module create drawer.
	 *
	 * @throws Exception
	 */
	public void generateQuote() throws Exception {
		getControl("quoteButton").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Check the checkbox on a particular row of the current subpanel.
	 * You must be on the subpanel to use this method. Leaves you on the
	 * subpanel with the checkbox on the specified row checked, regardless of
	 * its prior state.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void checkRecord(int rowNum) throws Exception {
		getControl(String.format("checkbox%02d", rowNum)).set("true");
	}

	/**
	 * Retrieve a reference to the edit mode version of a field on this Subpanel.
	 * <p>
	 *
	 * @param row	Index of row to get desired control for.
	 * @param fieldName	The VoodooControl name for the desired control.
	 * @return a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getEditField(int row, String fieldName) throws Exception {
		return ((StandardModule)identityModule).getField(fieldName).getListViewEditControl(row);
	}

	/**
	 * Retrieve a reference to the detail mode version of a field on this Subpanel.
	 * <p>
	 *
	 * @param row	Index of row to get desired control for.
	 * @param fieldName	The VoodooControl name for the desired control.
	 * @return a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getDetailField(int row, String fieldName) throws Exception {
		return ((StandardModule)identityModule).getField(fieldName).getListViewDetailControl(row);
	}

	/**
	 * Define the headers for this ListView. This will also update the controls
	 * to make the headers accessible.
	 *
	 * @param headersIn
	 * @throws Exception
	 */
	public void setHeaders(Set<String> headersIn) throws Exception {
		headers = headersIn;
		setHeaderControls();
	}

	/**
	 * Return the currently-defined column headers for this ListView.
	 *
	 * @return a List<String> of the names of the headers on this ListView.
	 */

	public Set<String> getHeaders() {
		return headers;
	}

	/**
	 * Add a header to Subpanel ListView definition. The added header goes at
	 * the end of the list. This can be used for initializing the ListView or
	 * for updating it later.
	 *
	 * @param toAdd
	 *            the SugarCRM internal name for the field
	 * @throws Exception
	 */
	public void addHeader(String toAdd) throws Exception {
		// TODO: Verify that the passed names are valid fields in this module.
		headers.add(toAdd);
		setHeaderControls();
	}

	/**
	 * Remove a header from Subpanel ListView definition. Indices of the
	 * remaining headers will be adjusted to reflect the removal.
	 *
	 * @param toRemove
	 * @throws Exception
	 */
	public void removeHeader(String toRemove) throws Exception {
		// TODO: Differentiate non-existence from success?
		headers.remove(toRemove);
		setHeaderControls();
	}

	/**
	 * Regenerates controls for accessing the column headers.
	 *
	 * @throws Exception
	 */
	public void setHeaderControls() throws Exception {
		// Remove control definitions for existing column headers.
		Iterator<String> i = controls.keySet().iterator();
		while (i.hasNext()) {
			String controlName = i.next();
			if (controlName.startsWith("header"))
				i.remove();
		}

		// Add control definitions for all current column headers.
		for (String header : headers) {
			// Transform the SugarCRM name into a VoodooControl name.
			String controlName = VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header));
			addControl(controlName, "th", "css", getHeaderHook(header));
		}
	}

	/**
	 * Get the hook for a particular column header
	 *
	 * @param header
	 *            the SugarCRM internal name for the field you want to generate
	 *            a hook for. Note that this will build the hook regardless of
	 *            whether the field exists.
	 * @return the hook string for the column header corresponding to the specified header name.
	 */
	 String getHeaderHook(String header) throws Exception {
		 return ( identityModule.getSubpanel().getHookString()  + " .orderBy" + header);
	 }

	/**
	 * Sorts the list view by the specified column in either ascending or
	 * descending order. You must already be on the ListView you're sorting.
	 * This method leaves you on the ListView, now sorted by the specified
	 * column.
	 *
	 * @param header
	 *            the SugarCRM internal name for the field you want to sort by and prepend "header" as text.
	 * @param ascending
	 *            true for ascending, false for descending.
	 * @throws SortFailedException
	 *             if the column could not be sorted as requested.
	 */
	public void sortBy(String header, boolean ascending) throws Exception {
		VoodooControl columnHeader = getControl(header);
		// First time we navigate to a Module and we haven't sorted a column
		// we need to click on that column to trigger the class to change and have either
		// _asc or _desc, i.e. sorting_asc and sorting_desc
		columnHeader.scrollIntoViewIfNeeded(false);
		columnHeader.click(); // hack hack hack
		VoodooUtils.waitForReady();
		String headerClass = columnHeader.getAttribute("class");
		VoodooUtils.voodoo.log.info(header + " has class value of: " + headerClass);
		String direction = (ascending ? " ascending" : " descending");

		// If the sort is already correct, do nothing.
		if (ascending && headerClass.contains("sorting_asc") || !ascending && headerClass.contains("sorting_desc")) {
			VoodooUtils.voodoo.log.info("Column is already sorted " + direction + ".  Skipping click.");
		} else { // sort is not already correct, so sort it.
			// Max 2 attempts -- first sort may not be the desired direction,
			// but the second should be for sure.
			for (int i = 0; i < 2; i++) {
				columnHeader.scrollIntoViewIfNeeded(false);
				columnHeader.click();
				VoodooUtils.waitForReady();
				headerClass = columnHeader.getAttribute("class");

				// if the sort was successful, we're done
				if (ascending && headerClass.contains("sorting_asc") || !ascending && headerClass.contains("sorting_desc")) {
					VoodooUtils.voodoo.log.info("Sorted column " + header + " " + direction + ".");
					return;
				}
			}

			// If we haven't returned by now, the sort failed.
			VoodooUtils.voodoo.log.severe("Could not sort header \"" + header + "\" not defined in this view!");
			throw (new SortFailedException(header));
		}
	}

} // StandardSubpanel