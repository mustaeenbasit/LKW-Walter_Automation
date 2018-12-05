package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20886 extends SugarTest {
	DataSource ds1, ds2;

	public void setup() throws Exception {
		ds1 = testData.get(testName);
		ds2 = testData.get(testName+"_1");
		sugar().productCatalog.api.create(ds1);
		sugar().login();	
		// change display number on list view 
		sugar().admin.setSystemSettings(ds2.get(0));
	}

	/**
	 *  Verify clicking more in list view will display more product catalog
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20886_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		int i,j=1,k,l;
		// TODO VOOD-727
		VoodooControl loadMore = new VoodooControl("button", "css" ,"button.btn-link.more");
		k = l = Integer.parseInt(ds2.get(0).get("maxEntriesPerPage"));	
		// click More product link and verify it only load the number of records that set in system setting
		while (loadMore.queryVisible()){
			new VoodooControl("tr", "css" ,"div.flex-list-view-content tbody tr:nth-child("+k+1+")").assertExists(false);	
			loadMore.click();			
			j=j+1;
			for (i=k+1;i<l*j&&i<=ds1.size(); i++){
				new VoodooControl("tr", "css" ,"div.flex-list-view-content tbody tr:nth-child("+i+")").assertElementContains(ds1.get(ds1.size()-i).get("name"), true);
			}
			k=i;
		}		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}