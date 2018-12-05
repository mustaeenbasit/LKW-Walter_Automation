package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_20300 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify that activities stream generated correct when a meeting is created from Planned Activities(record is in following status)
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_20300_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// TODO: VOOD-976
		VoodooControl dashboardTitle = new VoodooControl("a", "css", "span.fld_name.detail a");
		if(!dashboardTitle.queryContains("My Dashboard", true)) {
			// TODO:VOOD-1256 
			// sugar.dashboard.chooseDashboard("My Dashboard"); || sugar.dashboard.chooseDashboard(1); 
			VoodooUtils.pause(1000); // pause required as chooseDashboard method does
			dashboardTitle.click();
			new VoodooControl("a", "css", "span.fld_name.detail li a").click();
			VoodooUtils.pause(1000); // pause required as chooseDashboard method does
		}

		// create meeting from planned activities dashlet
		new VoodooControl("h4", "css", "div.dashboard-pane li:nth-child(4) div h4").waitForVisible();
		new VoodooControl("a", "css", "div.dashboard-pane li:nth-child(4) div > div > div > span > a").click();  
		new VoodooControl("a", "css", "div.dashboard-pane li:nth-child(4) div span ul li a").click();

		// use this line to wait for the meeting create page to load
		sugar.alerts.waitForLoadingExpiration();
		FieldSet customData = testData.get(testName).get(0);
		sugar.meetings.createDrawer.getEditField("name").set(testName);
		sugar.meetings.createDrawer.save();

		// verifying meeting create activity on  record view activity stream
		sugar.accounts.recordView.getControl("activityStreamButton").waitForVisible();
		sugar.accounts.recordView.showActivityStream(); 
		sugar.alerts.waitForLoadingExpiration();
		String assert1Str = String.format("%s %s to %s", customData.get("assert1"),testName,sugar.accounts.getDefaultData().get("name"));
		String assert2Str = String.format("%s %s %s",customData.get("assert2"),sugar.accounts.getDefaultData().get("name"),sugar.accounts.moduleNameSingular);
		String assert3Str = String.format("%s %s",customData.get("assert3"),testName);
		sugar.accounts.recordView.activityStream.assertCommentContains(assert1Str, 1, true);

		// TODO: VOOD-977
		VoodooControl moduleLabelCtrl = new VoodooControl("div", "css", ".activitystream-list.results > li div.label-module");
		moduleLabelCtrl.assertEquals(customData.get("module_me"), true);

		// verifying meeting create activity on  on accounts list view activity stream
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		sugar.accounts.listView.activityStream.assertCommentContains(assert1Str, 1, true); 
		moduleLabelCtrl.assertEquals(customData.get("module_me"), true);

		// verifying meeting create activity on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();

		// the meeting create and link are happened at same time, so these messages' position are kind random in activity stream, so the assertCommentContains not fit here
		stream.assertElementContains(assert1Str, true);
		stream.assertElementContains(assert2Str, true);

		// show Me as module icon for create meeting and link user to meeting message, show Ac as module icon for link meeting to account message
		for(int i=1; i<=3; i++) { // 3 is for number of assert to match
			VoodooControl taggedCtrl = new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type("+ i +") div .tagged");
			VoodooControl labelCtrl = new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type("+ i +") div.label-module");
			if(taggedCtrl.queryContains(customData.get("assert"+ i +""),true)) {
				if(i==1)
					labelCtrl.assertEquals(customData.get("module_acc"), true);
				else if(i==2 || i==3)
					labelCtrl.assertEquals(customData.get("module_me"), true);
			}
		}

		// TODO: VOOD-953, VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}