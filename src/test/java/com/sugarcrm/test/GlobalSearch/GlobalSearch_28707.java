package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class GlobalSearch_28707 extends SugarTest {
	DataSource leadData = new DataSource();

	public void setup() throws Exception {
		leadData = testData.get(testName);
		DataSource accountData = testData.get(testName+"_accountData");
		sugar().leads.api.create(leadData);
		sugar().accounts.api.create(accountData);
		FieldSet fs = new FieldSet();
		fs.put("lastName", "Test");
		sugar().contacts.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify user can select multiple facet criteria within one facet dashlet
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28707_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter any value in search bar & hit Enter.
		// TODO: CB-252,VOOD-1437
		sugar().navbar.getControl("globalSearch").append(leadData.get(0).get("description") + '\uE007');
		FieldSet customData = testData.get(testName+"_customData").get(0);

		// Verify count within module dashlet before selecting any facet criteria
		// TODO: VOOD-1848
		VoodooControl leadsFacetCtrl = new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().leads.moduleNamePlural+"']");
		leadsFacetCtrl.assertContains(customData.get("leadsInitialCount"), true);
		VoodooControl accountsFacetCtrl = new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().accounts.moduleNamePlural+"']");
		accountsFacetCtrl.assertContains(customData.get("accountsInitialCount"), true);

		// Verify count within other dashlet before selecting any facet criteria
		// TODO: VOOD-1848
		VoodooControl dashletCtrl = new VoodooControl("span", "css", "[data-facet-criteria='assigned_user_id'] span");
		dashletCtrl.assertContains(customData.get("assignedToMeInitialCount"), true);

		// Select multiple facet criteria within a facet dashlet like Leads(5)& Accounts(1)
		leadsFacetCtrl.click();
		accountsFacetCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 6 records are displayed on my search page
		new VoodooControl("span", "css", "[data-voodoo-name='collection-count'] span").assertContains(customData.get("countAfterSelection"), true);

		// Verify count within modules dashlet do not change.
		leadsFacetCtrl.assertContains(customData.get("leadsInitialCount"), true);
		accountsFacetCtrl.assertContains(customData.get("accountsInitialCount"), true);

		// Verify count within other dashlet change.
		dashletCtrl.assertContains(customData.get("countAfterSelection"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}