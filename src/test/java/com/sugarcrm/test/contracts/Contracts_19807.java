package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19807 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.accounts.api.create();
		FieldSet fs = new FieldSet();
		for (int i = 0; i < 4; i++) {
			fs.put("name", testName+"_"+i);
			sugar.contracts.api.create(fs);
		}
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Mass Update Contract_Verify that updating the selected contracts by mass update works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19807_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Contracts listview and check several Contract record check boxes
		sugar.contracts.navToListView();
		sugar.contracts.listView.checkRecord(1);
		sugar.contracts.listView.checkRecord(2);
		
		// Actions -> Mass Update
		sugar.contracts.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.listView.getControl("massUpdateButton").click();
		
		// Click "Select" button next to Account Name to select an account
		// TODO: VOOD-1723
		new VoodooControl("img", "css", "#mass_update_table tr:nth-child(1) td:nth-child(2) img").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Fill end date and Customer Signed Date
		new VoodooControl("input", "id", "end_datejscal_field").set(customData.get("endDate"));
		new VoodooControl("input", "id", "customer_signed_datejscal_field").set(customData.get("customerSignedDate"));
		
		// Click "Select" button of "Team ID" to select the team
		new VoodooControl("img", "css", "#MassUpdate_team_name_table .firstChild img").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "#MassUpdate tr:nth-child(3) td:nth-child(2) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Fill 'Start date' and 'Status'
		new VoodooControl("input", "id", "start_datejscal_field").set(customData.get("startDate"));
		new VoodooControl("select", "id", "mass_status").set(customData.get("status"));
		
		// Fill 'Company Signed Date'
		new VoodooControl("input", "id", "company_signed_datejscal_field").set(customData.get("companySignedDate"));
		
		// Click "Select" button of "Assigned to" to select the user whom these contracts will be assigned to
		new VoodooControl("img", "css", "#mass_assigned_user_name_btn img").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "table tr:nth-child(3) td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click "Update" button
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		
		// Verify that selected Contract records in "Contract" list view displayed correctly
		for (int i = 1; i < 3; i++) {
			sugar.contracts.listView.verifyField(i, "account_name", sugar.contracts.getDefaultData().get("account_name"));
			sugar.contracts.listView.verifyField(i, "status", customData.get("status"));
			sugar.contracts.listView.verifyField(i, "date_start", customData.get("startDate"));
			sugar.contracts.listView.verifyField(i, "date_end", customData.get("endDate"));
			sugar.contracts.listView.verifyField(i, "assignedTo", customData.get("assignedTo"));
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}