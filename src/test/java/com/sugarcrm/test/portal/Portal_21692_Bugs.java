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
public class Portal_21692_Bugs extends PortalTest {
	ContactRecord myContact;
	VoodooControl portalSettings, layout, bugsModule, recordView, publishBtn;

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		portalSettings = sugar().admin.adminTools.getControl("portalSettings");
		layout = new VoodooControl("a", "css", "#Layouts tr:nth-child(2) td a");
		bugsModule = new VoodooControl("a", "css", "#Buttons td:nth-child(1) tr:nth-child(2) td a");
		recordView = new VoodooControl("a", "css", "#viewBtnRecordView tr:nth-child(2) td a");
		publishBtn = new VoodooControl("input", "id", "publishBtn");
		sugar().accounts.api.create();
		sugar().bugs.api.create();
		sugar().login();

		// Enable bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal in admin, portal contact setup
		sugar().admin.portalSetup.enablePortal();
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
	}

	/**
	 * Verify admin user can edit portal record view layout (For Bugs)
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21692_Bugs_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1119
		// Navigate to Admin -> Sugar Portal - Layouts -> Bugs -> Record view and verify the layout change is save and deployed in sugar side.
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		layout.click();
		VoodooUtils.waitForReady();

		// For Bugs
		bugsModule.click();
		VoodooUtils.waitForReady();
		bugsModule.click();
		VoodooUtils.waitForReady();

		// Add & remove some fields to detail view layout ('Source' field is replaced by 'Show in Portal')
		new VoodooControl("div", "css", "#toolbox div[data-name='portal_viewable']").dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) div[data-name='source']"));

		// Save and deploy.
		publishBtn.click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// go to bugs listview
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);

		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.showMore();

		// TODO: VOOD-1139
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set(Boolean.toString(true));
		sugar().bugs.recordView.save();
		sugar().logout();

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Verify the layout change is updated to portal side.
		// For Bugs
		portal().navbar.navToModule(sugar().bugs.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1116
		new VoodooControl("a", "css", "#content td:nth-child(2) div a").click();
		portal().alerts.waitForLoadingExpiration();
		portal().bugs.recordView.showMore();

		// Verify 'show in portal' field is on record view, while 'source' field is not
		new VoodooControl("div", "css", "#content div[data-name='portal_viewable'].record-label").assertEquals(testData.get(testName).get(0).get("show_in_portal_lbl_txt"), true);
		new VoodooControl("div", "css", "#content div[data-name='source'].record-label").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}