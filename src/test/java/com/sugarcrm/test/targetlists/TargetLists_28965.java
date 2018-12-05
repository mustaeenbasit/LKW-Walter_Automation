package com.sugarcrm.test.targetlists;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_28965 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Verify tooltip is displayed properly when hover over plus sign in subpanels in Target List record view 
	 *
	 * @throws Exception
	 */
	@Test
	public void TargetLists_28965_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to target list recorview -> Contact Subpanel -> select from reports dropdown
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		ArrayList<StandardSubpanel> subPanelLists = new ArrayList<StandardSubpanel>();
		subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural));
		subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.contacts.moduleNamePlural));
		subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.leads.moduleNamePlural));
		subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.users.moduleNamePlural));
		subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.accounts.moduleNamePlural));
		// subPanelLists.add(sugar.targetlists.recordView.subpanels.get(sugar.campaigns.moduleNamePlural));
		
		for(StandardSubpanel subPanelList : subPanelLists) {
			// Hover over the "+" icon available in the target, contact, lead, user, account and campaign subpanel.
			subPanelList.getControl("addRecord").hover();
			VoodooUtils.waitForReady();
			
			// Tooltip should display "Create" text when hover over the "+" icon available under target, contact, lead, user, account and campaign subpanel.
			new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains("Create", true);
			subPanelList.scrollIntoViewIfNeeded(false);
		}
		
		// TODO: VOOD-VOOD-1561
		// Once this VOOD-1561 is resolved uncomment Line#37 and remove Line#52,#53,#53,#54,#55,#56,#57
		// Hover over the "+" icon available in the campaign subpanel.
		VoodooControl campaignSubpanel = new VoodooControl("a", "css", ".filtered.layout_Campaigns .fld_create_button a");
		campaignSubpanel.scrollIntoViewIfNeeded(false);
		campaignSubpanel.hover();
		VoodooUtils.waitForReady();
		
		// Tooltip should display "Create" text when hover over the "+" icon available under campaign subpanel
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains("Create", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}