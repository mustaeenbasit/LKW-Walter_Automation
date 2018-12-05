package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23603 extends SugarTest {
	String accountName = "";
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);

		// Link Account Name to a Contact Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		accountName = sugar().accounts.getDefaultData().get("name");
		sugar().contacts.recordView.getEditField("relAccountName").set(accountName);
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();
	}

	/**
	 * Create quote_Verify that the related product record for the quote are added to "Products" sub-panel
	 * when creating quote.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23603_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Quote
		FieldSet myFieldSet = new FieldSet();
		String contactName = sugar().contacts.getDefaultData().get("fullName");
		myFieldSet.put("billingContactName", contactName);
		myFieldSet.put("billingAccountName", accountName);
		sugar().quotes.create(myFieldSet);
		
		// Edit the Quote to add a Line Item
		// TODO: VOOD-930
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#edit_button").click();
		VoodooControl addGroup = new VoodooControl("input", "css", "input#add_group");
		VoodooUtils.waitForReady();
		addGroup.scrollIntoView();
		addGroup.click();
		DataSource qliData = testData.get(testName);
		new VoodooControl("input", "css", "input#bundle_name_group_0").set(qliData.get(0).get("groupName"));

		// Add First Product
		VoodooControl addRow = new VoodooControl("input", "css", "input[name='Add Row']");
		addRow.click();
		new VoodooControl("input", "css", "input#quantity_1").set(qliData.get(0).get("quantity"));
		new VoodooControl("input", "css", "input#name_1").set(qliData.get(0).get("name"));
		new VoodooControl("input", "css", "input#discount_price_1").set(qliData.get(0).get("unitPrice"));
		new VoodooControl("input", "css", "input#discount_amount_1").set(qliData.get(0).get("discountAmount"));
		new VoodooControl("select", "css", "select#tax_class_select_name_1").set(qliData.get(0).get("ifTaxable"));
		
		// Add Second Product
		addRow.click();
		new VoodooControl("input", "css", "input#quantity_2").set(qliData.get(1).get("quantity"));
		new VoodooControl("input", "css", "input#name_2").set(qliData.get(1).get("name"));
		new VoodooControl("input", "css", "input#discount_price_2").set(qliData.get(1).get("unitPrice"));
		new VoodooControl("input", "css", "input#discount_amount_2").set(qliData.get(1).get("discountAmount"));
		new VoodooControl("select", "css", "select#tax_class_select_name_2").set(qliData.get(1).get("ifTaxable"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// Check if the Quote and Products created for the Contact are visible in the Contact recordview Subpanels
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// TODO: VOOD-1000. After it is resolved, uncomment below lines and
		// remove new VoodooControl lines below

		//	FieldSet fs = new FieldSet();
		//	fs.put("name", myQuote.getRecordIdentifier());
		//	sugar().contacts.recordView.subpanels.get(sugar().quotes.moduleNamePlural).expandSubpanel();
		//	sugar().contacts.recordView.subpanels.get(sugar().quotes.moduleNamePlural).verify(1, fs, true);
		//
		//	fs.put("name", qliData.get(0).get("name"));
		//	sugar().contacts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).expandSubpanel();
		//	sugar().contacts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).verify(1, fs, true);
		//
		//	fs.put("name", qliData.get(1).get("name"));
		//	sugar().contacts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).expandSubpanel();
		//	sugar().contacts.recordView.subpanels.get(sugar().quotedLineItems.moduleNamePlural).verify(2, fs, true);

		VoodooControl quotesSubpanel = new VoodooControl("div", "css", "div[data-subpanel-link='billing_quotes']");
		quotesSubpanel.scrollIntoViewIfNeeded(true);
		String quoteName = sugar().quotes.getDefaultData().get("name");
		new VoodooControl("a", "css", ".layout_Quotes .fld_name a").assertEquals(quoteName, true);
		VoodooControl quotedLineItemsSubpanel = new VoodooControl("div", "css", "div[data-voodoo-name='Products']");
		quotedLineItemsSubpanel.scrollIntoViewIfNeeded(true);

		// Verifying the qli Records are present in the qli subpanel
		// Sorted QLI by name desc. to maintain the order
		new VoodooControl("th", "css", quotedLineItemsSubpanel.getHookString() + " .orderByname").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Products .fld_name a").assertEquals(qliData.get(1).get("name"), true);
		new VoodooControl("a", "css", ".layout_Products .single:nth-child(2) td:nth-child(2) a").assertEquals(qliData.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
