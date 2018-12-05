package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;

public class Campaigns_19627 extends SugarTest {
	CampaignRecord myCampaign;
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * UI bug_Verify that 'Launch Wizard' button is displayed on campaign detail 
	 * page, ROI page and status page.
	 * */
	@Test
	public void Campaigns_19627_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-1072: Library support needed for controls in Campaign Detail view.
		// Button controls
		VoodooControl launchWizardBtn = new VoodooControl("input", "css", "table tr td:nth-of-type(4).buttons #launch_wizard_button");
		VoodooControl launchWizardBtnOnROI = new VoodooControl("input", "css", "table tr td:nth-of-type(2) #launch_wizard_button");
		VoodooControl launchWizardBtnOnStatus = new VoodooControl("input", "css", "table tr td:nth-of-type(3) #launch_wizard_button");
		VoodooControl viewROIBtn = new VoodooControl("input", "id", "viewRoiButtonId");
		VoodooControl viewStatusBtn = new VoodooControl("input", "id", "view_status_button");
		VoodooControl topRightContainer = new VoodooControl("td", "css", "table tr td:nth-of-type(4).buttons");
		VoodooControl topRightContainerOnROI = new VoodooControl("td", "css", "table tr td:nth-of-type(2)");
		VoodooControl topRightContainerOnStatus = new VoodooControl("td", "css", "table tr td:nth-of-type(3)");
		
		// Create a Campaign
		myCampaign = (CampaignRecord) sugar.campaigns.create();
		myCampaign.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verifying that the Launch Wizard Button is visible on the top right corner
		topRightContainer.assertAttribute("align", "right", true);
		launchWizardBtn.assertVisible(true);
		
		// Navigate to ROI page
		viewROIBtn.click();
		// Verifying that the Launch Wizard Button is visible on the top right corner
		topRightContainerOnROI.assertAttribute("align", "right", true);
		launchWizardBtnOnROI.assertVisible(true);
		
		// Navigate to Status page
		viewStatusBtn.click();
		// Verifying that the Launch Wizard Button is visible on the top right corner
		topRightContainerOnStatus.assertAttribute("align", "right", true);
		launchWizardBtnOnStatus.assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
