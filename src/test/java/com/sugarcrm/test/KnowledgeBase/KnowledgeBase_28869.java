package com.sugarcrm.test.KnowledgeBase;

import static org.junit.Assert.assertFalse;
import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28869 extends SugarTest {
	DataSource kbCreateData = new DataSource();
	ListView kbListView;
	FieldSet qaUser = new FieldSet();
	int kbCreateDataSize = 0;

	public void setup() throws Exception {
		kbCreateData = testData.get(testName);
		kbListView = sugar().knowledgeBase.listView;
		qaUser = sugar().users.getQAUser();
		kbCreateDataSize = kbCreateData.size();

		// Login as Admin
		sugar().login();

		// Enable the KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Logout from admin
		sugar.logout();

		// Login as qaUser
		sugar.login(qaUser);

		// Navigate to the KB module list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Create 3 KB records with statuses other than Approved and without Publish Date
		VoodooControl kbNameEditField = sugar().knowledgeBase.createDrawer.getEditField("name");
		VoodooControl kbStatusEditField = sugar().knowledgeBase.createDrawer.getEditField("status");

		for(int i = 0; i < kbCreateDataSize; i++) {
			kbListView.create();
			kbNameEditField.set(kbCreateData.get(i).get("name"));
			kbStatusEditField.set(kbCreateData.get(i).get("status"));
			sugar().knowledgeBase.createDrawer.save();
		}
	}

	/**
	 * Verify that KB can be edited in list view when Edit a KB whose status!=Approved 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28869_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl kbListViewNameDetailField = kbListView.getDetailField(1, "name");
		VoodooControl kbListViewStatusDetailField = kbListView.getDetailField(1, "status");

		String draftRecordName = kbCreateData.get(kbCreateDataSize - 1).get("name");

		// Assert the name of the record to be edited
		kbListViewNameDetailField.assertEquals(draftRecordName, true);

		// Assert that the KB record we are editing has a status = Draft
		kbListViewStatusDetailField.assertEquals(sugar().knowledgeBase.defaultData.get("status"), true);

		// Edit the 1st KB record i.e having status = Draft
		kbListView.editRecord(1);

		VoodooControl kbListViewStatusEditField = kbListView.getEditField(1, "status");
		String statusApproved = kbCreateData.get(0).get("statusApproved");
		String warningMessage = kbCreateData.get(0).get("warningMessage");

		// Edit it by changing status=Approved
		kbListViewStatusEditField.set(statusApproved);

		// Click on Save button
		kbListView.saveRecord(1);

		// Assert that a yellow warning bar appears : "Schedule this article to be published by specifying the Publish 
		// Date. Do you wish to continue without entering a Publish Date?".
		sugar().alerts.getWarning().assertContains(warningMessage, true);

		// Click on "Cancel" in the yellow warning popup window 
		sugar().alerts.getWarning().cancelAlert();

		// Assert that the yellow waring bar disappears 
		sugar().alerts.getWarning().assertVisible(false);

		// Assert that Save button remains as enabled. User can click it again.
		assertFalse("Save button is not enabled.", kbListView.getControl("save01").isDisabled());

		// Cancel the inline editing
		kbListView.cancelRecord(1);

		// Logout from qaUser
		sugar().logout();

		// Login as Admin
		sugar().login();

		// Go to studio->KB->Layouts->list view, add "Published Date" in the default list view.
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add "Publish Date" field to "ListView"
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(new VoodooControl("li", "css", "#Default li[data-name='language']"));

		// Save and Deploy
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from admin user
		sugar().logout();

		// Log-in as qaUser
		sugar().login(qaUser);

		// Navigate to the KB list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Edit the first KB record and change the status = Approved.
		kbListView.editRecord(1);
		kbListViewStatusEditField.set(statusApproved);

		// Click on the save button
		kbListView.saveRecord(1);

		// Assert that A yellow warning bar appears.  "Schedule this article to be published by specifying the Publish 
		// Date. Do you wish to continue without entering a Publish Date?".
		sugar().alerts.getWarning().assertContains(warningMessage, true);

		// Click on "Cancel" in the yellow warning popup window
		sugar().alerts.getWarning().cancelAlert();

		// Assert that the Calendar appears at "Published Date" field, today is highlighted.
		// TODO: VOOD-910 - Need to have a lib to support for the datePicker and timePicker widget in BWC and sidecar modules
		new VoodooControl("div", "css", ".layout_KBContents .datepicker.dropdown-menu").assertVisible(true);
		DateTime today = new DateTime();
		String monthAndYear = String.format("%s %s", today.monthOfYear().getAsText(),today.year().getAsText());

		// Assert that the current month and year are displayed in the Datepicker drop down
		new VoodooControl("th", "css", ".layout_KBContents .datepicker-days .switch").assertEquals(monthAndYear, true);

		// Assert that today is Highlighted/Active
		new VoodooControl("td", "css", ".layout_KBContents .datepicker-days .day.active").assertEquals(today.dayOfMonth().getAsText(), true);

		// Click on a future date (In this script the next day is clicked)
		new VoodooControl("td", "css", ".layout_KBContents .datepicker-days .day.active + td").click();
		VoodooUtils.waitForReady();

		// Save the record
		kbListView.saveRecord(1);

		// Assert the record Name
		kbListViewNameDetailField.assertEquals(draftRecordName, true);

		// Assert that the Status = Approved
		kbListViewStatusDetailField.assertEquals(statusApproved, true);

		// Assert that the Publish Date = the date selected above (i.e tomorrow's date)
		new VoodooControl("span", "css", ".fld_active_date.list").assertEquals(DateTime.now().plusDays(1).toString("MM/dd/yyyy"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}