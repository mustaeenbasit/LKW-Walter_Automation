package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_23258 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that creating a Contact via the Contacts Subpanel of an Account 
	 * populates the Account Address for the Contact
	 * @throws Exception
	 */
	@Test
	public void Accounts_23258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).addRecord();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.getEditField("primaryAddressStreet").assertEquals(myAccount.get("billingAddressStreet"), true);
		sugar().contacts.createDrawer.getEditField("primaryAddressCity").assertEquals(myAccount.get("billingAddressCity"), true);
		sugar().contacts.createDrawer.getEditField("primaryAddressState").assertEquals(myAccount.get("billingAddressState"), true);
		sugar().contacts.createDrawer.getEditField("primaryAddressPostalCode").assertEquals(myAccount.get("billingAddressPostalCode"), true);
		sugar().contacts.createDrawer.getEditField("primaryAddressCountry").assertEquals(myAccount.get("billingAddressCountry"), true);
		sugar().contacts.createDrawer.getEditField("lastName").set(myAccount.getRecordIdentifier());
		sugar().contacts.createDrawer.save();

		FieldSet con = new FieldSet();
		con.put("primaryAddressStreet", myAccount.get("billingAddressStreet"));
		con.put("primaryAddressCity", myAccount.get("billingAddressCity"));
		con.put("primaryAddressState", myAccount.get("billingAddressState"));
		con.put("primaryAddressPostalCode", myAccount.get("billingAddressPostalCode"));
		con.put("primaryAddressCountry", myAccount.get("billingAddressCountry"));
		con.put("lastName", myAccount.getRecordIdentifier());

		ContactRecord myCon = new ContactRecord(con);		
		myCon.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}