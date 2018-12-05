package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21925 extends SugarTest {

	public void setup() throws Exception {
		// Create two lead records and Login as a valid user
		sugar().leads.api.create(testData.get(testName));
		sugar().login();
	}

	/**
	 * Mass Update Leads_Verify that confirmation warning message appears on attempt to Mass Update with empty fields
	 * @throws Exception
	 */
	@Test
	public void Leads_21925_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the Leads list view and select several lead records.
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();

		// Choose Mass Update action.
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.massUpdate();		

		// Select "Lead Source" from available fields in Drop down list and do not select any option from drop down
		FieldSet massUpdateData = testData.get(testName + "_customData").get(0);
		sugar().leads.massUpdate.getControl(String.format("massUpdateField%02d", 2)).set(massUpdateData.get("filterLead"));
		// TODO: VOOD-660 Mass Update should identify fields by VoodooGrimoire internal names.
		new VoodooControl("div", "css", ".massupdate.row-fluid").click();
		new VoodooControl("div", "css", "div[id='select2-drop'] ul li").click();
		
		// Select "Tags" from available fields in Drop down list and leave text box blank
		sugar().leads.massUpdate.addRow(2);
		sugar().leads.massUpdate.getControl(String.format("massUpdateField%02d", 3)).set(massUpdateData.get("filterTag"));
		// TODO: VOOD-1160 Need lib support for adding multiple teams during mass update
		new VoodooControl("input", "css", "input[name='append_tag']").click();

		// Click on Update button.
		sugar().leads.massUpdate.update();

		// A pop-up warning message is appearing, Click Cancel.
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().cancelAlert();
		sugar().alerts.getWarning().assertVisible(false);

		// Selected record in list view is not updated.
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("leadSource").assertEquals(massUpdateData.get("leadSource"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}