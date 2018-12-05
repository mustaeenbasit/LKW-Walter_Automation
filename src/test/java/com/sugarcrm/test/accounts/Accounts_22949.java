package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22949 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().login();
	}

	/**
	 * Account Detail_Member Organizations sub-panel_close_selecting pop-up window
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22949_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click "Link Existing Record" button on "Member Organizations" sub-panel
		StandardSubpanel memberOrganizations = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizations.clickLinkExisting();
		
		// Cancel the pop-up box
		sugar().accounts.searchSelect.cancel();
		
		// Verify no matching account member record is displayed on "Member ORGANIZATIONS" sub-panel
		memberOrganizations.expandSubpanel();
		Assert.assertEquals("Row count is not equal to 0 in subpanel", 0, memberOrganizations.countRows());
		memberOrganizations.assertContains(testName, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}