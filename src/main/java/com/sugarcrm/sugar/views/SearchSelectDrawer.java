package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;
import com.sugarcrm.sugar.records.Record;

/**
 * Models the Search and Select View for standard SugarCRM modules.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class SearchSelectDrawer extends View {
	RecordsModule identityModule;

	/**
	 * Constructor of this class passes a basic general css hook string for its view to its super.
	 * <p>
	 * @throws Exception
	 */
	public SearchSelectDrawer() throws Exception {
		super("div", "css", ".drawer.active");
	}

	/**
	 * Set the module of this Search and Select Drawer.
	 * <p>
	 * @param identityModule RecordsModule of this search and select drawer.
	 * @throws Exception
	 */
	public void setModule(RecordsModule identityModule) throws Exception {
		this.identityModule = identityModule;
		setHookString(".layout_" + identityModule.moduleNamePlural + ".drawer.active");
		setControls();
	}

	/**
	 * Set controls using this Search and Select Drawers hook string.
	 * <p>
	 * @throws Exception
	 */
	public void setControls() throws Exception {
		// Common elements
		addControl("moduleTitle", "span", "css", getHookString() + " .fld_title");
		addControl("count", "span", "css", getHookString() + " .fld_collection-count");
		
		// Common controls
		addControl("cancel", "a", "css", getHookString() + " span[data-voodoo-name='close'] a");
		addControl("create", "a", "css", getHookString() + " span[data-voodoo-name='create_button'] a");
		addControl("link", "a", "css", getHookString() + " span[data-voodoo-name='link_button'] a");
		addControl("toggleSidebar", "button", "css", getHookString() + " span[data-voodoo-name='sidebar_toggle'] button");

		// Common search
		addControl("search", "input", "css", getHookString() + " [data-voodoo-name='filter-quicksearch'] input");

		for (int i = 1; i <= 20; i++) {
			// Build internal Voodoo names for each control in a row.
			String selectInput = String.format("selectInput%02d", i);
			String preview = String.format("preview%02d", i);

			// Build a string prefix that represents the current row in each
			// control.
			String currentRow = getHookString() + " div[data-voodoo-type='view'] tbody tr:nth-of-type(" + i + ")";

			// Add Voodoo controls for all controls in the row.
			addControl(selectInput, "input", "css", currentRow + " input");
			addControl(preview, "a", "css", currentRow + " a[data-event='list:preview:fire']");
		}
	}

	/**
	 * Cancel this Search and Select view (SSV).
	 * <p>
	 * Will return you to the previous view from which you came.<br>
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancel").click();
	}
	
	/**
	 * Click the create button on this Search and Select view (SSV).
	 * Opens the primary button's drop down list to expose alternate actions if needed.
	 * <p>
	 * Will take you to the create screen for this SSVs' module.<br>
	 * 
	 * @throws Exception
	 */
	public void create() throws Exception {
		getControl("create").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the Link button on this Search and Select view (SSV).
	 * <p>
	 * Will return you to the previous view from which you came and link the
	 * records you marked in this SSV to the parentModule.<br>
	 * 
	 * @throws Exception
	 */
	public void link() throws Exception {
		getControl("link").click();
		sugar().alerts.getAlert().waitForElement();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Toggle the Sidebar for this Search and Select view (SSV).
	 * <p>
	 * Leaves you on this view with the sidebar Open/Closed.<br>
	 * 
	 * @throws Exception
	 */
	public void toggleSidebar() throws Exception {
		getControl("toggleSidebar").click();
	}

	/**
	 * Search for a specific record in this Search and Select View (SSV).
	 * <p>
	 * Must be on the SSV to use.<br>
	 * When used the list of records are updated to display matching results.<br>
	 * 
	 * @param searchString String of record to search for.
	 * @throws Exception
	 */
	public void search(String searchString) throws Exception {
		getControl("search").set(searchString);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Preview a record in this Search and Select View (SSV).
	 * <p>
	 * Must be on the SSV to use.<br>
	 * When used, a preview pane will display the information for the desired record.<br>
	 * Leaves you on the SSV with the preview pane open displaying information.<br>
	 * 
	 * @param rowNum Int index of the row to click preview.
	 * @throws Exception
	 */
	public void preview(int rowNum) throws Exception {
		sugar().previewPane.setModule(identityModule);
		getControl(String.format("preview%02d", rowNum)).click();
	}

	/**
	 * Preview a record in this Search and Select View (SSV).
	 * <p>
	 * Must be on the SSV to use.<br>
	 * When used, a preview pane will display the information for the desired record.<br>
	 * Leaves you on the SSV with the preview pane open displaying information.
	 * <p>
	 * NOTE: This method will search first before clicking preview on the first returned result.
	 * 
	 * @param record Record to preview.
	 * @throws Exception
	 */
	public void preview(Record record) throws Exception {
		search(record.getRecordIdentifier());
		preview(1);
	}

	/**
	 * Select a record in this Search and Select View (SSV).
	 * <p>
	 * Must be on the SSV to use.<br>
	 * When used, the desired record will be selected (checkbox checked, radio button clicked).
	 * <p>
	 * NOTE: If this method is used in a relate field (non-subpanel), then this method will return you
	 * to the previous view as only a single record can be related this way, otherwise this method will leave
	 * you on the SSV with with the desired record selected.
	 * 
	 * @param record Record to select.
	 * @throws Exception
	 */
	public void selectRecord(Record record) throws Exception {
		new VoodooControl("input", "xpath", "//div[@class='layout_" + identityModule.moduleNamePlural
				+ " drawer active']//div[text()[contains(., '" + record.getRecordIdentifier() + "')]]").click();
	}

	/**
	 * Select a record in this Search and Select View (SSV).
	 * <p>
	 * Must be on the SSV to use.<br>
	 * When used, the desired record will be selected (checkbox checked, radio button clicked).
	 * <p>
	 * NOTE: If this method is used in a relate field (non-subpanel), then this method will return you
	 * to the previous view as only a single record can be related this way, otherwise this method will leave
	 * you on the SSV with with the desired record selected.
	 * 
	 * @param	rowNum	Int index of the record to select.
	 * @throws Exception
	 */
	public void selectRecord(int rowNum) throws Exception {
		getControl(String.format("selectInput%02d", rowNum)).click();
	}

	/**
	 * Returns a count of rows
	 * <p>
	 * You must be on Search and Select view to use.<br>
	 * Leaves you on SSV view
	 * 
	 * @return an Integer containing row count
	 * @throws Exception
	 */
	public int countRows() throws Exception{
		// TODO: VOOD-915
		VoodooControl row = new VoodooControl("div", "id", "content");
		return Integer.parseInt(row.waitForElement().executeJavascript("return jQuery('.layout_" + identityModule.moduleNamePlural + " tr.single').length;").toString());
	}
}