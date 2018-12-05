package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21141 extends SugarTest {
	DataSource notesData = new DataSource();
	DataSource contactData = new DataSource();
	int notesSize = 0;

	public void setup() throws Exception {
		contactData = testData.get(testName+"_contacts");
		notesData = testData.get(testName);

		// multiple contacts record
		sugar().contacts.api.create(contactData);

		// multiple notes record
		notesSize = notesData.size();
		for(int i = 0; i < notesSize - 1; i++) 
			// created by has "admin2" value by default, via API
			sugar().notes.api.create(notesData.get(i));

		// Creating 1 note record by "qauser" and then logout
		sugar().login(sugar().users.getQAUser());
		sugar().notes.create(notesData.get(notesSize - 1));
		sugar().logout();

		// login as admin
		sugar().login();
	}
	/**
	 * Sort Note_Verify that notes can be sorted in listview
	 * @throws Exception
	 */
	@Test
	public void Notes_21141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Relating each Note to Contact in List View
		sugar().notes.navToListView();
		int contactSize = contactData.size();
		for (int i = 0; i < contactSize; i++) {
			sugar().notes.listView.editRecord(i+1);
			sugar().notes.listView.getEditField(i+1, "contact").set(contactData.get(i).get("lastName"));
			sugar().notes.listView.saveRecord(i+1);
		}

		// Set subject in Ascending Order
		sugar().notes.listView.sortBy("headerName", true);

		// Verification of subject in Ascending Order
		for (int i = 0; i < notesSize; i++)
			sugar().notes.listView.verifyField(i+1, "subject", notesData.get(i).get("subject"));

		// Set Contact in Ascending Order
		sugar().notes.listView.sortBy("headerContactname", true);

		// Verification of Contacts in Ascending Order
		for (int i = 0; i < contactSize; i++)
			sugar().notes.listView.verifyField(i+1, "contact", contactData.get(i).get("lastName"));

		// Set Created By in Ascending Order
		sugar().notes.listView.sortBy("headerCreatedbyname", true);

		String adminValue = "admin2";
		// Verification of Created By in Ascending Order
		for (int i = 0; i < notesSize - 1; i++) 
			sugar().notes.listView.verifyField(i+1, "created_by", adminValue);

		// Verification of Created By in Ascending Order, created by qauser
		String qaUserName = sugar().users.getQAUser().get("userName");
		sugar().notes.listView.verifyField(notesSize, "created_by", qaUserName);

		// Set subject in Descending Order
		sugar().notes.listView.sortBy("headerName", false);

		// Verification of subject in Descending Order
		for (int i = 1; i <= notesSize; i++) 
			sugar().notes.listView.verifyField(i, "subject", notesData.get(notesSize- i).get("subject"));

		// Set Contact in Descending Order
		sugar().notes.listView.sortBy("headerContactname", false);

		// Verification of Contacts in Descending Order
		for (int i = 1; i <= contactSize; i++)
			sugar().notes.listView.verifyField(i, "contact", contactData.get(contactSize- i).get("lastName"));

		// Set Created By in Descending Order
		sugar().notes.listView.sortBy("headerCreatedbyname", false);

		// Verification of Created By in Descending Order
		sugar().notes.listView.verifyField(1, "created_by", qaUserName);
		for (int i = 2; i <= notesSize; i++)
			sugar().notes.listView.verifyField(i, "created_by", adminValue);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}