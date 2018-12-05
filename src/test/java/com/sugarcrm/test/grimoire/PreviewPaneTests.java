package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class PreviewPaneTests extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	@Test
	public void listViewAndSubpanelPreview() throws Exception {
		VoodooUtils.voodoo.log.info("Running listViewAndSubpanelPreview()...");

		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// TODO: VOOD-473
		// Verify single element on preview pane from listview manually
		VoodooControl topNavLink = new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='Accounts']"); 
		if(!(topNavLink.queryVisible())) {
			// If the link in the top nav is hidden, use the overflow menu instead. 
			new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='Accounts']").click();
		} else {
			topNavLink.click();
		}
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.previewRecord(01);
		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.getPreviewPaneField("name").assertContains(myAccount.getRecordIdentifier(), true);

		// Verify all elements on preview pane from listview automatically 
		myAccount.verifyPreviewListView();

		// Verify all elements on preview pane from subpanel automatically
		myContact.verifyPreviewSubpanel(myAccount);

		VoodooUtils.voodoo.log.info("listViewAndSubpanelPreview() complete.");
	}

	@Test
	public void previewNextRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running previewNextRecord()...");

		// Create 2 contact records 
		FieldSet fs = new FieldSet();
		fs.put("lastName", "FirstRecord");
		sugar().contacts.api.create(fs);

		fs.put("lastName", "SecondRecord");
		sugar().contacts.api.create(fs);

		// Preview first contact record in the list view 
		sugar().contacts.navToListView();
		sugar().contacts.listView.previewRecord(1);

		VoodooControl fullName = sugar().previewPane.getPreviewPaneField("fullName");
		// Verify that correct record is previewed
		fullName.assertContains("SecondRecord", true);

		// Preview next record in the list 
		sugar().previewPane.gotoNextRecord();

		// Verify that correct record is previewed
		fullName.assertContains("FirstRecord", true);

		VoodooUtils.voodoo.log.info("previewNextRecord() complete.");
	}

	@Test
	public void previewPreviousRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running previewPreviousRecord()...");

		// Create 2 contact records 
		FieldSet fs = new FieldSet();
		fs.put("lastName", "FirstRecord");
		sugar().contacts.api.create(fs);

		fs.put("lastName", "SecondRecord");
		sugar().contacts.api.create(fs);

		// Preview second contact record in the list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.previewRecord(2);

		VoodooControl fullName = sugar().previewPane.getPreviewPaneField("fullName");
		// Verify that correct record is previewed
		fullName.assertContains("FirstRecord", true);

		// Preview previous record in the list 
		sugar().previewPane.gotoPreviousRecord();

		// Verify that correct record is previewed
		fullName.assertContains("SecondRecord", true);

		VoodooUtils.voodoo.log.info("previewPreviousRecord() complete.");
	}

	public void cleanup() throws Exception {}
}