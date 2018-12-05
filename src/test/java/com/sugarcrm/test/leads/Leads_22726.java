package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22726 extends SugarTest{
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().leads.api.create(customData);
		sugar().login();
	}

	/**
	 * Merge Duplicate_Verify that leads can be merged with records searched by "Filter Condition" from the detail view of the lead by entering data in primary rows.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22726_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-568, VOOD-578, VOOD-738
		new VoodooControl("a", "css", ".fld_find_duplicates_button a").click();
		new VoodooControl("td", "css", "table.duplicates tbody tr td:nth-of-type(1)").click();
		new VoodooControl("a", "css", ".fld_merge_duplicates_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "div.rows-div.fluid-div div div:nth-child(2) div.box div:nth-child(1) div input").click();
		new VoodooControl("a", "css", ".fld_save_button.merge-duplicates-headerpane").click();
		sugar().alerts.getWarning().confirmAlert();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Expected result
		String fullNameStr = String.format("Mr. %s %s", customData.get(0).get("firstName"),customData.get(0).get("lastName")); 
		sugar().leads.recordView.getDetailField("fullName").assertEquals(fullNameStr, true);
		sugar().leads.recordView.getDetailField("title").assertEquals(customData.get(0).get("title"), true);
		sugar().leads.recordView.getDetailField("phoneMobile").assertEquals(customData.get(1).get("phoneMobile"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}