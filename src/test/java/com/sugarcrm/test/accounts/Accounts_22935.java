package com.sugarcrm.test.accounts;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map.Entry;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Accounts_22935 extends SugarTest {
	AccountRecord myAccount;
	StandardSubpanel opportunitiesSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that creating new opportunity related to the account in full form is canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		opportunitiesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get("Opportunities");
		opportunitiesSubpanel.scrollIntoViewIfNeeded(false);
		opportunitiesSubpanel.addRecord();

		FieldSet recordData = sugar.opportunities.getDefaultData();

		// Determine which view Sugars UI is in, Opportunity with RLI inline or Opportunity and no RLI
		boolean oppView = sugar().admin.api.isOpportunitiesView();
		// If Sugars UI is Opportunity only view, remove "rli" named fields from fieldSet
		if(oppView) {
			for (Iterator<Entry<String, String>> it = recordData.entrySet().iterator(); it.hasNext(); ) {
				Entry<String, String> entry = it.next();
				if (entry.getKey().startsWith("rli")) {
					it.remove();
					VoodooUtils.voodoo.log.info("Entry: <" + entry.getKey() + ", " + entry.getValue() + "> was removed from the fieldSet of data");
				}
			}
		} else { // If Sugars UI is RLI view, remove "date_closed" and "Case" as in "likelyCase" named fields from fieldSet
			for (Iterator<Entry<String, String>> it = recordData.entrySet().iterator(); it.hasNext(); ) {
				Entry<String, String> entry = it.next();
				if (entry.getKey().equals("date_closed") || entry.getKey().endsWith("Case")) {
					it.remove();
					VoodooUtils.voodoo.log.info("Entry: <" + entry.getKey() + ", " + entry.getValue() + "> was removed from the fieldSet of data");
				}
			}
		}

		// Fill all fields on the createDrawer
		sugar.opportunities.createDrawer.showMore();		
		sugar.opportunities.createDrawer.setFields(recordData);

		// Click Cancel
		sugar.opportunities.createDrawer.cancel();
		
		// Expand Subpanel and verify that it is empty
		opportunitiesSubpanel.expandSubpanel();
		assertTrue("opportunities Subpanel is not Empty", opportunitiesSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
