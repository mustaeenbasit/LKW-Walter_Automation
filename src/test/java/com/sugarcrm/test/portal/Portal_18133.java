package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Portal_18133 extends PortalTest {
	ContactRecord myContact;
	FieldSet portalSetupData;
	AccountRecord relAcc;

	public void setup() throws Exception {
		portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().cases.api.create(); // shows in portal field inactive (i.e by design)
		relAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();

		// Create portal set up
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
	}

	/**
	 * Verify portal cases list view should respect portal ACLs 
	 * @throws Exception
	 */
	@Test
	public void Portal_18133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link Account with contacts & cases subpanel
		relAcc.navToRecord();
		StandardSubpanel subpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		subpanel.linkExistingRecord(myContact);
		subpanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		subpanel.scrollIntoViewIfNeeded(false);
		subpanel.addRecord();
		sugar().cases.createDrawer.getEditField("name").set(testData.get(testName).get(0).get("name"));

		// TODO: VOOD-1139
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set(Boolean.toString(true));
		sugar().cases.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().logout();

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));

		// login as portal user
		portal().login(portalUser);
		portal().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1121 & VOOD-1116
		// Verify only 1 case show on dashboard under Cases module
		new VoodooControl("a", "css", ".thumbnail.layout_Cases table tbody tr .fld_name a").assertEquals(testData.get(testName).get(0).get("name"), true);

		// TODO: VOOD-1031
		// View cases 
		sugar().navbar.clickModuleDropdown(sugar().cases);
		portal().cases.menu.getControl("viewCases").click();
		portal().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1121 & VOOD-1116
		// Verify case record
		new VoodooControl("a", "css", ".layout_Cases table tbody tr .fld_name a").assertEquals(testData.get(testName).get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 