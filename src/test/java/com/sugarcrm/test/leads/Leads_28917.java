package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28917 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that associated Account name should be visible in lead list view for converted Leads.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_28917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		// Verify Account name field is blank in lead.
		VoodooControl accountName = sugar().leads.listView.getDetailField(1, "accountName");
		accountName.assertEquals("", true);

		// Go to the action drop down of newly created lead and select Convert.
		sugar().leads.listView.openRowActionDropdown(1);
		// TODO: VOOD-498 - Need ListView functionality for all row actions
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Associate Accounts data
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Save and Convert the lead
		new VoodooControl("a", "css", ".convert-headerpane a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Verify the account name field at lead record view.
		sugar().leads.recordView.getDetailField("accountName").assertEquals(testName, true);

		// Verify the account name field at lead list view.
		sugar().leads.navToListView();
		accountName.assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}