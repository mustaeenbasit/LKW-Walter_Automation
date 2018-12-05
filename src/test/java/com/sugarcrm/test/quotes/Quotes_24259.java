package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Quotes_24259 extends SugarTest {
	AccountRecord myAcct;
	OpportunityRecord myOpp;
	StandardSubpanel quotesSubpanel;
	DataSource accData;
	FieldSet accToCreate;

	public void setup() throws Exception {
		sugar.login();
		accData = testData.get(testName);
		accToCreate = accData.get(0);
		myAcct = (AccountRecord) sugar.accounts.api.create(accToCreate);
		myOpp = (OpportunityRecord) sugar.opportunities.api.create();

		// Link the new opportunity with the account
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.editRecord(1);
		sugar.opportunities.createDrawer.getEditField("relAccountName").set(myAcct.getRecordIdentifier());
		sugar.opportunities.listView.saveRecord(1);
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Test Case 24259: Verify that account information is carried over when create quote from quotes sub-panel of opportunity record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Quotes_24259_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add a new related quote from Quotes subpanel
		quotesSubpanel = sugar.opportunities.recordView.subpanels.get("Quotes");
		myOpp.navToRecord(); // Script may fail without reloading opps list view
		quotesSubpanel.addRecord();
		VoodooUtils.pause(2500);

		/* sugar.quotes.editView.getEditField("name").waitForVisible();
		TODO: construction above doesn't work for bwc modules, so pause is the only way to properly interact with quotes edit view */

		// Verify the related account details are correctly populated for the quote edit view
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.editView.getEditField("billingStreet").assertEquals(accToCreate.get("billingAddressStreet"), true);
		sugar.quotes.editView.getEditField("billingCity").assertEquals(accToCreate.get("billingAddressCity"), true);
		sugar.quotes.editView.getEditField("billingState").assertEquals(accToCreate.get("billingAddressState"), true);
		sugar.quotes.editView.getEditField("billingPostalCode").assertEquals(accToCreate.get("billingAddressPostalCode"), true);
		sugar.quotes.editView.getEditField("billingCountry").assertEquals(accToCreate.get("billingAddressCountry"), true);
		VoodooUtils.focusDefault();
		sugar.quotes.editView.cancel();
		VoodooUtils.waitForAlertExpiration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}