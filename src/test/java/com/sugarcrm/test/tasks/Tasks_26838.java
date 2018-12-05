package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tasks_26838 extends SugarTest {
	VoodooControl tasksSubpanelCtrl, layoutCtrl, listViewCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		ContactRecord myContact = (ContactRecord)sugar.contacts.api.create();
		TaskRecord myTask = (TaskRecord)sugar.tasks.api.create();
		sugar.login();

		// Link Tasks to Contacts
		myContact.navToRecord();
		StandardSubpanel tasksSubPanel = sugar.contacts.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		tasksSubPanel.linkExistingRecord(myTask);

		// Studio -> Tasks
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542, VOOD-1507
		tasksSubpanelCtrl = new VoodooControl("a", "id", "studiolink_Tasks");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl = new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a");
		saveBtnCtrl = new VoodooControl("input", "css" ,"input[name='savebtn']");
		tasksSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click(); 
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='contact_phone']").dragNDrop(moveHere);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that the Contact phone is correct when add it into Task listview 
	 * @throws Exception
	 */
	@Test
	public void Tasks_26838_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Tasks Listview
		sugar.tasks.navToListView();

		// TODO: VOOD-1036
		// Verify contact phone is on Tasks listview as header column & Contact Phone field displays the "Office Phone" of the contact
		new VoodooControl("th", "css", ".sorting.orderBycontact_phone").assertVisible(true);
		new VoodooControl("a", "css", ".fld_contact_phone.list div a").assertEquals(sugar.contacts.getDefaultData().get("phoneWork"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}