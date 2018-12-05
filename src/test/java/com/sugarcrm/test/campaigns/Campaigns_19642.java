package com.sugarcrm.test.campaigns;


import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Campaigns_19642 extends SugarTest {
	CampaignRecord myCampaign;
	DataSource targetLists, customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		targetLists = testData.get(testName+"_targetLists");
		
		// Creating 5 Target List records
		sugar.targetlists.api.create(targetLists);
		
		// Creating campaign record
		myCampaign = (CampaignRecord) sugar.campaigns.api.create();
		
		sugar.login();
		
		// In System Settings save view to show 2 entries in list view
		sugar.admin.setSystemSettings(customData.get(0));
	}

	/**
	 * Verify that Target List Pagination function works fine in the popup
	 * when selecting via Target List subpanel in a Campaign.
	 * */
	@Test
	public void Campaigns_19642_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myCampaign.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1072 Library support needed for controls in Campaign Detail view.
		VoodooControl actionDropDown = new VoodooControl("span", "css", ".pagination .sugar_action_button .ab");
		VoodooControl selectOption = new VoodooControl("span", "css", ".pagination .sugar_action_button .subnav.ddopen");
		VoodooControl firstRow = new VoodooControl("td", "css", "table tr.oddListRowS1 td:nth-of-type(3)");
		VoodooControl secondRow = new VoodooControl("td", "css", "table tr.evenListRowS1 td:nth-of-type(3)");
		VoodooControl nextButton = new VoodooControl("button", "id", "popupViewNextButton");
		VoodooControl previousButton = new VoodooControl("button", "id", "popupViewPrevButton");
		VoodooControl firstPageButton = new VoodooControl("button", "id", "popupViewStartButton");
		VoodooControl lastPageButton = new VoodooControl("button", "id", "popupViewEndButton");
		
		// Selecting the action dropdown in target lists subpanel and clicking 'select' option
		actionDropDown.click();
		selectOption.click();
		VoodooUtils.focusWindow(1);
		
		// Verifying that only 2 target lists are displayed on page 1 
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, lines 65 and 66 can be removed
		firstRow.assertEquals(targetLists.get(4).get("targetlistName"), true);
		secondRow.assertEquals(targetLists.get(3).get("targetlistName"), true);
		
		// Verifying that on page 1 the Previous button is disabled
		Boolean isDisabled = previousButton.isDisabled();
		Assert.assertEquals(isDisabled, true);
		
		// Navigating to page 2
		nextButton.click();
		// Verifying that only 2 target lists are displayed on page 2 
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, lines 77 and 78 can be removed
		firstRow.assertEquals(targetLists.get(2).get("targetlistName"), true);
		secondRow.assertEquals(targetLists.get(1).get("targetlistName"), true);
		
		// Navigating to page 3
		nextButton.click();
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, line 85 can be removed
		// Verifying that only 1 target list is displayed on page 3 
		firstRow.assertEquals(targetLists.get(0).get("targetlistName"), true);
		
		// Verifying that on page 3 the Next button is disabled
		isDisabled = nextButton.isDisabled();
		Assert.assertEquals(isDisabled, true);
		
		// Navigating back to page 2 (with previous button)
		previousButton.click();
		// Verifying that only 2 target lists are displayed on page 2 
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, lines 96 and 97 can be removed
		firstRow.assertEquals(targetLists.get(2).get("targetlistName"), true);
		secondRow.assertEquals(targetLists.get(1).get("targetlistName"), true);
		
		// Navigating back to page 1 (with previous button)
		previousButton.click();
		// Verifying that only 2 target lists are displayed on page 1 
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, lines 104 and 105 can be removed
		firstRow.assertEquals(targetLists.get(4).get("targetlistName"), true);
		secondRow.assertEquals(targetLists.get(3).get("targetlistName"), true);
		
		// Verifying that on page 1 the First page button is disabled
		isDisabled = firstPageButton.isDisabled();
		Assert.assertEquals(isDisabled, true);
		
		// Navigating back to page 3 (with last page button)
		lastPageButton.click();
		// Verifying that only 1 target lists is displayed on page 3 
		// TODO: VOOD-1526 Need a method in VoodooControl.java to return count of the control on the page
		// Once the above is fixed, line 116 can be removed
		firstRow.assertEquals(targetLists.get(0).get("targetlistName"), true);
		
		// Verifying that on page 3 the Last page button is disabled
		isDisabled = lastPageButton.isDisabled();
		Assert.assertEquals(isDisabled, true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}