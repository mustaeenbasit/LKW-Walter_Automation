package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28706 extends SugarTest {
	DataSource leadData = new DataSource();

	public void setup() throws Exception {
		leadData = testData.get(testName);
		DataSource accountData = testData.get(testName+"_accountData");
		sugar().leads.api.create(leadData);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Verify one facet criteria within a facet dashlet
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28706_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter any value in search bar & hit Enter.
		// TODO: CB-252,VOOD-1437
		sugar().navbar.getControl("globalSearch").append(leadData.get(0).get("description") + '\uE007');
		FieldSet customData = testData.get(testName+"_customData").get(0);

		// Verify count within other dashlet before selecting any facet criteria
		// TODO: VOOD-1848
		VoodooControl dashletCtrl = new VoodooControl("span", "css", "[data-facet-criteria='assigned_user_id'] span");
		dashletCtrl.assertContains(customData.get("totalInitialCount"), true);

		// Select one facet criteria within a facet dashlet like Leads(5)
		// TODO: VOOD-1848
		VoodooControl facetCriteriaCtrl = new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().leads.moduleNamePlural+"']");
		facetCriteriaCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 5 records are displayed on my search page
		new VoodooControl("span", "css", "[data-voodoo-name='collection-count'] span").assertContains(customData.get("countAfterSelection"), true);

		// Verify count within module dashlet do not change.
		facetCriteriaCtrl.assertContains(customData.get("moduleInitialCount"), true);

		// Verify count within other dashlet change.
		dashletCtrl.assertContains(customData.get("countAfterSelection"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}