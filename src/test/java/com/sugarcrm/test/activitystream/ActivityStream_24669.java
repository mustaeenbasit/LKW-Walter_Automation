package com.sugarcrm.test.activitystream;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_24669 extends SugarTest {
	ArrayList<Record> myAccs;
	DataSource ds;
	
	public void setup() throws Exception {		
		ds = testData.get(testName);
		myAccs = sugar.accounts.api.create(ds);
		sugar.login(sugar.users.getQAUser());
		sugar.accounts.navToListView();	
	}

	/**
	 * Mass Update won't generate Activity Streams.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_24669_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet data = new FieldSet();
		data.put("Assigned to", sugar.users.getQAUser().get("lastName"));
		
		sugar.accounts.listView.toggleSelectAll();
		sugar.accounts.massUpdate.performMassUpdate(data);
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().assertVisible(true);
		
		// verify assigned to field is updated
		for(int i=1;i<=ds.size();i++) {
			sugar.accounts.listView.verifyField(i, "relAssignedTo", sugar.users.getQAUser().get("lastName"));
		}
		
		// verify record is not changed to following
		sugar.accounts.listView.openRowActionDropdown(1);
		sugar.accounts.listView.getControl("follow01").assertElementContains(ds.get(0).get("description"), true);
		
		// verify no activity message on accounts list activity stream
		sugar.accounts.listView.showActivityStream();
		VoodooUtils.waitForAlertExpiration();
		// TODO VOOD-969
		for(int i=1;i<=ds.size();i++) {
			if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type("+ i +") div .tagged").queryExists())
				sugar.accounts.listView.activityStream.assertCommentContains(ds.get(1).get("description"), 1, false);	
		}
		sugar.accounts.listView.showListView();
		
		// verify no activity message on accounts record view activity stream
		for(Record acc: myAccs) {
			acc.navToRecord();
			sugar.alerts.waitForLoadingExpiration();
			sugar.accounts.recordView.showActivityStream();
			if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(1) div .tagged").queryExists())
				sugar.accounts.recordView.activityStream.assertCommentContains(ds.get(1).get("description"), 1, false);	
			}
				
		// verify no activity message on home activity stream		
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		sugar.alerts.waitForLoadingExpiration();
		ActivityStream stream = new ActivityStream();
		
		// TODO VOOD-969
		for(int i=1;i<=ds.size();i++) {
			if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(" +i+ ") div .tagged").queryExists())
				stream.assertCommentContains(ds.get(1).get("description"), i, false);	
		}

		// TODO VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);
		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}