package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23663 extends SugarTest {
	DataSource contactRecord = new DataSource();
	AccountRecord account1, account2;
	
	public void setup() throws Exception {
		contactRecord = testData.get(testName);
		FieldSet dataRecord = new FieldSet();
		dataRecord.put("name", contactRecord.get(0).get("relAccountName"));
		account1 = (AccountRecord) sugar().accounts.api.create(dataRecord);
		dataRecord.clear();
		dataRecord.put("name", contactRecord.get(1).get("relAccountName"));
		account2 = (AccountRecord) sugar().accounts.api.create(dataRecord);
		dataRecord.clear();
		sugar().login();
	}

	/**
	 * Sort contact_Verify that contacts can be sorted by each column in "Contact List" view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23663_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().contacts.navToListView();
		for (int i = 0; i < contactRecord.size(); i++) {
			sugar().contacts.listView.create();
			sugar().contacts.createDrawer.getEditField("lastName").set(contactRecord.get(i).get("lastName"));
			sugar().contacts.createDrawer.getEditField("title").set(contactRecord.get(i).get("title"));
			sugar().contacts.createDrawer.showMore();
			sugar().contacts.createDrawer.getEditField("phoneWork").set(contactRecord.get(i).get("phoneWork"));
			sugar().contacts.createDrawer.getEditField("relAccountName").set(contactRecord.get(i).get("relAccountName"));
			sugar().alerts.getWarning().cancelAlert();
			sugar().contacts.createDrawer.getEditField("emailAddress").set(contactRecord.get(i).get("emailAddress"));
			sugar().contacts.createDrawer.getEditField("relAssignedTo").set(contactRecord.get(i).get("relAssignedTo"));
			sugar().contacts.createDrawer.save();
		}
		sugar().contacts.navToListView();
		
		// Sort by name
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "fullName").assertEquals(contactRecord.get(1).get("lastName"), true);
		sugar().contacts.listView.getDetailField(2, "fullName").assertEquals(contactRecord.get(0).get("lastName"), true);
		sugar().contacts.listView.sortBy("headerFullname", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(contactRecord.get(0).get("lastName"), true);
		sugar().contacts.listView.getDetailField(2, "fullName").assertContains(contactRecord.get(1).get("lastName"), true);
		sugar().contacts.listView.sortBy("headerFullname", false);
		
		// Sort by title
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "title").assertEquals(contactRecord.get(1).get("title"), true);
		sugar().contacts.listView.getDetailField(2, "title").assertEquals(contactRecord.get(0).get("title"), true);
		sugar().contacts.listView.sortBy("headerTitle", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "title").assertContains(contactRecord.get(0).get("title"), true);
		sugar().contacts.listView.getDetailField(2, "title").assertContains(contactRecord.get(1).get("title"), true);
		sugar().contacts.listView.sortBy("headerTitle", false);
		
		// Sort by Account Name
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "relAccountName").assertEquals(contactRecord.get(1).get("relAccountName"), true);
		sugar().contacts.listView.getDetailField(2, "relAccountName").assertEquals(contactRecord.get(0).get("relAccountName"), true);
		sugar().contacts.listView.sortBy("headerAccountname", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "relAccountName").assertContains(contactRecord.get(0).get("relAccountName"), true);
		sugar().contacts.listView.getDetailField(2, "relAccountName").assertContains(contactRecord.get(1).get("relAccountName"), true);
		sugar().contacts.listView.sortBy("headerAccountname", false);
		
		// Sort by Email
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "emailAddress").assertEquals(contactRecord.get(1).get("emailAddress"), true);
		sugar().contacts.listView.getDetailField(2, "emailAddress").assertEquals(contactRecord.get(0).get("emailAddress"), true);
		sugar().contacts.listView.sortBy("headerEmail", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "emailAddress").assertContains(contactRecord.get(0).get("emailAddress"), true);
		sugar().contacts.listView.getDetailField(2, "emailAddress").assertContains(contactRecord.get(1).get("emailAddress"), true);
		sugar().contacts.listView.sortBy("headerEmail", false);
		
		// Sort by office phone
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "phoneWork").assertEquals(contactRecord.get(1).get("phoneWork"), true);
		sugar().contacts.listView.getDetailField(2, "phoneWork").assertEquals(contactRecord.get(0).get("phoneWork"), true);
		sugar().contacts.listView.sortBy("headerPhonework", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "phoneWork").assertContains(contactRecord.get(0).get("phoneWork"), true);
		sugar().contacts.listView.getDetailField(2, "phoneWork").assertContains(contactRecord.get(1).get("phoneWork"), true);
		sugar().contacts.listView.sortBy("headerPhonework", false);
		
		// Sort by assigned user name User
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "relAssignedTo").assertEquals(contactRecord.get(1).get("relAssignedTo"), true);
		sugar().contacts.listView.getDetailField(2, "relAssignedTo").assertEquals(contactRecord.get(0).get("relAssignedTo"), true);
		sugar().contacts.listView.sortBy("headerAssignedusername", true);
		
		// Verifying listview record before sorting
		sugar().contacts.listView.getDetailField(1, "relAssignedTo").assertContains(contactRecord.get(0).get("relAssignedTo"), true);
		sugar().contacts.listView.getDetailField(2, "relAssignedTo").assertContains(contactRecord.get(1).get("relAssignedTo"), true);
		sugar().contacts.listView.sortBy("headerAssignedusername", false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
