package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28555 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		ContactRecord myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();

		// Link Opp to an account
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		// Link Contact to Opportunity
		sugar().opportunities.listView.clickRecord(1);
		sugar().alerts.getWarning().closeAlert();
		sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
	}	

	/**
	 * Verify that Status column (not Sales Stage) is displayed in the Opportunities sub-panel in the Account/Contact record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28555_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel oppSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		// Expand Subpanel since it is collapsed by default
		oppSubpanel.expandSubpanel();

		// Verify Sales stage field is not displaying in Opportunities subpanel
		// TODO: VOOD-609
		FieldSet customData = testData.get(testName).get(0);
		VoodooControl subpanelColumnCtrl = new VoodooControl("tr", "css", ".layout_Opportunities table thead tr:nth-child(1)");
		subpanelColumnCtrl.assertContains(customData.get("field1"), false);

		// Verify Status field is displayed in the Opportunities subpanel
		subpanelColumnCtrl.assertContains(customData.get("field2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}