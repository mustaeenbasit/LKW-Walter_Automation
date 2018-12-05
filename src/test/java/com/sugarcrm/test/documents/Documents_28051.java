package com.sugarcrm.test.documents;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class Documents_28051 extends SugarTest {
	VoodooControl dragCtrl, dropCtrl;

	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * Verify that Reordering Subpanels in Backward Compatible Modules saves changes to Layout.
	 * @throws Exception
	 */
	@Test
	public void Documents_28051_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Document detail view
		sugar().documents.navToListView();
		sugar().documents.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify "Accounts" subpanel lies on second position in subpanel list.
		// TODO: VOOD-1870, VOOD-972
		VoodooControl secondSubpanelCtrl = new VoodooControl("li", "css", "#subpanel_list li:nth-child(2) div");
		secondSubpanelCtrl.assertContains(sugar().accounts.moduleNamePlural, true);

		// Verify "Contacts" subpanel lies on third position in subpanel list
		VoodooControl thirdSubpanelCtrl = new VoodooControl("li", "css", "#subpanel_list li:nth-child(3) div");
		thirdSubpanelCtrl.assertContains(sugar().contacts.moduleNamePlural, true);

		// Drag & Drop contacts subpanel above accounts subpanel so that contacts subpanel becomes second in the list.
		// TODO: VOOD-1843
		dragCtrl = new VoodooControl("div", "id", "subpanel_title_contacts");
		dropCtrl = new VoodooControl("div", "id", "subpanel_title_accounts");
		dragCtrl.dragNDrop(dropCtrl);
		VoodooUtils.waitForReady();

		// Refresh the page as asked in TC
		VoodooUtils.refresh();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// Verify "Accounts" subpanel lies on third position in subpanel list.
		thirdSubpanelCtrl.assertContains(sugar().accounts.moduleNamePlural, true);

		// Verify "Contacts" subpanel should remain above "Accounts" subpanel(i.e. on second position).
		secondSubpanelCtrl.assertContains(sugar().contacts.moduleNamePlural, true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}