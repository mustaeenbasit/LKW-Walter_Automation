package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22947 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().login();
	}

	/**
	 * Account Detail - Member Organizations sub-panel - Select_Verify that corresponding account member records 
	 * are displayed  using search function on the select account members pop-up box.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22947_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Link Existing Record" button on "Member Organizations" sub-panel
		StandardSubpanel memberOrganizationSubpanel = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizationSubpanel.clickLinkExisting();
		
		// Enter account name into search field in the pop-up box
		sugar().accounts.searchSelect.search(testName);
		
		// Verify matching account member(s) is displayed on the list of the pop-up box
		// TODO: VOOD-1487
		new VoodooControl("div", "css", ".layout_Accounts .list.fld_name div").assertEquals(testName, true);
		sugar().accounts.searchSelect.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}