package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19800 extends SugarTest {
	DataSource ContractData;
	
	public void setup() throws Exception {
		ContractData = testData.get(testName);
		sugar.contracts.api.create(ContractData);
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Search Contract_Verify that cleaning all the search done before works correctly by clicking "Clear" button.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19800_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		sugar.contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Basic Search
		// Enter input text in the Contract name search bar and click 'Clear' button
		VoodooControl nameBasic = sugar.contracts.listView.getControl("nameBasic");
		nameBasic.set(sugar.contracts.getDefaultData().get("name"));
		VoodooUtils.focusDefault();
		sugar.contracts.listView.clearSearchForm();
		
		// Verify that 'Clear' only clear inputed search option, not do search
		VoodooUtils.focusFrame("bwc-frame");
		nameBasic.assertEquals("", true);
		VoodooUtils.focusDefault();
		Assert.assertFalse("Record count is equal to 1 when it should not", sugar.contracts.listView.countRows() == 1);
		
		// Advance Search
		// Enter any input text in the Contract name search bar and click 'Clear' button
		sugar.contracts.listView.getControl("advancedSearchLink").click();
		
		// TODO: VOOD-975
		new VoodooControl("input", "id", "name_advanced").set(sugar.contracts.getDefaultData().get("name"));
		new VoodooControl("input", "id", "search_form_clear_advanced").click();
		
		// Verify that 'Clear' only clear inputed search option, not do search
		nameBasic.assertEquals("", true);
		VoodooUtils.focusDefault();
		Assert.assertFalse("Record count is equal to 1 when it should not", sugar.contracts.listView.countRows() == 1);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}