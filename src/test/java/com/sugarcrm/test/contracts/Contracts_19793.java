package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19793 extends SugarTest {
	FieldSet fs;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.contracts.api.create();
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Edit Contract_Verify that the selected contract can be edited when editing all fields.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19793_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Get current date
		String date = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		
		//  Click "Contracts" tab on top navigation bar.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		
		//  Click a link of contract name in "Contract" list view.
		sugar.contracts.listView.clickRecord(1);
		
		//  Edit fields :"Contract Name","Status"," Start Date","End Date","Team ID" and "Assigned to".
		sugar.contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.editView.getEditField("name").set(fs.get("name"));
		sugar.contracts.editView.getEditField("status").set(fs.get("status"));
		sugar.contracts.editView.getEditField("account_name").set(fs.get("account_name"));
		sugar.contracts.editView.getEditField("date_start").set(date);
		sugar.contracts.editView.getEditField("date_end").set(date);
		sugar.contracts.editView.getEditField("assignedTo").set(fs.get("assignedTo"));
		sugar.contracts.editView.getEditField("teams").set(fs.get("teams"));
		VoodooUtils.focusDefault();
		
		// Click "save" button.
		sugar.contracts.editView.save();
		
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that the edited contract is displayed correctly on the detail view page.
		sugar.contracts.detailView.getDetailField("name").assertEquals(fs.get("name"), true);
		sugar.contracts.detailView.getDetailField("status").assertEquals(fs.get("status"), true);
		sugar.contracts.detailView.getDetailField("account_name").assertEquals(fs.get("account_name"), true);
		sugar.contracts.detailView.getDetailField("date_start").assertEquals(date, true);
		sugar.contracts.detailView.getDetailField("date_end").assertEquals(date, true);
		sugar.contracts.detailView.getDetailField("assignedTo").assertEquals(fs.get("assignedTo"), true);
		sugar.contracts.detailView.getDetailField("teams").assertContains(fs.get("teams"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}