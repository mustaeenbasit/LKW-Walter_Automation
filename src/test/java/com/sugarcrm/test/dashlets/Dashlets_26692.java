package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26692 extends SugarTest {
	FieldSet accountName;
	DataSource ds ;
	AccountRecord myAccount;
	OpportunityRecord myOpportunity;
	RevLineItemRecord myRevLineItem;
	FieldSet fs;
	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();
		
		// Create Account record
		accountName = new FieldSet();
		accountName.put("name", ds.get(0).get("accountName"));
		myAccount = (AccountRecord)sugar.accounts.api.create(accountName);
		
		// Create Opportunity record through UI so it is related to Account and has RLI linked to it.
		FieldSet opportunityFieldSet = new FieldSet();
		opportunityFieldSet.put("rli_likely",ds.get(0).get("unitPrice"));
		opportunityFieldSet.put("relAccountName",ds.get(0).get("accountName") );
		myOpportunity = (OpportunityRecord)sugar.opportunities.create(opportunityFieldSet);
	}

	/**
	 * Verify that Opportunity Metrics Dashlet shows the same results regardless of comma and thousand separators.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26692_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		myAccount.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO: VOOD-591
		VoodooControl helpDashboardCtrl= new VoodooControl( "a", "css", "div[data-voodoo-name='help-dashboard-headerpane'] span[data-voodoo-name='name'] .dropdown a");
		if (helpDashboardCtrl.queryVisible()){
			helpDashboardCtrl.click();
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", "div[data-voodoo-name='help-dashboard-headerpane'] .dropdown-menu li:nth-of-type(1) a").click();
			sugar.alerts.waitForLoadingExpiration();
		}
		new VoodooControl("div","css",".deal-amount-metric.active").assertEquals(ds.get(0).get("active_amount"),true);

		// Change separator in user's profile, set '1000s separator:' to . and set 'Decimal Symbol:' to ,	
 		fs =  new FieldSet();
 		fs.put("advanced_grouping_seperator", ".");
 		fs.put("advanced_decimal_separator", ",");
 		sugar.users.setPrefs(fs);
        
		myAccount.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		
		new VoodooControl("div","css",".deal-amount-metric.active").waitForVisible();
		new VoodooControl("div","css",".deal-amount-metric.active").assertEquals(ds.get(0).get("active_amount_one"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
