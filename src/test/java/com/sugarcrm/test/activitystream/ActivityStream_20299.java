package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_20299 extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		sugar.login();
		myAcc = (AccountRecord) sugar.accounts.api.create();

		// Unfollow the account record
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleFollow(1);
	}

	/**
	 * Verify that activities stream generated correct when a call is created from Planned Activities.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_20299_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts module and select one not-Following account record.
		myAcc.navToRecord();
		DataSource ds = testData.get(testName);
		FieldSet data = new FieldSet();
		data.put("name", ds.get(0).get("name"));

		// TODO: VOOD-976
		// open my dashboard on record view if it doesn't show up by default
		new VoodooControl("span", "css", "span[data-type='dashboardtitle'] span.fld_name.detail i").click();	
		VoodooControl dashboardCtrl = new VoodooControl("a", "css", "span[data-type='dashboardtitle'] ul.dropdown-menu a");
		dashboardCtrl.waitForVisible();		
		if(dashboardCtrl.getText().equals(ds.get(0).get("myDashboard")))
			dashboardCtrl.click();	

		new VoodooControl("span", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']").waitForVisible();
		new VoodooControl("a", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']/..//a[@data-original-title='Actions']").click();
		new VoodooControl("ul", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']/..//ul[@class='dropdown-menu'][@data-menu='dropdown']").waitForVisible();
		new VoodooControl("a", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']/..//ul[@class='dropdown-menu']//li[2]//a").click();

		// create call from planned activities dashlet
		sugar.calls.createDrawer.setFields(data);
		sugar.calls.createDrawer.save();

		// Look at activities streams in Accounts record view.
		sugar.accounts.recordView.getControl("activityStreamButton").waitForVisible();
		sugar.accounts.recordView.showActivityStream();	

		// Verify the activities stream - Linked meeting_name to account_name in Cl module icon.
		sugar.accounts.recordView.activityStream.assertCommentContains(ds.get(0).get("assert1") + myAcc.getRecordIdentifier() , 1, true);

		// TODO: VOOD-977
		VoodooControl labelCtrl = new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(1) div.label-module");
		labelCtrl.assertEquals(ds.get(0).get("module"), true);

		// Look at activities streams in Accounts list view
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();

		// TODO: VOOD-969
		// shouldn't show the call create activity on accounts list view activity stream
		VoodooControl activityStreamCtrl = new VoodooControl("span", "css", ".activitystream-list.results > li:nth-of-type(1) div .tagged");
		if(activityStreamCtrl.queryVisible())
			sugar.accounts.listView.activityStream.assertCommentContains(ds.get(0).get("assert1"), 1, false);	

		// check call create activity on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();

		// the call create and link user to call are happened at same time, so these two messages' position are kind random in activity stream
		if(activityStreamCtrl.queryContains(ds.get(0).get("assert3"), true)) {
			stream.assertCommentContains(ds.get(0).get("assert3"), 1, true);
		}
		else {
			stream.assertCommentContains(ds.get(0).get("assert3"), 2, true);
		}	
		// TODO: VOOD-977
		labelCtrl.assertEquals(ds.get(0).get("module"), true);
		new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(1) div.label-module").assertEquals(ds.get(0).get("module"), true);

		// TODO: VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}