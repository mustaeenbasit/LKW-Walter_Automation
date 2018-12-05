package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26709 extends SugarTest {
	AccountRecord myAccount;
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();
		
		ds = testData.get(testName);
		myAccount = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * Single quote is allowed in the email address field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26709_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		
		sugar.accounts.recordView.edit();
		sugar.accounts.recordView.showMore();
		
		// TODO: VOOD-896
		new VoodooControl("input", "css", ".newEmail.input-append").set(ds.get(0).get("email"));
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("input", "css", ".newEmail.input-append").set(ds.get(1).get("email"));
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("input", "css", ".newEmail.input-append").set(ds.get(2).get("email"));

		sugar.accounts.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		new VoodooControl("span", "css", ".fld_email.detail").assertContains(ds.get(0).get("email"), true);
		new VoodooControl("span", "css", ".fld_email.detail").assertContains(ds.get(1).get("email"), true);
		new VoodooControl("span", "css", ".fld_email.detail").assertContains(ds.get(2).get("email"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
