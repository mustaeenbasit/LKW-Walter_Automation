package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24325 extends SugarTest {
	QuoteRecord myQuote;
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	StandardSubpanel quotesSubpanel;
	DataSource quoteDS;

	public void setup() throws Exception {
		sugar().login();
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myQuote = (QuoteRecord) sugar().quotes.create(); // TODO: Should be changed with api.create() when SFA-2652 is fixed
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		quoteDS = testData.get(testName);

		// Open opportunity record view and link it with the quote
		myOpp.navToRecord();
		quotesSubpanel = sugar().opportunities.recordView.subpanels.get("Quotes");
		quotesSubpanel.clickLinkExisting();
		new VoodooControl("span", "css", ".fld_name.list").waitForVisible();
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Test Case 24325: Edit Quote_Verify that quote can be modified when using "edit" function in "Quotes" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24325_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inline-edit a related quote. Save record
		quotesSubpanel.editRecord(1);
		sugar().quotes.editView.waitForVisible();

		sugar().quotes.editView.setFields(quoteDS.get(0));
		sugar().quotes.editView.save();
		VoodooUtils.waitForAlertExpiration();

		// Verify the quote is successfully modified
		quotesSubpanel.expandSubpanel();
		quotesSubpanel.verify(1, quoteDS.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}