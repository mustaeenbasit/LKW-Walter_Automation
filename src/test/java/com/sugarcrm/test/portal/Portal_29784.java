package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_29784 extends PortalTest {
	ContactRecord myContact;
	FieldSet noteData = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().bugs.api.create();

		// Portal user created
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalContactData);

		// Enable Bugs module
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal and set Max query result to '2' in admin
		noteData = testData.get(testName).get(0);
		FieldSet portalSettings = new FieldSet();
		portalSettings.put("enablePortal", noteData.get("enablePortal"));
		portalSettings.put("maxQueryResult", noteData.get("maxResult"));
		sugar().admin.portalSetup.configurePortal(portalSettings);

		// Relate contact with account
		// TODO: VOOD-1108
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// go to bugs listview
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);
		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.showMore();

		// Enable show in portal checkbox 
		// TODO: VOOD-1139
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set(noteData.get("enablePortal"));
		sugar().bugs.recordView.save();
		sugar().logout();
	}

	/**
	 * Verify in Portal, after click into the "Show More" button user should get all list of Notes
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_29784_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to portal URL, login as portal user
		portal().loginScreen.navigateToPortal();
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().login(portalUser);

		// Bugs
		portal().navbar.navToModule(sugar().bugs.moduleNamePlural);
		portal().bugs.listView.getDetailField(1, "name").click();

		// Adding notes in Notes subpanel of Bugs module in Portal
		// TODO: VOOD-1049 : Support subpanel in Portal
		VoodooControl noteAdd = new VoodooControl("a", "css", ".btn.addNote");
		VoodooControl noteDesc = new VoodooControl("textarea", "css", ".edit.fld_description textarea");
		VoodooControl noteName = new VoodooControl("input", "css", ".edit.fld_name input");
		VoodooControl noteSave = new VoodooControl("a", "css", ".fld_save_button a");

		// Adding three records in the notes subpanel
		int notesRecCount = Integer.parseInt(noteData.get("notesCount"));
		for (int i = 1; i <= notesRecCount; i++) {
			noteAdd.click();
			noteName.set(noteData.get("note" + i));
			noteDesc.set(testName);
			noteSave.click();
		}

		// Asserting only 2 records visible in note subpanel
		VoodooUtils.waitForReady();
		VoodooControl resultSet = new VoodooControl("ul", "css", ".nav-tabs.results");
		resultSet.scrollIntoViewIfNeeded(false);
		resultSet.assertContains(noteData.get("note3"), true);
		resultSet.assertContains(noteData.get("note2"), true);
		resultSet.assertContains(noteData.get("note1"), false);

		// Click 'Show More' link
		VoodooControl showMore = new VoodooControl("a", "css", ".fld_show_more_button a");
		showMore.click();
		VoodooUtils.waitForReady();

		// Asserting all the three records visible in notes subpanel
		for (int i = 1; i <= notesRecCount; i++) {
			resultSet.assertContains(noteData.get("note" + i), true);
		}

		// Verify 'Show More' link no more visible
		showMore.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}