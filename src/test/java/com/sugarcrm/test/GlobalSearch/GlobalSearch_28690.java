package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28690 extends SugarTest {
	public void setup() throws Exception {
		// Create 2 records in Sidecar module (Accounts and Cases) and 1 in BWC module (Campaigns)
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().cases.api.create(fs);
		sugar().campaigns.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify the functionality of Preview on global search result page 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28690_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter any value in the Search bar and hit enter
		// TODO: CB-252: Need CB support to simulate input of any keyboard key i.e tab, esc, VOOD-1437: Need lib support to send "Key" to a control
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		String searchString = testName.substring(0, 6);
		globalSearchCtrl.append(searchString + '\uE007');
		VoodooUtils.waitForReady();

		// Verify that Preview icon is enabled for each side car module and disabled for BWC module
		// TODO: VOOD-1848: Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		VoodooControl accountsPreviewBtn = new VoodooControl("a", "xpath", "//*[@class='nav search-results']/li[contains(.,'Ac')]/span/a");
		Assert.assertFalse("Accounts preview button is disabled", accountsPreviewBtn.isDisabled());
		Assert.assertTrue("Cases preview button is enabled", new VoodooControl("a", "xpath", "//*[@class='nav search-results']/li[contains(.,'Ca')]/span/a").isDisabled());
		Assert.assertFalse("Campaigns preview button is disabled", new VoodooControl("a", "xpath", "//*[@class='nav search-results']/li[contains(.,'Cs')]/span/a").isDisabled());

		// Click on Preview icon of any record
		accountsPreviewBtn.click();

		// Verify that preview of the record on the right hand side pane displayed with it's detail
		// TODO: VOOD-1848: Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		VoodooControl nameCtrl = new VoodooControl("a", "css", ".preview-pane .fld_name a");
		new VoodooControl("span", "css", ".preview-pane .fld_picture .label-Accounts").assertVisible(true);
		nameCtrl.assertVisible(true);

		// Click on forward arrow in the preview pane
		// TODO: VOOD-1848: Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		new VoodooControl("a", "css", ".btn-right").click();
		VoodooUtils.waitForReady();

		// Verify that preview of the record on the right hand side pane displayed with it's detail
		// TODO: VOOD-1848: Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		new VoodooControl("span", "css", ".preview-pane .fld_picture .label-Cases").assertVisible(true);
		nameCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}