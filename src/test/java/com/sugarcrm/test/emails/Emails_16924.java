package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class Emails_16924 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify editing single email address in Edit View.
	 */
	@Test
	public void Emails_16924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource accounts = testData.get(testName);

		// Go to record (such as account) edit view.
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();

		// Enter the value of required fields (name).
		sugar().accounts.createDrawer.getEditField("name").set(accounts.get(0).get("name"));
		// Enter an Email address in email field and save.
		sugar().accounts.createDrawer.getEditField("emailAddress").set(accounts.get(0).get("emailaddress"));
		sugar().accounts.createDrawer.save();

		// Go to its record view.
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		// Verify that the Email address is saved in the record.
		sugar().accounts.recordView.getDetailField("name").assertContains(accounts.get(0).get("name"), true);
		sugar().accounts.recordView.getDetailField("emailAddress").assertContains(accounts.get(0).get("emailaddress"), true);

		// Edit record mark email address 'opt out' and save.
		sugar().accounts.recordView.edit();
		// TODO: VOOD-707
		new VoodooControl("button", "css", "button[data-emailproperty='opt_out']").click();
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.showMore();

		VoodooControl emailCtrl = sugar().accounts.recordView.getDetailField("emailAddress").getChildElement("a", "css", "[rel=tooltip]");
		// Verify that the email address is opted out.
		emailCtrl.assertAttribute("data-original-title", accounts.get(0).get("emailaddress") + " " + accounts.get(0).get("optOut"));

		// Edit record mark email address 'invalid' and save.
		sugar().accounts.recordView.edit();
		// TODO: VOOD-707
		new VoodooControl("button", "css", "button[data-emailproperty='invalid_email']").click();
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.showMore();
		// Verify that the email address is marked invalid.
		emailCtrl.assertAttribute("data-original-title", accounts.get(0).get("emailaddress") + " " + accounts.get(0).get("invalid"));

		// Edit record unmark email address 'out out' and save.
		sugar().accounts.recordView.edit();
		// TODO: VOOD-707
		new VoodooControl("button", "css", "button[data-emailproperty='opt_out']").click();
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.showMore();
		// Verify that the email address is not opted out.
		emailCtrl.assertAttribute("data-original-title", accounts.get(0).get("emailaddress") + " " + accounts.get(0).get("invalidPrimary"));

		// Edit record unmark email address 'invalid' and save.
		sugar().accounts.recordView.edit();
		// TODO: VOOD-707
		new VoodooControl("button", "css", "button[data-emailproperty='invalid_email']").click();
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.showMore();
		// Verify that the email address is back to valid email address.
		emailCtrl.assertAttribute("data-original-title", accounts.get(0).get("emailaddress") + " " + accounts.get(0).get("primary"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
