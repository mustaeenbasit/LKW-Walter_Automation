package com.sugarcrm.test.activitystream;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Activitystream_17457_3 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * Show tip message in the comments input box
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void Activitystream_17457_3_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		String tip = ds.get(0).get("tip");

		// TODO VDDD-474 need replace these controls after lib file is done
		VoodooUtils.voodoo.log.info("Check tip message on record view");
		myAccount.navToRecord();
		sugar.accounts.recordView.getControl("activityStreamButton").click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.accounts.listView.activityStream.createComment(ds.get(0).get("post"));
		sugar.alerts.waitForLoadingExpiration();
		// The use of sugar.accounts. is because we do not have a
		// HomeModule.java file for support
		// When HomeModule.java is available then the following lines should be
		// updated to use it.
		sugar.accounts.listView.activityStream.getControl("streamInput").assertAttribute("data-placeholder", tip);

		// Restore 
		new VoodooControl("button", "css", "li[data-module='Home'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "xpath", "//li[@data-module='Home']/span/div/ul/li[contains(.,'My Dashboard')]/a").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}