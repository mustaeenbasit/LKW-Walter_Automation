package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_19099 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();		
	}

	/**
	 * All links in calls list view.
	 * @throws Exception
	 */
	@Test
	public void Calls_19099_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Log call with relatedTo field as Contact with their parent names
		sugar().navbar.selectMenuItem(sugar().calls, "create"+sugar().calls.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(myContact.getRecordIdentifier());

		// Click "Save" button
		sugar().calls.createDrawer.save();

		// Go to Call record view
		sugar().calls.listView.clickRecord(1);

		// Verify link of the 'contact' can lead to correct contact detail view 
		sugar().calls.recordView.getDetailField("relatedToParentName").click();
		sugar().contacts.recordView.getDetailField("fullName").assertContains(myContact.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}