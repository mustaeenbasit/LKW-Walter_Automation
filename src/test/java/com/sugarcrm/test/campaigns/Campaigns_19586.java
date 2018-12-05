package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19586 extends SugarTest {
	FieldSet emailSetup;
	DataSource inboundData;

	public void setup() throws Exception {
		sugar().login();
		emailSetup = testData.get(testName).get(0);
		inboundData = testData.get(testName +"_inboundEmail");

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// Add new bounce handling account.
		sugar().admin.navToAdminPanelLink("inboundEmail");
		sugar().navbar.selectMenuItem(sugar().inboundEmail, "newBounceMailAccount");

		// TODO: VOOD-1082 (Lib support needed to create bounce handling account)
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "name").set(inboundData.get(0).get("name"));
		new VoodooControl("input", "id", "email_user").set(inboundData.get(0).get("email"));
		new VoodooControl("input", "id", "email_password").set(inboundData.get(0).get("password"));
		new VoodooControl("input", "id", "from_addr").set(inboundData.get(0).get("email"));

		// Assign team
		new VoodooControl("button", "css", "#lineLabel_team_name td:nth-child(1) span button.button.firstChild").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#MassUpdate table.list.view tbody tr.oddListRowS1 td:nth-child(1) input").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "button").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Newsletter - Verify that Admin-Email can be set with using the existing mailbox by using
	 * "Set Up Email " shortcut.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19586_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set up an email account for campaign
		sugar().campaigns.navToListView();
		sugar().navbar.selectMenuItem(sugar().campaigns, "setUpEmail");
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1072 Library support needed for controls in Campaign Detail view.
		new VoodooControl("input", "css", "#wiz_next_button").click();

		// Verify the message after clicking on next button at "Setup Email" step.
		// TODO: VOOD-1072 Library support needed for controls in Campaign Detail view.
		new VoodooControl("td", "css", ".list.view td").assertContains(inboundData.get(0).get("assertText"), true);

		// Create New Mail Account
		// TODO: VOOD-1072 Library support needed for controls in Campaign Detail view.
		new VoodooControl("input", "id", "create_mbox").click();
		new VoodooControl("select", "id", "protocol").set(inboundData.get(0).get("protocol"));
		new VoodooControl("input", "id", "ssl").click();
		VoodooUtils.pause(3000); //TODO: TR-8001 Studio Buttons remain disabled longer than intended
		new VoodooControl("input", "id", "next_button_div").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#wiz_submit_button").click();
		VoodooUtils.waitForReady(180000);
		VoodooUtils.focusDefault();

		// Verify the New Mail Account is created.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("inboundEmail").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".list.view tr.evenListRowS1 td:nth-child(3) a").assertContains("SugarCRM", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}