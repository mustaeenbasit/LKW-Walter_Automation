package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_17009 extends SugarTest{
	AccountRecord acc1, acc2;
	ContactRecord con1, con2;
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().login();

		ds= testData.get(testName);
		//Create 2 contacts with different account
		FieldSet newData = new FieldSet();
		newData.put("name", ds.get(0).get("firstName"));
		newData.put("workPhone", ds.get(0).get("firstName"));
		acc1 = (AccountRecord) sugar().accounts.api.create(newData);
		con1 = (ContactRecord) sugar().contacts.api.create(ds.get(0));

		// TODO: VOOD-649 record.edit(FieldSet data) doesn't handle pop up
		con1.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(acc1.getRecordIdentifier());
		// cancel copy address from account, so there will only one contact list out during duplicate check
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		newData.put("name", ds.get(0).get("lastName"));
		newData.put("workPhone", ds.get(0).get("firstName"));
		acc2 = (AccountRecord) sugar().accounts.api.create(newData);
		con2 = (ContactRecord) sugar().contacts.api.create(ds.get(1));

		con2.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(acc2.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 *  Verify auto duplicate check on first and last name and related account while creating a contact
	 *  @throws Exception
	 */
	@Test
	public void Contacts_17009_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("firstName").set(ds.get(2).get("firstName"));
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(2).get("lastName"));
		sugar().contacts.createDrawer.getEditField("relAccountName").set(acc1.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();

		sugar().contacts.createDrawer.getControl("duplicateCount").assertContains("1 duplicates found.", true);
		sugar().contacts.createDrawer.getControl("duplicateHeaderRow").assertExists(true);

		// TODO: VOOD-566 to support field access in duplicate check
		new VoodooControl("tbody", "css", "table.duplicates tbody").assertContains(con1.get("firstName"), true);
		new VoodooControl("tbody", "css", "table.duplicates tbody").assertContains(con1.get("lastName"), true);

		sugar().contacts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
