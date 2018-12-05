package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_20225 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify group Subpanels In Record Views 
	 * @throws Exception
	 */
	@Test
	public void Accounts_20225_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Define all subpanel of the Accounts module under StandardSubpanel and add them into ArrayList
		ArrayList <StandardSubpanel> subpanels = new ArrayList<StandardSubpanel>();
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural));
		subpanels.add(sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural));

		// Verify that expand and collapse feature is working on each subpanel(except Campaign Log)
		for (int i = 0; i < subpanels.size(); i++) {
			StandardSubpanel defaultSubpanelRecord = subpanels.get(i);
			defaultSubpanelRecord.expandSubpanel();
			Assert.assertTrue("The subpanel is not empty", defaultSubpanelRecord.isEmpty());
			defaultSubpanelRecord.collapseSubpanel();
		}

		// Verifying Campaign Log Toggling
		// TODO: VOOD-1344: Need library support for 'Campaign Log' subpanel in Record view of any module
		VoodooControl campaignLogSubpanel = new VoodooControl("div", "css", "div[data-voodoo-name='CampaignLog']");
		campaignLogSubpanel.click();
		new VoodooControl("div", "css", "div[data-voodoo-name='CampaignLog'] li").assertAttribute("class", "empty", true);
		campaignLogSubpanel.click(); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}