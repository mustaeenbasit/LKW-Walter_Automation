package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19511 extends SugarTest {
	DataSource leadsDS = new DataSource();
	StandardSubpanel leadsSubpanel;
	
	public void setup() throws Exception {
		// Initialize Test Data
		leadsDS = testData.get(testName);
		ArrayList<Record> leadRecords = sugar().leads.api.create(leadsDS);
		sugar().targetlists.api.create();
		sugar().login();	
		
		// Nav to target list record view and add leads records to leads subpanel
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);
		leadsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecords(leadRecords);
	}
	/**
	 * Target List - Leads management_Verify that "Edit" function in "Leads" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19511_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Expand Leads subpanel
		leadsSubpanel.expandSubpanel();
		
		// In "Leads" sub-panel edit lead record and save
		// TODO: VOOD-1424: Make StandardSubpanel.verify() verify specified value is in correct column.
		leadsSubpanel.editRecord(1);
		leadsSubpanel.getEditField(1, "firstName").set(leadsDS.get(0).get("firstName"));
		leadsSubpanel.getEditField(1, "lastName").set(leadsDS.get(0).get("lastName"));
		leadsSubpanel.saveAction(1);
		
		// Verify modified lead record is displayed in the "Leads" sub-panel.
		leadsSubpanel.getDetailField(1, "fullName").assertEquals(leadsDS.get(0).get("fullName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}