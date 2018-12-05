package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27816 extends SugarTest {
	FieldSet customData;
	VoodooControl callsButtonCtrl;

	public void setup() throws Exception {
		sugar.calls.api.create();
		callsButtonCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		sugar.login();
	}
	/**
	 * Verify that reminder time should not change (keep as default value) after inline edit it
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27816_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Calls > Layouts > Listview.
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		callsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542 -Need library support for studio
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add Popup Reminder Time and Email Reminder Time fields to the Calls list view
		VoodooControl dropHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='reminder_time']").dragNDrop(dropHere);
		new VoodooControl("li", "css", ".draggable[data-name='email_reminder_time']").dragNDrop(dropHere);

		// Save and Deploy
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to calls list view
		sugar.calls.navToListView();

		// Edit Pop up reminder time
		customData = testData.get(testName).get(0);
		sugar.calls.listView.editRecord(1);
		new VoodooSelect("div", "css", ".fld_reminder_time.edit").set(customData.get("popUpReminderTime"));
		sugar.calls.listView.saveRecord(1);
		sugar.calls.listView.create();
		
		// Verify Default popup reminder time should have the default value
		sugar.calls.createDrawer.getEditField("remindersPopup").assertContains(customData.get("defaultPopUpReminderTime"), true);
		sugar.calls.createDrawer.getEditField("remindersEmail").assertContains(customData.get("defaultEmailPopUp"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}