package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calls_19096 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Edit call_Verify that "Scheduling" name is displayed as selected for scheduling a call.
	 * @throws Exception
	 */
	@Test
	public void Calls_19096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Calls > View Calls > Call Details > Edit record
		sugar.navbar.selectMenuItem(sugar.calls, "view"+sugar.calls.moduleNamePlural);
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		
		// Declare a fieldset for each invitee
		FieldSet invitee1Record = new FieldSet();
		FieldSet invitee2Record = new FieldSet();
		
		// create a fieldset for each invitee
		FieldSet contactData = testData.get(testName).get(0);
		invitee1Record.put("name", contactData.get("user1"));
		invitee2Record.put("name", contactData.get("user2"));
		
		// TODO: VOOD-1537 replace verifyInvitee with verifyInviteeEquals
		// Verify each invitee name
		sugar.meetings.recordView.verifyInvitee(1, invitee2Record);
		sugar.meetings.recordView.verifyInvitee(2, invitee1Record);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}