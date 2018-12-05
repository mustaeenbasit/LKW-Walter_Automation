package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18340 extends SugarTest {
	FieldSet quotesData;

	public void setup() throws Exception {
		quotesData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create an opportunities related to account record created and it creates a RLI record as well 
		sugar().opportunities.create();

		// Quote is created from Revenue Line Item record  
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Set Valid until Field of Quotes and Save the record
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(quotesData.get("date_quote_expected_closed"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
	}

	/**
	 * Verify that correct message is displayed when updating the quoted revenue line item.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18340_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change any value in the record and click save
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("name").set(testName);
		sugar().revLineItems.recordView.getControl("saveButton").click(); // Used control instead of method because in method there is 'waitForLoadingExpiration()' and in this time success alert is disappears

		// Verify that he alert appears with the message: "Notice This item has already been converted to Quote. Your changes will not be updated in the corresponding Quote."
		// Verify that the message should appear in blue color: "sugar().alerts.getInfo() method is for Blue messages"
		sugar().alerts.getInfo().assertContains(quotesData.get("alertMessage"), true);

		// Go to RLI list view and change record there 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1, "salesStage").set(quotesData.get("salesStage"));
		sugar().revLineItems.listView.getControl("save01").click(); // Used control instead of method because in method there is 'waitForLoadingExpiration()' and in this time success alert is disappears

		// Verify that he alert appears with the message: "Notice This item has already been converted to Quote. Your changes will not be updated in the corresponding Quote."
		sugar().alerts.getInfo().assertContains(quotesData.get("alertMessage"), true);

		// Go to corresponding opportunity subpanel and update RLI record there.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		rliSubpanel.scrollIntoView();
		rliSubpanel.editRecord(1);
		rliSubpanel.getEditField(1, "likelyCase").set(quotesData.get("likelyCase"));
		rliSubpanel.getControl("saveActionRow01").click(); // Used control instead of method because in method there is 'waitForLoadingExpiration()' and in this time success alert is disappears

		// Verify that he alert appears with the message: "Notice This item has already been converted to Quote. Your changes will not be updated in the corresponding Quote."
		sugar().alerts.getInfo().assertContains(quotesData.get("alertMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}