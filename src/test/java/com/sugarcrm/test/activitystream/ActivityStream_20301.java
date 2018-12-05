package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_20301 extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		myAcc = (AccountRecord) sugar.accounts.api.create();

		sugar.login();
	}

	/**
	 *Verify that activities stream generated correct when a call is created from Planned Activities(record is in following status)
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_20301_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAcc.navToRecord();
		DataSource ds = testData.get(testName);
		FieldSet data = new FieldSet();
		data.put("name", ds.get(0).get("name"));

		// TODO: VOOD-976
		// open my dashboard on record view if it doesn't show up by default
		new VoodooControl("a", "css", "[data-type='dashboardtitle'] span.fld_name.detail a").click();
		VoodooControl dashboardCtrl = new VoodooControl("a", "css", "span[data-type='dashboardtitle'] ul.dropdown-menu a");
		dashboardCtrl.waitForVisible();
		if(dashboardCtrl .getText().equals(ds.get(0).get("myDashboard")))
			dashboardCtrl .click();

		// Create call from planned activities dashlet
		// TODO: VOOD-976 need lib support of RHS on record view
		new VoodooControl("a", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']/..//a[@data-original-title='Actions']").click();
		new VoodooControl("a", "xpath", "//div[@class='dashboard']//h4[.='Planned Activities']/..//ul[@class='dropdown-menu'][@data-menu='dropdown']//li[2]//a").click();

		sugar.calls.createDrawer.setFields(data);
		sugar.calls.createDrawer.save();
		sugar.accounts.recordView.getControl("activityStreamButton").waitForVisible();
		sugar.accounts.recordView.showActivityStream();	
		sugar.accounts.recordView.activityStream.assertCommentContains(ds.get(0).get("assert1") + myAcc.getRecordIdentifier(), 1, true);

		// TODO: VOOD-977 need defined control for the module icon on activity stream
		VoodooControl labelCtrl = new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type(1) div.label-module");
		labelCtrl.assertEquals(ds.get(0).get("module"), true);

		// Should show the call create activity on accounts list view activity stream
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		sugar.accounts.listView.activityStream.assertCommentContains(ds.get(0).get("assert1") + myAcc.getRecordIdentifier(), 1, true);	
		labelCtrl.assertEquals(ds.get(0).get("module"), true);

		// Check call create activity on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();
		stream.assertElementContains(ds.get(0).get("assert1") + myAcc.getRecordIdentifier(), true);
		stream.assertElementContains(ds.get(0).get("assert3"), true);

		// Show Ca as module icon for create meeting and link user to meeting call, show Ac as module icon for link call to account message
		for(int i=1;i<=3;i++) {
			if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type("+i+") div .tagged").queryContains(ds.get(0).get("assert1"),true))
				new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type("+i+") div.label-module").assertEquals(ds.get(0).get("module1"), true);
			else if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type("+i+") div .tagged").queryContains(ds.get(0).get("assert2"),true))
				new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type("+i+") div.label-module").assertEquals(ds.get(0).get("module"), true);
			else if(new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type("+i+") div .tagged").queryContains(ds.get(0).get("assert3"),true))
				new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type("+i+") div.label-module").assertEquals(ds.get(0).get("module"), true);
		}

		// TODO: VOOD-954 Clean up will fail if not on dashboard page after navigation to home
		sugar.navbar.clickModuleDropdown(sugar.home);
		// TODO: VOOD-953 need defined control of My Dashboard menu item under home tab
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}