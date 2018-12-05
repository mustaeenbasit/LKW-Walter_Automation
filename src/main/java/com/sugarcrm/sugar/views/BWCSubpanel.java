package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;
import com.sugarcrm.sugar.records.Record;

/**
 * Models a Subpanel for backwards-compatibility SugarCRM modules.
 * <p>
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class BWCSubpanel extends Subpanel {
	public BWCSubpanel(RecordsModule identityModule) throws Exception {
		super(identityModule, "li", "css", "#whole_subpanel_" + identityModule.bwcSubpanelName.toLowerCase());
		isBWC = true;
		
		addControl("subpanelActionsMenu", "span", "css", getHookString() + " .pagination .sugar_action_button .ab");
		addControl("composeEmail", "a", "css", getHookString() + " #Activities_composeemail_button");
		addControl("subpanelTitle", "span", "css", getHookString() + " h3 > span");
		// For teams module
		addControl("teamMembership", "a", "css", getHookString() + " #team_memberships_select_button");
		
		for(int i = 1; i <= 20; i++) {
			String expandAction = String.format("expandActionRow%02d", i);
			String unlinkRecord = String.format("unlinkRecordRow%02d", i);
			String viewRecord = String.format("viewRecordRow%02d", i);
			String recordName = String.format("recordNameRow%02d", i);

			String currentRow = getHookString() + " .list.view tr:nth-of-type(" + (i+2) + ")";

			// Common Subpanel controls
			addControl(expandAction, "span", "css", currentRow + " .sugar_action_button .ab");
			
			if(currentRow.contains("whole_subpanel_users")) {
				addControl(unlinkRecord, "a", "css", currentRow + " .listViewTdToolsS1");
			} else {
				addControl(unlinkRecord, "a", "css", currentRow + " a[id*='remove_" + i + "']");	
			}
			addControl(viewRecord, "a", "css", "#" + identityModule.bwcSubpanelName.toLowerCase() + "_edit_" + i);
			addControl(recordName, "a", "css", currentRow + " td:nth-of-type(2) a");
		}
	}
	
	/**
	 * Verify that this subpanel contains desired data.
	 * <p>
	 * 
	 * @param rowNum	1-based index of the row in this subpanel to verify against.
	 * @param verifyData	Record Data to verify in this subpanel.
	 * @param shouldExist	true if this subpanel should contain the data, false if not.
	 * @throws Exception
	 */
	public void verify(int rowNum, FieldSet verifyData, boolean shouldExist) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: Needs to work for specific columns and rows
		for(String verifyThis : verifyData.keySet()) {
			VoodooControl row = new VoodooControl("tr", "css", getHookString() + " .list.view tr:nth-of-type(" + (rowNum+2) + ")");
			row.assertContains(verifyData.get(verifyThis), shouldExist);
		}
		VoodooUtils.focusDefault();
	}
	
	/**
	 * View a record in this subpanel.
	 * <p>
	 * When used this will take you to the recordView/detailView of the record.<br>
	 * This will click on the name or subject of a record.<br>
	 * Must have a record in this subpanel to click on.
	 * 
	 * @param row	1-based index of the record in this subpanel.
	 * @throws Exception
	 */
	public void viewRecord(int row) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("viewRecordRow%02d", row)).scrollIntoViewIfNeeded(true); // scroll + click
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click on a record in this subpanel by index.
	 * <p>
	 * Must have a record in this subpanel to click on.<br>
	 * When used, you will be on the recordView/detailView of the record.<br>
	 * You will no longer be on the detail view of the parent record.
	 * 
	 * @param row	1-based index of the row where a record exists.
	 * @throws Exception
	 */
	@Override
	public void clickRecord(int row) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("recordNameRow%02d", row)).click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Edit a record in this subpanel.
	 * <p>
	 * You must be on the DetailView.<br>
	 * This method will take you to the records edit view or record view, in edit mode.<br>
	 * 
	 * @param row	1-based index of the row you want to edit.
	 * @throws Exception
	 */
	@Override
	public void editRecord(int row) throws Exception {
		// TODO: Need to figure out if this can be implemented
		VoodooUtils.voodoo.log.severe("Not Yet Implemented.");
	}

	/**
	 * Unlink a record in this subpanel.
	 * <p>
	 * You must be on the DetailView.<br>
	 * This method will trigger the unlink action for the specified row in this
	 * subpanel and prompt you to confirm the action while leaving you on the
	 * DeatilView attached to the Default Content.
	 * 
	 * @param row	1-based index of the row you want to unlink.
	 * @throws Exception
	 */
	@Override
	public void unlinkRecord(int row) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		if(getControl(String.format("expandActionRow%02d", row)).queryExists()) {
			VoodooUtils.focusDefault();
			expandSubpanelRowActions(row);
			VoodooUtils.focusFrame("bwc-frame");
		}
		getControl(String.format("unlinkRecordRow%02d", row)).click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
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
		VoodooUtils.focusFrame("bwc-frame");
		getControl(String.format("expandActionRow%02d", row)).click();
		VoodooUtils.pause(750);
		VoodooUtils.focusDefault();
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
		// TODO: Need to figure out if this can be implemented
		VoodooUtils.voodoo.log.info("This method is not implemented yet!");
		return null;
	}
	
	/**
	 * Clicks on the subpanel actions drop down to expose menu items.
	 * <p>
	 * Must be in a BWCDetailView to use.<br>
	 * Must have more than 1 menu action possible in this subpanel.<br>
	 * When used, the menu actions possible for this subpanel are visible.
	 * 
	 * @throws Exception
	 */
	public void expandSubpanelActionsMenu() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("subpanelActionsMenu").click();
		new VoodooControl("ul", "css", ".subnav.ddopen").waitForVisible();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click on the specified subpanel action in this subpanel.
	 * <p>
	 * Must be in a BWCDetailView to use.<br>
	 * If more than 1 menu action is possible in this subpanel, you will need
	 * to expose the menu actions first.
	 * 
	 * @param menu	Sugar ID of the menu item to click on.
	 * @throws Exception
	 */
	public void subpanelAction(String menu) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", getHookString() + " " + menu).click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click on the Compose Email link in Activities Subpanel.
	 * <p>
	 * Can only be used in the detailView of a record that has the Activities Subpanel.<br>
	 * When used, the user will have the Compose Email View displayed.<br>
	 * Leaves user focused on the defaultContent.
	 * 
	 * @throws Exception
	 */
	public void composeEmail() throws Exception {
		expandSubpanelActionsMenu();
		VoodooUtils.focusFrame("bwc-frame");
		getControl("composeEmail").click();
		BWCComposeEmail compose = new BWCComposeEmail();
		compose.getControl("sendButton").waitForVisible();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Determine if this subpanel is empty.
	 * 
	 * @return true if this Subpanel is empty, false otherwise.
	 * @throws Exception
	 */
	public boolean isEmpty() throws Exception {
		boolean status = false;
		VoodooUtils.focusFrame("bwc-frame");
		status = new VoodooControl("td", "css", getHookString() + " .list.view .oddListRowS1 td").queryContains("No data", true);
		VoodooUtils.focusDefault();
		return status;
	}

	/**
	 * Add a header to  Subpanel definition.
	 *
	 * @param toAdd
	 *            the SugarCRM internal name for the field
	 * @throws Exception
	 */
	public void addHeader(String toAdd) throws Exception {
		// TODO: VOOD-1674 for BWC modules.
		VoodooUtils.voodoo.log.severe("BMCSubpanel addHeader() not implemented");
	}

	/**
	 * Remove a header from Subpanel definition
	 *
	 * @param toRemove
	 * @throws Exception
	 */
	public void removeHeader(String toRemove) throws Exception {
		// TODO: VOOD-1674 for BWC modules.
		VoodooUtils.voodoo.log.severe("BMCSubpanel removeHeader() not implemented");
	}


	/**
	 * Sorts the subpanel listview by the specified column in either ascending or
	 * descending order
	 *
	 * @param header
	 *            the SugarCRM internal name for the field you want to sort by and prepend "header" as text.
	 * @param ascending
	 *            true for ascending, false for descending.
	 *@throws Exception
	 */
	public void sortBy(String header, boolean ascending) throws Exception {
		// TODO: VOOD-1674 for BWC modules.
		VoodooUtils.voodoo.log.severe("BMCSubpanel sortBy() not implemented");
	}
}// BWCSubpanel