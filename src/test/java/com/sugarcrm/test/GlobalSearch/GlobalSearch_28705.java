package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28705 extends SugarTest {
	public void setup() throws Exception {
		// Create 3 records in Accounts, Cases and Contracts Module
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().cases.api.create(fs);
		sugar().contracts.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify Facet Dashlet Functionality 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28705_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter any value in the Search bar and hit enter
		// TODO: CB-252, VOOD-1437
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		String searchString = testName.substring(0, 6);
		globalSearchCtrl.append(searchString + '\uE007');
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1848
		VoodooControl contractsCtrl = new VoodooControl("li", "css", "[data-facet-criteria='Contracts']");
		VoodooControl accountsCtrl = new VoodooControl("li", "css", "[data-facet-criteria='Accounts']");
		VoodooControl casesCtrl = new VoodooControl("li", "css", "[data-facet-criteria='Cases']");
		VoodooControl assignedUserId = new VoodooControl("li", "css", "[data-facet-criteria='assigned_user_id']");
		VoodooControl createdBy = new VoodooControl("li", "css", ".layout_Home li:nth-child(3) .dashlet-content > div");
		VoodooControl resultCount = new VoodooControl("span", "css", ".fld_collection-count .count");
		
		// Verify Facet dashlets are appeared in RHS pane with modules
		contractsCtrl.assertVisible(true);
		accountsCtrl.assertVisible(true);
		casesCtrl.assertVisible(true);
				
		// Verify user can Select/Deselect my facet criteria that is available within each Facet Dashlet
		assignedUserId.click();
		VoodooUtils.waitForReady();
		assignedUserId.assertAttribute("class", "selected", true);
		assignedUserId.click();
		VoodooUtils.waitForReady();
		assignedUserId.assertAttribute("class", "selected", false);
		
		// Verify results within each Facet Dashlets will be sorted by count
		contractsCtrl.click();
		VoodooUtils.waitForReady();
		resultCount.assertContains("(1)", true);	
		new VoodooControl("span", "css", ".nav.search-results .label-Contracts").assertContains("Co", true);
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		resultCount.assertContains("(2)", true);	
		new VoodooControl("span", "css", ".nav.search-results .label-Accounts").assertContains("Ac", true);
		
		// Verify results for any Facet Dashlets When count is 0, the facet is visible but disabled
		createdBy.assertVisible(true);
		createdBy.assertAttribute("class", "disabled", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}