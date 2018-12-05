package com.sugarcrm.test.accounts;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_18319 extends SugarTest {

	public void setup() throws Exception {
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().cases.api.create();
		sugar().contacts.api.create();
		sugar().login();

		// relate account with contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();

		// relate account with cases
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().cases.recordView.save();
	}

	/**
	 * Preview record from subpanel list
	 * @throws Exception
	 */
	@Test
	public void Accounts_18319_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).hover();
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).clickPreview(1);

		sugar().previewPane.setModule(sugar().contacts);
		
		// Verify that RHS preview pane render with the detail fields of the record
		sugar().previewPane.getPreviewPaneField("fullName").assertElementContains(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().previewPane.getPreviewPaneField("department").assertElementContains(sugar().contacts.getDefaultData().get("department"), true);
		sugar().previewPane.getPreviewPaneField("phoneMobile").assertElementContains(sugar().contacts.getDefaultData().get("phoneMobile"), true);
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).hover();

		sugar().previewPane.setModule(sugar().cases);
		
		// Verify another record RHS preview pane render with the detail fields of the record
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).clickPreview(1);
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("name").assertElementContains(sugar().cases.getDefaultData().get("name"), true);
		sugar().previewPane.getPreviewPaneField("resolution").assertElementContains(sugar().cases.getDefaultData().get("resolution"), true);
		sugar().previewPane.getPreviewPaneField("description").assertElementContains(sugar().cases.getDefaultData().get("description"), true);
		sugar().previewPane.getPreviewPaneField("relAccountName").assertElementContains(sugar().cases.getDefaultData().get("relAccountName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
