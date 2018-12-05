package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29402 extends SugarTest {
	DataSource KBData = new DataSource();

	public void setup() throws Exception {
		KBData = testData.get(testName);
		sugar().cases.api.create();
		sugar().knowledgeBase.api.create(KBData);

		// Enable knowledge Base Module and sub panel
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);

		// Logout from Admin user and Login as a QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that KB record is able to add at the sub panel in Cases record
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29402_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open one Case record
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel KBSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		KBSubpanel.scrollIntoViewIfNeeded(false);

		// In KB sub panel, click on action drop down and select "Link Existing Record"
		// TODO: RS-1255
		// KBSubpanel.linkExistingRecord(myKBRecord);
		KBSubpanel.getControl("expandSubpanelActions").click();
		new VoodooControl("a", "css", ".filtered.layout_KBContents .fld_select_button.panel-top-for-cases a").click();
		sugar().knowledgeBase.searchSelect.getControl("cancel").waitForVisible();

		// Choose several KB records
		sugar().knowledgeBase.searchSelect.selectRecord(1);
		sugar().knowledgeBase.searchSelect.selectRecord(2);
		sugar().knowledgeBase.searchSelect.selectRecord(3);

		// Click on Add button
		sugar().knowledgeBase.searchSelect.link();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that the KB records have been added in the sub panel
		for (int i = 0; i < KBData.size(); i++) {
			KBSubpanel.assertContains(KBData.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}