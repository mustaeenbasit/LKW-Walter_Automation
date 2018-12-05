package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21602 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();

		// Adding 6 Account records
		for (int i = 1; i <= 6; i++) {
			fs.put("name", "Account_" + i);
			sugar().accounts.api.create(fs);
			fs.clear();
		}

		// Adding 6 Contact records
		for (int i = 1; i <= 6; i++) {
			fs.put("lastName", "Contact_" + i);
			sugar().contacts.api.create(fs);
			fs.clear();
		}

		// Adding 6 Opportunity records  
		for (int i = 1; i <= 6; i++) {
			fs.put("name", "Opportunity_" + i);
			sugar().opportunities.api.create(fs);
			fs.clear();
		}

		// Login as admin
		sugar().login();
	}

	/**
	 * Verify Dashlet pagination works correctly upon clicking the 'More <record>...' link in dashlets [Dashlets_pagination]
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21602_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		DataSource customData = testData.get(testName);

		// Create a new dashboard page on Home
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);

		// TODO: VOOD-960 - Dashlet Selection
		VoodooControl moduleDropDown = new VoodooSelect("div", "css", ".fld_module .select2-container");
		VoodooControl dashletSaveBtn = new VoodooControl("a", "css", ".active .fld_save_button a");

		// Adding Accounts, Contacts and Opportunities dashlets
		for (int i = 0; i < customData.size(); i++) {
			// Click 'Add Row' link
			new VoodooControl("p", "css", ".add-row p").click();
			sugar().home.dashboard.addDashlet(i+1 , 1);

			// Search and select 'List View' dashlet's link
			// TODO: VOOD-960 - Dashlet Selection
			new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(customData.get(0).get("dashletType"));
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".list-view .fld_title a").click();
			VoodooUtils.waitForReady();

			// Selecting module from the drop down
			moduleDropDown.set(customData.get(i).get("module"));
			VoodooUtils.waitForReady();

			// Save dashlet
			dashletSaveBtn.click();
			VoodooUtils.waitForReady();
		}

		// Save Dashboard
		sugar().home.dashboard.save();

		// TODO: VOOD-591 - Dashlets support
		VoodooControl sixthRecordInOpportunities = new VoodooControl("tr", "css", ".row-fluid tr:nth-child(6)");
		VoodooControl sixthRecordInContacts = new VoodooControl("tr", "css", ".row-fluid:nth-child(2) tr:nth-child(6)");
		VoodooControl sixthRecordInAccounts = new VoodooControl("tr", "css", ".row-fluid:nth-child(3) tr:nth-child(6)");

		// Verify that 5 records in each of the dashlets are displayed
		new VoodooControl("tr", "css", ".row-fluid tr:nth-child(5)").assertExists(true);
		new VoodooControl("tr", "css", ".row-fluid:nth-child(2) tr:nth-child(5)").assertExists(true);
		new VoodooControl("tr", "css", ".row-fluid:nth-child(3) tr:nth-child(5)").assertExists(true);

		// Verify that 6 records are not displayed in each of the dashlets
		sixthRecordInOpportunities.assertExists(false);
		sixthRecordInContacts.assertExists(false);
		sixthRecordInAccounts.assertExists(false);

		// Clicking 'More opportunities...' link
		new VoodooControl("button", "css", ".row-fluid .more").click();
		VoodooUtils.waitForReady();

		// Clicking 'More contacts...' link
		new VoodooControl("button", "css", ".row-fluid:nth-child(2) .more").click();
		VoodooUtils.waitForReady();

		// Clicking 'More accounts...' link
		new VoodooControl("button", "css", ".row-fluid:nth-child(3) .more").click();
		VoodooUtils.waitForReady();

		// Verify that 6 records are displayed in each of the dashlets post clicking the 'More <record>...' link
		sixthRecordInOpportunities.assertExists(true);
		sixthRecordInContacts.assertExists(true);
		sixthRecordInAccounts.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}