package com.sugarcrm.test.subpanels;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_create extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	@Test
	public void Subpanels_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		FieldSet callsData = sugar().calls.getDefaultData();
		StandardSubpanel callsSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.create(callsData);

		// TODO: VOOD-1424 - Once story resolved below code should use verify method
		callsSubpanel.getDetailField(1, "name").assertEquals(callsData.get("name"), true);
		callsSubpanel.getDetailField(1, "status").assertEquals(callsData.get("status"), true);
		callsSubpanel.getDetailField(1, "date_start_date").assertContains(callsData.get("date_start_date"), true);
		callsSubpanel.getDetailField(1, "date_end_date").assertContains(callsData.get("date_end_date"), true);
		callsSubpanel.getDetailField(1, "assignedTo").assertEquals("Administrator", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}