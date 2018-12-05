package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Leads_17666 extends SugarTest {
	public void setup() throws Exception {	
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * New filter cannot be created if all sub panels (Related All) are displayed
	 * @throws Exception
	 */	
	@Test
	public void Leads_17666_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showDataView();
		
		// TODO: VOOD-598.  Once it is fixed, please update the following steps: 
		//Open Related drop down, select All
		VoodooSelect relatedFilter = new VoodooSelect("a", "css", "div.related-filter a");
		relatedFilter.set("All");
		//Verify the module badge to present these modules
		new VoodooControl("h4", "css", "div.filtered.tabbable.layout_Calls div.subpanel-header h4").assertContains("Calls", true);
		new VoodooControl("div", "css", "div.filtered.tabbable.layout_Meetings div.subpanel-header h4").assertContains("Meetings", true);
		new VoodooControl("div", "css", "div.filtered.tabbable.layout_Tasks div.subpanel-header h4").assertContains("Tasks", true);
		new VoodooControl("div", "css", "div.filtered.tabbable.layout_Notes div.subpanel-header h4").assertContains("Notes", true);
		new VoodooControl("div", "css", "div.filtered.tabbable.layout_CampaignLog div.subpanel-header h4").assertContains("Campaign Log", true);
		new VoodooControl("div", "css", "div.filtered.tabbable.layout_Emails div.subpanel-header h4").assertContains("Emails", true);		
		
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}
		
	public void cleanup() throws Exception {}
}
