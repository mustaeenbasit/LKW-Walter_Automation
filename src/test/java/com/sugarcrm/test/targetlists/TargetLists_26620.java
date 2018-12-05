package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class TargetLists_26620 extends SugarTest {
	TargetListRecord targetListRecord;
	DataSource dataSource;
	
	public void setup() throws Exception {
		sugar.login();
		targetListRecord = (TargetListRecord) sugar.targetlists.api.create();
		dataSource = testData.get(testName);
	}

	/**
	 * Copy targetlist only fields
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		targetListRecord.navToRecord();
		sugar.targetlists.recordView.copy();
		sugar.targetlists.recordView.getEditField("targetlistName").set(dataSource.get(0).get("targetlistName"));
		sugar.targetlists.createDrawer.save();
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().closeAlert();
		sugar.targetlists.recordView.getDetailField("targetlistName").waitForVisible();
		sugar.targetlists.recordView.getDetailField("targetlistName").assertContains(dataSource.get(0).get("targetlistName"), true);
		sugar.targetlists.recordView.getDetailField("description").assertContains(targetListRecord.get("description"), true);
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.assertContains(dataSource.get(0).get("targetlistName"), true);
		sugar.targetlists.listView.assertContains(targetListRecord.get("description"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
