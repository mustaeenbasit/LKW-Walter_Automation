package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Opportunities_18401 extends SugarTest {
	RevLineItemRecord rliRec;
	AccountRecord accRec;
	OpportunityRecord oppRec;

	public void setup() throws Exception {
		accRec = (AccountRecord) sugar().accounts.api.create();
		oppRec = (OpportunityRecord) sugar().opportunities.api.create();
		rliRec = (RevLineItemRecord) sugar().revLineItems.api.create();
		sugar().productCatalog.api.create();
		sugar().login();

		// associating the opportunity to the account
		oppRec.navToRecord();
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(accRec.getRecordIdentifier());
		sugar().opportunities.recordView.save();

		// associating the RLI to the product catalog and the opportunity
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(oppRec.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
		sugar().logout();
	}

	/**
	 * ENT/ULT: Verify that for each converted Revenue Line Item, a column in
	 * the Revenue Line Items list view should enable to user to navigate to
	 * corresponding Quote.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18401_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().login(sugar().users.getQAUser());
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.scrollIntoView();
		rliSubpanel.expandSubpanel();

		rliSubpanel.toggleSelectAll();
		rliSubpanel.openActionDropdown();
		rliSubpanel.generateQuote();
		VoodooUtils.focusFrame("bwc-frame");
		String date = sugar().quotes.getDefaultData().get("date_quote_expected_closed");
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(date);
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// verifying that the quote is displayed on the record view
		VoodooControl quoteName = sugar().revLineItems.recordView.getDetailField("quoteName");
		quoteName.assertEquals(oppRec.getRecordIdentifier(), true);
		quoteName.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// verifying that the user is navigated to the quote record view
		sugar().quotes.detailView.getDetailField("name").assertEquals(oppRec.getRecordIdentifier(), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}