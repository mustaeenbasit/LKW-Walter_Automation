package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Campaigns_19388 extends SugarTest {
	DataSource campaignsData = new DataSource();
	FieldSet customData = new FieldSet();
	FieldSet maxEntries = new FieldSet();
	
	public void setup() throws Exception {
		campaignsData = testData.get(testName+"_campaigns");
		sugar().campaigns.api.create(campaignsData);
		customData = testData.get(testName).get(0);
		sugar().login();
		
		// Set max listview items per page to 2 
		maxEntries.put("maxEntriesPerPage", customData.get("maxEntriesPerPageNew"));
		sugar().admin.setSystemSettings(maxEntries);
		maxEntries.clear();
		
		// TODO VOOD-1722: NavToModule fails with Page not ready
		VoodooUtils.refresh();
	}

	/**
	 * Verify that editing campaign by Edit icon from list view can be canceled.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19388_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns list view.
		sugar().navbar.navToModule(sugar().campaigns.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-766: Need defined control for pagination buttons
		VoodooControl nextPageBtn = new VoodooControl("button", "id", "listViewNextButton_top");
		nextPageBtn.click();
		VoodooUtils.focusDefault();
		
		// Click the "Edit" icon from list view for a campaign
		sugar().campaigns.listView.editRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Edit campaign information, such as "Name", "Status"
		sugar().campaigns.editView.getEditField("name").set(customData.get("nameNew"));
		sugar().campaigns.editView.getEditField("status").set(customData.get("statusNew"));
		VoodooUtils.focusDefault();
		
		// Cancel the editing
		sugar().campaigns.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Selecting the next page button
		nextPageBtn.click();
		VoodooUtils.focusDefault();
		
		// Verifying that the record is still displayed on the second page
		sugar().campaigns.listView.verifyField(1, "name", campaignsData.get(0).get("name"));
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that the campaign is not updated
		sugar().campaigns.detailView.getDetailField("name").assertContains(campaignsData.get(0).get("name"), true);
		sugar().campaigns.detailView.getDetailField("status").assertContains(campaignsData.get(0).get("status"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}