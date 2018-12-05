package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19409 extends SugarTest {
	DataSource sortedData = new DataSource();

	public void setup() throws Exception {
		sortedData = testData.get(testName);
		CampaignRecord myCampaign;

		// Create Campaign Record
		myCampaign = (CampaignRecord) sugar.campaigns.api.create();
		sugar.login();

		// Change campaign type to "Newsletter" (3 target lists are linked to Newsletter campaign)
		myCampaign.navToRecord();
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.editView.getEditField("type").set("Newsletter");
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
	}

	/**
	 * Target List management_Verify that column header sort function in the "Target List" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19409_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to detail view of Newsletter type Campaign 
		sugar.navbar.selectMenuItem(sugar.campaigns , "viewNewsletters");
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		// Sort by target list name in subpanel
		VoodooControl sortByTargetlist = new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(1) span");
		VoodooControl targetList1 = new VoodooControl("td", "css", "#list_subpanel_prospectlists span[sugar='slot1b']");
		VoodooControl targetList2 = new VoodooControl("td", "css", "#list_subpanel_prospectlists span[sugar='slot7b']");
		VoodooControl targetList3 = new VoodooControl("td", "css", "#list_subpanel_prospectlists span[sugar='slot13b']");

		// TODO: VOOD-1534
		// Click to sort in descending order
		sortByTargetlist.click();

		// Verifying down arrow of descending order
		new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(1) [alt='Sorted Descending']")
		.assertExists(true);

		// TODO: VOOD-1072
		// Verifying Target list by name data correctly sorted in descending order
		targetList1.assertEquals(sortedData.get(0).get("descOrderTLName"), true);
		targetList2.assertEquals(sortedData.get(1).get("descOrderTLName"), true);
		targetList3.assertEquals(sortedData.get(2).get("descOrderTLName"), true);

		// TODO: VOOD-1534
		// Click to sort in ascending order
		sortByTargetlist.click();

		// Verifying up arrow of ascending order
		new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(1) [alt='Sorted Ascending']")
		.assertExists(true);

		// TODO: VOOD-1072
		// Verifying Target list by name data correctly sorted in ascending order
		targetList1.assertEquals(sortedData.get(0).get("ascOrderTLName"), true);
		targetList2.assertEquals(sortedData.get(1).get("ascOrderTLName"), true);
		targetList3.assertEquals(sortedData.get(2).get("ascOrderTLName"), true);

		// TODO: VOOD-1072
		// Sort by target list type
		VoodooControl sortByType = new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(3) span");
		VoodooControl listType1 = new VoodooControl("td", "css", ".oddListRowS1 td:nth-child(3)");
		VoodooControl listType2 = new VoodooControl("td", "css", ".evenListRowS1 td:nth-child(3)");
		VoodooControl listType3 = new VoodooControl("td", "css", "tr:nth-child(5).oddListRowS1 td:nth-child(3)");

		// TODO: VOOD-1534
		// Click to sort in descending order
		sortByType.click();

		// Verifying down arrow of descending order
		new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(3) [alt='Sorted Descending']")
		.assertExists(true);

		// TODO: VOOD-1072
		// Verifying listType data correctly sorted in descending order
		listType1.assertEquals(sortedData.get(0).get("descOrderTypeList"), true);
		listType2.assertEquals(sortedData.get(1).get("descOrderTypeList"), true);
		listType3.assertEquals(sortedData.get(2).get("descOrderTypeList"), true);

		// TODO: VOOD-1534
		// Click to sort in ascending order
		sortByType.click();

		// Verifying up arrow of ascending order
		new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(3) [alt='Sorted Ascending']")
		.assertExists(true);

		// TODO: VOOD-1072
		// Verifying listType data correctly sorted in ascending order
		listType1.assertEquals(sortedData.get(0).get("ascOrderTypeList"), true);
		listType2.assertEquals(sortedData.get(1).get("ascOrderTypeList"), true);
		listType3.assertEquals(sortedData.get(2).get("ascOrderTypeList"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}