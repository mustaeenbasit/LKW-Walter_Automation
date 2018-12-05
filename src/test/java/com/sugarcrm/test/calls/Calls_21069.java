package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_21069 extends SugarTest {
	DataSource ds, ds1;
	
	public void setup() throws Exception {
		ds = testData.get(testName); // call data
		ds1 = testData.get(testName+"_1"); // contact data
		sugar.contacts.api.create(ds1);
		sugar.login();
		
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar.contacts.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		
		for (int i=0; i< ds.size(); i++) {
			contactSubpanel.addRecord();
			sugar.calls.createDrawer.setFields(ds.get(i));
			sugar.calls.createDrawer.save();
			sugar.contacts.recordView.gotoNextRecord();
		}
	}

	/**
	 * Verify that call detail view can be displayed when clicking a call record's name link in the call list view sorted by Contact column
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_21069_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.calls.navToListView();
		
		sugar.calls.listView.clickRecord(1);
		for (int i=0; i< ds1.size(); i++) {
			// Verify that contact record appear in guest list 
			// TODO: VOOD-1222 - Need library support to verify fields in ListView and RecordView of Calls/Meetings module
			new VoodooControl("span", "css", ".detail.fld_invitees").assertContains(ds1.get(i).get("lastName"), true);
			sugar.calls.recordView.gotoNextRecord();
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}