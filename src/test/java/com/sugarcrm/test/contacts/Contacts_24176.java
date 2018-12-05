package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24176 extends SugarTest {
	ContactRecord myContact;
	NoteRecord myNote;
	DataSource noteDS;
	StandardSubpanel notesSub;

	public void setup() throws Exception {
		sugar().login();
		myContact = (ContactRecord) sugar().contacts.api.create();
		myNote = (NoteRecord) sugar().notes.api.create();
		noteDS = testData.get(testName);

		// Link note record with contact
		myContact.navToRecord();
		notesSub = sugar().contacts.recordView.subpanels.get("Notes");
		notesSub.clickLinkExisting();
		new VoodooControl("span", "css", ".fld_name.list").waitForVisible();
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Test Case 24176: Edit note or attachment_Verify that a related note can be edited from Notes subpanel of a contact record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24176_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inline-edit note's subject and save
		notesSub.editRecord(1);
		new VoodooControl("input", "css", ".fld_name.edit input").set(noteDS.get(0).get("subject"));
		notesSub.saveAction(1);
		VoodooUtils.waitForAlertExpiration();

		// Verify that subject value was correctly modified
		notesSub.verify(1, noteDS.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
