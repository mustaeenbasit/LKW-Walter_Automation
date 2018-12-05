package com.sugarcrm.test.roles;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_27771 extends SugarTest {
	FieldSet customData = new FieldSet();
	UserRecord jim;
	VoodooControl contactsButtonCtrl, salesAdminCtrl, editCtrl, donotCallCtrl, donotCallSelectCtrl, saveRoleCtrl;

	public void setup() throws Exception {	
		customData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that Read-Only Role for fields that are marked as Reportable can be Saved in Convert Lead view 
	 * @throws Exception
	 */
	@Test
	public void Roles_27771_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1200 - After resolved, we can use with default CSV data
		// Jim user
		FieldSet jimData = testData.get(testName+"_user").get(0);
		jim = (UserRecord)sugar().users.create(jimData);
		VoodooUtils.focusDefault();

		// Studio -> Contacts -> Do not Call (reportable is checked)	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		contactsButtonCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsButtonCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "do_not_call").click();
		VoodooUtils.waitForReady();

		// Verify Do not call field has reportable value set True
		assertTrue("reportable should be checked!", (Boolean.parseBoolean(new VoodooControl("input", "css", "input[name='reportableCheckbox']").getAttribute("checked"))));
		sugar().admin.studio.getControl("studioButton").click();
		VoodooUtils.waitForReady();
		contactsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// layout subpanel
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooUtils.waitForReady();

		// Verify do not call field is on record view
		new VoodooControl("div", "css", "#panels div[data-name='do_not_call']").assertExists(true);
		VoodooUtils.focusDefault();

		// In Admin > Role Management > Sales Admin, set Accounts > Type to Read-Only.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Sales Administrator'
		// xpath needed here because there is neither proper id (or class) available for 'Sales Administrator' nor it has stable position.
		// TODO: VOOD-580
		salesAdminCtrl = new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a");
		salesAdminCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Set Do not call to 'Read Only'
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		editCtrl = new VoodooControl("a", "css", "#contentTable tr:nth-of-type(6) td a");
		editCtrl.click();
		VoodooUtils.waitForReady();
		donotCallCtrl = new VoodooControl("div", "css", ".detail.view #do_not_calllink");
		donotCallCtrl.click();
		donotCallSelectCtrl = new VoodooControl("select", "id", "flc_guiddo_not_call");
		donotCallSelectCtrl.set(customData.get("read_permission"));
		saveRoleCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		saveRoleCtrl.click();
		VoodooUtils.waitForReady();

		// Jim user associate with Sales administrator Role
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "user_name_advanced").set(jim.get("userName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("td", "css", ".oddListRowS1 td:nth-of-type(3)").click();
		VoodooUtils.focusWindow(0);
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Log out of Sugar as Admin and log in as Jim 
		sugar().logout();
		sugar().login(jim);

		// Contact record with default data
		sugar().contacts.create();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("firstName"));

		// Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Convert Lead
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-585		
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Associate Account
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Save & Convert
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().leads.recordView.showMore();

		// Verify the Leads record is converted in record view
		sugar().leads.recordView.getDetailField("status").assertContains(customData.get("converted_lbl"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}