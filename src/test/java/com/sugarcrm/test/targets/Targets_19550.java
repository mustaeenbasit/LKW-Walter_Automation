package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Targets_19550  extends SugarTest {
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		sugar().targets.api.create();
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		sugar().login();
	}

	/**
	 *  Target - Convert Target: Verify that a target can be converted to a lead..
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_19550_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-1151
		new VoodooControl("a", "css", ".fld_convert_button.detail a").click();
		sugar().targets.createDrawer.showMore();

		// Select a Campaign in campaign dropdown
		// TODO: VOOD-1418
		new VoodooSelect("a", "css", "div.record span.fld_campaign_name div a").set(myCampaign.getRecordIdentifier());
		sugar().targets.createDrawer.save();

		// Verify user moves to Targets record view after save , converted Target displayed properly,
		sugar().targets.recordView.assertVisible(true);
		String fullName= sugar().targets.getDefaultData().get("firstName")+" "+sugar().targets.getDefaultData().get("lastName");
		sugar().targets.recordView.getDetailField("fullName").assertEquals(fullName, true);

		//Verify that converted lead link displayed properly
		// TODO: VOOD-1298
		VoodooControl convertedTarget = new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] div");
		convertedTarget.assertContains("Converted Lead: "+fullName, true);

		// Verify that user navigated record view of lead on clicking converted lead link and converted Lead displayed properly
		VoodooControl convertedLeadLink = convertedTarget.getChildElement("a", "css","a");
		convertedLeadLink.click();
		sugar().leads.recordView.assertVisible(true);
		sugar().targets.recordView.getDetailField("fullName").assertEquals(fullName, true);

		// Verify the Campaign name is displayed properly on Lead record view
		// TODO: VOOD-1418
		sugar().leads.recordView.showMore();
		new VoodooControl("span", "css", "span[data-voodoo-name='campaign_name']").assertEquals(myCampaign.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
