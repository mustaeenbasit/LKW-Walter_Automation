package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_18059 extends SugarTest {
	TargetListRecord myTL;
	LeadRecord  myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
		myTL = (TargetListRecord) sugar().targetlists.create();
	}

	/**
	 * Test Case 18059: Be able to add Leads records to targetlist from lisview
	 * @throws Exception
	 */
	@Test
	public void Leads_18059_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads module, select a record in listview, click on action drop down to select "Add to Target List"
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-528
		// search TL and update
		new VoodooControl("a", "css", ".fld_addtolist_button a").click();
		new VoodooSelect("span", "css", ".fld_prospect_lists_name").set(myTL.getRecordIdentifier());
		new VoodooControl("a", "css" , ".fld_update_button a").click();
		sugar().alerts.getSuccess().closeAlert();

		// Verify if the lead appears into subpanel
		myTL.navToRecord();
		StandardSubpanel leadsSubpanel = sugar().targetlists.recordView.subpanels.get("Leads");
		leadsSubpanel.expandSubpanel();
		leadsSubpanel.getDetailField(1, "fullName").assertEquals(myLead.get("fullName"), true);

		// TODO: VOOD-1424 - Once resolved, below line will work and delete above matcher code line
		// FieldSet leadFullName = new FieldSet();
		// leadFullName.put("fullName", myLead.get("fullName"));
		// leadsSubpanel.verify(1,leadFullName,true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}