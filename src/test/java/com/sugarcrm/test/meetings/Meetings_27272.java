package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_27272 extends SugarTest {
	VoodooControl meetingsCtrl, callsCtrl, layoutSubPanelCtrl, listViewSubPanelCtrl;
	StandardSubpanel meetingsSubpanel, callsSubpanel;
	MeetingRecord myMeeting;
	CallRecord myCall;
		
	public void setup() throws Exception {
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		myCall = (CallRecord) sugar().calls.api.create();
		sugar().contacts.api.create();
		sugar().login();
		
		// Go to Admin > Studio > Meetings > Layouts > Listview. 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		meetingsCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		callsCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
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
		sugar().admin.studio.getControl("studioButton").click();
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
	 * Verify that Start Date and End Date in Meeting/Calls subpanel listview are read-only.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27272_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to contacts record and link meeting/call records with the Contact.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		meetingsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		callsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsSubpanel.linkExistingRecord(myMeeting);
		callsSubpanel.linkExistingRecord(myCall);
		
		// Hover to below subpanels to make Meeting subpanel visible.
		sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural).hover();
		sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural).hover();
				
		// TODO: VOOD-609
		// Verify that "Start Date" and "End Date" fields are visible i.e. fields are read only.
		new VoodooControl("div", "css", "[data-voodoo-name='Meetings'] .fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", "[data-voodoo-name='Meetings'] .fld_date_end div").assertVisible(true);
		
		// TODO: VOOD-502, editRecord() is not working.
		//meetingsSubpanel.editRecord(1);
		new VoodooControl("span", "css", "[data-voodoo-name='Meetings'] tbody tr:nth-of-type(1) .fa.fa-caret-down").click();
		new VoodooControl("a", "css", "[data-voodoo-name='Meetings'] tbody .fld_edit_button.list a").click();
		
		// TODO: VOOD-609
		// Verify that there is no edit ability for Start Date or End Date.
		new VoodooControl("div", "css", "[data-voodoo-name='Meetings'] .fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", "[data-voodoo-name='Meetings'] .fld_date_end div").assertVisible(true);
		new VoodooControl("input", "css", "[data-voodoo-name='Meetings'] .fld_date_end input").assertExists(false);
		new VoodooControl("input", "css", "[data-voodoo-name='Meetings'] .fld_date_end input").assertExists(false);
		meetingsSubpanel.saveAction(1);
		
		// TODO: VOOD-609
		// Verify that "Start Date" and "End Date" fields are visible i.e. fields are read only.
		new VoodooControl("div", "css", "[data-voodoo-name='Calls'] .fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", "[data-voodoo-name='Calls'] .fld_date_end div").assertVisible(true);
		
		// TODO: VOOD-502, editRecord() is not working.
		//callsSubpanel.editRecord(1);
		new VoodooControl("span", "css", "[data-voodoo-name='Calls'] tr:nth-of-type(1) .fa.fa-caret-down").click();
		new VoodooControl("a", "css", "[data-voodoo-name='Calls'] .fld_edit_button.list a").click();
		
		// TODO: VOOD-609
		// Verify that there is no edit ability for Start Date or End Date.
		new VoodooControl("div", "css", "[data-voodoo-name='Calls'] .fld_date_start div").assertVisible(true);
		new VoodooControl("div", "css", "[data-voodoo-name='Calls'] .fld_date_end div").assertVisible(true);
		new VoodooControl("input", "css", "[data-voodoo-name='Calls'] .fld_date_end input").assertVisible(false);
		new VoodooControl("input", "css", "[data-voodoo-name='Calls'] .fld_date_end input").assertVisible(false);
		callsSubpanel.saveAction(1);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}