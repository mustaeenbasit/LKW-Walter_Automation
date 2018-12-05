package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28731 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Verify global search results page. 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28731_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Global search something and hit Enter.
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		// TODO: CB-252,VOOD-1437
		String searchString1 = accountData.get(0).get("workPhone");
		globalSearchCtrl.append(searchString1 + '\uE007');
		VoodooUtils.waitForReady();

		FieldSet customData = testData.get(testName+"_customData").get(0);
		// Verify "Title Header" search string
		sugar().globalSearch.getControl("headerpaneTitle").assertEquals(customData.get("title"), true);

		// Verify  Module icon 
		VoodooControl rowCtrl = sugar().globalSearch.getRow(1);
		rowCtrl.assertContains(customData.get("moduleIcon"), true);

		// Verify name of the record
		rowCtrl.assertContains(accountData.get(0).get("name"), true);

		// Verify search results page display the highlighted fields that match search string.
		rowCtrl.assertContains(searchString1, true);
		// TODO: VOOD-1843
		new VoodooControl("strong", "css", rowCtrl.getHookString()+" "+".secondary span strong").getTag().contains(customData.get("tag"));

		// Global search some record with long string and hit Enter.
		String searchString2 = accountData.get(2).get("description");
		globalSearchCtrl.set(searchString2 + '\uE007');
		VoodooUtils.waitForReady();

		// Verify it Display certain amount of data
		rowCtrl.assertContains(searchString2.substring(0, 15), true);

		// Click on the record in results page.
		// TODO: VOOD-1843 Uncomment L#60 & remove L#61 once VOOD-1843 is resolved
		// sugar().globalSearch.getRow(1).getChildElement("a", "css", ".nav.search-results .list.fld_name a").click();
		new VoodooControl("a", "css", rowCtrl.getHookString()+" "+".list.fld_name a").click();
		VoodooUtils.waitForReady();

		// Verify user should be able to navigate to the record
		sugar().accounts.recordView.assertVisible(true);
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountData.get(2).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}