package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Users_29688 extends SugarTest {
	FieldSet maxEntryPerPage = new FieldSet();
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().targetlists.api.create();
		sugar().login();

		// TODO: VOOD-822
		// Create report by UI
		VoodooControl rowAndCol = new VoodooControl("img", "css", "[name='rowsColsImg']");
		VoodooControl usersModule = new VoodooControl("table", "id", "Users");
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		VoodooControl userNameField = new VoodooControl("input", "id", "Users_user_name");
		VoodooControl saveReport = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRun = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");

		// 2 reports against Users module
		for(int i=1; i<3; i++){
			sugar().navbar.selectMenuItem(sugar().reports, "createReport");
			VoodooUtils.focusFrame("bwc-frame");
			rowAndCol.click();
			usersModule.click();
			nextBtn.click();
			userNameField.click();
			nextBtn.click();
			saveReport.set(testName+"_"+i);
			saveAndRun.click();
			sugar().alerts.waitForLoadingExpiration();
			VoodooUtils.focusDefault();
		}

		// Changing listview Items per page in Admin > System Settings to '1', needed for more record functionality
		maxEntryPerPage.put("maxEntriesPerPage",customDS.get(0).get("maxEntriesPerPage"));
		sugar().admin.setSystemSettings(maxEntryPerPage);
	}

	/**
	 * Verify that correct count of Total records displaying in "Search and Select Reports" drawer
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29688_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);
		StandardSubpanel usersSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		usersSubpanel.clickOnSelectFromReport();

		// Verify Header Should display correct count of total records contains by Search List for applied filter.
		// TODO: VOOD-1843
		VoodooControl count = new VoodooControl("span", "css", sugar().reports.searchSelect.getControl("count").getHookString() +" .count");
		count.assertEquals(customDS.get(0).get("headerCount"), true);

		// Click on 2+ Tool-tip ~ Click for total link.
		// Verify Header Should display correct count of total records contains by Search List for applied filter.
		new VoodooControl("a", "css",count.getHookString()).click();
		VoodooUtils.waitForReady();
		count.assertEquals(customDS.get(1).get("headerCount"), true);

		// Click on More reports
		// TODO: VOOD-1487
		new VoodooControl("button", "css", ".layout_Reports button[data-action='show-more']").click();
		VoodooUtils.waitForReady();

		// Verify only 2 reports which we have created in set up
		Assert.assertTrue("Reports count not equals two", sugar().reports.searchSelect.countRows() == Integer.parseInt(customDS.get(2).get("maxEntriesPerPage")));
		count.assertEquals(customDS.get(2).get("headerCount"), true);
		sugar().reports.searchSelect.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}