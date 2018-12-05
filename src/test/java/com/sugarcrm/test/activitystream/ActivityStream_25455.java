package com.sugarcrm.test.activitystream;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class ActivityStream_25455 extends SugarTest {
	ArrayList<StandardModule> modules;
	ArrayList<StandardRecord> records;

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.quotedLineItems);
		modules = new ArrayList<StandardModule>();
		modules.add(sugar.revLineItems);
		modules.add(sugar.quotedLineItems);
		records = new ArrayList<StandardRecord>();
		for(StandardModule mod: modules) {
			records.add((StandardRecord) mod.api.create());
		}
	}

	/**
	 * Correct Activity Stream messages when create a meeting that is related to RLI or QLI.
	 *
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_25455_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds=testData.get(testName);
		ActivityStream stream = new ActivityStream();
		int i = 0;
		for(StandardRecord rec: records) {
			sugar.meetings.navToListView();
			sugar.navbar.selectMenuItem(sugar.meetings, "create" + sugar.meetings.moduleNameSingular);
			sugar.meetings.createDrawer.getEditField("name").set(ds.get(i).get("name"));
			sugar.meetings.createDrawer.getEditField("relatedToParentType").set(ds.get(i).get("related"));
			if(i == 0){
				sugar.meetings.createDrawer.getEditField("relatedToParentName").set(sugar.revLineItems.getDefaultData().get("name"));
			}else{
				sugar.meetings.createDrawer.getEditField("relatedToParentName").set(sugar.quotedLineItems.getDefaultData().get("name"));
			}
			sugar.meetings.createDrawer.save();

			// check activity messages
			sugar.navbar.selectMenuItem(sugar.home, "activityStream");

			// the meeting create and link are happened at same time, so these messages' position are kind random in activity stream
			stream.assertElementContains(ds.get(i).get("assert1") +rec.getRecordIdentifier(), true);
			stream.assertElementContains(ds.get(i).get("assert2"), true);

			for(int j=1;j<=3;j++) {
				// TODO: VOOD-977
				VoodooControl labelCtrl = new VoodooControl("div", "css", ".activitystream-list.results li:nth-of-type("+j+") div.label-module");

				// TODO: VOOD-969
				VoodooControl activityStreamCtrl = new VoodooControl("span", "css", ".activitystream-list.results > li:nth-of-type("+j+") div .tagged");

				// if message is about Linked meeting to RLI/QLI then display RL/QL as module icon
				if(activityStreamCtrl.queryContains(ds.get(0).get("assert1"),true))
					labelCtrl.assertEquals(ds.get(0).get("module1"), true);
				// if message is about Created meeting then display Me as module icon
				else if(activityStreamCtrl.queryContains(ds.get(0).get("assert2"),true))
					labelCtrl.assertEquals(ds.get(0).get("module2"), true);
				// if message is about Linked user to meeting then display Me as module icon
				else if(activityStreamCtrl.queryContains(ds.get(0).get("assert3"),true))
					labelCtrl.assertEquals(ds.get(0).get("module2"), true);
			}

			i++;
		}

		// change back to my dashboard
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}