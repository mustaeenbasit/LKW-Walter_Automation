package com.sugarcrm.test.dashboards;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Dashboards_26652 extends SugarTest {
	LeadRecord lead;
	
	public void setup() throws Exception {
		sugar.login();

		lead = (LeadRecord)sugar.leads.api.create();
		lead.navToRecord();
	}

	/**
	 * 26652 Verify that Help dashboard can open (expand) and close all help dahlets
	 * @throws Exception
	 */
	@Ignore("Blocking due to VOOD-591 and 592")
	@Test
	public void Dashboards_26652_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-963
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "span.fld_name.detail a");
		
		// Select Help Dashboard unless it is already selected
		if(!dashboardTitle.getText().contains("Help Dashboard"))
			// Failing due to VOOD-591 and VOOD-592 - chooseDashboard() method in Dashboard.java
			// has different CSS for record view causing it to fail
			sugar.leads.dashboard.chooseDashboard("Help Dashboard");

		
		sugar.leads.dashboard.openActionMenu();
		
		// Confirm Edit button is not in Help Dashboard actions list
		new VoodooControl("a", "css", "ul.dropdown-menu.fld_edit_button a").assertVisible(false);
		
		// Hit 'Close All' button on Help Dashboard in Leads detailview
		new VoodooControl("a", "css", "span.fld_collapse_button.detail a").click();
		VoodooUtils.pause(1000);
		
		// Assert Help dashlets closed
		new VoodooControl("a", "css", "div.help-more a").assertVisible(false);
		new VoodooControl("a", "css", "div.resource-info a").assertVisible(false);
		
		// Hit 'Open All' button on Help Dashboard in Leads detailview
		sugar.leads.dashboard.openActionMenu();
		new VoodooControl("a", "css", "span.fld_expand_button.detail a").click();
		
		// Assert Help dashlets open
		new VoodooControl("a", "css", "div.help-more a").assertVisible(true);
		new VoodooControl("a", "css", "div.resource-info a").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}	
}