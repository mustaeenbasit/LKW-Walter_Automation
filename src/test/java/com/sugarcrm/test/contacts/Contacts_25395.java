package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_25395 extends SugarTest {
	DataSource contactsAndFilterData = new DataSource();

	public void setup() throws Exception {
		contactsAndFilterData = testData.get(testName);
		FieldSet contactData = new FieldSet();
		for(int i = contactsAndFilterData.size(); i > 0 ; i--) {
			contactData.put("firstName", contactsAndFilterData.get(i-1).get("firstName"));
			contactData.put("lastName", contactsAndFilterData.get(i-1).get("lastName"));
			contactData.put("leadSource", contactsAndFilterData.get(i-1).get("leadSource"));
			sugar().contacts.api.create(contactData);
		}

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/** Create a filter in contact list view
	 * @throws Exception
	 */
	@Test
	public void Contacts_25395_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts module
		// TODO: VOOD-1463
		sugar().contacts.navToListView();
		VoodooControl customFilterCtrl = new VoodooControl("div", "css", ".search-filter-dropdown li:nth-child(2)");

		// Create a filter and select "Lead Source", "is any of",  "Other", "Partner", "Email" and etc.
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();
		for(int i = 0; i < contactsAndFilterData.size(); i++) {
			sugar().contacts.listView.filterCreate.setFilterFields(contactsAndFilterData.get(0).get("filterField"), contactsAndFilterData.get(1).get("filterField"), contactsAndFilterData.get(0).get("operator"), contactsAndFilterData.get(i).get("leadSource"), 1);
		}

		// Verify that the multiple values are selected and presented correctly.
		// TODO:VOOD-1766
		for(int i = 0; i < contactsAndFilterData.size(); i++) {
			new VoodooControl("div", "css", ".fld_lead_source li:nth-of-type("+(i+1)+") div").assertEquals((contactsAndFilterData.get(i).get("leadSource")), true);
		}

		for(int i = 0; i < contactsAndFilterData.size(); i++) {

			// Preview one of the record, look at the "Lead Source" field.
			sugar().contacts.listView.previewRecord(i+1);
			sugar().previewPane.showMore();

			// Verify that the value of Lead Source matches one of the values you have selected.
			sugar().previewPane.getPreviewPaneField("fullName").assertContains((contactsAndFilterData.get(i).get("firstName")) + " " + (contactsAndFilterData.get(i).get("lastName")), true);
			sugar().previewPane.getPreviewPaneField("leadSource").assertContains((contactsAndFilterData.get(i).get("leadSource")), true);
		}

		// Click on "x icon" in any value of the filter Search, such as delete "Email".
		// TODO: VOOD-1766
		new VoodooControl("a", "css", ".fld_lead_source li:nth-of-type(3) a").click();
		VoodooUtils.waitForReady();

		// Verify that the list is re-loading and a new list is generated base on the values are selected.
		sugar().contacts.listView.verifyField(1, "fullName", (contactsAndFilterData.get(0).get("firstName")) + " " + (contactsAndFilterData.get(0).get("lastName")));
		sugar().contacts.listView.verifyField(2, "fullName", (contactsAndFilterData.get(1).get("firstName")) + " " + (contactsAndFilterData.get(1).get("lastName")));
		sugar().contacts.listView.getControl("checkbox03").assertExists(false);

		// Give a filter name.
		sugar().contacts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().contacts.listView.filterCreate.save();


		// Verify the saved filter
		sugar().contacts.listView.openFilterDropdown();
		customFilterCtrl.assertEquals(testName, true);
		customFilterCtrl.click();

		// Verify that the filter is saved with correct record.
		for(int i = 0; i < contactsAndFilterData.size() - 1; i++) {

			// Preview one of the record, look at the "Lead Source" field.
			sugar().contacts.listView.previewRecord(i+1);
			sugar().previewPane.showMore();

			// Verify that the value of Lead Source matches one of the values you have selected.
			sugar().previewPane.getPreviewPaneField("fullName").assertContains((contactsAndFilterData.get(i).get("firstName")) + " " + (contactsAndFilterData.get(i).get("lastName")), true);
			sugar().previewPane.getPreviewPaneField("leadSource").assertContains((contactsAndFilterData.get(i).get("leadSource")), true);
		}

		// Remove custom filter
		// TODO: VOOD-998
		new VoodooControl("span", "css", ".choice-filter-label").click();
		sugar().contacts.listView.filterCreate.delete();
		sugar().alerts.getWarning().confirmAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
