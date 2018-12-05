package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

/**
 * Models the ListView for Manufacturers. This class overrides the methods of base class
 * @author Mohd. Shariq <mshariq@sugarcrm.com>
 *
 */

public class ManufacturersListView extends BWCListView {
	/**
	 * The SugarCRM internal names for the fields that appear as ListView
	 * column headers, in the order in which they appear.  This list should be
	 * updated any time the ListView is altered (e.g. by Studio updates).
	 */
	public ManufacturersListView(RecordsModule parentModule) throws Exception {
		super(parentModule);

		int linkColumn = 1; // default Column of Listview which holds the Record Identifier string

		// Top row
		addControl("moduleTitle", "div", "css", ".moduleTitle");

		// create button control
		addControl("createButton", "a", "id", "btn_create");

		// pagination controls
		addControl("startButton", "button", "css" ,".list.view  tr:nth-child(1) tbody [name='listViewStartButton']");
		addControl("endButton", "button", "css" ,".list.view  tr:nth-child(1) tbody [name='listViewEndButton']");
		addControl("prevButton", "button", "css" ,".list.view  tr:nth-child(1) tbody [name='listViewPrevButton']");
		addControl("nextButton", "button", "css" ,".list.view  tr:nth-child(1) tbody [name='listViewNextButton']");
		addControl("pageNumbers", "span", "css" ,".list.view  tr:nth-child(1) tbody .pageNumbers");

		for (int i = 1; i <= 99; i++) {
			// Build internal Voodoo names for each control in a row.
			String delete = String.format("delete%02d", i);
			String link = String.format("link%02d", i);
			// Build a string prefix that represents the current row in each
			// control.
			String currentRow = ".list.view tbody tr:nth-of-type(" + (i+2) + ")";

			// Add Voodoo controls for all controls in the row.
			addControl(delete, "a", "css", currentRow + " .single a");
			addControl(link, "a", "css", currentRow + " td:nth-of-type(" + linkColumn + ") a");
		}
	}

	/**
	 * Click the create button on this list view.
	 *
	 * Leaves you on the record view to create a record.
	 *
	 * @throws Exception
	 */
	public void clickCreate() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("createButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Click the delete link on a particular row of the current ListView.
	 *
	 * You must be on the BWCListView to use this method.
	 * Leaves you on the BWCListView and displays the delete confirmation prompt.
	 *
	 * @param rowNum one-based number of the row you want to access.
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(int rowNum) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("delete%02d", rowNum)).click();
	}

	@Override
	public void toggleSelectAll() throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	@Override
	public void checkRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	@Override
	public void uncheckRecord(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	@Override
	public void toggleRecordCheckbox(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
	}

	@Override
	public void toggleFavorite(int rowNum) throws Exception {
		throw new Exception("This operation is not possible in this List View");
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
	@Override
	public void editRecord(int rowNum) throws Exception {
		clickRecord(rowNum);
	}

	/**
	 * Get the desired row.
	 * @param rowIndex 1 based index representing the desired row on this list view
	 * @return VoodooControl representing the desired row.
	 * @throws Exception
     */
	public VoodooControl getRow(int rowIndex) throws Exception {
		return new VoodooControl("tr", "css", ".list.view tbody tr:nth-of-type(" + (rowIndex + 2) + ")");
	}

	/**
	 * Get the desired field
	 * @param row 1 based index of the desired row.
	 * @param fieldName String SugarField name of the desired Field
	 * @return VoodooControl representing the desired field on the desired row
	 * @throws Exception
     */
	public VoodooControl getField(int row, String fieldName) throws Exception {
		return new VoodooControl(sugar().manufacturers.getField(fieldName).listViewDetailControl.getTag(),
									sugar().manufacturers.getField(fieldName).listViewDetailControl.getStrategyName(),
									getRow(row).getHookString() + " " + sugar().manufacturers.getField(fieldName).listViewDetailControl.getHookString());
	}
}