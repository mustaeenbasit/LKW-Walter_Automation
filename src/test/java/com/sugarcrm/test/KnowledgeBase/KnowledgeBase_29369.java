package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29369 extends SugarTest {
	DataSource KBData = new DataSource();
	UserRecord chris;
	
	public void setup() throws Exception {
		KBData = testData.get(testName);
		sugar().knowledgeBase.api.create(KBData);
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
				
		chris = (UserRecord) sugar().users.create();
		sugar().logout();
		
		// Login as qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that all KB can be access/edit by every user in 7.7
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29369_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to knowledgeBase listview
		sugar().knowledgeBase.navToListView();
		
		// Verify count of rows seen by qauser
		Assert.assertTrue("Row count is not equal to 5", sugar().knowledgeBase.listView.countRows() == 5);
		
		// Open each record knowledgeBase of knowledgeBase, edit and save
		sugar().knowledgeBase.listView.clickRecord(1);
		for (int i = 0; i < KBData.size(); i++) {
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.getEditField("name").set(testName+"_"+i);
			sugar().knowledgeBase.recordView.save();
			sugar().knowledgeBase.recordView.gotoNextRecord();
			sugar().alerts.waitForLoadingExpiration();
		}
		
		// Go to knowledgeBase listview
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		
		// Verify that user is able to edit any KB
		for (int i = 0; i < KBData.size(); i++) {
			sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName+"_"+(4-i), true);
			sugar().knowledgeBase.recordView.gotoNextRecord();
			sugar().alerts.waitForLoadingExpiration();
		}
		
		// Logout as qauser and login as Chris
		sugar().logout();
		sugar().login(chris);
		
		// Go to knowledgeBase listview
		sugar().knowledgeBase.navToListView();
		
		// Verify that count of rows seen by Chris is also 5 (same count seen by qauser)
		Assert.assertTrue("Row count is not equal to 5", sugar().knowledgeBase.listView.countRows() == 5);
		sugar().knowledgeBase.listView.clickRecord(1);
		
		// Verify that user (Chris) can also see updates from other party (qauser)
		for (int i = 0; i < KBData.size(); i++) {
			sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName+"_"+(4-i), true);
			sugar().knowledgeBase.recordView.gotoNextRecord();
			sugar().alerts.waitForLoadingExpiration();
		}
		
		// Go to knowledgeBase listview
		sugar().knowledgeBase.navToListView();
		
		// Open each one and edit and save
		sugar().knowledgeBase.listView.clickRecord(1);
		for (int i = 0; i < KBData.size(); i++) {
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.getEditField("name").set(testName+"_"+i);
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getEditField("date_publish").set(sugar().knowledgeBase.getDefaultData().get("date_publish"));
			sugar().knowledgeBase.recordView.getEditField("date_expiration").set(sugar().knowledgeBase.getDefaultData().get("date_expiration"));
			sugar().knowledgeBase.recordView.save();
			sugar().knowledgeBase.recordView.gotoNextRecord();
			sugar().alerts.waitForLoadingExpiration();
		}
		
		// Go to knowledgeBase listview
		sugar().knowledgeBase.navToListView();
		
		// Verify that user is able to edit any KB
		sugar().knowledgeBase.listView.clickRecord(1);
		for (int i = 0; i < KBData.size(); i++) {
			sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName+"_"+(4-i), true);
			sugar().knowledgeBase.recordView.gotoNextRecord();
			sugar().alerts.waitForLoadingExpiration();
		}
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}