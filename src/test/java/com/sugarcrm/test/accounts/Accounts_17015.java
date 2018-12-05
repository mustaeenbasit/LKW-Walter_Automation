package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17015  extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify auto duplicate check on both account name AND billing city while creating a new account
	 * @throws Exception
	 */
	@Test
	public void Accounts_17015_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("billingAddressCity").set(customData.get("billingAddressCity"));
		sugar().accounts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration(30000); // Required to complete save action and shown duplicate panel
		
		// TODO: VOOD-653
		// The duplicate found panel is shown with the matched accounts info.  
		sugar().accounts.createDrawer.getControl("duplicateCount").assertVisible(true);
		new VoodooControl("div","css","div[data-voodoo-name='dupecheck-list-edit']").assertVisible(true);
		
		// Should find all the matched accounts with Name matches with "Starts With" Operator AND Billing City matches based on "Starts With" Operator.
		new VoodooControl("div","css","div[data-voodoo-name='dupecheck-list-edit'] .fld_name.list").assertContains(customData.get("name"), true);
		new VoodooControl("div","css","div[data-voodoo-name='dupecheck-list-edit'] .fld_billing_address_city.list").assertContains(customData.get("billingAddressCity"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
		}

	public void cleanup() throws Exception {}
}