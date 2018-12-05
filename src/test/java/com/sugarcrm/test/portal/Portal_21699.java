package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21699 extends PortalTest {
	FieldSet customData = new FieldSet();
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().bugs.api.create();
		sugar().accounts.api.create();

		// TODO: VOOD-1108
		// Create portal set up
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);

		sugar().login();

		// TODO: VOOD-1108
		// Relate contact with account
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// Custom Enable portal in admin (code from library itself)
		// TODO: VOOD-1030 - Portal application timing 
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.portalSetup.getControl("configurePortal").click();
		VoodooUtils.waitForReady();
		VoodooControl enablePortalCtrl = sugar().admin.portalSetup.configurePortal.getControl("enablePortal");
		enablePortalCtrl.waitForVisible(60000);
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		enablePortalCtrl.set("true");
		sugar().admin.portalSetup.configurePortal.getControl("logoURL").set(customData.get("logo_url"));
		sugar().admin.portalSetup.configurePortal.getControl("maxQueryResult").set(customData.get("modified_page_record"));
		sugar().admin.portalSetup.configurePortal.getControl("defaultUser").set(customData.get("assigned_user_for_portal_reg"));
		sugar().admin.portalSetup.configurePortal.getControl("save").click();
		sugar().admin.studio.waitForAJAX(60000);
		VoodooUtils.focusDefault();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Create bugs
		sugar().bugs.navToListView();
		sugar().bugs.listView.create();

		// TODO: VOOD-1139
		VoodooControl showsInPortalCtrl = new VoodooControl("input", "css", "span.fld_portal_viewable.edit input");

		// relAssignedTo field set below as:
		// qauser with shows in portal (active)
		// qauser with shows in portal (active)
		// qauser with shows in portal (inactive)
		// administrator with shows in portal (active)
		VoodooControl nameCtrl = sugar().bugs.createDrawer.getEditField("name");
		VoodooControl assignedUserCtrl = sugar().bugs.createDrawer.getEditField("relAssignedTo");
		// TODO: VOOD-1925 loop sequence anti-pattern
		for (int i = 0; i < 4; i++) {
			nameCtrl.set(customData.get("bug_name") + (i+1));
			sugar().bugs.createDrawer.showMore();
			if(i != 3) { // assigned user
				assignedUserCtrl.set(customData.get("assigned_user_for_portal_reg"));
			}
			if ( i != 2) { // shows in portal field as checked
				showsInPortalCtrl.set("true");
			}
			sugar().bugs.createDrawer.save();
			sugar().bugs.listView.create();
		}

		sugar().bugs.createDrawer.cancel(); // dismiss bug drawer

		sugar().logout();
	}

	/**
	 * Verify admin used use can config portal from sugar portal editor
	 * @throws Exception
	 */
	@Test
	public void Portal_21699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));

		// Navigate to portal URL
		portal().loginScreen.navigateToPortal();

		// TODO: VOOD-832
		// Verify Logo URL
		new VoodooControl("img", "css", ".welcome img").assertAttribute("src", customData.get("logo_url"), true);

		// login as portal user
		portal().login(portalUser);

		// Bugs
		// TODO: VOOD-1031
		sugar().navbar.clickModuleDropdown(sugar().bugs);

		// Verify View Bugs view
		portal().bugs.menu.getControl("viewBugs").click();
		portal().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1047, VOOD-1096
		// Verify records as per page record limit
		new VoodooControl("tr", "css", "#content tbody tr").assertVisible(true);
		new VoodooControl("tr", "css", "#content tbody tr:nth-of-type(2)").assertVisible(true);

		// verify third record is not visible
		VoodooControl thirdRecordCtrl = new VoodooControl("tr", "css", "#content tbody tr:nth-of-type(3)");
		thirdRecordCtrl.assertVisible(false);

		// More button
		new VoodooControl ("button", "css", "#content div.main-pane.span8 div:nth-child(4) button").click();
		sugar().alerts.waitForLoadingExpiration();

		// verify third record is visible after more click
		thirdRecordCtrl.assertVisible(true);

		// logout from portal user
		portal().logout();

		// Verifying Default user assigned for New Portal Registrations
		portal().loginScreen.startSignup();

		// portal sign-up page
		portal().signupScreen.getControl("firstName").set(customData.get("firstName"));
		portal().signupScreen.getControl("lastName").set(customData.get("lastName"));
		portal().signupScreen.getControl("email").set(customData.get("email"));
		portal().signupScreen.getControl("country").set(customData.get("country"));
		portal().signupScreen.getControl("company").set(customData.get("company"));
		portal().signupScreen.getControl("signup").click();
		portal().alerts.waitForLoadingExpiration();
		portal().signupScreen.getControl("back").click();

		// Navigate to actual (i.e Sugar) login URL
		sugar().loginScreen.navigateToSugar();
		sugar().login();

		// Verify lead record created after Portal sign up with relassignedTo as qauser
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "relAssignedTo", customData.get("assigned_user_for_portal_reg"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}