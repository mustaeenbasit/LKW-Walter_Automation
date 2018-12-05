package com.sugarcrm.test.subpanels;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17782 extends SugarTest {
	StandardSubpanel contactSubpanel;
	FieldSet filterData;
	ArrayList<Record> contactList;
	DataSource contactRecords;
	VoodooControl filterIcon;
	VoodooSelect customFilterSelect;

	public void setup() throws Exception {
		contactRecords = testData.get(testName);
		sugar.accounts.api.create();
		filterIcon = new VoodooControl("span", "css", ".choice-filter-label");
		customFilterSelect = new VoodooSelect ("div", "css", ".select2.search-filter");
		contactList = sugar.contacts.api.create(contactRecords);
		sugar.login();

		// Relate a related subpanel (contacts) in accounts module
		// TODO: VOOD-1491
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		contactSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		VoodooUtils.waitForReady();
		contactSubpanel.linkExistingRecords(contactList);
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.contacts.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Create Custom Filter
		// TODO: VOOD-486		
		filterData = testData.get(testName+"_filter").get(0);
		filterIcon.click();
		new VoodooSelect ("a", "css", ".fld_filter_row_name a").set(filterData.get("fieldName"));
		new VoodooSelect ("a", "css", ".fld_filter_row_operator a").set(filterData.get("operatorValue"));
		new VoodooControl ("input", "css", ".fld_last_name input").set(filterData.get("filterValue"));
		new VoodooControl ("input", "css", ".controls.span6 input").set(filterData.get("filterName"));
		VoodooUtils.waitForReady();
		new VoodooControl ("a", "css", ".btn-primary.save_button").click();
		VoodooUtils.waitForReady();
	}
	/**
	 * Verify- Able to filter related records in subpanel by custom filter
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17782_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Search any starting string of the Contact associated with this Accounts record.
		customFilterSelect.set(filterData.get("allContacts"));
		sugar.accounts.recordView.setSearchString(filterData.get("searchString"));

		// Selecting the custom filter
		// TODO VOOD-486
		customFilterSelect.set(filterData.get("filterName"));
		filterIcon.assertEquals(filterData.get("filterName"), true);
		VoodooUtils.waitForReady();
		int count = contactSubpanel.countRows();
		String name;
		for (int i = count-1; i >= 0; i--) {
			name = "Mr. "+contactRecords.get(i).get("firstName")+" "+contactRecords.get(i).get("lastName");
			contactSubpanel.getDetailField(count - i,"fullName").assertEquals(name, true);
		}

		// Click on Related drop down and select the related Calls module
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.calls.moduleNamePlural);

		// Verify, that selected module name is shown as the first tag in the filter bar
		sugar.accounts.recordView.getControl("relatedSubpanelChoice").assertEquals(sugar.calls.moduleNamePlural, true);

		// Verify on selecting any other module from related subpanel the filter  
		sugar.accounts.recordView.getControl("searchFilter").assertEquals("", true);
		filterIcon.assertEquals(filterData.get("filterName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}