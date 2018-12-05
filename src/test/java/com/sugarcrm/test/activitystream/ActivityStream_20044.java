package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_20044 extends SugarTest {	
	ContactRecord myCon;
	
	public void setup() throws Exception {
		sugar().login();
		myCon = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Verify that handle correctly if XSS in Activity Stream module
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_20044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.selectMenuItem(sugar().home, "activityStream");
		ActivityStream stream = new ActivityStream();
		stream.createComment(ds.get(0).get("post"));
		sugar().alerts.getSuccess();
		sugar().alerts.closeAllSuccess();
		String message = new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(2) div .tagged").getText();
		VoodooUtils.voodoo.log.info("message " + message + "...");
		stream.assertCommentContains(ds.get(0).get("assert"), 1, true);		
		
		// TODO VOOD-954 the clean up will fail if not change back to dashboard page
		sugar().navbar.clickModuleDropdown(sugar().home);
		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.showActivityStream();
		sugar().accounts.listView.activityStream.createComment(ds.get(0).get("post"));
		sugar().alerts.getSuccess();
		sugar().alerts.closeAllSuccess();
		sugar().accounts.listView.activityStream.assertCommentContains(ds.get(0).get("assert"), 1, true);
		
		myCon.navToRecord();
		sugar().contacts.recordView.showActivityStream();
		sugar().contacts.recordView.activityStream.createComment(ds.get(0).get("post"));
		sugar().alerts.getSuccess();
		sugar().alerts.closeAllSuccess();
		sugar().contacts.recordView.activityStream.assertCommentContains(ds.get(0).get("assert"), 1, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}