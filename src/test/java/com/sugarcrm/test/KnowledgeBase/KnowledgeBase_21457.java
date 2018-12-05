package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21457 extends SugarTest {
	DataSource kbData = new DataSource();
	
	public void setup() throws Exception {
		// Initializing KB records test data
		kbData = testData.get(testName + "_kb");
				
		// Create KB records with different view counts
		sugar().knowledgeBase.api.create(kbData);
		
		// Login as admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Adding View Count to Knowledge Base > Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on studio link  
		// TODO: VOOD-1509 Support Studio Module Search Layout View
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// Click on KB in studio panel 
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		
		// Click on layout
		new VoodooControl("a", "css", "#layoutsBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		// Click on search
		new VoodooControl("a", "css", "#searchBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		// Click on search
		new VoodooControl("a", "css", "#FilterSearchBtn tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		// Dragging and dropping 'View Count' field from hidden to default 
		new VoodooControl("li", "css", "[data-name='viewcount']").dragNDrop(new VoodooControl("li", "css", "[data-name='status']"));
		VoodooUtils.waitForReady();
		
		// Saving settings
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that correct KB articles are displayed upon filtering based on KB View Count [search_viewing_frequency]
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21457_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		DataSource customData = testData.get(testName);
		
		// Navigate to KB Module
		sugar().knowledgeBase.navToListView();
		
		// Sorting KB Records before applying filters
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		
		// Verify all 5 KB records are displayed before applying filters
		Assert.assertTrue("Row count is not equal to 5, when it should.", sugar().knowledgeBase.listView.countRows() == kbData.size());
		
		// Creating filters
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// TODO: VOOD-1785 Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		// Setting filter field as 'View Count'
		new VoodooSelect("div", "css", "div[data-filter='field']").set(customData.get(0).get("field"));
		VoodooSelect operator = new VoodooSelect("div", "css", "div[data-filter='operator']");
		VoodooControl value = new VoodooControl("input", "css", ".detail.fld_viewcount input");
		
		// Applying filter criterion 1
		operator.set(customData.get(0).get("operator"));
		value.set(customData.get(0).get("value"));	
		VoodooUtils.waitForReady();
		
		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(0).get("rowCount")), 0);
		
		// Applying filter criterion 2
		operator.set(customData.get(1).get("operator"));
		value.set(customData.get(1).get("value"));	
		VoodooUtils.waitForReady();
		
		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(1).get("rowCount")), 1);
		
		// Applying filter criterion 3
		operator.set(customData.get(2).get("operator"));
		value.set(customData.get(2).get("value"));	
		VoodooUtils.waitForReady();
		
		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(2).get("rowCount")), 0);
		
		// Applying filter criterion 4
		operator.set(customData.get(3).get("operator"));
		value.set(customData.get(3).get("value"));	
		VoodooUtils.waitForReady();

		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(3).get("rowCount")), 1);
		
		// Applying filter criterion 5
		operator.set(customData.get(4).get("operator"));
		value.set(customData.get(4).get("value"));	
		VoodooUtils.waitForReady();

		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(4).get("rowCount")), 0);
		
		// Applying filter criterion 6
		operator.set(customData.get(5).get("operator"));
		value.set(customData.get(5).get("value"));	
		VoodooUtils.waitForReady();

		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(5).get("rowCount")), 1);
		
		// Applying filter criterion 7
		operator.set(customData.get(6).get("operator"));
		value.set(customData.get(6).get("value"));	
		VoodooUtils.waitForReady();

		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(6).get("rowCount")), 0);
		
		// Applying filter criterion 8
		operator.set(customData.get(7).get("operator"));
		
		// TODO: VOOD-1766 Support range operators like “is between” in filters
		new VoodooControl("input", "css", ".detail.fld_viewcount_min input").set(customData.get(7).get("value"));
		new VoodooControl("input", "css", ".detail.fld_viewcount_max input").set(customData.get(7).get("valueMax"));	
		VoodooUtils.waitForReady();

		// Verify that the records are displayed as per the above filter criterion
		verifyFilteredRecords(Integer.parseInt(customData.get(7).get("rowCount")), 0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	// Method to verify records in listview
	private void verifyFilteredRecords(int numberOfRecordsToBeVerified, int indexStart) throws Exception {
		// Verify that expected number of KB records are returned
		Assert.assertTrue("Row count is not equal to " + numberOfRecordsToBeVerified + ", when it should.", sugar().knowledgeBase.listView.countRows() == numberOfRecordsToBeVerified);
		
		//  Verify records in each of the rows in listview
		for (int i = 1 ; i <= numberOfRecordsToBeVerified ; i++) {
			sugar().knowledgeBase.listView.getDetailField(i, "name").assertEquals(kbData.get(indexStart).get("name"), true);
			indexStart++;
		} 
	}

	public void cleanup() throws Exception {}
}