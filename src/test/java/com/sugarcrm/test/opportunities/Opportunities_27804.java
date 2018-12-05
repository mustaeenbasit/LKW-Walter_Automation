package com.sugarcrm.test.opportunities;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_27804 extends SugarTest {
	OpportunityRecord myOpp;
	AccountRecord myAcc;
	
	public void setup() throws Exception {
		sugar().login();
		myAcc = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify that sales stage changes to status while check for duplicates on opportunity record view after Opps to Opps + RLIs conversion
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27804_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.api.switchToRevenueLineItemsView();

		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myOpp.navToRecord();
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "span.fld_find_duplicates_button.detail a").click();
		sugar().alerts.waitForLoadingExpiration(60000);
		// Assert only correct field is displayed
		// TODO: VOOD-1359
		new VoodooControl("th", "xpath", "//*[@id='drawers']//th[@data-fieldname='sales_stage']").assertVisible(false);
		new VoodooControl("th", "xpath", "//*[@id='drawers']//th[@data-fieldname='sales_status']").assertVisible(true);
		sugar().opportunities.createDrawer.cancel();
		sugar().alerts.waitForLoadingExpiration(60000); // for Opp listView

		sugar().admin.api.switchToOpportunitiesView();
		
		myOpp.navToRecord();
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "span.fld_find_duplicates_button.detail a").click();
		sugar().alerts.waitForLoadingExpiration(60000);
		// Assert only correct field is displayed
		// TODO: VOOD-1359
		new VoodooControl("th", "xpath", "//*[@id='drawers']//th[@data-fieldname='sales_stage']").assertVisible(true);
		new VoodooControl("th", "xpath", "//*[@id='drawers']//th[@data-fieldname='sales_status']").assertVisible(false);
		// TODO: Investigate why a warning appears at this stage
		if (sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().confirmAlert();
		sugar().opportunities.createDrawer.cancel();
		sugar().alerts.waitForLoadingExpiration(60000); // for Opp listView

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}