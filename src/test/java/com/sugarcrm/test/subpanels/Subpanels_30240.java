package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_30240 extends SugarTest {
	public void setup() throws Exception {
		DataSource contactsData = testData.get(testName);
		sugar().accounts.api.create();
		// Creating 50 contact records
		sugar().contacts.api.create(contactsData);
		sugar().login();
	}

	/**
	 * Verify that in Search & Select drawer correct label should be shown after selecting all records.
	 * @throws Exception
	 */
	@Test
	public void Subpanels_30240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet moreRecordsMessage = testData.get(testName + "_assertionData").get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Navigating to contact subpanel and clicking on link existing record in account record view
		StandardSubpanel contactsSubpanels = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanels.clickLinkExisting();
		
		// TODO: VOOD-1487
		// Select toggle all checkbox to select all record(20 record selected)
		new VoodooControl("input", "class", "toggle-all").click();
		
		// Clicking on Select all record link(All record selected in result set)
		new VoodooControl("button", "css", "button[data-action='select-all']").click();
		
		// Verifying '30 more records selected.' label is displaying in SSV.
		new VoodooControl("li", "css", ".selected-records-container .more-pills-label").assertEquals(moreRecordsMessage.get("moreSelectedRecord"), true);
		sugar().contacts.searchSelect.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}