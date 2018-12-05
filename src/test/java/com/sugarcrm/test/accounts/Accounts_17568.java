package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17568 extends SugarTest {
	DataSource contactsData = new DataSource();
	ArrayList<Record> contactDataList = new ArrayList<Record>();
	VoodooSelect searchFilter;
	VoodooControl relatedFilter;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		contactsData = testData.get(testName);
		
		contactDataList = sugar().contacts.api.create(contactsData);

		sugar().login();
	}

	/**
	 * Verify UI for subpanel predefined filter All 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17568_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to Accounts
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Link Contacts Records in Contacts Subpanel of Accounts
		StandardSubpanel contactsSP = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSP.linkExistingRecords(contactDataList);

		// Select the Contacts Filter from Related Filter Dropdown
		// TODO: VOOD-486,468
		relatedFilter = sugar().accounts.recordView.getControl("relatedSubpanelFilter");
		relatedFilter.click();
		searchFilter = new VoodooSelect("input", "css", "#select2-drop .select2-search input");
		searchFilter.set(sugar().contacts.moduleNamePlural);

		// Verify the Related drop down, the "All" label has changed to Contacts.
		sugar().accounts.recordView.getControl("relatedSubpanelChoice").assertEquals(sugar().contacts.moduleNamePlural, true);

		// Verify only Contacts Subpanel is visible.
		StandardSubpanel tasksSP = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		StandardSubpanel callsSP = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		StandardSubpanel leadsSP = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		StandardSubpanel opportunitiesSP = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		contactsSP.assertVisible(true);
		tasksSP.assertVisible(false);
		callsSP.assertVisible(false);
		leadsSP.assertVisible(false);
		opportunitiesSP.assertVisible(false);

		// Select the All Contacts Filter
		// TODO: VOOD-486,468
		new VoodooSelect("a", "css", "[data-voodoo-name='filter-filter-dropdown'] .select2-choice").set(contactsData.get(0).get("lastName"));

		// Verify All Contact Records are listed in Contacts Subpanel
		for (int i = 0; i < contactsData.size(); i++) 
			contactsSP.getDetailField(i+1, "fullName").assertEquals(contactsData.get(contactsData.size()-(i+1)).get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}