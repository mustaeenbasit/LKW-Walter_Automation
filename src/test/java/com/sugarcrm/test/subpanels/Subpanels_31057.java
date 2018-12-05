package com.sugarcrm.test.subpanels;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class Subpanels_31057 extends SugarTest {
	ArrayList<Record> revLineItemRecordsList = new ArrayList<>();
	OpportunityRecord myOppRecord;
	DataSource revLineItemData = new DataSource();

	public void setup() throws Exception {
		revLineItemData = testData.get(testName);
		// create account record
		sugar().accounts.api.create();
		// create opp record
		myOppRecord = (OpportunityRecord) sugar().opportunities.api.create();
		// create 5 revLineItem record
		revLineItemRecordsList = sugar().revLineItems.api.create(revLineItemData);
		sugar().login();
	}

	/**
	 * Verify that same number of Quote records are appearing in Quotes "Bill To" & Quotes"Ship To" sub-panles
	 *
	 * @throws Exception
	 */
	@Test
	public void Subpanels_31057_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOppRecord.navToRecord();
		if (sugar().alerts.getWarning().queryVisible()) {
			sugar().alerts.getWarning().closeAlert();
		}
		// link account record to opp record.
		// TODO: VOOD-444
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.recordView.save();

		// mass update rli record to link with opportunity record
		FieldSet massUpdateRecord = new FieldSet();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		massUpdateRecord.put("Opportunity Name", sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdateRecord);

		// Generate a Quote for each of the 5 RLIs individually (one quote per RLI).
		for (int i = 1; i <= revLineItemData.size(); i++) {
			sugar().revLineItems.navToListView();
			sugar().revLineItems.listView.clickRecord(i);
			sugar().revLineItems.recordView.openPrimaryButtonDropdown();
			// Generate quote from the RLI record by selecting "Generate Quote" menu item
			sugar().revLineItems.recordView.getControl("generateQuote").click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusFrame("bwc-frame");
			// Set name and valid until Field of Quotes and Save the record
			sugar().quotes.editView.getEditField("name").set(revLineItemData.get(i - 1).get("quoteName"));
			sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
			VoodooUtils.focusDefault();
			sugar().quotes.editView.save();
		}

		// Navigate to Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// TODO: VOOD-2019: Change in Quotes subpanel in Account & Contacts module records view
		VoodooControl quotesBillTo = new VoodooControl("div", "css", "[data-subpanel-link='quotes']");
		quotesBillTo.scrollIntoViewIfNeeded(false);
		quotesBillTo.click();
		VoodooUtils.waitForReady();
		// Verify that there are 5 Quote records in Quotes(Bill to) sub-panel
		new VoodooControl("span", "css", "[data-subpanel-link='quotes'] .count").assertContains(String.valueOf(revLineItemData.size()), true);
		for (int i = 0; i < revLineItemData.size(); i++) {
			new VoodooControl("table", "css", "[data-subpanel-link='quotes'] .table").assertContains(revLineItemData.get(i).get("quoteName"), true);
		}

		// TODO: VOOD-2019: Change in Quotes subpanel in Account & Contacts module records view
		VoodooControl quotesShipTo = new VoodooControl("div", "css", "[data-subpanel-link='quotes_shipto']");
		quotesShipTo.scrollIntoViewIfNeeded(false);
		quotesShipTo.click();
		VoodooUtils.waitForReady();
		// Verify that there are 5 Quote records in Quotes(Ship to) sub-panel
		new VoodooControl("span", "css", "[data-subpanel-link='quotes_shipto'] .count").assertContains(String.valueOf(revLineItemData.size()), true);
		for (int i = 0; i < revLineItemData.size(); i++) {
			new VoodooControl("table", "css", "[data-subpanel-link='quotes_shipto'] .table").assertContains(revLineItemData.get(i).get("quoteName"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}