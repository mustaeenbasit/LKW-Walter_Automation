package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23507 extends SugarTest {
	ContactRecord con1;

	public void setup() throws Exception {
		sugar().login();
		con1 = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Verify that contact can be deleted from detail view
	 * @throws Exception
	 */
	@Test
	public void Contacts_23507_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		con1.navToRecord();
		sugar().contacts.recordView.delete();
		sugar().alerts.getWarning().cancelAlert();
		sugar().contacts.recordView.getControl("primaryButtonDropdown").assertVisible(true);

		// TODO VOOD-581
		new VoodooControl("span", "css", "span[data-fieldname='full_name']").assertContains(con1.get("salutation")+" "+con1.get("firstName")+" "+con1.get("lastName"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}


