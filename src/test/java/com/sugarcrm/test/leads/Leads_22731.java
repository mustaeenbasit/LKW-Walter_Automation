package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22731 extends SugarTest {
	public void setup() throws Exception {
		DataSource leads = testData.get(testName);
		sugar().leads.api.create(leads);
		sugar().login();
	}

	/**
	 * Verify that records selected from leads list view can be canceled
	 * merging.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22731_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Select more than 1 lead
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		new VoodooControl("input", "css", ".fld_first_name.edit input").set(sugar().leads.getDefaultData().get("firstName"));
		new VoodooControl("input", "css", ".fld_last_name.edit input").set(sugar().leads.getDefaultData().get("lastName"));
		new VoodooControl("a", "css", ".fld_cancel_button.merge-duplicates-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify that only 2 records are visible, after merge cancel
		Assert.assertTrue("Records are not equals two", sugar().leads.listView.countRows() == 2);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}