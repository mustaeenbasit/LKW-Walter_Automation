package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tags_29892 extends SugarTest {
	String oppName = "";

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().opportunities.api.create(fs);
		sugar().revLineItems.api.create();
		sugar().login();

		// Adding related opportunity to RLI record 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		oppName = sugar().opportunities.getDefaultData().get("name");
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(oppName);
		sugar().revLineItems.recordView.save();
	}

	/**
	 * Verify that User should able to change the Opportunity Name in RLI subpanel for Tags Module.
	 * @throws Exception
	 */
	@Test
	public void Tags_29892_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to RLI record view and add a tag to it.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		FieldSet tagsData = testData.get(testName).get(0);
		// TODO: VOOD-1772
		new VoodooControl("input", "css", ".fld_tag.edit .select2-input").set(tagsData.get("tagName") + '\uE007');
		VoodooUtils.waitForReady();
		sugar().revLineItems.recordView.save();

		// Navigating to Tags record view 
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);

		// Expanding RLI subpanel and updating related opportunity record
		StandardSubpanel rliSubpanel = (StandardSubpanel) sugar().tags.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		VoodooControl relOppCtrl = rliSubpanel.getDetailField(1, "relOpportunityName");
		relOppCtrl.assertEquals(oppName, true);
		rliSubpanel.editRecord(1);
		rliSubpanel.getEditField(1, "relOpportunityName" ).set(testName);
		rliSubpanel.saveAction(1);

		// Asserting the related opportunity value is updated for RLI record
		relOppCtrl.assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}