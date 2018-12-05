package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.PortalTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Portal_21692_Cases extends PortalTest {
	ContactRecord myContact;
	VoodooControl portalSettings, layout, casesModule, recordView, publishBtn; 

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		portalSettings = sugar().admin.adminTools.getControl("portalSettings");
		
		// TODO: VOOD-1119
		layout = new VoodooControl("a", "css", "#Layouts tr:nth-child(2) td a");
		casesModule = new VoodooControl("a", "css", "#Buttons td:nth-child(2) tr:nth-child(2) td a");
		recordView = new VoodooControl("a", "css", "#viewBtnRecordView tr:nth-child(2) td a");
		publishBtn = new VoodooControl("input", "id", "publishBtn");
		
		// 1 Account and 1 Case record created via API
		sugar().accounts.api.create();
		sugar().cases.api.create();
		sugar().login();

		// Enable portal in admin, portal contact setup
		sugar().admin.portalSetup.enablePortal();
		myContact = (ContactRecord)sugar().contacts.api.create(portalSetupData);

		// Case module enable - link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Verify admin user can edit portal record view layout. (For Cases)
	 * @throws Exception
	 */
	@Test
	public void Portal_21692_Cases_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin -> Sugar Portal - Layouts -> Cases -> Record view
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Portal settings
		portalSettings.click();
		layout.click();
		VoodooUtils.waitForReady();

		// For Cases
		casesModule.click();
		VoodooUtils.waitForReady();
		recordView.click();
		VoodooUtils.waitForReady();

		// Add & remove some fields to detail view layout ('Description' field is replaced by 'Source')
		new VoodooControl("div", "css", "#toolbox div[data-name='source']").dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) div[data-name='description']"));

		// Save and deploy.
		publishBtn.click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Enable shows in portal field in Cases
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));

		// TODO: VOOD-1139
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set(Boolean.toString(true));
		sugar().cases.recordView.save();
		sugar().logout();

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// For Cases
		portal().navbar.navToModule(sugar().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1116
		new VoodooControl("a", "css", "#content td:nth-child(2) div a").click();
		portal().alerts.waitForLoadingExpiration();
		portal().cases.recordView.showMore();

		// Verify 'Source' field is on record view, while 'description' field is not
		new VoodooControl("div", "css", ".record-label[data-name='source']").assertEquals(testData.get(testName).get(0).get("source_lbl_txt"), true);
		new VoodooControl("div", "css", ".record-label[data-name='description']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}