package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.*;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;

import org.openqa.selenium.TimeoutException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Models the ListView for standard SugarCRM modules.
 *
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class ListView extends View implements IListView {
	/**
	 * The SugarCRM internal names for the fields that appear as ListView column
	 * headers, in the order in which they appear. This list should be updated
	 * any time the ListView is altered (e.g. by Studio updates).
	 */
	private List<String> headers = new ArrayList<String>();
	public ActivityStream activityStream;
	public FilterCreateView filterCreate ;

	public ListView(Module parentModule) throws Exception {
		super(parentModule);
		activityStream = new ActivityStream();
		filterCreate = new FilterCreateView(parentModule);
		// Do not create controls in this class that begin with "header"; that
		// is reserved for programmatically-generated controls for accessing
		// the column headers.

		// Top row
		addControl("moduleTitle", "span", "css", ".fld_title.list-headerpane");
		addControl("count", "span", "css", ".fld_collection-count.list-headerpane");
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
		addControl("searchClear", "i", "css", ".filter-view.search.layout_"+parentModule.moduleNamePlural+" .fa.fa-times.add-on");
		addControl("searchFilterCurrent", "span", "css", ".choice-filter.choice-filter-clickable span");

		// Column header row
		addControl("selectAllCheckbox", "input", "css", ".checkall input");
		addControl("actionDropdown", "input", "css", "div[data-voodoo-name='recordlist'] a.dropdown-toggle");
		addControl("massUpdateButton", "a", "css", ".fld_massupdate_button a");
		addControl("deleteButton", "a", "css", ".fld_massdelete_button a");
		addControl("exportButton", "a", "css", ".fld_export_button a");
		addControl("moreColumn", "div", "css", "th.morecol div");

		// Misc alerts, etc.
		addControl("selectedRecordsAlert", "div", "css", "[data-voodoo-name='recordlist'] [data-target='alert'] span");
		addControl("selectAllRecordsLink", "div", "css", "[data-voodoo-name='recordlist'] [data-target='alert'] span button");
		addControl("clearSelectionsLink", "div", "css", "[data-voodoo-name='recordlist'] [data-target='alert'] span button");
		addControl("confirmDelete", "a", "css", ".alert-btn-confirm");
		addControl("showListView","button","css","button[data-view='list']");
		addControl("activityStream","button","css","button[data-view='activitystream']");
		addControl("cancelDelete", "a", "css", ".alert-btn-cancel");
		addControl("emptyListViewMsg","div","css","[data-voodoo-name='list-bottom'] .block-footer");
		addControl("horizontalScrollBar", "div", "css", "div.flex-list-view-content");

		for (int i = 1; i <= 99; i++) {
			// Build internal Voodoo names for each control in a row.
			String checkbox = String.format("checkbox%02d", i);
			String favoriteStar = String.format("favoriteStar%02d", i);
			String link = String.format("link%02d", i);
			String preview = String.format("preview%02d", i);
			String dropdown = String.format("dropdown%02d", i);
			String edit = String.format("edit%02d", i);
			String unfollow = String.format("unfollow%02d", i);
			String follow = String.format("follow%02d", i);
			String delete = String.format("delete%02d", i);
			String save = String.format("save%02d", i);
			String cancel = String.format("cancel%02d", i);

			// Build a string prefix that represents the current row in each
			// control.
			String currentRow = "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(" + i + ")";

			// Add Voodoo controls for all controls in the row.
			addControl(checkbox, "input", "css", currentRow + " input");
			addControl(favoriteStar, "i", "css", currentRow + " [data-voodoo-name='my_favorite'] button");
			addControl(link, "a", "css", currentRow + " span[class*='name'] a");
			addControl(preview, "a", "css", currentRow + " a[data-event='list:preview:fire']");
			addControl(dropdown, "a", "css", currentRow + " a[data-toggle='dropdown']");
			addControl(edit, "a", "css", currentRow + " span[data-voodoo-name='edit_button'] a");
			addControl(unfollow, "a", "css", currentRow + " span.fld_follow_button a");
			addControl(follow, "a", "css", currentRow + " span.fld_follow_button a");
			addControl(delete, "a", "css", currentRow + " ul.dropdown-menu a[name='delete_button']"); // TODO:
			addControl(save, "a", "css", currentRow + " span.fld_inline-save.edit a");
			addControl(cancel, "a", "css", currentRow + " span.fld_inline-cancel.edit a");
		}

		// Static links
		addControl("showMore", "a", "css", ".main-content div[data-voodoo-name='list-bottom'] button[data-action='show-more']");

		// TODO: Activity Stream toggle & controls (possibly a separate view).
		// TODO: Preview panel (possibly a separate view).
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
	 * Click the create button. You must already be on the ListView. Leaves you
	 * on the open CreateDrawer for the current module.
	 *
	 * @throws Exception
	 */
	public void create() throws Exception {
		getControl("createButton").click();
		VoodooUtils.waitForReady();
		((StandardModule)parentModule).createDrawer.getControl("cancelButton").waitForVisible(30000);
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
	 * Toggle column header list view by the specified column in either active or
	 * inactive state. You must already be on the ListView you're doing.
	 * This method leaves you on the ListView, now take action by the specified
	 * column which you requested.
	 *
	 * @param headerName
	 *            String as SugarCRM internal name for the field which is defined to respective modules
	 * @throws Exception
	 */
	public void toggleHeaderColumn(String headerName) throws Exception{
		ArrayList<String> headerNames = new ArrayList<String>(Arrays.asList(headerName));
		toggleHeaderColumns(headerNames);
	}

	/**
	 * Toggle column headers list view by the specified column in either active or
	 * inactive state. You must already be on the ListView you're doing.
	 * This method leaves you on the ListView, now take action by the specified
	 * columns which you requested.
	 *
	 * @param headerNames
	 *            array list of String as SugarCRM internal name for the field which is defined to respective modules
	 * @throws Exception
	 */
	public void toggleHeaderColumns(ArrayList<String> headerNames) throws Exception {
		getControl("moreColumn").click();
		for(String headerName: headerNames)
			new VoodooControl("button", "css", "button[data-field-toggle='"+headerName+"']").click();
		getControl("moreColumn").click(); // to close settings dropdown
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
				columnHeader.scrollIntoViewIfNeeded(false);
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
	 * Verifies that the cell specified by the field name and row number
	 * contains the string value.
	 *
	 * @param row
	 *            the 1-based number of the row you want to verify
	 * @param field
	 *            VoodooGrimoire name for the field you want to verify (i.e. As it is in ModuleField.csv's)
	 * @param value
	 *            the string value you want to verify is contained within the
	 *            field.
	 * @throws Exception
	 */
	public void verifyField(int row, String field, String value) throws Exception {
		((StandardModule) parentModule).getField(field).getListViewDetailControl(row).assertContains(value, true);
	}

	/**
	 * Clicks the select all checkbox to select or deselect all records on the
	 * current page.
	 *
	 * @throws Exception
	 */
	public void toggleSelectAll() throws Exception {
		getControl("selectAllCheckbox").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Opens the action dropdown next to the massall checkbox. You must already
	 * be on the ListView of the desired module and some records must already be
	 * checked. Leaves you on the ListView with the action dropdown open.
	 *
	 * @throws Exception
	 */
	public void openActionDropdown() throws Exception {
		getControl("actionDropdown").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the Mass Update action in the actions dropdown. You must already
	 * be on the ListView with some records checked and the actions dropdown
	 * open. Leaves you on the ListView with records checked and the Mass Update
	 * panel open.
	 *
	 * @throws Exception
	 */
	public void massUpdate() throws Exception {
		getControl("massUpdateButton").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the delete action in the actions dropdown. You must already be on
	 * the ListView with some records checked and the actions dropdown open.
	 * Leaves you on the ListView with records checked and the delete
	 * confirmation alert open.
	 *
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getControl("deleteButton").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the export action in the actions dropdown. You must already be on
	 * the ListView with some records checked and the actions dropdown open.
	 * Leaves you on the ListView and invokes the browser's Save functionality
	 * to save an exported file (result depends on browser and its settings).
	 *
	 * @throws Exception
	 */
	public void export() throws Exception {
		getControl("exportButton").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Clicks the Confirm link on the delete confirmation alert. You must
	 * already be on the ListView with some records checked and the delete
	 * confirmation alert visible. Leaves you on the ListView with the
	 * previously-checked records deleted.
	 *
	 * @throws Exception
	 */
	public void confirmDelete() throws Exception {
		getControl("confirmDelete").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Clicks the Cancel link on the delete confirmation alert. You must already
	 * be on the ListView with some records checked and the delete confirmation
	 * alert visible. Leaves you on the ListView with the records undeleted.
	 *
	 * @throws Exception
	 */
	public void cancelDelete() throws Exception {
		getControl("cancelDelete").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Clicks the filter dropdown to open it. You must already be on the desired
	 * ListView. Leaves you on the ListView with the filter dropdown open.
	 *
	 * @throws Exception
	 */
	public void openFilterDropdown() throws Exception {
		getControl("filterDropdown").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Selects the All filter from the filters dropdown. You must already be on
	 * the desired ListView with the filter dropdown open. Leaves you on the
	 * ListView with the All filter selected.
	 *
	 * @throws Exception
	 */
	public void selectFilterAll() throws Exception {
		getControl("filterAll").click();
		VoodooUtils.pause(500);
		VoodooUtils.waitForReady();
	}

	/**
	 * Selects the Assigned To Me filter from the filters dropdown. You must
	 * already be on the desired ListView with the filter dropdown open. Leaves
	 * you on the ListView with the All filter selected.
	 *
	 * @throws Exception
	 */
	public void selectFilterAssignedToMe() throws Exception {
		getControl("filterAssignedToMe").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Selects the My Favorites filter from the filters dropdown. You must
	 * already be on the desired ListView with the filter dropdown open. Leaves
	 * you on the ListView with the All filter selected.
	 *
	 * @throws Exception
	 */
	public void selectFilterMyFavorites() throws Exception {
		getControl("filterMyFavorites").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Selects the Recently Viewed filter from the filters dropdown. You must
	 * already be on the desired ListView with the filter dropdown open. Leaves
	 * you on the ListView with the All filter selected.
	 *
	 * @throws Exception
	 */
	public void selectFilterRecentlyViewed() throws Exception {
		getControl("filterRecentlyViewed").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Selects the Recently Created filter from the filters dropdown. You must
	 * already be on the desired ListView with the filter dropdown open. Leaves
	 * you on the ListView with the All filter selected.
	 *
	 * @throws Exception
	 */
	public void selectFilterRecentlyCreated() throws Exception {
		getControl("filterRecentlyCreated").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Selects the Create New action from the filters dropdown. You must already
	 * be on the desired ListView with the filter dropdown open. Leaves you on
	 * the ListView with the filter creation panel open.
	 *
	 * @throws Exception
	 */
	public void selectFilterCreateNew() throws Exception {
		getControl("filterCreateNew").click();
		sugar().alerts.waitForLoadingExpiration();
		filterCreate = new FilterCreateView(parentModule);
	}

	/**
	 * Sets the search string in the filters panel.
	 *
	 * @param toSearch
	 *            the string you wish to search for.
	 * @throws Exception
	 */
	public void setSearchString(String toSearch) throws Exception {
		getControl("searchFilter").set(toSearch);
		/*
		 * If we're too quick, we end up waitingForLoadingExperation and moving on.
		 * Here, we wait for the loading bar to appear first.
		 *
		 * However, if we're too slow, the loading bar will disappear before we call
		 * waitForVisible, causing an UnfoundElement Exception, so we catch and don't
		 * worry about not finding the loading div. Because we poll ever 2 seconds
		 * the waitForVisible will always miss the alert and is effectively a 2 second
		 * wait.
		 *
		 * TODO: VOOD-1339 The results of 1339 should allow us to catch to loading div properly
		 */
		try {
			sugar().alerts.getProcess().waitForVisible(2000);
		} catch(UnfoundElementException|TimeoutException e) {}
		sugar().alerts.waitForLoadingExpiration(30000);
	}

	/**
	 * Clear the search string in the filters panel.
	 * @throws Exception
	 */
	public void clearSearch() throws Exception {
		getControl("searchClear").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Check the checkbox on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the checkbox on the specified row checked, regardless of
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
	 * Uncheck the checkbox on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the checkbox on the specified row unchecked, regardless of
	 * its prior state.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void uncheckRecord(int rowNum) throws Exception {
		getControl(String.format("checkbox%02d", rowNum)).set("false");
	}

	/**
	 * Toggle the checkbox on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the checkbox on the specified row in the opposite of its
	 * prior state (i.e. checked if it was unchecked before or unchecked if it
	 * was checked before).
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void toggleRecordCheckbox(int rowNum) throws Exception {
		getControl(String.format("checkbox%02d", rowNum)).click();
	}

	/**
	 * Toggle the favorite star on aparticular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the favorite star on the specified row in the opposite of
	 * its prior state (i.e. on if it was off before or off if it was on
	 * before).
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void toggleFavorite(int rowNum) throws Exception {
		getControl(String.format("favoriteStar%02d", rowNum)).click();
	}

	/**
	 * Click the "select all records" link in the selected records alert row.
	 *
	 * You must be on the ListView with the select all records link visible.<br />
	 * Leaves you on the ListView with all records (not just the first page) selected.
	 *
	 * @throws Exception
	 */
	public void clickSelectAllRecordsLink() throws Exception {
		getControl("selectAllRecordsLink").click();
		getControl("selectedRecordsAlert").waitForVisible();
		// TODO: VOOD-1226 This is awful, but it seems like we need to wait for some JS to finish
		// executing and it's not obvious to me that there's an element we can wait for to
		// appear/disappear, although maybe we can wait for the alert's text to change.  Revisit
		// when Candybean has more wait conditions.
		VoodooUtils.pause(1000);
	}

	/**
	 * Click the "select all records" link in the selected records alert row.
	 *
	 * You must be on the ListView with the select all records link visible.<br />
	 * Leaves you on the ListView with all records (not just the first page) selected.
	 *
	 * @throws Exception
	 */
	public void clickClearSelectionsLink() throws Exception {
		getControl("clearSelectionsLink").click();
	}

	/**
	 * Click the record link on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * RecordView of the record on the specified row.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void clickRecord(int rowNum) throws Exception {
		getControl(String.format("link%02d", rowNum)).click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks the preview button on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView and shows a preview of the record on the specified row in the
	 * right sidebar.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void previewRecord(int rowNum) throws Exception {
		getControl(String.format("preview%02d", rowNum)).click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().previewPane.setModule(parentModule);
	}

	/**
	 * Clicks the save button on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the updated record displayed.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to save.
	 * @throws Exception
	 */
	public void saveRecord(int rowNum) throws Exception {
		getControl(String.format("save%02d", rowNum)).click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Clicks the cancel button on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with the original record displayed.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to cancel.
	 * @throws Exception
	 */
	public void cancelRecord(int rowNum) throws Exception {
		getControl(String.format("cancel%02d", rowNum)).click();
		VoodooUtils.waitForReady();
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
	 * Toggle your follow status on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView with your follow status on the specified row in the opposite of
	 * its prior state (i.e. on if it was off before or off if it was on
	 * before).
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void toggleFollow(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("follow%02d", rowNum)).click();
	}

	/**
	 * Click the delete link on a particular row of the current ListView.
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView and displays the delete confirmation prompt.
	 *
	 * @param rowNum
	 *            one-based number of the row you want to access.
	 * @throws Exception
	 */
	public void deleteRecord(int rowNum) throws Exception {
		openRowActionDropdown(rowNum);
		getControl(String.format("delete%02d", rowNum)).click();
		VoodooUtils.pause(500);
	}

	/**
	 * Click the Activity Stream button to change the view to activity stream.
	 *
	 * You must be on the ListView view to use this method. Leaves you on the
	 * Activity Stream view.
	 *
	 * If already on the Activity Stream view, use of this method will cause an
	 * exception.
	 *
	 * @throws Exception
	 */
	public void showActivityStream() throws Exception {
		getControl("activityStream").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Click the List View button to change the view to the list view.
	 *
	 * You must be on the Activity Stream view to use this method. Leaves you on
	 * the ListView view.
	 *
	 * If already on the ListView view, use of this method will cause an
	 * exception.
	 *
	 * @throws Exception
	 */
	public void showListView() throws Exception {
		getControl("showListView").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Retrieve a reference to the detail mode version of a field on the list view.
	 *
	 * @param rowNum 1-based index of row to get field in
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 * @throws Exception
	 */
	public VoodooControl getDetailField(int rowNum, String fieldName) throws Exception {
		return ((StandardModule)parentModule).getField(fieldName).getListViewDetailControl(rowNum);
	}

	/**
	 * Sets fields of a record on the listView per row
	 *
	 * Must be on the List View in the edit mode for a records row.
	 * Once complete this method will require more action, save,
	 * cancel or do nothing.
	 *
	 * @param rowNum 1-based index of row to set fields in
	 * @param data FieldSet of data to use for setting
	 * @throws Exception
	 */
	public void setEditFields(int rowNum, FieldSet data) throws Exception {
		for(String field : data.keySet()) {
			if(field != null && data.get(field) != null) {
				((StandardModule)parentModule).getField(field).getListViewEditControl(rowNum).scrollIntoViewIfNeeded(false);
				((StandardModule)parentModule).getField(field).getListViewEditControl(rowNum).set(data.get(field));
			}
		}
	}

	/**
	 * Retrieve a reference to the edit mode version of a field on the list view.
	 *
	 *  @param rowNum 1-based index of row to get field in
	 * @param fieldName - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getEditField(int rowNum, String fieldName) throws Exception {
		return ((StandardModule)parentModule).getField(fieldName).getListViewEditControl(rowNum);
	}

	/**
	 * Update a record by row in the List View.
	 *
	 * Must be on the List View of a module.
	 * Once complete this method will save the changes and return
	 * user back to list view detail mode.
	 *
	 * @param rowNum 1-based index of row to edit
	 * @param data FieldSet of data to use for editing
	 * @throws Exception
	 */
	public void updateRecord(int rowNum, FieldSet data) throws Exception {
		editRecord(rowNum);
		setEditFields(rowNum, data);
		saveRecord(rowNum);
	}

	/**
	 * Clicks on the Show More link.
	 * <p>
	 * When used, another X number of records will be displayed on the list view.<br>
	 * Leaves you on the list view with more records showing.
	 * <p>
	 * Must be on the list view to use. <br>
	 * When no additional record exist to show, the show more link is not present.
	 *
	 * @throws Exception
	 */
	public void showMore() throws Exception {
		if(getControl("showMore").queryVisible()) {
			getControl("showMore").click();
			sugar().alerts.waitForLoadingExpiration();
		}
	}

	/**
	 * Asserts that there's no record
	 *
	 * You must be on the ListView to use this method. Leaves you on the
	 * ListView.
	 *
	 * @throws Exception
	 */
	public void assertIsEmpty() throws Exception {
		getControl(String.format("checkbox%02d", 1)).assertExists(false);
	}

	/**
	 * Returns a count of rows
	 * <p>
	 * You must be on Listview.<br>
	 * Leaves you on Listview
	 *
	 * @return an Integer containing row count
	 * @throws Exception
	 */
	public int countRows() throws Exception{
		// TODO: VOOD-915
		VoodooControl row = new VoodooControl("div", "id", "content");
		return Integer.parseInt(row.waitForElement().executeJavascript("return jQuery('.flex-list-view-content tr.single').length;").toString());
	}

	/**
	 * Create a new Filter for this ListView.
	 * <p>
	 * Must be on the module listView to use.<br>
	 * When used, you will be left on this listView with the results displayed being those that match the filter.
	 *
	 * @param filterData FieldSet of data to use to create filter for this listView
	 * @throws Exception
	 */
	public void createFilter(FieldSet filterData) throws Exception {
		openFilterDropdown();
		selectFilterCreateNew();
		filterCreate.create(filterData);
	}

	/**
	 * Select a custom filter.
	 * <p>
	 * Must be on the listview to use.<br>
	 * Must have a custom filter to choose.<br>
	 * When used, you will remain on the module listview with the results matching the filter data.
	 *
	 * @param filter String name of the custom filter to choose.
	 * @throws Exception
	 */
	public void selectFilter(String filter) throws Exception {
		new VoodooSelect("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type").set(filter);
		VoodooUtils.waitForReady();
	}
}