package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23506 extends SugarTest {
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
	public void Contacts_23506_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		con1.navToRecord();
		sugar().contacts.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.listView.getControl("createButton").assertVisible(true);

		// TODO VOOD-581
	    new VoodooControl("div", "css", "div.flex-list-view-content").assertContains(con1.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}


