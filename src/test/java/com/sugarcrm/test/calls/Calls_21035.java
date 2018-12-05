package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21035 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName + "_contactData");
		sugar().contacts.api.create(customDS);
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Close and Create New Call_Verify that contact invitees are still associated to a call
	 * after creating a new call by clicking "Close and Create New" button on the original call's edit view
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_21035_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Calls and Schedule a call, and add a contact to it.
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(customDS.get(0).get("lastName"));
		sugar().calls.recordView.save();

		// Click "Edit" button in the call's detail view and add another contact to this call.
		sugar().calls.recordView.edit();
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(customDS.get(1).get("lastName"));
		sugar().calls.recordView.save();

		// Click "Close and Create New" button in the call's edit view.
		sugar().calls.recordView.closeAndCreateNew();

		// Verify that the two contact are associated with the new call. A new call is created and its status is 'Scheduled'.
		VoodooControl inviteesCtrl = sugar().calls.createDrawer.getControl("invitees");
		inviteesCtrl.assertContains(customDS.get(0).get("lastName"), true);
		inviteesCtrl.assertContains(customDS.get(1).get("lastName"), true);

		// Verify status is 'Scheduled'
		FieldSet customFS = testData.get(testName).get(0);
		sugar().calls.createDrawer.getEditField("status").assertEquals(customFS.get("status2"), true);

		// Click "Save" button.
		sugar().calls.createDrawer.save();

		// Go to the original call's detail view.
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(2);

		// Verify that the two contact are associated with the original call.  The call status is updated to held.
		FieldSet verifyInviteeFS = new FieldSet();
		verifyInviteeFS.put("lastName", customDS.get(0).get("lastName"));
		sugar().calls.recordView.verifyInvitee(2, verifyInviteeFS);
		verifyInviteeFS.clear();
		verifyInviteeFS.put("fullName", customDS.get(1).get("lastName"));
		sugar().calls.recordView.verifyInvitee(3, verifyInviteeFS);

		// The call status is updated to held.
		sugar().calls.recordView.getDetailField("status").assertEquals(customFS.get("status1"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}