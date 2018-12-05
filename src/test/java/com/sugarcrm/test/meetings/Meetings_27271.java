package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27271 extends SugarTest {
	VoodooControl meetingsCtrl, callsCtrl, layoutSubPanelCtrl, listViewSubPanelCtrl, studioFooterCtrl;
		
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().calls.api.create();
		sugar().login();
		
		// Go to Admin > Studio > Meetings > Layouts > Listview. 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		meetingsCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		callsCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		listViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		listViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Enable "End Date" to Default column.
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='date_end']").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		
		// Go to Admin > Studio > Calls > Layouts > Listview.
		studioFooterCtrl.click();
		callsCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		listViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Enable "End Date" to Default column.
		new VoodooControl("li", "css", ".draggable[data-name='date_end']").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Start Date and End Date in Meeting/Calls listview are read-only.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27271_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		
		// Verify that Start Date and End Date are visible in listview.
		// TODO: VOOD-1217
		//sugar().meetings.listView.getDetailField(1, "date_start_date").assertExists(true);
		//sugar().meetings.listView.getDetailField(1, "date_end_date").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_end div").assertVisible(true);
		sugar().meetings.listView.editRecord(1);
		
		// Verify that Start Date and End Date are disabled for editing.
		// TODO: VOOD-1217
		//sugar().meetings.listView.getEditField(1, "date_start_date").assertVisible(false);
		//sugar().meetings.listView.getEditField(1, "date_end_date").assertVisible(false);
		new VoodooControl("div", "css", ".fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_end div").assertVisible(true);
		new VoodooControl("input", "css", ".fld_date_start input").assertVisible(false);
		new VoodooControl("input", "css", ".fld_date_end input").assertVisible(false);
		
		sugar().calls.navToListView();
		
		// Verify that Start Date and End Date are visible in listview.
		// TODO: VOOD-1217
		//sugar().calls.listView.getDetailField(1, "date_start_date").assertExists(true);
		//sugar().calls.listView.getDetailField(1, "date_end_date").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_end div").assertVisible(true);
		
		sugar().calls.listView.editRecord(1);
		
		// Verify that Start Date and End Date are disabled for editing.
		// TODO: VOOD-1217
		//sugar().calls.listView.getEditField(1, "date_start_date").assertVisible(false);
		//sugar().calls.listView.getEditField(1, "date_end_date").assertVisible(false);
		new VoodooControl("div", "css", ".fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", ".fld_date_end div").assertVisible(true);
		new VoodooControl("input", "css", ".fld_date_start input").assertVisible(false);
		new VoodooControl("input", "css", ".fld_date_end input").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}