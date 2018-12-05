package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17355 extends SugarTest{
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		myAccount = (AccountRecord)sugar().accounts.api.create();

		// Login as Admin user
		sugar().login();
	}

	/**
	 * should be able to preview the related to record from an activity stream message(from record detail view)
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17355_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the contact and relate it to an account
		// TODO: VOOD-444
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();

		// Open activity stream on home page
		sugar().navbar.selectMenuItem(sugar().home, "activityStream");

		// Click preview icon next to a message about create to activities
		// TODO: VOOD-474
		VoodooControl activityStreamFilterCtrl = new VoodooControl("a", "css", ".filter-view.search .table-cell:nth-child(2) .search-filter a");
		activityStreamFilterCtrl.click();
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Click preview icon next to message about create contact
		sugar().contacts.recordView.activityStream.togglePreviewButton(2);

		// Verify that it opens the Contacts record's preview panel
		sugar().previewPane.setModule(sugar().contacts);
		sugar().previewPane.getPreviewPaneField("fullName").assertElementContains(myContact.getRecordIdentifier(), true);

		// Click preview icon next to a message about update to activities
		// TODO: VOOD-474
		activityStreamFilterCtrl.click();
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-child(6)").click();
		VoodooUtils.waitForReady();

		// Click preview icon next to message about update contact
		sugar().contacts.recordView.activityStream.togglePreviewButton(1);

		// Verify that it opens the Contacts record's preview panel
		sugar().previewPane.getPreviewPaneField("fullName").assertElementContains(myContact.getRecordIdentifier(), true);

		// Click preview icon next to a message about related to activities
		// TODO: VOOD-474
		activityStreamFilterCtrl.click();
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-child(3)").click();
		VoodooUtils.waitForReady();

		// Click preview icon next to message about update contact
		sugar().contacts.recordView.activityStream.togglePreviewButton(1);

		// Verify that it opens the Accounts record's preview panel
		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.getPreviewPaneField("name").assertElementContains(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}