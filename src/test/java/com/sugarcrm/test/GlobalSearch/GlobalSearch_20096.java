package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20096 extends SugarTest {
	FieldSet searchData = new FieldSet();

	public void setup() throws Exception {
		searchData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("workPhone", searchData.get("workPhoneAccount"));
		sugar().accounts.api.create(fs);
		fs.clear();
		fs.put("phoneMobile", searchData.get("mobilePhoneContact"));
		sugar().contacts.api.create(fs);
		fs.clear();
		fs.put("phoneFax", searchData.get("faxPhoneLeads"));
		sugar().leads.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify that global search works correctly for Phone number search
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Perform Global Search for Account's Work Phone
		String accountWorkPhone = searchData.get("workPhoneAccount");
		sugar().navbar.setGlobalSearch(accountWorkPhone + '\uE007' );
		VoodooUtils.waitForReady();

		// Clicking on account record in Global Search window 
		sugar().globalSearch.getRow(1).assertVisible(true);
		sugar().globalSearch.clickRecord(1);

		// Verify that user is navigated to account record and contains Account's Work Phone
		sugar().accounts.recordView.assertVisible(true);
		sugar().accounts.recordView.getDetailField("workPhone").assertEquals(accountWorkPhone, true);

		// Perform Global Search for Contacts Mobile Phone
		String contactsMobilePhone = searchData.get("mobilePhoneContact");
		sugar().navbar.setGlobalSearch(contactsMobilePhone + '\uE007' );
		VoodooUtils.waitForReady();

		// Clicking on contacts record in Global Search window 
		sugar().globalSearch.getRow(1).assertVisible(true);
		sugar().globalSearch.clickRecord(1);

		// Verify that user is navigated to contacts record and contains contact's Mobile Phone
		sugar().contacts.recordView.assertVisible(true);
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getDetailField("phoneMobile").assertEquals(contactsMobilePhone, true);

		// Perform Global Search for Leads Fax phone
		String leadsFax = searchData.get("faxPhoneLeads");
		sugar().navbar.setGlobalSearch(leadsFax + '\uE007' );
		VoodooUtils.waitForReady();

		// Clicking on Leads record in Global Search window 
		sugar().globalSearch.getRow(1).assertVisible(true);
		sugar().globalSearch.clickRecord(1);

		// Verify that user is navigated to Leads record and contains Lead's Fax phone
		sugar().leads.recordView.assertVisible(true);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("phoneFax").assertEquals(leadsFax, true);

		// Global Search for partial phone number of existing contact, lead, account record
		String partialPhoneNumber = accountWorkPhone.substring(0, 3);
		sugar().navbar.setGlobalSearch(partialPhoneNumber);
		VoodooUtils.waitForReady();
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		searchResultCtrl.assertContains(accountWorkPhone, true);
		searchResultCtrl.assertContains(contactsMobilePhone, true);
		searchResultCtrl.assertContains(leadsFax, true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}