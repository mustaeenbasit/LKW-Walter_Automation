package com.sugarcrm.test.grimoire;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class GlobalSearchTests extends SugarTest {
	ArrayList<Record> accountRecords = new ArrayList<Record>();
	String searchStr = "Account";

	public void setup() throws Exception {
		DataSource ds = testData.get(testName);

		accountRecords = sugar().accounts.api.create(ds);
		sugar().login();

		sugar().navbar.setGlobalSearch(searchStr);
		sugar().navbar.viewAllResults();
	}

	@Test
	public void toggleSidebar() throws Exception {
		VoodooUtils.voodoo.log.info("Running toggleSidebar()...");

		// TODO: VOOD-1848
		VoodooControl dashboard = new VoodooControl("div", "css", ".dashboard");
		dashboard.assertVisible(true);

		sugar().globalSearch.toggleSidebar();
		dashboard.assertVisible(false);

		sugar().globalSearch.toggleSidebar();
		dashboard.assertVisible(true);

		VoodooUtils.voodoo.log.info("toggleSidebar() complete.");
	}

	@Test
	public void getRowByIndex() throws Exception {
		VoodooUtils.voodoo.log.info("Running getRowByIndex()...");

		// TODO: VOOD-1828
		sugar().globalSearch.getRow(1).assertContains("Ac", true);

		VoodooUtils.voodoo.log.info("getRowByIndex() complete.");
	}

	@Test
	public void getRowByRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running getRowByRecord()...");

		AccountRecord myAcc = (AccountRecord)accountRecords.get(4);
		VoodooControl rowByRecord = sugar().globalSearch.getRow(myAcc);
		rowByRecord.assertContains(myAcc.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("getRowByRecord() complete.");
	}

	@Test
	public void previewRecordByRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running previewRecordByRecord()...");

		sugar().globalSearch.preview(accountRecords.get(3));
		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.getPreviewPaneField("name").assertEquals(accountRecords.get(3).getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("previewRecordByRecord() complete.");
	}

	@Test
	public void clickRecordByIndex() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickRecordByIndex()...");

		sugar().globalSearch.clickRecord(2);
		sugar().accounts.recordView.getDetailField("name").assertElementContains("Ac", true);

		VoodooUtils.voodoo.log.info("clickRecordByIndex() complete.");
	}

	@Test
	public void clickRecordByRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickRecordByRecord()...");

		sugar().globalSearch.clickRecord(accountRecords.get(4));
		sugar().accounts.recordView.assertElementContains(accountRecords.get(4).getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("clickRecordByRecord() complete.");
	}

	@Test
	public void viewAllResults() throws Exception {
		VoodooUtils.voodoo.log.info("Running viewAllResults()...");

		sugar().globalSearch.getControl("headerpaneTitle").assertElementContains(searchStr, true);

		VoodooUtils.voodoo.log.info("viewAllResults() complete.");
	}

	public void cleanup() throws Exception {}
}