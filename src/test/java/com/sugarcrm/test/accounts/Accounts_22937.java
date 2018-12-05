package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22937 extends SugarTest {
	AccountRecord account;
	StandardSubpanel casesSubpanel;
	VoodooControl subpanelDiv;
	DataSource caseData;
	String accountName, caseName;
	
	public void setup() throws Exception {
		account = (AccountRecord)sugar().accounts.api.create();
		caseData = testData.get("Accounts_22937");
		
		sugar().login();
		account.navToRecord();
	}

	/**
	 * Verify that creating new case related to the account in full form is canceled.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22937_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		StandardSubpanel casesSubpanel = sugar().accounts.recordView.subpanels.get("Cases");
		casesSubpanel.addRecord();
		
		sugar().cases.createDrawer.showMore();
		
		for(String controlName : caseData.get(0).keySet())
			sugar().cases.createDrawer.getEditField(controlName).set(caseData.get(0).get(controlName));
		
		sugar().cases.createDrawer.cancel();
			
		// Verify creation was cancelled
		subpanelDiv = new VoodooControl("div", "css", "div[data-voodoo-name='Cases']");
		for(String value : caseData.get(0).values())
			subpanelDiv.assertContains(value, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}