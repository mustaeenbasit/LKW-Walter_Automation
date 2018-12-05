package com.sugarcrm.test.accounts;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_17004 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	@Test
	public void Accounts_17004_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		String workPhone = "408.454.9600",
			   workPhone101 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789A",
			   workPhone100 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
			   workPhone0 = "";
		FieldSet data = new FieldSet();
			
		sugar().accounts.navToListView();

		// Normal phone number, length < 100
		data.put("workPhone", workPhone);
		sugar().accounts.listView.updateRecord(1, data);
		sugar().accounts.listView.verifyField(1, "workPhone", workPhone);
			
		// Phone number is longer than max, max = 100
		data.put("workPhone", workPhone101);		
		sugar().accounts.listView.updateRecord(1, data);
		sugar().accounts.listView.verifyField(1, "workPhone", workPhone100);
			
		// Phone number is empty
		data.put("workPhone", workPhone0);	
		sugar().accounts.listView.updateRecord(1, data);
		sugar().accounts.listView.verifyField(1, "workPhone", workPhone0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
