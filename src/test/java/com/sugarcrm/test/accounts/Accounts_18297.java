package com.sugarcrm.test.accounts;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_18297 extends SugarTest {
	AccountRecord myAccount1;
	AccountRecord myAccount2;
	LeadRecord myLead;
		
	public void setup() throws Exception {
		sugar().login();
		myAccount1 = (AccountRecord)sugar().accounts.api.create();
		FieldSet acc2 = new FieldSet();
		acc2.put("name", "TestAcc2");
		myAccount2 = (AccountRecord)sugar().accounts.api.create(acc2);
		myLead = (LeadRecord)sugar().leads.api.create();
		myLead.navToRecord();
		sugar().leads.recordView.showLess();
		
	}
	
	/** 
	 * User's selection of the Show More pane should be preserved per module
	 */	
	@Test
	public void Accounts_18297_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		myAccount1.navToRecord();
		//TODO need to refine this case after assert visible is added
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getControl("showLess").assertExists(true);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getControl("showLess").assertExists(true);
		sugar().accounts.recordView.cancel();
		
		myAccount2.navToRecord();
		
		sugar().accounts.recordView.getControl("showLess").assertExists(true);
		
		myLead.navToRecord();
		
		sugar().leads.recordView.getControl("showMore").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
