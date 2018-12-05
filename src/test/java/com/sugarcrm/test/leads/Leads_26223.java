package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_26223 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {	
		sugar().leads.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Stickyness in Preview of Leads records
	 * @throws Exception
	 */
	@Test
	public void Leads_26223_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// TODO: VOOD-679
		// Verify that Teams field is in the record preview
		VoodooControl teamCtrl = new VoodooControl("div", "css", "div.control-group.teamset");
		teamCtrl.assertContains(customData.get("team"), true);	

		// Verify that Show Less link in the preview
		sugar().previewPane.getControl("showLess").assertVisible(true);

		// Navigate away to Accounts module
		sugar().accounts.navToListView();
		sugar().leads.navToListView();
		sugar().leads.listView.previewRecord(1);

		// Again, Verify that Teams field is in the record preview
		teamCtrl.assertContains(customData.get("team"), true);	

		// Again, Verify that Show Less link in the preview
		sugar().previewPane.getControl("showLess").assertVisible(true);

		// Close preview
		sugar().previewPane.closePreview();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}