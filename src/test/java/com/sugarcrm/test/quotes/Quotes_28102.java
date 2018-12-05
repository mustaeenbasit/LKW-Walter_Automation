package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_28102 extends SugarTest {
	DataSource contactsDS = new DataSource();

	public void setup() throws Exception {
		contactsDS = testData.get(testName);
		sugar().contacts.api.create(contactsDS);
		sugar().login();
	}

	/**
	 * Verify Quick search work for billing contact name
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_28102_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select Create Quotes from Quote module drop-down
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("billingContactName").click();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-805, VOOD-1260
		VoodooControl firstName = new VoodooControl("input", "id", "first_name_advanced");
		VoodooControl lastName = new VoodooControl("input", "id", "last_name_advanced");
		VoodooControl search = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl listRecords = new VoodooControl("table", "css", "table.list.view");
		String firstNameSubstr = contactsDS.get(0).get("firstName").substring(0, 2);

		// Verify records with search filter (i.e firstName starts with "Jo")
		firstName.set(firstNameSubstr);
		search.click();
		listRecords.assertContains(contactsDS.get(0).get("fullName"), true);
		listRecords.assertContains(contactsDS.get(1).get("fullName"), true);
		listRecords.assertContains(contactsDS.get(2).get("fullName"), true);
		listRecords.assertContains(contactsDS.get(3).get("fullName"), false);
		listRecords.assertContains(contactsDS.get(4).get("fullName"), false);

		// Verify records with search filter (i.e firstName starts with "Jo" AND lastName starts with "S")
		firstName.set(firstNameSubstr);
		lastName.set(contactsDS.get(0).get("lastName").substring(0, 1));
		search.click();
		listRecords.assertContains(contactsDS.get(0).get("fullName"), true);
		listRecords.assertContains(contactsDS.get(1).get("fullName"), true);
		listRecords.assertContains(contactsDS.get(2).get("fullName"), false);
		listRecords.assertContains(contactsDS.get(3).get("fullName"), false);
		listRecords.assertContains(contactsDS.get(4).get("fullName"), false);
		VoodooUtils.closeWindow();
		sugar().quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}