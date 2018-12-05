package com.sugarcrm.sugar.views;

import java.util.ArrayList;
import java.util.List;

import com.sugarcrm.sugar.SortFailedException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

/**
 * Models the ListView for standard SugarCRM modules. 
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class BWCListView extends View implements IListView {
	/**
	 * The SugarCRM internal names for the fields that appear as ListView
	 * column headers, in the order in which they appear.  This list should be
	 * updated any time the ListView is altered (e.g. by Studio updates).
	 */
	private List<String> headers = new ArrayList<String>();

	public BWCListView(RecordsModule parentModule) throws Exception {
		super(parentModule, "div", "css", ".listViewBody");

		int linkColumn = 4; // default Column of Listview which holds the Record Identifier string

		// Do not create controls in this class that begin with "header"; that
		// is reserved for programmatically-generated controls for accessing
		// the column headers.

		// Top row
		addControl("moduleTitle", "div", "css", ".moduleTitle");

		// Basic Search panel
		addControl("nameBasic", "input", "id", "name_basic");
		addControl("myItemsCheckbox", "input", "id", "current_user_only_basic");
		addControl("myFavoritesCheckbox", "input", "id", "favorites_only_basic");
		addControl("searchButton", "input", "id", "search_form_submit");
		addControl("clearButton", "input", "id", "search_form_clear");
		addControl("advancedSearchLink", "input", "id", "advanced_search_link");
		addControl("startButton", "button", "css" ,"#listViewStartButton_top");
		addControl("endButton", "button", "css" ,"#listViewEndButton_top");
		addControl("prevButton", "button", "css" ,"#listViewPrevButton_top");
		addControl("nextButton", "button", "css" ,"#listViewNextButton_top");

		// Advanced Search panel
		addControl("basicSearchLink", "a", "id", "basic_search_link");

		// Column header row
		addControl("selectAllCheckbox", "input", "id", "massall_top");
		addControl("selectDropdown", "span", "css", ".sugar_action_button span");
		addControl("actionDropdown", "span", "css", "#actionLinkTop span.ab");
		addControl("massUpdateButton", "a", "css", "#massupdate_listview_top");
		addControl("deleteButton", "a", "css", "#delete_listview_top");
		// TODO: https://sugarcrm.atlassian.net/browse/TR-5080
		addControl("exportButton", "a", "css", "[id*='export_listview_top']");
		addControl("actionDropdownUL", "ul", "css", "#actionLinkTop .subnav.ddopen");

		// Selection menu
		addControl("selectThisPage", "a", "css", "#button_select_this_page_top");
		addControl("selectAll", "a", "css", "#button_select_all_top");
		addControl("deselectAll", "a", "css", "#button_deselect_top");

		for(int i=1; i <= 99; i++){
			// Build internal Voodoo names for each control in a row.
			String checkbox = String.format("checkbox%02d", i);
			String favoriteStar = String.format("favoriteStar%02d", i);
			String edit = String.format("edit%02d", i);
			String link = String.format("link%02d", i);
			String details = String.format("details%02d", i);

			// Build a string prefix that represents the current row in each control.
			String currentRow = "table.list.view tbody tr:nth-of-type(" + (i + 2) + ")";

			// Add Voodoo controls for all controls in the row.
			addControl(checkbox, "input", "css", currentRow + " input");
			addControl(favoriteStar, "i", "css", currentRow + " div.star div");

			addControl(link, "a", "css", currentRow + " td:nth-of-type(" + linkColumn + ") a");

			addControl(edit, "a", "css", currentRow + " a.quickEdit");
			addControl(details, "a", "css", currentRow + " span[data-voodoo-name='edit_button'] a");
		}		

		// TODO: Activity Stream toggle & controls (possibly a separate view).
		// TODO: Preview panel (possibly a separate view).
	}

	/**
	 * A few BWC modules do not use default Column 4 for Record Identifier, hence
	 * for these modules, we override the default controls
	 * 
	 * @param colNum
	 * @throws Exception
	 */
	public void setLinkColumn(int colNum) throws Exception {
		//This will override the standard link identifiers in BWCListView
		for(int i=1; i <= 99; i++){
			// Build internal Voodoo names for each control in a row.
			String link = String.format("link%02d", i);
			// Build a string prefix that represents the current row in each control.
			String currentRow = "table.list.view tbody tr:nth-of-type(" + (i + 2) + ")";
			addControl(link, "a", "css", currentRow + " td:nth-of-type(" + colNum + ") a");
		}	
	}

	/**
	 * Define the headers for this ListView.  This will also update the
	 * controls to make the headers accessible.
	 * @param	headersIn	a list of Sugar's internal names for the header fields	
	 * @throws Exception 
	 */
	public void setHeaders(List<String> headersIn) throws Exception {
		headers = headersIn;
		setHeaderControls();
	}

	/**
	 * Return the currently-defined column headers for this ListView. 
	 * @return	a list of Sugar's internal names for the header fields 
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * Add a header to Voodoo's ListView definition.  The added header goes at
	 * the end of the list.  This can be used for initializing the ListView or
	 * for updating it later.
	 * @param toAdd the SugarCRM internal name for the field
	 * @throws Exception 
	 * @deprecated Depends on setHeaderControls, which is not yet implemented.
	 */
	public void addHeader(String toAdd) throws Exception {
		// TODO: Verify that the passed names are valid fields in this module.
		headers.add(toAdd);
		setHeaderControls();
	}

	/**
	 * Remove a header from Voodoo's ListView definition.  Indices of the
	 * remaining headers will be adjusted to reflect the removal.
	 * @param	toRemove	fieldname of the header to be removed.
	 * @throws Exception 
	 * @deprecated Depends on setHeaderControls, which is not yet implemented.
	 */
	public void removeHeader(String toRemove) throws Exception {
		// TODO: Differentiate non-existence from success?
		headers.remove(toRemove);
		setHeaderControls();
	}

	/**
	 * Regenerates controls for accessing the column headers.
	 * @throws Exception
	 * @deprecated Not yet implemented.
	 */
	public void setHeaderControls() throws Exception {
		VoodooUtils.voodoo.log.severe("Method setHeaderControls() not implemented.");
	}

	/**
	 * Get the hook for a particular column header
	 * @param header the SugarCRM internal name for the field you want to
	 * generate a hook for.  Note that this will build the hook regardless of
	 * whether the field exists.
	 * @return a hook for the column header for the specified field.
	 * @deprecated Depends on setHeaderControls, which is not yet implemented.
	 */
	String getHeaderHook(String header) {
		VoodooUtils.voodoo.log.severe("Method getHeaderHook() not implemented.");
		return(null);
	}

	/**
	 * Return the module title of the current ListView.
	 * 
	 * You must be on the desired ListView.
	 * @return a String containing the ListView's module title
	 * @throws Exception
	 */
	public String getModuleTitle() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		String toReturn = getControl("moduleTitle").getText();
		VoodooUtils.focusDefault();
		return(toReturn);
	}

	/**
	 * Asserts that the module title of the current ListView contains the
	 * provided string (for purposes of ensuring the current page is the
	 * expected page).
	 * 
	 *  You must be on the desired ListView.
	 * @param toVerify the string that should appear in the ListView's module
	 * title.
	 * @throws Exception
	 */
	public void verifyModuleTitle(String toVerify) throws Exception {
		getControl("moduleTitle").assertEquals(toVerify, true);
	}

	/**
	 * Sorts the list view by the specified column in either ascending or
	 * descending order.
	 * You must already be on the ListView you're sorting.
	 * This method leaves you on the ListView, now sorted by the specified column.
	 * @param header the SugarCRM internal name for the field you want to sort by.
	 * @param ascending true for ascending, false for descending.
	 * @throws SortFailedException if the column could not be sorted as
	 * requested.
	 * @deprecated Not yet implemented.
	 */
	public void sortBy(String header, boolean ascending) throws Exception {
		VoodooUtils.voodoo.log.severe("Method sortBy() not implemented.");

		// TODO: Implement using logic similar to below.  Remove comment and
		// commented code when method is implemented.
		/*		VoodooControl columnHeader = getControl(header);
		String headerClass = columnHeader.getAttribute("class");
		String direction = (ascending ? " ascending" : " descending");

		// If the sort is already correct, do nothing.
		if(ascending && headerClass.matches("sorting_asc") ||
			!ascending && headerClass.matches("sorting_desc")) {
			VoodooUtils.voodoo.log.info("Column is already sorted " +	direction +
					".  Skipping click.");
		} else { // sort is not already correct, so sort it.
			// Max 2 attempts -- first sort may not be the desired direction,
			// but the second should be for sure.
			for(int i = 0; i < 2; i++) {
				columnHeader.click();
				headerClass = columnHeader.getAttribute("class");

				// if the sort was successful, we're done
				if(ascending && headerClass.matches("sorting_asc") ||
						!ascending && headerClass.matches("sorting_desc")) {
					VoodooUtils.voodoo.log.info("Sorted column " + header + " " + direction + ".");
					return;
				}
			}

			// If we haven't returned by now, the sort failed.
			VoodooUtils.voodoo.log.severe("Could not sort header \"" + header + "\" not defined in this view!");
			throw(new SortFailedException(header));
		}
		 */	}

	/**
	 * Verifies that the cell specified by the field name and row number
	 * contains the string value.
	 * 
	 * @param row the 1-based number of the row you want to verify
	 * @param field the SugarCRM internal name for the field you want to verify 
	 * @param value the string value you want to verify is contained within the field.
	 * @throws Exception
	 */
	public void verifyField(int row, String field, String value) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		((RecordsModule) parentModule).getField(field).getListViewDetailControlBwc(row).assertEquals(value, true);
		VoodooUtils.focusDefault();
	}

	/**
	 * Clicks the select all checkbox to select or deselect all records on the
	 * current page.
	 * @throws Exception 
	 */
	public void toggleSelectAll() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("selectAllCheckbox").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(500);
	}

	/**
	 * Opens the action dropdown next to the massall checkbox.
	 * You must already be on the ListView of the desired module and some
	 * records must already be checked.
	 * Leaves you on the ListView with the action dropdown open.
	 * @throws Exception
	 */
	public void openSelectDropdown() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("selectDropdown").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(500);
	}

	/**
	 * Clicks the Mass Update action in the actions dropdown.
	 * You must already be on the ListView with some records checked and the
	 * actions dropdown open.
	 * Leaves you on the ListView with records checked and the Mass Update
	 * panel open.
	 * @throws Exception
	 */
	public void massUpdate() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("massUpdateButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(500);
	}

	/**
	 * Clicks the delete action in the actions dropdown.
	 * <p>
	 * You must already be on the ListView with some records checked and the
	 * actions dropdown open.<br>
	 * When used, you will be prompted to confirm or cancel the delete via a browser modal dialog popup.
	 * <p>
	 * !! NOTE !!: The focus after using this method will not be set back to default. It is recommended that after
	 * accepting the browser modal dialog popup, you should focusDefault();
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("deleteButton").click();
		VoodooUtils.pause(500);
		// Can't refocus to default until after the alert is confirmed or canceled.
	}

	/**
	 * Clicks the export action in the actions dropdown.
	 * You must already be on the ListView with some records checked and the
	 * actions dropdown open.
	 * Leaves you on the ListView and invokes the browser's Save functionality
	 * to save an exported file (result depends on browser and its settings).
	 * @throws Exception
	 */
	public void export() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("exportButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(500);
	}

	/**
	 * Clicks OK on the delete confirmation JavaScript alert.  (Just a pass-
	 * through for VoodooUtils.acceptDialog().)
	 * You must already be on the ListView with some records checked and the
	 * delete confirmation JavaScript alert visible.
	 * Leaves you on the ListView with the previously-checked records deleted.
	 * @throws Exception
	 */
	public void confirmDelete() throws Exception {
		VoodooUtils.acceptDialog();
		VoodooUtils.pause(2000); // needed because the page doesn't immediately respond and shows no indication that a process is completed.
	}

	/**
	 * Sets the search string in the filters panel.
	 * @param toSearch the string you wish to search for.
	 * @throws Exception
	 */
	public void basicSearch(String toSearch) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");		
		getControl("nameBasic").set(toSearch);
		getControl("searchButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(1000);
	}

	/**
	 * Clicks the Clear button on the search form.
	 * @throws Exception
	 */
	public void clearSearchForm() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("clearButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(1000);
	}

	/**
	 * Clicks the Search button on the search form.
	 * @throws Exception
	 */
	public void submitSearchForm() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("searchButton").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(1000);
	}

	/**
	 * Check the checkbox on a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the ListView with the checkbox on the specified row
	 * checked, regardless of its prior state.
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void checkRecord(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("checkbox%02d", rowNum)).set("true");
		VoodooUtils.focusDefault();
	}

	/**
	 * Uncheck the checkbox on a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the ListView with the checkbox on the specified row
	 * unchecked, regardless of its prior state.
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void uncheckRecord(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("checkbox%02d", rowNum)).set("false");
		VoodooUtils.focusDefault();
	}

	/**
	 * Toggle the checkbox on a a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the ListView with the checkbox on the specified row
	 * in the opposite of its prior state (i.e. checked if it was unchecked
	 * before or unchecked if it was checked before).
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void toggleRecordCheckbox(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("checkbox%02d", rowNum)).click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Toggle the favorite star on aparticular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the ListView with the favorite star on the specified row
	 * in the opposite of its prior state (i.e. on if it was off before or off
	 * if it was on before).
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void toggleFavorite(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("favoriteStar%02d", rowNum)).click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Clicks the record link on a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the RecordView of the record on the specified row.
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void clickRecord(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("link%02d", rowNum)).click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Edit the record on a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the EditView of the specified record.
	 * 
	 * @param rowNum one-based row number of the record you want to access.
	 * @throws Exception 
	 */
	public void editRecord(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("edit%02d", rowNum)).click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Click the delete link on a particular row of the current ListView.
	 * 
	 * You must be on the ListView to use this method.
	 * Leaves you on the ListView and displays the delete confirmation prompt.
	 * 
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception 
	 */
	public void deleteRecord(int rowNum) throws Exception {
		checkRecord(rowNum);
		openActionDropdown();
		delete();
	}

	/**
	 * Opens the action dropdown next to the massall checkbox.
	 * <p>
	 * You must already be on the BWCListView of the desired module and some records must already be checked.<br>
	 * Leaves you on the ListView with the action dropdown open.
	 * 
	 * @throws Exception
	 */
	public void openActionDropdown() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("actionDropdown").click();
		getControl("actionDropdownUL").waitForVisible();
		VoodooUtils.focusDefault();
	}

	/**
	 * Returns a count of rows
	 * <p>
	 * You must already be on the BWCListView of the desired module.<br>
	 * Leaves you on the BWCListview of the desired module.
	 *  
	 * @return an Integer containing row count
	 * @throws Exception
	 */
	public int countRows() throws Exception{
		// TODO: VOOD-915
		VoodooControl row = new VoodooControl("div", "id", "contentTable");
		VoodooUtils.focusFrame("bwc-frame");
		return Integer.parseInt(row.waitForElement().executeJavascript("return jQuery('tr[class$=\"ListRowS1\"]').length;").toString());
	}
}