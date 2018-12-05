package com.sugarcrm.test.portal;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21711 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalContact = new FieldSet();

	public void setup() throws Exception {
		portalContact = testData.get("env_portal_contact_setup").get(0);

		// Setup Portal Access
		// Create a new contact with portal login info.
		sugar().accounts.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create(portalContact);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.portalSetup.enablePortal();

		// TODO: VOOD-1108
		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
		sugar().logout();
	}

	/**
	 * Verify portal user can edit the profile information.
	 * @throws Exception
	 */
	
	@Test
	public void Portal_21711_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet userRecord = testData.get(testName).get(0);

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();

		// login to portal
		portal().login(portalUser);
		portal().navbar.navToProfile();

		// TODO: VOOD-1053 
		// Modify user record i.e. password, email, first name, last name, phone number, country, state.
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary[name='edit_button']").click();
		new VoodooControl("input", "css", ".fld_first_name.edit [name='first_name']").set(userRecord.get("firstName"));
		new VoodooControl("input", "css", ".fld_last_name.edit [name='last_name']").set(userRecord.get("lastName"));
		new VoodooControl("input", "css", ".fld_email.edit input").set(userRecord.get("email"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='current_password']").set(portalContact.get("password"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='new_password']").set(userRecord.get("newPassword"));
		new VoodooControl("input", "css", ".fld_portal_password.edit [name='confirm_password']").set(userRecord.get("newPassword"));
		new VoodooControl("input", "css", ".fld_phone_work.edit input").set(userRecord.get("phoneWork"));
		new VoodooControl("input", "css", ".fld_primary_address_city.edit input").set(userRecord.get("primaryAddressCity"));
		new VoodooControl("input", "css", ".fld_primary_address_state.edit input").set(userRecord.get("primaryAddressState"));

		// save record 
		new VoodooControl("a", "css", ".fld_save_button.detail a").click();	
		sugar().alerts.waitForLoadingExpiration();

		// Assert that all data updated successfully
		new VoodooControl("a", "css", "[data-name='full_name']").assertContains(userRecord.get("firstName") +" " + userRecord.get("lastName") , true);
		new VoodooControl("span", "css", ".fld_email.detail").assertEquals(userRecord.get("email"), true);
		new VoodooControl("span", "css", "[data-name='phone_work']").assertContains(userRecord.get("phoneWork"), true);
		new VoodooControl("span", "css", "[data-name='primary_address_city']").assertContains(userRecord.get("primaryAddressCity"), true);
		new VoodooControl("span", "css", "[data-name='primary_address_state']").assertContains(userRecord.get("primaryAddressState"), true);
		portal().logout();

		// Log into Portal again to verify password updated successfully 
		portalUser.put("password",userRecord.get("newPassword"));

		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
