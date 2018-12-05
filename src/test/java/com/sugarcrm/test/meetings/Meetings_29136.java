package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Meetings_29136 extends SugarTest {
	Record contact;
	VoodooControl meetingsCtrl, layoutSubPanelCtrl, recordViewCtrl, displayTypePanel, saveAndDeployBtn;
	
	public void setup() throws Exception {
		contact = sugar().contacts.api.create();
		sugar().login();
		
		// TODO: VOOD-1506: Support Studio Module RecordView Layouts View
		meetingsCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		displayTypePanel = new VoodooControl("select", "css" ,"#panels div:nth-child(2) span:nth-child(3) select");
		saveAndDeployBtn = new VoodooControl("input", "id" ,"publishBtn");

		// Go to Admin > Studio > Meetings > Layouts > Record View  
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();
		
		// Changing the "Display Type" from Panel to Tab
		displayTypePanel.set("Tab");
		VoodooUtils.waitForReady();
		saveAndDeployBtn.click();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify that user should be able to view list of invitees in meetings module
	 * @throws Exception
	 */
	@Test
	public void Meetings_29136_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));

		// TODO: VOOD-629: Add support for accessing and manipulating individual components of a VoodooSelect.
		VoodooControl addRow = new VoodooControl("button", "css", ".cell.buttons [data-action='addRow']");
		VoodooControl inviteesSearchBox = new VoodooControl("input", "css", ".select2-drop-active .select2-search input");
		VoodooControl searchResultsFirstRow = new VoodooControl("div", "css", "[role='listbox'] li div:nth-child(1)");

		// Clicking the '+' button and setting the contact first name
		addRow.click();
		inviteesSearchBox.set(contact.get("firstName"));

		// Verify that the contact created is visible in the searched dropdown
		searchResultsFirstRow.assertContains(contact.getRecordIdentifier(), true);
		searchResultsFirstRow.click();
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}