package com.sugarcrm.test.subpanels;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Subpanels_31054 extends SugarTest {
	AccountRecord accountRecord;
	ContactRecord contactRecord;

	public void setup() throws Exception {
		accountRecord = (AccountRecord) sugar().accounts.api.create();
		contactRecord = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that there are two sub-panels of Quotes module for Accounts & Contacts records
	 *
	 * @throws Exception
	 */
	@Test
	public void Subpanels_31054_Accounts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "_Accounts"+ "...");

		DataSource subpanelNameData = testData.get(testName);
		accountRecord.navToRecord();

		// Verify that there are two quotes sub-panels: 1) Quotes Bill to 2) Quotes Ship to
		// TODO: VOOD-2019: Change in Quotes subpanel in Account & Contacts module records view
		new VoodooControl("h4", "css", "[data-subpanel-link='quotes'] .subpanel-header h4").assertEquals(subpanelNameData.get(0).get("subpanelName"), true);
		new VoodooControl("h4", "css", "[data-subpanel-link='quotes_shipto'] .subpanel-header h4").assertEquals(subpanelNameData.get(1).get("subpanelName"), true);

		VoodooUtils.voodoo.log.info(testName + "_Accounts complete.");
	}

	@Test
	public void Subpanels_31054_Contacts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "_Contacts"+ "...");

		DataSource subpanelNameData = testData.get(testName);
		contactRecord.navToRecord();
		// Verify that there are two quotes sub-panels: 1) Quotes Bill to 2) Quotes Ship to
		// TODO: VOOD-2019: Change in Quotes subpanel in Account & Contacts module records view
		new VoodooControl("h4", "css", "[data-subpanel-link='quotes'] .subpanel-header h4").assertEquals(subpanelNameData.get(1).get("subpanelName"), true);
		new VoodooControl("h4", "css", "[data-subpanel-link='billing_quotes'] .subpanel-header h4").assertEquals(subpanelNameData.get(0).get("subpanelName"), true);

		VoodooUtils.voodoo.log.info(testName + "_Contacts complete.");
	}

	public void cleanup() throws Exception {}
}