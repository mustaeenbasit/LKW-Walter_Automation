package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23504 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 *  Verify that contact can be duplicated
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23504_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet contactData = testData.get(testName).get(0);

		// Click "Copy" button in detail view.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.copy();
		sugar().contacts.createDrawer.getEditField("firstName").set(contactData.get("firstname"));
		sugar().contacts.createDrawer.getEditField("lastName").set(contactData.get("lastname"));
		sugar().contacts.createDrawer.save();

		// TODO VOOD-581
		new VoodooControl("span", "css", "span[data-fieldname='full_name']").assertContains(contactData.get("firstname")+" "+contactData.get("lastname"),true);

		FieldSet dupCon = sugar().contacts.getDefaultData().deepClone();
		dupCon.put("firstName", contactData.get("firstname"));
		dupCon.put("lastName", "con1");
		dupCon.put("fullName", "Mr." + " " + contactData.get("firstname") + " " + contactData.get("lastname"));
		ContactRecord con2 = new ContactRecord(dupCon);
		con2.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
