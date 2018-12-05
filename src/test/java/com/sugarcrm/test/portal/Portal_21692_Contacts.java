package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Portal_21692_Contacts extends PortalTest {
	ContactRecord myContact;
	VoodooControl portalSettings, layout, contactsModule, recordView, publishBtn;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		portalSettings = sugar().admin.adminTools.getControl("portalSettings");

		// TODO: VOOD-1119
		layout = new VoodooControl("a", "css", "#Layouts tr:nth-child(2) td a");
		contactsModule = new VoodooControl("a", "css", "#Buttons td:nth-child(3) tr:nth-child(2) td a");
		recordView = new VoodooControl("a", "css", "#viewBtnRecordView tr:nth-child(2) td a");
		publishBtn = new VoodooControl("input", "id", "publishBtn");
		sugar().login();

		// Enable portal in admin, portal contact setup
		sugar().admin.portalSetup.enablePortal();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
	}

	/**
	 * Verify admin user can edit portal record view layout. (For Contacts)
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21692_Contacts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin -> Sugar Portal - Layouts -> Bugs -> Record view and verify the layout change is save and deployed in sugar side.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Portal settings
		portalSettings.click();
		VoodooUtils.waitForReady();
		layout.click();
		VoodooUtils.waitForReady();

		// For Contacts
		contactsModule.click();
		VoodooUtils.waitForReady();
		recordView.click();
		VoodooUtils.waitForReady();

		// Add & remove some fields to detail view layout ('Title' field is replaced by 'Alternate Address City')
		new VoodooControl("div", "css", "#toolbox div[data-name='alt_address_city']").dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) div[data-name='title']"));

		// Save and deploy.
		publishBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// For Portal Contact profile
		portal().navbar.navToProfile();

		// Verify 'Address city' field is on record view, while 'title' field is not
		new VoodooControl("div", "css", ".record-label[data-name='alt_address_city']").assertEquals(testData.get(testName).get(0).get("alternate_address_city_lbl_txt"), true);
		new VoodooControl("div", "css", ".record-label[data-name='title']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}