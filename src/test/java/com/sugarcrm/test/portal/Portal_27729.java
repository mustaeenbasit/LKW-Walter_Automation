package com.sugarcrm.test.portal;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_27729 extends PortalTest {
	FieldSet  portalContactData, fs;
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		portalContactData = testData.get("env_portal_contact_setup").get(0);
		sugar().login();

		// Create account record 
		myAccount = (AccountRecord) sugar().accounts.api.create();

		// Create a case record 
		sugar().cases.api.create();

		// Enable sugar portal 
		sugar().admin.portalSetup.enablePortal();

		// Create portal set up
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);
		
		// Link Contact with Accounts
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Mass update case record 
	    sugar().cases.navToListView();
	    sugar().alerts.waitForLoadingExpiration();
	    sugar().cases.listView.toggleSelectAll();
	    FieldSet fieldsToUpdate= new FieldSet();
	    fieldsToUpdate.put(fs.get("show_in_portal"), fs.get("confirm_add"));
	    fieldsToUpdate.put(fs.get("account_name"), myAccount.getRecordIdentifier());
	    sugar().cases.massUpdate.performMassUpdate(fieldsToUpdate);
	    sugar().alerts.waitForLoadingExpiration();
	    
	    sugar().logout();
	}

	/**
	 * Verify Account name field is not available from Portal Cases list view and record view layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_27729_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName",myContact.get("portalName"));
		portalUser.put("password",myContact.get("password"));
		portal().loginScreen.navigateToPortal();

		// login to portal
		portal().login(portalUser);

		// Navigate to case module 
		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Verify "Account name" field is not listed in listview hidden and default columns.
		new VoodooControl("th", "css", "[data-fieldname='account_name']").assertExists(false);
		VoodooControl numberCtrl =  new VoodooControl("a", "css", "#content .list-view .fld_name.list a");

		// Click number in list view to navigate to case details view 
		numberCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify "Account name" field is not available in record view layout
		new VoodooControl("span", "css", "[data-voodoo-name='account_name']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
} 