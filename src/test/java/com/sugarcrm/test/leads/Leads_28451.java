package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28451 extends SugarTest {
	FieldSet leadsData = new FieldSet();

	public void setup() throws Exception {
		leadsData = testData.get(testName).get(0);

		// Login as an Admin user
		sugar().login();

		// Create a Lead record with three email address
		// TODO: VOOD-444
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		// TODO: VOOD-1005		
		VoodooControl addBtnCtrl = new VoodooControl("a", "css", ".btn.addEmail");
		VoodooControl emailAddressFieldCtrl = sugar().leads.createDrawer.getEditField("emailAddress");

		// Add 3 Email addresses
		emailAddressFieldCtrl.set(leadsData.get("firstEmailAddress"));
		addBtnCtrl.click();
		emailAddressFieldCtrl.set(leadsData.get("secondEmailAddress"));
		addBtnCtrl.click();
		emailAddressFieldCtrl.set(leadsData.get("thirdEmailAddress"));

		// Add office phone and save the lead record
		sugar().leads.createDrawer.getEditField("phoneWork").set(leadsData.get("officePhone"));
		sugar().leads.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
	}
	/**
	 * Verify that primary email and office phone number are searched correctly in Leads
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_28451_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click inside Search bar to expand it
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed

		// Select "Leads" module from global search dropdown		
		sugar().navbar.searchModule("Leads");
		
		// In global search field, enter cnn.com
		globalSearchCtrl.set(leadsData.get("firstEmailAddress").substring(6));
		VoodooUtils.waitForReady();

		// All email with related to cnn.com should display
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		searchResultCtrl.assertContains(leadsData.get("firstEmailAddress"), true);
		searchResultCtrl.assertContains(leadsData.get("secondEmailAddress"), true);
		searchResultCtrl.assertContains(leadsData.get("thirdEmailAddress"), true);

		// In global search field, enter 408
		globalSearchCtrl.set(leadsData.get("officePhone").substring(1,4));
		VoodooUtils.waitForReady();

		// Verify that the whole phone number field is highlighted in the office phone number field (Strong html tag assert that the phone number is in bold)
		// TODO: VOOD-1951
		searchResultCtrl.assertContains(leadsData.get("officePhone"), true);
		new VoodooControl("strong", "css", ".dropdown-menu.search-results .secondary span:nth-child(2) strong").assertEquals(leadsData.get("officePhone").substring(1), true);
		
		// Select "All" from global search dropdown
		sugar().navbar.searchAll();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}