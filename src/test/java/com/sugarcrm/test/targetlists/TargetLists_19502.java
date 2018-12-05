package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class TargetLists_19502 extends SugarTest{
	ArrayList<Record> leadRecords;
	DataSource myLeads;

	public void setup() throws Exception {
		myLeads = testData.get(testName);
		sugar.targetlists.api.create();
		leadRecords = sugar.leads.api.create(myLeads);
		sugar.login();		
	}

	/**
	 * Verify that lead list can be sort by "Referred By" as DESC in "Leads" sub-panel in Target List record view
	 */
	@Test
	public void TargetLists_19502_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		StandardSubpanel leadsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecords(leadRecords);

		// Setting Values in Referred By field in Leads Subpanel
		// TODO: VOOD-598
		for(int i = 1; i <= myLeads.size(); i++) {
			leadsSubpanel.editRecord(i);
			new VoodooControl("input", "css", ".fld_refered_by.edit input").set(myLeads.get(i-1).get("lastName"));
			leadsSubpanel.saveAction(i);
		}

		// Sorting the Referred By field in descending order
		leadsSubpanel.sortBy("headerReferedby", false);
		VoodooUtils.waitForReady();

		// Verifying the Records are Sorted in Descending order by Referred By Field
		// TODO: VOOD-1424
		new VoodooControl("div", "css", ".single:nth-child(1) .fld_refered_by.list").assertEquals(myLeads.get(1).get("lastName"), true);
		new VoodooControl("div", "css", ".single:nth-child(2) .fld_refered_by.list").assertEquals(myLeads.get(0).get("lastName"), true);

		// Sorting Refered by in Ascending order
		leadsSubpanel.sortBy("headerReferedby", true);
		VoodooUtils.waitForReady();

		// Verifying the Records are Sorted in Ascending order by Referred By Field
		// TODO: VOOD-1424
		new VoodooControl("div", "css", ".single:nth-child(1) .fld_refered_by.list").assertEquals(myLeads.get(0).get("lastName"), true);
		new VoodooControl("div", "css", ".single:nth-child(2) .fld_refered_by.list").assertEquals(myLeads.get(1).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
