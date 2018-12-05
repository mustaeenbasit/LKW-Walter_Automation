package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29338 extends SugarTest {

	public void setup() throws Exception {
		// KB record
		sugar().knowledgeBase.api.create();

		// Campaign record
		sugar().campaigns.api.create();

		// Login as an Admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate Admin-> Studio -> KB -> Add Relationship.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_KBContents").click();

		// Relationships
		// TODO: VOOD-1505
		new VoodooControl("td", "id", "relationshipsBtn").click();

		// Add Relationship
		new VoodooControl("input", "css", "input[name=addrelbtn]").click();
		new VoodooControl("select", "css", "#relationship_type_field option:nth-of-type(2)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rhs_mod_field option:nth-of-type(5)").click();
		VoodooUtils.waitForReady();

		// Save & Deploy
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that KB record is saved in the related field through by adding a new relationship
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29338_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaign record, and add related KB field
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		// TODO: VOOD-1851
		new VoodooControl("input", "css", "#LBL_CAMPAIGN_INFORMATION .yui-ac-input").set(kbName);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Save
		sugar().campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify The KB record should appear in the related field.
		VoodooControl kbDetailCtrl =  new VoodooControl("span", "css", "#LBL_CAMPAIGN_INFORMATION td:nth-child(4) a span");
		kbDetailCtrl.assertEquals(kbName, true);

		// Click on KB link
		kbDetailCtrl.click();

		VoodooUtils.focusDefault();
		// TODO: VOOD-1382
		new VoodooControl("div", "css", ".filtered.layout_Campaigns").click();

		// Verify The Campaign record appears in the sub panel with correct info. 
		// Verify name
		FieldSet campaignData = sugar().campaigns.getDefaultData();
		new VoodooControl("a", "css", ".list.fld_name a").assertEquals(campaignData.get("name"), true);
		new VoodooControl("div", "css", ".list.fld_status div").assertEquals(campaignData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}