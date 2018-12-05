package com.sugarcrm.test.KnowledgeBase;

import java.util.ArrayList;
import org.junit.Assert;
import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21462 extends SugarTest {
	DataSource kbRecords = new DataSource();

	// Picking different dates for publish dates
	String currentDate = DateTime.now().toString("MM/dd/yyyy");
	String yesterdayDate = DateTime.now().minusDays(1).toString("MM/dd/yyyy");
	String tomorrowDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");
	// Picking 1st date of last month so that last 30 days date and last month date will not collapse when choosing filter.
	String firstDateOfLastMonth = DateTime.now().minusMonths(1).dayOfMonth().withMinimumValue().toString("MM/dd/yyyy");
	// Picking last date of Next month so that Next 30 days date and Next month date will not collapse when choosing filter.
	String lastDateOfNextMonth = DateTime.now().plusMonths(1).dayOfMonth().withMaximumValue().toString("MM/dd/yyyy");
	String lastYearDate = DateTime.now().minusYears(1).toString("MM/dd/yyyy");
	String nextYearDate = DateTime.now().plusYears(1).toString("MM/dd/yyyy");

	public void setup() throws Exception {
		kbRecords = testData.get(testName);
		FieldSet fs = new FieldSet();
		ArrayList<String> publishDates = new ArrayList<String>();
		publishDates.add(currentDate);
		publishDates.add(yesterdayDate);
		publishDates.add(tomorrowDate);
		publishDates.add(firstDateOfLastMonth);
		publishDates.add(lastDateOfNextMonth);
		publishDates.add(lastYearDate);
		publishDates.add(nextYearDate);

		// Creating multiple KB records with different publish dates
		for (int i = 0; i < publishDates.size(); i++) {
			fs.put("date_publish", publishDates.get(i));
			fs.put("name", kbRecords.get(i).get("name"));
			sugar().knowledgeBase.api.create(fs);
			fs.clear();
		}

		// Login to sugar
		sugar().login();
		
		// Enable Kb module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Adding publish date from hidden to default in studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add "Publish date" field to "ListView"
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(new VoodooControl("li", "css", "#Default li[data-name='language']"));

		// Save and Deploy
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault(); 
	}

	/**
	 * Test Case 21462: search_published
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21462_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// ListView Articles
		String KB1 = kbRecords.get(0).get("name");
		String KB2 = kbRecords.get(1).get("name");
		String KB3 = kbRecords.get(2).get("name");
		String KB4 = kbRecords.get(3).get("name");
		String KB5 = kbRecords.get(4).get("name");
		String KB6 = kbRecords.get(5).get("name");
		String KB7 = kbRecords.get(6).get("name");
		
		// KB List View Controls
		ListView kbListView = sugar().knowledgeBase.listView;
		VoodooControl firstRowArticleName = kbListView.getDetailField(1, "name");
		VoodooControl secondRowArticleName = kbListView.getDetailField(2, "name");
		VoodooControl thirdRowArticleName = kbListView.getDetailField(3, "name");
		VoodooControl fourthRowArticleName = kbListView.getDetailField(4, "name");
		VoodooControl fifthRowArticleName = kbListView.getDetailField(5, "name");
		
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		// KB ListView Publish dates row controls
		VoodooControl firstRowPublishDate = new VoodooControl("span", "css", ".list.fld_active_date");
		VoodooControl secondRowPublishDate = new VoodooControl("span", "css", ".flex-list-view-content tbody tr:nth-child(2) .list.fld_active_date");
		VoodooControl thirdRowPublishDate = new VoodooControl("span", "css", ".flex-list-view-content tbody tr:nth-child(3) .list.fld_active_date");
		VoodooControl fourthRowPublishDate = new VoodooControl("span", "css", ".flex-list-view-content tbody tr:nth-child(4) .list.fld_active_date");
		VoodooControl fifthRowPublishDate = new VoodooControl("span", "css", ".flex-list-view-content tbody tr:nth-child(5) .list.fld_active_date");
		
		// Navigate knowledge base listview
		sugar().knowledgeBase.navToListView();

		// Click on create filter
		kbListView.getControl("searchFilterCurrent").click();
		VoodooUtils.waitForReady();

		// Filter1: Verifying 'is equal to' operator for publish date
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		VoodooSelect kbFilterOperator = new VoodooSelect("div", "css", "div[data-filter='operator']");
		VoodooControl kbFilterValue = new VoodooControl("input", "css", ".detail.fld_active_date input");
		new VoodooSelect("div", "css", "div[data-filter='field']").set(kbRecords.get(0).get("filterField"));
		kbFilterOperator.set(kbRecords.get(0).get("publishDateFilterOperator"));
		kbFilterValue.set(currentDate);
		VoodooUtils.waitForReady();

		// Verifying filter result should display only one record which is match to date
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB1, true);
		firstRowPublishDate.assertEquals(currentDate, true);

		// Filter2: Verifying 'before' operator for publish date
		kbFilterOperator.set(kbRecords.get(1).get("publishDateFilterOperator"));
		kbFilterValue.set(currentDate);
		VoodooUtils.waitForReady();

		// Verifying filter result should display 3 records which are before the entered date
		Assert.assertTrue(kbListView.countRows() == 3);
		firstRowArticleName.assertEquals(KB6, true);
		firstRowPublishDate.assertEquals(lastYearDate, true);
		secondRowArticleName.assertEquals(KB4, true);
		secondRowPublishDate.assertEquals(firstDateOfLastMonth, true);
		thirdRowArticleName.assertEquals(KB2, true);
		thirdRowPublishDate.assertEquals(yesterdayDate, true);

		// Filter3: Verifying 'after' operator for publish date
		kbFilterOperator.set(kbRecords.get(2).get("publishDateFilterOperator"));
		kbFilterValue.set(currentDate);
		VoodooUtils.waitForReady();

		// Verifying filter result should display 3 records which are before the entered date
		Assert.assertTrue(kbListView.countRows() == 3);
		firstRowArticleName.assertEquals(KB7, true);
		firstRowPublishDate.assertEquals(nextYearDate, true);
		secondRowArticleName.assertEquals(KB5, true);
		secondRowPublishDate.assertEquals(lastDateOfNextMonth, true);
		thirdRowArticleName.assertEquals(KB3, true);
		thirdRowPublishDate.assertEquals(tomorrowDate, true);

		// Filter4: Verifying 'yesterday' operator for publish date
		kbFilterOperator.set(kbRecords.get(3).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only one record which is match to date
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB2, true);
		firstRowPublishDate.assertEquals(yesterdayDate, true);

		// Filter5: Verifying 'today' operator for publish date
		kbFilterOperator.set(kbRecords.get(4).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only one record which is match to date
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB1, true);
		firstRowPublishDate.assertEquals(currentDate, true);

		// Filter6: Verifying 'tomorrow' operator for publish date
		kbFilterOperator.set(kbRecords.get(5).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only one record which is match to date
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB3, true);
		firstRowPublishDate.assertEquals(tomorrowDate, true);

		// Filter7: Verifying 'Last 7 days' operator for publish date
		kbFilterOperator.set(kbRecords.get(6).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records which are match with last 7 days
		Assert.assertTrue(kbListView.countRows() == 2);
		firstRowArticleName.assertEquals(KB2, true);
		firstRowPublishDate.assertEquals(yesterdayDate, true);
		secondRowArticleName.assertEquals(KB1, true);
		secondRowPublishDate.assertEquals(currentDate, true);

		// Filter8: Verifying 'next 7 days' operator for publish date
		kbFilterOperator.set(kbRecords.get(7).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records which are match with next 7 days
		Assert.assertTrue(kbListView.countRows() == 2);
		firstRowArticleName.assertEquals(KB3, true);
		firstRowPublishDate.assertEquals(tomorrowDate, true);
		secondRowArticleName.assertEquals(KB1, true);
		secondRowPublishDate.assertEquals(currentDate, true);

		// Filter9: Verifying 'last 30 days' operator for publish date
		kbFilterOperator.set(kbRecords.get(8).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records which are match with last 30 days
		Assert.assertTrue(kbListView.countRows() == 2);
		firstRowArticleName.assertEquals(KB2, true);
		firstRowPublishDate.assertEquals(yesterdayDate, true);
		secondRowArticleName.assertEquals(KB1, true);
		secondRowPublishDate.assertEquals(currentDate, true);

		// Filter10: Verifying 'next 30 days' operator for publish date
		kbFilterOperator.set(kbRecords.get(9).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records which are match with last 30 days
		Assert.assertTrue(kbListView.countRows() == 2);
		firstRowArticleName.assertEquals(KB3, true);
		firstRowPublishDate.assertEquals(tomorrowDate, true);
		secondRowArticleName.assertEquals(KB1, true);
		secondRowPublishDate.assertEquals(currentDate, true);

		// Filter11: Verifying 'last month' operator for publish date
		kbFilterOperator.set(kbRecords.get(10).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records when, month of yesterdayDate = month firstDateOfLastMonth else display only one record.
		// Picking month of 'yesterdayDate' and firstDateOfLastMonth
		// Verifying month is equal or not
		if (yesterdayDate.regionMatches(0, firstDateOfLastMonth, 0, 2)) {
			Assert.assertTrue(kbListView.countRows() == 2);
			firstRowArticleName.assertEquals(KB4, true);
			firstRowPublishDate.assertEquals(firstDateOfLastMonth, true);
			secondRowArticleName.assertEquals(KB2, true);
			secondRowPublishDate.assertEquals(yesterdayDate, true);
		}
		else {
			Assert.assertTrue(kbListView.countRows() == 1);
			firstRowArticleName.assertEquals(KB4, true);
			firstRowPublishDate.assertEquals(firstDateOfLastMonth, true);
		}

		// Filter12: Verifying 'this month' operator for publish date
		kbFilterOperator.set(kbRecords.get(11).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result in different scenario
		// Picking month of 'before30DaysDate' and firstDateOfLastMonth
		// Verifying month is equal or not
		// 1: Verifying when month of yesterdayDate != month of currentDate filter should display two records
		if (!(yesterdayDate.regionMatches(0, currentDate, 0, 2))) {
			Assert.assertTrue(kbListView.countRows() == 2);
			firstRowArticleName.assertEquals(KB3, true);
			firstRowPublishDate.assertEquals(tomorrowDate, true);
			secondRowArticleName.assertEquals(KB1, true);
			secondRowPublishDate.assertEquals(currentDate, true);
		}
		// 2: Verifying when month of tomorrow != month of currentDate filter should display two records
		else if (!(tomorrowDate.regionMatches(0, currentDate, 0, 2))) {
			Assert.assertTrue(kbListView.countRows() == 2);
			firstRowArticleName.assertEquals(KB2, true);
			firstRowPublishDate.assertEquals(yesterdayDate, true);
			secondRowArticleName.assertEquals(KB1, true);
			secondRowPublishDate.assertEquals(currentDate, true);
		} 
		// 3: Verifying when both above conditions not matched filter should display three records
		else {
			Assert.assertTrue(kbListView.countRows() == 3);
			firstRowArticleName.assertEquals(KB3, true);
			firstRowPublishDate.assertEquals(tomorrowDate, true);
			secondRowArticleName.assertEquals(KB2, true);
			secondRowPublishDate.assertEquals(yesterdayDate, true);
			thirdRowArticleName.assertEquals(KB1, true);
			thirdRowPublishDate.assertEquals(currentDate, true);
		}

		// Filter13: Verifying 'next month' operator for publish date
		kbFilterOperator.set(kbRecords.get(12).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 2 records when, month of yesterdayDate = month firstDateOfLastMonth else display only one record.
		// Picking month of 'yesterdayDate' and firstDateOfLastMonth
		// Verifying month is equal or not
		if (tomorrowDate.regionMatches(0, lastDateOfNextMonth, 0, 2)) {
			Assert.assertTrue(kbListView.countRows() == 2);
			firstRowArticleName.assertEquals(KB5, true);
			firstRowPublishDate.assertEquals(lastDateOfNextMonth, true);
			secondRowArticleName.assertEquals(KB3, true);
			secondRowPublishDate.assertEquals(tomorrowDate, true);
		}
		else {
			Assert.assertTrue(kbListView.countRows() == 1);
			firstRowArticleName.assertEquals(KB5, true);
			firstRowPublishDate.assertEquals(lastDateOfNextMonth, true);
		}

		// Filter14: Verifying 'last year' operator for publish date
		kbFilterOperator.set(kbRecords.get(13).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 1 records which is match to last year.
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB6, true);
		firstRowPublishDate.assertEquals(lastYearDate, true);

		// Filter15: Verifying 'this year' operator for publish date
		kbFilterOperator.set(kbRecords.get(14).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 5 records which is match to last year.
		Assert.assertTrue(kbListView.countRows() == 5);
		firstRowArticleName.assertEquals(KB5, true);
		firstRowPublishDate.assertEquals(lastDateOfNextMonth, true);
		secondRowArticleName.assertEquals(KB4, true);
		secondRowPublishDate.assertEquals(firstDateOfLastMonth, true);
		thirdRowArticleName.assertEquals(KB3, true);
		thirdRowPublishDate.assertEquals(tomorrowDate, true);
		fourthRowArticleName.assertEquals(KB2, true);
		fourthRowPublishDate.assertEquals(yesterdayDate, true);
		fifthRowArticleName.assertEquals(KB1, true);
		fifthRowPublishDate.assertEquals(currentDate, true);

		// Filter16: Verifying 'Next year' operator for publish date
		kbFilterOperator.set(kbRecords.get(15).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 1 records which is match to last year.
		Assert.assertTrue(kbListView.countRows() == 1);
		firstRowArticleName.assertEquals(KB7, true);
		firstRowPublishDate.assertEquals(nextYearDate, true);

		// Filter17: Verifying 'Next year' operator for publish date
		kbFilterOperator.set(kbRecords.get(16).get("publishDateFilterOperator"));
		VoodooUtils.waitForReady();
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		new VoodooControl("input", "css", "[name='active_date_min']").set(yesterdayDate);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='active_date_max']").set(tomorrowDate);
		VoodooUtils.waitForReady();

		// Verifying filter result should display only 3 records which is match range given in filter.
		Assert.assertTrue(kbListView.countRows() == 3);
		firstRowArticleName.assertEquals(KB3, true);
		firstRowPublishDate.assertEquals(tomorrowDate, true);
		secondRowArticleName.assertEquals(KB2, true);
		secondRowPublishDate.assertEquals(yesterdayDate, true);
		thirdRowArticleName.assertEquals(KB1, true);
		thirdRowPublishDate.assertEquals(currentDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 