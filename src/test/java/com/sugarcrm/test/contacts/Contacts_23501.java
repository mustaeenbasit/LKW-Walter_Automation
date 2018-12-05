package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23501 extends SugarTest{
	ContactRecord con1;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Create contact_Verify that creating a contact can be canceled
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23501_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().contacts.navToListView();
		sugar().navbar.selectMenuItem(sugar().contacts,ds.get(0).get("menu"));
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(0).get("lastname"));
		sugar().contacts.createDrawer.cancel();
		sugar().contacts.listView.getControl("createButton").assertVisible(true);

		new VoodooControl("div", "css", "div.flex-list-view-content").assertContains(ds.get(0).get("lastname"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}

}
