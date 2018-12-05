package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Emails_26516 extends SugarTest {
	FieldSet emailSettings = new FieldSet();
	DataSource allAccounts;
	String endAccount;
	public void setup() throws Exception {

		sugar.accounts.api.deleteAll();
		allAccounts = testData.get(testName);
		int maxRecord = 21;
		sugar.accounts.api.create(allAccounts);

		sugar.login();

		emailSettings.put("userName", "qa.sugar.qa.79@gmail.com");
		emailSettings.put("password", "sugarcrm");
		emailSettings.put("allowAllUsers", "true");
		sugar.admin.setEmailServer(emailSettings);
		endAccount = allAccounts.get(maxRecord).get("name");
		System.out.println("The last record is:" + endAccount);
	}
	/**
	 * Add multiple Email Address when compose email
	 *
	 * @throws Exception
	 */
	@Test
	public void Emails_26516_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration(20000);

		//TODO: VOOD-843. Lib support to handle email composer UI
		new VoodooControl("input", "css" ,"input[name='subject']").waitForVisible(5000);
		new VoodooControl("i", "css", "a[data-name='to_addresses'] i.icon-book").click();
		VoodooUtils.pause(1000);
		//TODO: VOOD_844. Lib support for handling inside of Address books in Email
		new VoodooControl("button", "css", "button[data-action='show-more']").click();
		new VoodooControl("input", "css", ".layout_Emails input.toggle-all").click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		new VoodooControl("ul", "css" ,"ul.select2-choices").waitForVisible(5000);
		new VoodooControl("ul", "css", "ul.select2-choices").assertContains(endAccount, true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
