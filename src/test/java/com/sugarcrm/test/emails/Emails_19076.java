package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_19076 extends SugarTest {
	DataSource allAccounts, emailSettings;
	String endAccount;

	public void setup() throws Exception {
		allAccounts = testData.get(testName);
		emailSettings = testData.get(testName+"_email_settings");
		
		sugar.login();
		sugar.accounts.api.create(allAccounts);
		
		// configure admin->Email Settings 
		sugar.admin.setEmailServer(emailSettings.get(0));
		
		int maxRecord = 24;
		endAccount = allAccounts.get(maxRecord).get("name");
		System.out.println("The last record is:" + endAccount);
	}

	/**
	 * Verify that address book searching successfully when select "More Recipients"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_19076_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);
		// TODO: VOOD-843. Lib support to handle email composer UI
		VoodooControl ccAddressIcon = new VoodooControl("a", "css", "div.controls.span8.record-cell.controls-one.btn-fit  a:nth-child(1) > span");
		ccAddressIcon.waitForVisible();
		ccAddressIcon.click();

		// Assert that CC box is visible
		new VoodooControl("label", "css", ".tright.record-label[data-name='cc_addresses']").assertVisible(true);
		VoodooControl ccAddressBook = new VoodooControl("a", "css", ".btn[data-name='cc_addresses']");
		ccAddressBook.assertVisible(true);
		ccAddressBook.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO: VOOD-844 Lib support for handling inside of Address books in Email
		new VoodooControl("input", "css", "div.main-pane.span8  div:nth-child(3)  input.search-name").set(allAccounts.get(5).get("name"));
		VoodooUtils.pause(2000); // pause needed to populate email account list 
		VoodooControl listItemControl = new VoodooControl("span", "css", "div.layout_Emails div.flex-list-view.left-actions.right-actions td:nth-child(2) span div");
		listItemControl.waitForVisible();
		listItemControl.assertEquals(allAccounts.get(5).get("name"), true);
		new VoodooControl("i", "css", ".add-on.icon-remove").waitForVisible();
		new VoodooControl("i", "css", ".add-on.icon-remove").click();
		
		// Click on "More Recipients".
		new VoodooControl("button", "css", "div.layout_Emails.pagination div:nth-child(2) button").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Select all results by checking the box. 
		new VoodooControl("input", "css", ".layout_Emails input.toggle-all").click();
		new VoodooControl("a", "css", ".btn.btn-primary[name='done_button']").click();
		
		new VoodooControl("ul", "css" ,"ul.select2-choices").waitForVisible(1000);
		// Assert The name of the selected recipient is populated in CC field. 
		new VoodooControl("span", "css", "[data-title='account25@a.com']").assertContains(endAccount, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
