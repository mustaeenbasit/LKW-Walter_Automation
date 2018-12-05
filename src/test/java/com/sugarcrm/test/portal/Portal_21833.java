package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21833 extends PortalTest {
	FieldSet customData, portalUserSetting;
	CaseRecord myCase1, myCase2;
	AccountRecord myAccount;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		portalUserSetting = testData.get("env_portal_contact_setup").get(0);
		FieldSet newCase = new FieldSet();
		newCase.put("name", customData.get("case_name1"));
		myCase1 = (CaseRecord) sugar().cases.api.create(newCase);
		newCase.clear();
		newCase.put("name", customData.get("case_name2"));
		myCase2 = (CaseRecord) sugar().cases.api.create(newCase);
		
		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();

		myAccount = (AccountRecord)sugar().accounts.api.create();

		// Create a new contact with portal login info.
		myContact = (ContactRecord) sugar().contacts.api.create(portalUserSetting);

		// Add Account to Contact
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
			
		// Link account with cases
		myCase1.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().cases.recordView.edit();
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set("true");
		sugar().cases.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(2);
		sugar().cases.recordView.edit();
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set("true");
		sugar().cases.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().logout();
	}
	
	/**
	 * Verify that columns can be sorted in order in cases module of portal list view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21833_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", portalUserSetting.get("portalName"));
		portalUser.put("password", portalUserSetting.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify search case by value in "Subject"
		for(int i = 1; i < 3; i++) { // Loop fixed for two times
			new VoodooControl("input", "css", "[data-voodoo-name='Cases'] div:nth-child(2) input").assertAttribute("placeholder", "Search by: Subject, Number", true);
			new VoodooControl("input", "css", "[data-voodoo-name='Cases'] div:nth-child(2) input").set(customData.get("case_name"+i));
			VoodooUtils.waitForReady();
			new VoodooControl("td", "css", "[data-voodoo-name='Cases'] div:nth-child(3) table tbody tr:nth-of-type(1) td:nth-child(2)").assertContains(customData.get("case_name"+i), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}