
package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21855 extends SugarTest {	
	DataSource searchText;

	public void setup() throws Exception {
		searchText = testData.get(testName);
		FieldSet fs = new FieldSet();
		for(int i = 0; i < searchText.size(); i++) {
			fs.clear();
			fs.put("firstName", searchText.get(i).get("fullName"));
			sugar().leads.api.create(fs);
		}
		sugar().login();
	}
	/**
	 * Verify searching leads by typing in the filter will return correct result
	 * 
	 * @throws Exception
	 */
	// TR-6221 (Verify searching leads by typing in the filter - Doesn't return the search results when searching something with symbol (eg.'s))
	@Test
	public void Leads_21855_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();

		for (int i = 0; i <searchText.size(); i++) {
			VoodooUtils.voodoo.log.info("Searching " + searchText.get(i).get("text"));
			sugar().leads.listView.setSearchString(searchText.get(i).get("text"));
			VoodooUtils.pause(1000); // TODO: VOOD-1395
			sugar().alerts.waitForLoadingExpiration();
			if(i==2) // search with negative data
				sugar().leads.listView.assertIsEmpty();
			else
				sugar().leads.listView.verifyField(1,"fullName",searchText.get(i).get("fullName"));
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
