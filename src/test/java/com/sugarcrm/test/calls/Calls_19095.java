package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calls_19095 extends SugarTest {

	// Setup: create and then link Calls, Contacts records 
	public void setup() throws Exception {
		sugar.calls.api.create();
		ContactRecord myContactRecord = (ContactRecord) sugar.contacts.api.create();
		sugar.login();

		// Create calls relationship with contact record
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(myContactRecord.get("firstName"));
		sugar.calls.recordView.save();
	}

	/**
	 * Relationship with other module: Verify that no error is displayed after removing the relationship between a call record and its parent object by deleting the call.
	 * @throws Exception
	 */
	@Test
	public void Calls_19095_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.getDetailField("relatedToParentName").click();
		StandardSubpanel callsSubpanel = sugar.contacts.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.expandSubpanel();
		callsSubpanel.unlinkRecord(1);
		VoodooUtils.waitForReady();
		sugar.calls.navToListView();
		VoodooUtils.waitForReady();
		sugar.alerts.getWarning().assertVisible(false);
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}