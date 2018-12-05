package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23549 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}
	/** 
	 * Create quote_Verify that a related quote can be created from contact detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23549_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		
		// TODO: VOOD-1000 (Subpanel methods not working for Quote and QuoteLineItem)
		VoodooControl quotesSubpanel = new VoodooControl("div", "class", "layout_Quotes");
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		
		// Clicking the + button on the Quotes subpanel
		new VoodooControl("a", "css", ".layout_Quotes .rowaction.btn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		String quoteName = sugar().quotes.getDefaultData().get("name");
		String closeDate = sugar().quotes.getDefaultData().get("date_quote_expected_closed");
		String billingAccount = sugar().quotes.getDefaultData().get("billingAccountName");
		
		// Entering mandatory fields and other fields
		sugar().quotes.editView.getEditField("name").set(quoteName);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(closeDate);
		sugar().quotes.editView.getEditField("billingAccountName").set(billingAccount);
		sugar().quotes.editView.getEditField("description").set(sugar().quotes.
				getDefaultData().get("description"));
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		sugar().quotes.editView.save();
		
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		
		// Expanding Quotes Subpanel
		quotesSubpanel.click();
		VoodooUtils.waitForReady();
		
		// Verifying subpanel data displays the Quote subject
		new VoodooControl("td", "css", ".layout_Quotes tr.single [data-type='name']").assertEquals
			(quoteName, true);
		
		// Verifying subpanel data displays the related billing account name
		new VoodooControl("td", "css", ".layout_Quotes tr.single [data-type='relate']").assertEquals
			(billingAccount, true);
		
		// Verifying subpanel data displays the expected close date
		new VoodooControl("td", "css", ".layout_Quotes tr.single [data-type='date']").assertEquals
			(closeDate, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
