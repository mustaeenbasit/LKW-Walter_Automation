package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_27787 extends SugarTest {
	DataSource acccountsData;
	FieldSet customData;
	
	public void setup() throws Exception {
		acccountsData = testData.get(testName);
		customData = testData.get(testName+"_custom").get(0);
		sugar.accounts.api.create(acccountsData);
		sugar.login();
	}
	/**
	*  Verify the filter results remain the same while switching on different views
	*  
	*  @throws Exception
	*/
	/**
	 * @throws Exception
	 */
	@Test
	public void ListView_27787_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.accounts.navToListView();
		sugar.accounts.listView.openFilterDropdown();
		sugar.accounts.listView.selectFilterCreateNew();
		
		// TODO: VOOD-555
		// Controls for Accounts filters
		VoodooSelect moduleFieldsDropdown = new VoodooSelect("div", "css", ".filter-definition-container .controls.span4");
		VoodooSelect filterCriteria = new VoodooSelect("select", "css", ".filter-definition-container div:nth-child(2)");
		VoodooSelect industryField = new VoodooSelect("input", "css", ".filter-definition-container div:nth-child(3) input");
		VoodooSelect tagInIndustryField = new VoodooSelect("div", "css", ".filter-options .filter-definition-container ul div");
		
		moduleFieldsDropdown.set(customData.get("moduleField"));
		filterCriteria.set(customData.get("filterCriteria"));
		industryField.set(customData.get("industryField"));
		VoodooUtils.waitForReady();
		int rows = sugar.accounts.listView.countRows();
		
		/* Verifying that 3 records are visible 
		 * Where 1 is the number of non technology accounts
		 */
		Assert.assertEquals("Number of records not equal to 3 when they should be", rows, (acccountsData.size() - 1));
		
		// Navigating to Contacts ListView
		sugar.contacts.navToListView();
		
		// Navigating back to the Accounts List View
		sugar.accounts.navToListView();
		
		// Verifying the filters are intact
		moduleFieldsDropdown.assertEquals(customData.get("moduleField"), true);
		filterCriteria.assertEquals(customData.get("filterCriteria"), true);
		tagInIndustryField.assertEquals(customData.get("industryField"), true);
		
		/* Verify that the filters still show the same number of records as before
		 * Where 1 is the number of non technology accounts
		 */
		Assert.assertEquals("Number of records not equal to 3 when they should be", rows, (acccountsData.size() - 1));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
		
	public void cleanup() throws Exception {}
}