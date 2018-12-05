package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23643 extends SugarTest {

	public void setup() throws Exception {
		sugar().campaigns.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Create Contact_Verify that three different information for campaign records is displayed under the
	 * three corresponding columns on the campaign list pop-up window from contact edit view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23643_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Edit view of Contacts module
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		VoodooUtils.waitForReady();

		// Click "Select" button next "Campaign" field
		// TODO: VOOD-1418
		new VoodooControl("a", "css", ".fld_campaign_name.edit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".select2-result").click();

		VoodooUtils.waitForReady();
		
		// Verify that three different information such as "Campaign", "Status" and "Type" for campaign records is displayed in Search and Select Drawer
		// TODO: VOOD-1671, VOOD-1517
		FieldSet campaignData = sugar().campaigns.getDefaultData();
		new VoodooControl("div", "css", ".layout_Campaigns .list.fld_name div").assertContains(campaignData.get("name"), true);
		new VoodooControl("div", "css", ".layout_Campaigns .list.fld_status div").assertContains(campaignData.get("status"), true);
		new VoodooControl("div", "css", ".layout_Campaigns .list.fld_campaign_type div").assertContains(campaignData.get("type"), true);
		sugar().campaigns.searchSelect.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}