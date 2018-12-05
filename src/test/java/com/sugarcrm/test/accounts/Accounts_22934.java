package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22934 extends SugarTest {
	AccountRecord account;
	StandardSubpanel opportunitiesSubpanel;
	DataSource opportunityFields, textMessages;
	
	public void setup() throws Exception {
		opportunityFields = testData.get("Accounts_22934");
		textMessages = testData.get("Accounts_22934_text_messages");
		
		account = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}
	
	/**
	 * 22934 Verify that creating new opportunity related to the account in-line can be canceled
	 * @throws Exception
	 */
	@Test
	public void Accounts_22934_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		account.navToRecord();
		opportunitiesSubpanel = sugar().accounts.recordView.subpanels.get("Opportunities");
		opportunitiesSubpanel.addRecord();
		
		sugar().accounts.createDrawer.showMore();
		
		for(String control_name : opportunityFields.get(0).keySet()){
			sugar().opportunities.createDrawer.getEditField(control_name).set(
					opportunityFields.get(0).get(control_name));
			}
		
		sugar().opportunities.createDrawer.cancel();
		opportunitiesSubpanel.expandSubpanel();
		
		// verify the opportunity was not created
		opportunitiesSubpanel.assertContains(textMessages.get(0).get("no_records_message"), true);
		
		for(String value : opportunityFields.get(0).values())
			opportunitiesSubpanel.assertContains(value, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}

