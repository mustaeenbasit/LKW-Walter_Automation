package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19106 extends SugarTest {
	CallRecord myCall;

	public void setup() throws Exception {
		sugar.contacts.api.create();
		myCall = (CallRecord)sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Verify auto-suggest in "Related To" field in call edit view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19106_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCall.navToRecord();

		// Edit a Call and save it's 'Related To' field as of type Contact but leave the actual contact field blank.
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.recordView.save();
		sugar.calls.recordView.getDetailField("relatedToParentType").assertContains(sugar.contacts.moduleNameSingular, true);

		// Edit that call and start typing a contact into the blank.
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("relatedToParentName").click();

		// TODO: VOOD-629
		new VoodooControl("input", "css", "#select2-drop input").set(sugar.contacts.getDefaultData().get("firstName"));
		//  Verify auto-suggest in "Related To" field in call edit view
		new VoodooControl("li", "css", "#select2-drop ul li[role='presentation']").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);
		new VoodooControl("li", "css", "#select2-drop ul li[role='presentation']").click();
		// save the record
		sugar.calls.recordView.save();

		// verify the 'Related To' field record 
		sugar.calls.recordView.getDetailField("relatedToParentType").assertContains(sugar.contacts.moduleNameSingular, true);
		sugar.calls.recordView.getDetailField("relatedToParentName").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}