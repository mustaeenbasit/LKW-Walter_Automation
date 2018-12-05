package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_27785 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
		
		// In Studio > Contacts > Layouts > Record, add Birthdate field and save.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-542
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Layout subpanel
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Record view
		VoodooControl recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "[data-name='birthdate']").dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// In Admin > Role Management > Sales Admin, set Contacts > Birthdate to Read-Only.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();
		
		// Click on 'Sales Administrator'
		VoodooUtils.focusFrame("bwc-frame");
		// xpath needed here because there is neither proper id (or class) available for 'Sales Administrator' nor it has stable position.
		VoodooControl salesAdministrator = new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a");
		salesAdministrator.click();
		
		// Set Birthdate to 'Read Only'
		// TODO: VOOD-580
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#contentTable tr:nth-of-type(6) td a").click();
		new VoodooControl("div", "css", ".detail.view #birthdatelink").click();
		new VoodooControl("select", "id", "flc_guidbirthdate").set("Read Only");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Log out of Sugar as Admin and log in as QAuser.
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Read-Only Role for field should be saved in Convert Lead view.
	 * @throws Exception
	 */
	@Test
	public void Roles_27785_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a contact record.
		sugar().contacts.api.create();
		sugar().contacts.navToListView();
		
		// Verify that contact record is saved without issue.
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));
		
		// Open Unconverted Lead.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Click on the arrow drop down to view the action links
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585
		// Click on Convert action link
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Confirm with Accounts info
		new VoodooControl("input", "css",
				"div[data-module='Accounts'] .fld_name.edit input").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css",
				"div[data-module='Accounts'] .fld_associate_button.convert-panel-header a")
				.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Click on 'Save and Convert' button.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a")
				.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that record is successfully save and converted.
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("status").assertContains("Converted", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
