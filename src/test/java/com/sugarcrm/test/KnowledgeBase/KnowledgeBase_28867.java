package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28867 extends SugarTest {
	NoteRecord myNotes;
	ContactRecord myContact;
	UserRecord customAdminUser;
	FieldSet kbData = new FieldSet();

	public void setup() throws Exception {
		kbData = testData.get(testName).get(0);

		// Creating a KB record with status='Publish' & isExternal= checked
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("status", kbData.get("status"));
		fs.put("isExternal", kbData.get("isExternal"));
		sugar().knowledgeBase.api.create(fs);
		fs.clear();

		// Creating a note record with DisplayInPortal checked
		fs.put("checkDisplayInPortal", kbData.get("checkDisplayInPortal"));
		myNotes = (NoteRecord) sugar().notes.api.create(fs);

		// Creating a Contact record with Portal Data
		FieldSet portalData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalData);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating a custom admin user
		customAdminUser = (UserRecord)sugar().users.create();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-987
		new VoodooControl("select", "id", "UserType").set(kbData.get("userType"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();
		sugar().logout();
	}

	/**
	 * Verify users can see notes in Sugar and Portal, and can add "Notes and Attachments" dashlet 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28867_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		try {
			// Login with custom Admin User
			customAdminUser.login();

			// Navigating record view of KB record
			sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
			sugar().knowledgeBase.listView.clickRecord(1);

			// Clicking link existing record in Notes subpanel of KB
			// TODO: VOOD-1815
			StandardSubpanel notesSubpanel = sugar().knowledgeBase.recordView.subpanels.get(sugar().notes.moduleNamePlural);
			notesSubpanel.scrollIntoViewIfNeeded(false);
			notesSubpanel.linkExistingRecord(myNotes);

			// Adding Dashlet Notes and Attachment in the Knowledge Base record view
			sugar().knowledgeBase.dashboard.clickCreate();
			sugar().knowledgeBase.dashboard.getControl("title").set(testName);
			sugar().knowledgeBase.dashboard.addRow();
			sugar().knowledgeBase.dashboard.addDashlet(1, 1);

			// TODO: VOOD-960
			new VoodooControl("input", "css", ".span4.search").set(kbData.get("description"));
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", "tr.single .fld_title a").click();
			new VoodooControl("a", "css", ".active .fld_save_button a").click();
			sugar().knowledgeBase.dashboard.save();

			// Asserting the values in notes subpanel and 'Notes and Attachment' dashlet in the Knowledge Base record view
			// TODO: VOOD-670
			notesSubpanel.scrollIntoViewIfNeeded(false);
			String defaultNoteSubject = sugar().notes.getDefaultData().get("subject");
			new VoodooControl("a", "css", ".list.fld_name a").assertEquals(defaultNoteSubject, true);
			new VoodooControl("a", "css", ".dashlet-content p a").assertEquals(defaultNoteSubject, true);
			sugar().logout();

			// Navigate to portal URL, login as portal user
			portal.loginScreen.navigateToPortal();
			FieldSet portalUser = new FieldSet();
			portalUser.put("userName", myContact.get("portalName"));
			portalUser.put("password", myContact.get("password"));
			portal.login(portalUser);

			// Navigate to KnowledgeBase
			portal.navbar.navToModule(portal.knowledgeBase.moduleNamePlural);

			// Clicking KB record in KB list view
			// TODO: VOOD-1096 : Portal Module Listview support
			new VoodooControl("a", "css", ".list.fld_name a").click();

			// Adding a note to the KB subpanel in Portal
			// TODO: VOOD-1049 : Support subpanel in Portal
			new VoodooControl("a", "css", ".activity a").assertEquals(sugar().notes.getDefaultData().get("subject"), true);
			new VoodooControl("a", "css", ".btn.addNote").click();
			new VoodooControl("input", "css", ".edit.fld_name input").set(kbData.get("secondNote"));
			new VoodooControl("textarea", "css", ".edit.fld_description textarea").set(kbData.get("description"));
			new VoodooControl("a", "css", ".fld_save_button a").click();
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".activity a").assertEquals(kbData.get("secondNote"), true);
			portal.logout();

			// Logging in as regular user
			sugar().loginScreen.navigateToSugar();
			sugar().login(sugar().users.getQAUser());

			// Navigating to KB list view
			sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
			sugar().knowledgeBase.listView.clickRecord(1);

			// Asserting the records in Notes subpanel of KB
			// TODO: VOOD-609
			notesSubpanel.scrollIntoViewIfNeeded(true);
			new VoodooControl("a", "css", ".dataTable .fld_name a").assertEquals(kbData.get("secondNote"), true);
			new VoodooControl("a", "css", "tr:nth-child(2) .fld_name a").assertEquals(defaultNoteSubject, true);
		}
		finally {
			// Logging out from sugar and logging in to sugar as default admin user
			sugar().loginScreen.navigateToSugar();
			sugar().logout();
			sugar().login();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}