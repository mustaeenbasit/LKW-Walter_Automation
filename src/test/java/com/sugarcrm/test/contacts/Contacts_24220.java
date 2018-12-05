package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_24220 extends SugarTest {
	ContactRecord contactRecord2;
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().contacts.api.create();
		FieldSet newData = new FieldSet();
		newData.put("firstName", fs.get("firstName"));
		newData.put("lastName", fs.get("lastName"));
		contactRecord2 = (ContactRecord) sugar().contacts.api.create(newData);
		sugar().login();
	}

	/**
	 * Verify making changes in contact record and click Next will prompt you if you want to discard changes.
	 * @throws Exception
	 */
	@Test
	public void Contacts_24220_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.getDetailField("department").hover();

		// TODO: VOOD-854
		new VoodooControl("i", "css", "[data-name='department'] i").click();
		sugar().contacts.recordView.getEditField("department").set(fs.get("department"));
		sugar().contacts.recordView.gotoNextRecord();

		// Verify alert message will prompt you about unsaved changes and if you want to leave page and discard them.
		sugar().alerts.getWarning().assertContains(fs.get("message"), true);
		sugar().alerts.getWarning().cancelAlert();

		// Verify on Cancel, Stay on the same record edit view.
		sugar().contacts.recordView.getEditField("department").assertEquals(fs.get("department"), true);

		sugar().contacts.recordView.gotoNextRecord();
		sugar().alerts.getWarning().confirmAlert();

		// Verify on clicking Confirm, Will discard changes in previous record and go to next record.
		FieldSet contactDefaultData = sugar().contacts.defaultData;
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(contactDefaultData.get("fullName"), true);
		contactRecord2.navToRecord();

		// Verify the changes are discarded
		sugar().contacts.recordView.getDetailField("department").assertEquals(contactDefaultData.get("department"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}