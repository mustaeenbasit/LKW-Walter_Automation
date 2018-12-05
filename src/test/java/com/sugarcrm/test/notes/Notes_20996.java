package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_20996 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().notes.api.create(ds);
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Search Note_Verify that search conditions for note can be cleared.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20996_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Notes" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);

		FieldSet fs = new FieldSet();
		fs.put("Contact", sugar().contacts.getDefaultData().get("firstName"));
		// TODO: VOOD-444
		sugar().notes.listView.checkRecord(1);
		sugar().notes.massUpdate.performMassUpdate(fs);

		// Create custom search filter (Filter > Create filter)
		sugar().notes.listView.openFilterDropdown();
		sugar().notes.listView.selectFilterCreateNew();	
		// Enter in filter section such as (Contact with condition "is any of" and select existing contacts in notes) and save it.
		sugar().notes.listView.filterCreate.setFilterFields("contact", "Contact", "is any of", sugar().contacts.getDefaultData().get("firstName"), 1);
		sugar().notes.listView.filterCreate.getControl("filterName").set(ds.get(0).get("subject"));
		VoodooUtils.waitForReady();
		sugar().notes.listView.filterCreate.save();

		// Check displayed notes on list view
		sugar().notes.listView.verifyField(1, "subject", ds.get(2).get("subject"));
		sugar().notes.listView.getControl("searchFilterCurrent").click();
		VoodooUtils.waitForReady();
		// delete the filter
		sugar().notes.listView.filterCreate.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that The search conditions are cleared and all the notes are displayed on the page.
		sugar().notes.listView.verifyField(1, "subject", ds.get(2).get("subject"));
		sugar().notes.listView.verifyField(2, "subject", ds.get(1).get("subject"));
		sugar().notes.listView.verifyField(3, "subject", ds.get(0).get("subject"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 