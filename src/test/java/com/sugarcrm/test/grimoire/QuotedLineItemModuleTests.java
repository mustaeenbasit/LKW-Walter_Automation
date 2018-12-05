package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class QuotedLineItemModuleTests extends SugarTest{
	String defaultPrice = "$0.00";

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().quotes.api.create();
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().quotedLineItems.navToListView();
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		// Verify all sort headers in listview
		for(String header : sugar().quotedLineItems.listView.getHeaders()){
			sugar().quotedLineItems.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().quotedLineItems.listView.clickRecord(1);

		// Verify subpanels
		sugar().quotedLineItems.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);
		sugar().quotedLineItems.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().quotedLineItems.listView.editRecord(1);

		// create relation because of TODO: VOOD-444
		// account name
		VoodooControl accountNamrFieldCtrl = sugar().quotedLineItems.listView.getEditField(1, "accountName");
		accountNamrFieldCtrl.set(sugar().quotedLineItems.getDefaultData().get("accountName"));
		accountNamrFieldCtrl.assertEquals(sugar().quotedLineItems.getDefaultData().get("accountName"), true);

		// status
		VoodooControl statusFieldCtrl = sugar().quotedLineItems.listView.getEditField(1, "status");
		statusFieldCtrl.set(sugar().quotedLineItems.getDefaultData().get("status"));
		statusFieldCtrl.assertEquals(sugar().quotedLineItems.getDefaultData().get("status"), true);

		// quote
		VoodooControl quoteNameFieldCtrl = sugar().quotedLineItems.listView.getEditField(1, "relQuoteName");
		quoteNameFieldCtrl.set(sugar().quotes.moduleNameSingular);
		quoteNameFieldCtrl.assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		// name
		sugar().quotedLineItems.listView.getEditField(1, "name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);

		// quantity
		sugar().quotedLineItems.listView.getEditField(1, "quantity").assertEquals(sugar().quotedLineItems.getDefaultData().get("quantity"), true);

		// cost price
		sugar().quotedLineItems.listView.getEditField(1, "cost").assertVisible(false);

		// unit price
		sugar().quotedLineItems.listView.getEditField(1, "unitPrice").assertContains("0.00", true);

		// discount amount
		sugar().quotedLineItems.listView.getEditField(1, "discountAmount").assertContains("0.00", true);

		// assigned to
		sugar().quotedLineItems.listView.getEditField(1, "assignedTo").assertEquals(sugar().quotedLineItems.getDefaultData().get("assignedTo"), true);

		// save the record with relation
		sugar().quotedLineItems.listView.saveRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// verify detail fields
		// name
		sugar().quotedLineItems.listView.getDetailField(1, "name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);

		// quantity
		sugar().quotedLineItems.listView.getDetailField(1, "quantity").assertEquals(sugar().quotedLineItems.getDefaultData().get("quantity"), true);

		// unit price
		sugar().quotedLineItems.listView.getDetailField(1, "unitPrice").assertEquals(defaultPrice, true);

		// cost price
		sugar().quotedLineItems.listView.getDetailField(1, "cost").assertEquals(defaultPrice, true);

		// discount amount
		sugar().quotedLineItems.listView.getDetailField(1, "discountAmount").assertEquals(defaultPrice, true);

		// account name
		sugar().quotedLineItems.listView.getDetailField(1, "accountName").assertEquals(sugar().quotedLineItems.getDefaultData().get("accountName"), true);

		// status
		sugar().quotedLineItems.listView.getDetailField(1, "status").assertEquals(sugar().quotedLineItems.getDefaultData().get("status"), true);

		// assigned to
		sugar().quotedLineItems.listView.getDetailField(1, "assignedTo").assertEquals(sugar().quotedLineItems.getDefaultData().get("assignedTo"), true);

		// quote name
		sugar().quotedLineItems.listView.getDetailField(1, "relQuoteName").assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().quotedLineItems.listView.clickRecord(1);

		// create relation because of TODO: VOOD-444
		sugar().quotedLineItems.recordView.edit();
		sugar().quotedLineItems.recordView.showMore();

		// account name
		VoodooControl accountNameFieldCtrl = sugar().quotedLineItems.recordView.getEditField("accountName");
		accountNameFieldCtrl.set(sugar().quotedLineItems.getDefaultData().get("accountName"));
		accountNameFieldCtrl.assertEquals(sugar().quotedLineItems.getDefaultData().get("accountName"), true);

		// contact name
		VoodooControl contactNameFieldCtrl = sugar().quotedLineItems.recordView.getEditField("relContactName");
		contactNameFieldCtrl.set(sugar().contacts.getDefaultData().get("lastName"));
		contactNameFieldCtrl.assertContains(sugar().contacts.getDefaultData().get("lastName"), true);

		// status
		VoodooControl statusFieldCtrl = sugar().quotedLineItems.recordView.getEditField("status");
		statusFieldCtrl.set(sugar().quotedLineItems.getDefaultData().get("status"));
		statusFieldCtrl.assertEquals(sugar().quotedLineItems.getDefaultData().get("status"), true);

		// quote name
		VoodooControl quoteNameFieldCtrl = sugar().quotedLineItems.recordView.getEditField("relQuoteName");
		quoteNameFieldCtrl.set(sugar().quotes.moduleNameSingular);
		quoteNameFieldCtrl.assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		// quantity
		sugar().quotedLineItems.recordView.getEditField("quantity").assertEquals(sugar().quotedLineItems.getDefaultData().get("quantity"), true);

		// part number
		sugar().quotedLineItems.recordView.getEditField("partNumber").assertEquals(sugar().quotedLineItems.getDefaultData().get("partNumber"), true);

		// description
		sugar().quotedLineItems.recordView.getEditField("description").assertEquals(sugar().quotedLineItems.getDefaultData().get("description"), true);

		// unit price
		sugar().quotedLineItems.recordView.getEditField("unitPrice").assertContains("0.00", true);

		// discount amount
		sugar().quotedLineItems.recordView.getEditField("discountAmount").assertContains("0.00", true);

		// assigned to
		sugar().quotedLineItems.recordView.getEditField("assignedTo").assertEquals(sugar().quotedLineItems.getDefaultData().get("assignedTo"), true);

		// save the record with relation
		sugar().quotedLineItems.recordView.save();

		// verify detail view fields
		// name
		sugar().quotedLineItems.recordView.getDetailField("name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);

		// quantity
		sugar().quotedLineItems.recordView.getDetailField("quantity").assertEquals(sugar().quotedLineItems.getDefaultData().get("quantity"), true);

		// unit price
		sugar().quotedLineItems.recordView.getDetailField("unitPrice").assertEquals(defaultPrice, true);

		// discount amount
		sugar().quotedLineItems.recordView.getDetailField("discountAmount").assertEquals(defaultPrice, true);

		// cost
		sugar().quotedLineItems.recordView.getDetailField("cost").assertEquals(defaultPrice, true);

		// list price
		sugar().quotedLineItems.recordView.getDetailField("listPrice").assertEquals(defaultPrice, true);

		// assigned to
		sugar().quotedLineItems.recordView.getDetailField("assignedTo").assertEquals(sugar().quotedLineItems.getDefaultData().get("assignedTo"), true);

		// part number
		sugar().quotedLineItems.recordView.getDetailField("partNumber").assertEquals(sugar().quotedLineItems.getDefaultData().get("partNumber"), true);

		// description
		sugar().quotedLineItems.recordView.getDetailField("description").assertEquals(sugar().quotedLineItems.getDefaultData().get("description"), true);

		// team
		sugar().quotedLineItems.recordView.getDetailField("teamName").assertContains("Global", true);

		// status
		sugar().quotedLineItems.recordView.getDetailField("status").assertEquals(sugar().quotedLineItems.getDefaultData().get("status"), true);

		// quote name
		sugar().quotedLineItems.recordView.getDetailField("relQuoteName").assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		// account name
		sugar().quotedLineItems.recordView.getDetailField("accountName").assertContains(sugar().accounts.getDefaultData().get("name"), true);

		// contact
		sugar().quotedLineItems.recordView.getDetailField("relContactName").assertContains(sugar().contacts.getDefaultData().get("lastName"), true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().quotedLineItems.listView.previewRecord(1);

		// Verify preview field values
		// name
		sugar().previewPane.getPreviewPaneField("name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);

		// product template
		sugar().previewPane.getPreviewPaneField("productTemplate").assertVisible(true);

		// quote name
		sugar().previewPane.getPreviewPaneField("relQuoteName").assertVisible(true);

		// account name
		sugar().previewPane.getPreviewPaneField("accountName").assertVisible(true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertVisible(true);

		// quantity
		sugar().previewPane.getPreviewPaneField("quantity").assertEquals(sugar().quotedLineItems.getDefaultData().get("quantity"), true);

		// unit price
		sugar().previewPane.getPreviewPaneField("unitPrice").assertEquals(defaultPrice, true);

		// cost
		sugar().previewPane.getPreviewPaneField("cost").assertEquals(defaultPrice, true);

		// list price
		sugar().previewPane.getPreviewPaneField("listPrice").assertEquals(defaultPrice, true);

		// part number
		sugar().previewPane.getPreviewPaneField("partNumber").assertEquals(sugar().quotedLineItems.getDefaultData().get("partNumber"), true);

		// discount amount
		sugar().previewPane.getPreviewPaneField("discountAmount").assertEquals(defaultPrice, true);

		// contact
		sugar().previewPane.getPreviewPaneField("relContactName").assertVisible(true);

		sugar().previewPane.showMore();

		// assigned to
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals(sugar().quotedLineItems.getDefaultData().get("assignedTo"), true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertEquals(sugar().quotedLineItems.getDefaultData().get("description"), true);

		// team
		sugar().previewPane.getPreviewPaneField("teamName").assertContains("Global", true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	public void cleanup() throws Exception {}
}