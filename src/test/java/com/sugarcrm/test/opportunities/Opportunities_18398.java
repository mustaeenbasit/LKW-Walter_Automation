package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18398 extends SugarTest {
	RevLineItemRecord rliRec;
	AccountRecord accRec;
	OpportunityRecord myOpp;
	FieldSet fs;

	public void setup() throws Exception {
		accRec= (AccountRecord) sugar().accounts.api.create();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		rliRec = (RevLineItemRecord) sugar().revLineItems.api.create();
		sugar().productCategories.api.create();
		fs = testData.get(testName).get(0);
		sugar().login(sugar().users.qaUser);

		// Linking RLI with Opportunity and Category record
		rliRec.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.getEditField("category").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();

		// Linking Opportunity and Account record
		myOpp.navToRecord();
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(accRec.getRecordIdentifier());
		sugar().opportunities.recordView.save();
	}

	/**
	 * Test Case 18398:Verify that RLI cannot be converted into Quote in case the RLI does not have a product linked to it (the "Product" field is blank).
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18398_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Opportunity record view and select RevenueLineItems subpanels
		myOpp.navToRecord();
		StandardSubpanel revLineItemsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.expandSubpanel();

		revLineItemsSubpanel.toggleSelectAll();
		revLineItemsSubpanel.openActionDropdown();
		revLineItemsSubpanel.generateQuote();

		// Verify the error message and RLI record link
		sugar().alerts.getError().assertContains(fs.get("error_message"), true);
		new VoodooControl("a", "css","#alerts a").assertEquals(rliRec.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}