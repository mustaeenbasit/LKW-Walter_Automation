package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Leads_17564 extends SugarTest {	
	public void setup() throws Exception {	
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * Verify all related sub panels are listed if user selects Related: "All" in Leads Data View
	 * @throws Exception
	 */	
	@Test
	public void Leads_17564_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showDataView();
		
		// TODO: VOOD-598.  Once it is fixed, please update the following steps.
		// SP-1819 - Please note this script may fail because of this bug.
		//Open Related drop down
		new VoodooSelect("a", "css", "div.related-filter a").set("All");
		new VoodooControl("div", "css", ".choice-filter").waitForVisible();
		new VoodooControl("div", "css", ".choice-filter").assertEquals("All Records", true);
		
		//Verify that all subpanels are listed	
		new VoodooSelect("div", "css", ".label.label-module.label-Calls.pull-left").assertVisible(true);
		new VoodooSelect("div", "css", ".label.label-module.label-Meetings.pull-left").assertVisible(true);
		new VoodooSelect("div", "css", ".label.label-module.label-Tasks.pull-left").assertVisible(true);
		new VoodooSelect("div", "css", ".label.label-module.label-Notes.pull-left").assertVisible(true);
		new VoodooSelect("div", "css", ".label.label-module.label-Emails.pull-left").assertVisible(true);
		new VoodooSelect("div", "css", ".label.label-module.label-CampaignLog.pull-left").assertVisible(true);	
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}	
	
	public void cleanup() throws Exception {}
}
