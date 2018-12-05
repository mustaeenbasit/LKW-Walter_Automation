package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.SortFailedException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Models the ListView for all Process Related Modules. This class does not extend
 * ListView.java on purpose because the differences are significant enough to warrant
 * independent implementation.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ProcessesListView extends View implements IListView {
	/**
	 * The SugarCRM internal names for the fields that appear as ListView column
	 * headers, in the order in which they appear. This list should be updated
	 * any time the ListView is altered (e.g. by Studio updates).
	 */
	private List<String> headers = new ArrayList<String>();

	public ProcessesListView(Module parentModule) throws Exception {
		super(parentModule, "div", "css", ".layout_pmse_Inbox");

		// Top row
		addControl("moduleTitle", "span", "css", "span[data-fieldname='title'] .fld_title");
		addControl("createButton", "a", "css", ".fld_create_button a");
		addControl("toggleSidebar", "button", "css", ".btn.btn-invisible.sidebar-toggle");

		// Filter bar
		addControl("filterDropdown", "a", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type");
		addControl("filterAll", "a", "css", ".search-filter-dropdown [data-id='all_records']");
		addControl("filterAssignedToMe", "a", "css", ".search-filter-dropdown [data-id='assigned_to_me']");
		addControl("filterMyFavorites", "a", "css", ".search-filter-dropdown [data-id='favorites']");
		addControl("filterRecentlyViewed", "a", "css", ".search-filter-dropdown [data-id='recently_viewed']");
		addControl("filterRecentlyCreated", "a", "css", ".search-filter-dropdown [data-id='recently_created']");
		addControl("filterCreateNew", "a", "css", ".search-filter-dropdown [data-id='create']");
		addControl("searchFilter", "input", "css", "div.filter-view.search input.search-name");
		addControl("searchSuggestion", "span", "css", "li.select2-results-dept-0.select2-result-selectable.select2-highlighted div.select2-result-label span.select2-match");

		for(int i = 1; i < 100; i++) {
			String dropdown = String.format("dropdown%02d", i);
			String history = String.format("history%02d", i);
			String showNotes = String.format("showNotes%02d", i);
			String reassign = String.format("reassign%02d", i);
			String cancel = String.format("cancel%02d", i);
			String disable = String.format("disable%02d", i);
			String showProcess = String.format("showProcess%02d", i);

			String currentRow = "div.flex-list-view table.dataTable tbody tr:nth-of-type(" + i + ")";

			addControl(dropdown, "a", "css", currentRow + " a.dropdown-toggle");
			addControl(history, "a", "css", currentRow + " .fld_History a");
			addControl(showNotes, "a", "css", currentRow + " .fld_viewNotes a");
			addControl(reassign, "a", "css", currentRow + " .fld_reassignButton a");
			addControl(cancel, "a", "css", currentRow + " .fld_cancelButton a");
			addControl(disable, "a", "css", currentRow + " .fld_disabled_button a");
			addControl(showProcess, "a", "css", currentRow + " .fld_edit_button a");
		}

		// Static links
		addControl("showMore", "a", "css", ".main-content div[data-voodoo-name='list-bottom'] button[data-action='show-more']");

		// Common Column Headers
		addHeader("cas_id");
		addHeader("pro_title");
		addHeader("cas_title");
		addHeader("prj_user_id_full_name");
	}

	/**
	 * Define the headers for this ListView. This will also update the controls
	 * to make the headers accessible.
	 *
	 * @param headersIn
	 * @throws Exception
	 */
	public void setHeaders(List<String> headersIn) throws Exception {
		headers = headersIn;
		setHeaderControls();
	}

	/**
	 * Return the currently-defined column headers for this ListView.
	 *
	 * @return	a List<String> of the names of the headers on this ListView.
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * Add a header to Voodoo's ListView definition. The added header goes at
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
	 * Remove a header from Voodoo's ListView definition. Indices of the
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
	 * @return	the hook string for the column header corresponding to the specified header name.
	 */
	String getHeaderHook(String header) {
		return (".orderBy" + header);
	}

	/**
	 * Return the module title of the current ListView.
	 *
	 * You must be on the desired ListView.
	 *
	 * @return a String containing the ListView's module title
	 * @throws Exception
	 */
	public String getModuleTitle() throws Exception	{
		return(getControl("moduleTitle").getText());
	}

	/**
	 * Asserts that the module title of the current ListView contains the
	 * provided string (for purposes of ensuring the current page is the
	 * expected page).
	 *
	 * You must be on the desired ListView.
	 *
	 * @param toVerify
	 *            the string that should appear in the ListView's module title.
	 * @throws Exception
	 */
	public void verifyModuleTitle(String toVerify) throws Exception {
		getControl("moduleTitle").assertEquals(toVerify, true);
	}

	/**
	 * Displays or shows the right-hand sidebar.
	 *
	 * You must be on the ListView. Leaves you on the ListView with the sidebar
	 * in the opposite open/closed state.
	 *
	 * @throws Exception
	 */
	public void toggleSidebar() throws Exception {
		getControl("toggleSidebar").click();
		VoodooUtils.pause(500); // TODO: VOOD-743
	}

	/**
	 * Sorts the list view by the specified column in either ascending or
	 * descending order. You must already be on the ListView you're sorting.
	 * This method leaves you on the ListView, now sorted by the specified
	 * column.
	 *
	 * @param header
	 *            the SugarCRM internal name for the field you want to sort by.
	 * @param ascending
	 *            true for ascending, false for descending.
	 * @throws com.sugarcrm.sugar.SortFailedException
	 *             if the column could not be sorted as requested.
	 */
	public void sortBy(String header, boolean ascending) throws Exception {
		VoodooControl columnHeader = getControl(header);
		// First time we navigate to a Module and we haven't sorted a column
		// we need to click on that column to trigger the class to change and have either
		// _asc or _desc, i.e. sorting_asc and sorting_desc
		columnHeader.click(); // hack hack hack
		VoodooUtils.pause(1000);
		sugar().alerts.waitForLoadingExpiration();
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
				columnHeader.click();
				sugar().alerts.waitForLoadingExpiration();
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
	/**
	 * Retrieve a reference to the detail mode version of a field on the list view.
	 * 
	 * @param rowNum 
	 *              1-based index of row to get field in 
	 * @param fieldName
	 *                  VoodooGrimoire name for the desired control.
	 * @return 
	 *        a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getDetailField(int rowNum, String fieldName) throws Exception {
		return ((StandardModule)parentModule).getField(fieldName).getListViewDetailControl(rowNum);
	}

	/**
	 * Verifies that the cell specified by the field name and row number
	 * equals the string value.
	 *
	 * @param row
	 *            the 1-based number of the row you want to verify
	 * @param field
	 *            VoodooGrimoire name for the field you want to verify (i.e. As it is in ProcessModuleFields.csv)
	 * @param value
	 *            the string value you want to verify is contained within the
	 *            field.
	 * @throws Exception
	 */
	public void verifyField(int row, String field, String value) throws Exception {
		getDetailField(row, field).assertEquals(value, true);
	}

	/**
	 * Verifies that the cell specified by the field name and row number
	 * contains the string value.
	 *
	 * @param row
	 *            the 1-based number of the row you want to verify
	 * @param field
	 *            VoodooGrimoire name for the field you want to verify (i.e. As it is in ProcessModuleFields.csv)
	 * @param value
	 *            the string value you want to verify is contained within the
	 *            field.
	 * @throws Exception
	 */
	public void verifyFieldContains(int row, String field, String value) throws Exception {
		getDetailField(row, field).assertContains(value, true);
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void toggleSelectAll() throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void checkRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void uncheckRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void toggleRecordCheckbox(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void toggleFavorite(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void clickRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Processes Listviews do not contain the functionality to perform this operation!
	 * @throws Exception
	 */
	public void deleteRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	/**
	 * Inline edit the record on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with specified row in inline edit mode.
	 *
	 * TODO: Add support for updating the inline edit fields and clicking save
	 * or cancel.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void editRecord(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("edit%02d", rowNum)).click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Opens the action dropdown on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the action dropdown for the specified row open.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void openRowActionDropdown(int rowNum) throws Exception {
		getControl(String.format("dropdown%02d", rowNum)).click();
		VoodooUtils.pause(500);
	}

	/**
	 * Click History in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, Process Management ListView to use.<br>
	 * When used, you will have a summary of the history concerning the desired process.<br>
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void showHistory(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("history%02d", rowNum)).click();
	}

	/**
	 * Click Show Notes in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, Process Management ListView to use.<br>
	 * When used, you will have the Process Notes view displayed.<br>
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void showNotes(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("showNotes%02d", rowNum)).click();
	}

	/**
	 * Click Disable in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, Process Management ListView to use.<br>
	 * When used, you will have the Process Notes view displayed.<br>
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void disable(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("disable%02d", rowNum)).click();
	}

	/**
	 * Click Disable in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, Process Management ListView to use.<br>
	 * When used, you will be prompted to confirm/cancel the Cancel Action.<br>
	 * After use, you will be left on the Process Management ListView with the desired Process status set to cancelled.
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void cancel(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("cancel%02d", rowNum)).click();
	}

	/**
	 * Click Reassign in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, Process Management ListView to use.<br>
	 * When used, you will be taken to the Reassign Process view.<br>
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void reassign(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("reassign%02d", rowNum)).click();
	}

	/**
	 * Click Show Process in a row on this ListView.
	 * <p>
	 * Must be in the Processes Module, My Processess ListView to use.<br>
	 * When used, you will be taken to the Process Activity view to Approve/Reject/Edit the related record.<br>
	 *
	 * @param rowNum Int index of the row desired
	 * @throws Exception
	 */
	public void showProcess(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("showProcess%02d", rowNum)).click();
		sugar().alerts.waitForLoadingExpiration();
	}
}