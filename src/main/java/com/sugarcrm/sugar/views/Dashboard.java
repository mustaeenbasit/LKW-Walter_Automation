package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.ControlNotDefinedException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.AppModel;

/**
 * Models the Dashboard (Home) screen in SugarCRM.
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class Dashboard extends View {
	protected static Dashboard dashboard;

	public static Dashboard getInstance() throws Exception {
		if (dashboard == null)
			dashboard = new Dashboard();
		return dashboard;
	}

	public Dashboard() throws Exception {
		addControl("headerPane", "div", "css", ".headerpane");
		addControl("firstDashlet", "div", "css", ".dashlet-content");

		addControl("create", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_add_button a[name='add_button']");
		addControl("delete", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_delete_button a[name='delete_button']");
		addControl("actionsDropdown", "a", "css", ".preview-headerbar .btn-toolbar a[data-toggle*='dropdown']");
		addControl("edit", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .dropdown-menu a[name='edit_button']");
		addControl("minAll", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .dropdown-menu a[name='collapse_button']");
		addControl("maxAll", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .dropdown-menu a[name='expand_button']");

		addControl("dashboard", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_name a");
		addControl("dashboardTitle", "span", "css", "div[data-voodoo-name*='dashboard-headerpane'] span[data-voodoo-name='name']");
		
		addControl("title", "input", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_name input");
		addControl("save", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .btn-toolbar .fld_create_button a");
		addControl("cancel", "a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .btn-toolbar .fld_create_cancel_button a");

		// Dashboard columns per row Page
		addControl("oneColumnPage", "button", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_layout button[data-value='1']");
		addControl("twoColumnPage", "button", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_layout button[data-value='2']");
		addControl("threeColumnPage", "button", "css", "div[data-voodoo-name*='dashboard-headerpane'] .fld_layout button[data-value='3']");

		// Dashboard Row controls
		addControl("addRow", "div", "css", ".well-invisible div[data-voodoo-type='layout'] .add-row");
		addControl("addRowOneColumn", "a", "css", ".well-invisible div[data-voodoo-type='layout'] .add-row .options a[data-value='1']");
		addControl("addRowTwoColumn", "a", "css", ".well-invisible div[data-voodoo-type='layout'] .add-row .options a[data-value='2']");
		addControl("addRowThreeColumn", "a", "css", ".well-invisible div[data-voodoo-type='layout'] .add-row .options a[data-value='3']");
	}

	/**
	 * Click on the save button to save a dashboard layout.
	 * 
	 * Must be in the dashboard create page. When done this will bring the user
	 * back to the current dashboard view.
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		getControl("save").click();
		sugar().alerts.getSuccess().closeAlert();
		getControl("dashboardTitle").waitForVisible();
	}

	/**
	 * Click on the cancel button in the dashboard layout.
	 * 
	 * Must be in the dashboard create/edit page. When done this will undo any
	 * changes and bring the user back to the current dashboard view.
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancel").click();
		getControl("dashboardTitle").waitForVisible();
		VoodooUtils.pause(1000);
	}

	/**
	 * Click on the delete button in the dashboard edit page.
	 * 
	 * Must be on the dashboard edit page.
	 * When done the user will be asked to confirm/cancel the action.
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getControl("delete").click();
		VoodooUtils.pause(2000);
	}
	
	/**
	 * Set the number of columns per page layout for a new dasboard.
	 * 
	 * Must be on the dashboard create page.
	 * 
	 * Once chosen, the user can change the layout by calling the action again.
	 * Leaves you on the dashboard create page with the dashboard layout displaying the
	 * specified number of columns.
	 * 
	 * @param column Number of columns to display per page
	 * @throws Exception
	 */
	public void setColumnsPerPage(int column) throws Exception {
		switch (column) {
			case 1: {
				getControl("oneColumnPage").click();
				break;
			}
			case 2: {
				getControl("twoColumnPage").click();
				break;
			}
			case 3: {
				getControl("threeColumnPage").click();
				break;
			}
			default: {
				throw new ControlNotDefinedException("Button for " + column + " dashboard columns.");				
			}
		}
		sugar().alerts.getAlert().confirmAlert();
		VoodooUtils.pause(1000);
	}

	/**
	 * Add an additional row to the dasboard.
	 * 
	 * Must be on the dasboard create/edit page.
	 * Leaves you on the dashboard create/edit page with an additional row
	 * for more dashlets.
	 * 
	 * @throws Exception
	 */
	public void addRow() throws Exception {
		getControl("addRow").click();
		VoodooUtils.pause(500);
	}

	/**
	 * Set the number of columns per row of a dashboard.
	 * 
	 * Must be on the dashboard create/edit page.
	 * Leaves you on the create/edit page with the dashboard layout set to display
	 * the specified number of columns per row.
	 * 
	 * This is dependent on the number of columns per page layout.
	 * If you have 1 column per page, you can have more than 1 column per row
	 * If you have more than 1 column you can only choose 1 column per row.
	 * 
	 * 
	 * @param column
	 * @throws Exception
	 */
	public void setColumnsPerRow(int column) throws Exception {
		switch (column) {
			case 1: {
				getControl("addRowOneColumn").click();
				break;
			}
			case 2: {
				getControl("addRowTwoColumn").click();
				break;
			}
			case 3: {
				getControl("addRowThreeColumn").click();
				break;
			}
			default: {
				throw new ControlNotDefinedException("Button for " + column + " dashboard columns per row.");				
			}
		}
		VoodooUtils.pause(1000);
	}
	
	/**
	 * Click Add Dashlet in a specific row/column
	 * 
	 * Must be on the dasboard create/edit page.
	 * Must have added a row and chosen how many columns to use per row.
	 * Leaves you on the Dashlet selection screen.
	 * 
	 * @param row Int of the row
	 * @param column Int of the column
	 * @throws Exception
	 */
	public void addDashlet(int row, int column) throws Exception {
		// Build css path based on params		
		String currentRow = ".row-fluid.sortable:nth-of-type(" + row + ") ";
		String currentCol = ".dashlet-container:nth-of-type(" + column + ")";
		
		new VoodooControl("div", "css", currentRow + currentCol + " .add-dashlet").click();
		//TODO: VOOD-1291 Add support for waiting for a dropdown drawer to finish rendering
		VoodooUtils.pause(2000);
		new VoodooControl("a", "css", "#drawers a[name='cancel_button']").waitForVisible();

	}
	
	/**
	 * Click on the action dropdown to expose more actions for a dashboard.
	 * 
	 * @throws Exception
	 */
	public void openActionMenu() throws Exception {
		getControl("actionsDropdown").click();
		VoodooUtils.pause(1000);
	}
	
	/**
	 * Edit a dashboard.
	 * 
	 * Must be on the desired dashboard to be edited.
	 * Once used, this will change the dashboard to be editable.
	 * 
	 * @throws Exception
	 */
	public void edit() throws Exception {
		openActionMenu();
		getControl("edit").click();
		VoodooUtils.pause(1000);
	}
	
	/**
	 * Minimize dashboards.
	 * 
	 * Must be on a Module Listview to use.
	 * When used, all dashboards in the RHS will collapse closed.
	 * 
	 * @throws Exception
	 */
	public void minimizeDashlets() throws Exception {
		getControl("minAll").click();
		VoodooUtils.pause(500);
	}
	
	/**
	 *  Maximize dashboards.
	 *  
	 *  Must be on a Module Listview to use.
	 *  When used, all dashboards in the RHS will expand open.
	 * @throws Exception
	 */
	public void maximizeDashlets() throws Exception {
		getControl("maxAll").click();
		VoodooUtils.pause(500);
	}
	
	/**
	 * Create a new dashboard.
	 * 
	 * @throws Exception
	 */
	public void clickCreate() throws Exception {
		getControl("create").click();
		VoodooUtils.pause(1000);
	}
	
	/**
	 * Choose which dashboard to display in module listview
	 * 
	 * Must be in a module listview to use.
	 * Must have more than 1 dashboard.
	 * 
	 * @param row Int representing the 1-based index of the dashboard to switch to
	 * @throws Exception
	 */
	public void chooseDashboard(int row) throws Exception {
		clickDashboardName();
		new VoodooControl("a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .dropdown-menu li:nth-of-type(" + row + ") a").click();
		VoodooUtils.waitForReady();
	}

	public void chooseDashboard(String dashboard) throws Exception {
		clickDashboardName();
		new VoodooControl("a", "xpath", "//div[@class='headerpane']//ul[@class='dropdown-menu']//a[.='" + dashboard + "']").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Exposes more dashboards to choose from.
	 * 
	 * Must be in a module listview to use.
	 * 
	 * @throws Exception
	 */
	public void clickDashboardName() throws Exception {
		getControl("dashboard").click();
		VoodooUtils.waitForReady();
	}
} // Dashboard
