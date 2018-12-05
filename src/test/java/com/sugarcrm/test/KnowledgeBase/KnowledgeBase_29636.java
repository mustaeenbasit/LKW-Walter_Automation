package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29636 extends SugarTest {
	FieldSet portalUser1 = new FieldSet();
	FieldSet portalUser2 = new FieldSet();
	FieldSet customData = new FieldSet();
	KBRecord myKBRecord;
	
	public void setup() throws Exception {
		
		// Initialize test data 
		sugar().accounts.api.create();
		customData = testData.get(testName).get(0);
		DataSource portalContactData = testData.get(testName + "_portalContacts");
		
		// Making a new KB article as externally (portally) visible with status as Published
		FieldSet kbData = new FieldSet();
		kbData.put("isExternal", customData.get("isExternal"));
		kbData.put("status", customData.get("status"));
		myKBRecord = (KBRecord) sugar().knowledgeBase.api.create(kbData);
		
		// Creating portally active contacts
		ContactRecord contact1 = (ContactRecord)sugar().contacts.api.create(portalContactData.get(0));
		ContactRecord contact2 = (ContactRecord)sugar().contacts.api.create(portalContactData.get(1));
		
		// Initialize portal user data 
		portalUser1.put("userName", contact1.get("portalName"));
		portalUser1.put("password", contact1.get("password"));
		portalUser2.put("userName", contact2.get("portalName"));
		portalUser2.put("password", contact2.get("password"));
		
		// Logging in as admin
		sugar().login();
		
		// Relate 2 Contacts to Account
		// TODO: VOOD-1108 - Provide a Portal Setup method
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		FieldSet updateContactsFS = new FieldSet();
		updateContactsFS.put("relAccountName", sugar().accounts.getDefaultData().get("name"));
		
		sugar().contacts.listView.editRecord(2);
		sugar().contacts.listView.setEditFields(2, updateContactsFS);
		// TODO: VOOD-2161 Warning thrown after choosing "Account Name" value during mass update of Contacts/Leads should be handled
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.listView.saveRecord(2);
		
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.setEditFields(1, updateContactsFS);
		// TODO: VOOD-2161 Warning thrown after choosing "Account Name" value during mass update of Contacts/Leads should be handled
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.listView.saveRecord(1);
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();
		
		// Logout admin
		sugar().logout();
	}

	/**
	 * Verify the "Usefulness for Articles" are reflected correctly with portal user and sugar user
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29636_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Login as qaUser
		FieldSet qaUser = sugar().users.getQAUser();
		sugar().login(qaUser);
		
		// Navigate to KB module, Open KB1 record view
		myKBRecord.navToRecord();

		// Select My Dashboard from the right hand side- "Usefulness for Articles" Dashlet is in "My Dashboard".
		sugar().dashboard.chooseDashboard(customData.get("myDashboard"));

		// Click on Useful button
		// TODO: VOOD-1783 - Need lib support for Vote buttons
		new VoodooControl("a", "css", "a[data-action='useful']").click();
		
		// Verify the percentage in the Dashlet is 100%
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("a", "css", ".nv-pie-hole-value").assertEquals(customData.get("hundred"), true);

		// Logout qauser
		sugar().logout();
		// Navigate to portal URL
		portal.loginScreen.navigateToPortal();
		
		// Login as portal user1
		portal.login(portalUser1);
		
		// Open KB1 and click on "Not Useful" button.
		// TODO: VOOD-1096 - Portal Module Listview support portal.knowledgeBase.listView.clickRecord(1); is not working
		VoodooControl firstRecord = new VoodooControl("a", "css", ".list.fld_name a");
		firstRecord.click();

		// TODO: VOOD-1783 - Need lib support for Vote buttons
		VoodooControl notUsefulButton = new VoodooControl("a", "css", ".detail.fld_usefulness a");
		notUsefulButton.click();
		
		// Logout from portal
		portal.logout();
		
		// Login as qauser and verify the percentage in the Dashlet is 50%
		sugar().loginScreen.navigateToSugar();
		sugar().login(qaUser);
		
		// Navigate to KB record view
		myKBRecord.navToRecord();
		
		// Verify the percentage in the Dashlet is 50 %
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("a", "css", ".nv-pie-hole-value").assertEquals(customData.get("fifty"), true);

		// Logout admin and Navigate to portal URL
		sugar().logout();
		portal.loginScreen.navigateToPortal();
		
		// Login as portal user1
		portal.login(portalUser2);
		
		// Open KB1 and click on "Not Useful" button.
		// TODO: VOOD-1096 : Portal Module Listview support, portal.knowledgeBase.listView.clickRecord(1); is not working
		firstRecord.click();

		// Click on "Not Useful" button.
		// TODO: VOOD-1783 - Need lib support for Vote buttons
		notUsefulButton.click();
		
		// Logout from portal
		portal.logout();
		
		// Login as qaUser and verify the percentage in the Dashlet is 33%
		sugar().loginScreen.navigateToSugar();
		sugar().login(qaUser);
		
		// Navigate to KB record view
		myKBRecord.navToRecord();
		
		// Verify the percentage in the Dashlet is 33 %
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("a", "css", ".nv-pie-hole-value").assertEquals(customData.get("thirtyThree"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}