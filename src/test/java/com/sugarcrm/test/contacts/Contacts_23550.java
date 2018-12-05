package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Contacts_23550 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().quotes.api.create();
		sugar().login();
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// TODO: VOOD-1000 Quotes Subpanel missing from Contacts record view
		// Scroll to Quotes subpanel, toggle dropdown and select 'Link existing'
		new VoodooControl("a", "css", ".layout_Quotes").scrollIntoViewIfNeeded(false);
		new VoodooControl("a", "css", ".layout_Quotes .btn.dropdown-toggle").click();
		new VoodooControl("a", "css", ".dropdown-menu .panel-top.fld_select_button .rowaction").click();

		// Select first record in list and click on 'Link'
		new VoodooControl("span", "css", ".fld_name.list").waitForVisible();
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that a related quote can be edited from contact detail view
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23550_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Trigger edit Quote records from Contacts' subpanel
		// TODO: VOOD-1000
		new VoodooControl("a", "css", ".layout_Quotes .flex-list-view").scrollIntoViewIfNeeded(false);
		new VoodooControl("a", "css", ".layout_Quotes .flex-list-view .fieldset .btn-group .rowaction.btn .fa-pencil").click();
		if (sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Update quote data fields and save changes
		FieldSet quoteFS = testData.get(testName).get(0);
		sugar().quotes.editView.setFields(quoteFS);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getControl("saveButton").click();

		// Verify that the quote is successfully updated in Contacts subpanel
		new VoodooControl("a", "css", ".layout_Quotes tr.single td span.fld_name div").assertEquals(quoteFS.get("name"), true);
		new VoodooControl("a", "css", ".layout_Quotes tr.single td span.fld_account_name div").assertEquals(quoteFS.get("billingAccountName"), true);
		new VoodooControl("a", "css", ".layout_Quotes tr.single td span.fld_date_quote_expected_closed div").assertEquals(quoteFS.get("date_quote_expected_closed"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
