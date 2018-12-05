package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28930 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that no "duplicate" action exists in Knowledge Base
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().knowledgeBase.navToListView();
		FieldSet fs = testData.get(testName).get(0);

		// Asserting the existence of 'Find Duplicates' option in RowAction dropdown
		// TODO: VOOD-568
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		new VoodooControl("ul", "css", "div[data-voodoo-name='recordlist'] tbody tr .dropdown-menu").assertContains(fs.get("findDuplicates"), false);

		// Asserting the existence of 'Find Duplicates' option in List view Action dropdown
		// TODO: VOOD-657
		sugar().knowledgeBase.listView.checkRecord(1);
		sugar().knowledgeBase.listView.openActionDropdown();
		new VoodooControl("ul", "css", ".actionmenu .dropdown-menu").assertContains(fs.get("findDuplicates"), false);
		sugar().knowledgeBase.listView.clickClearSelectionsLink();

		// Asserting the existence of 'Find Duplicates' option in record view Action dropdown
		// TODO: VOOD-738
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		new VoodooControl("ul", "css", ".fld_main_dropdown .dropdown-menu").assertContains(fs.get("findDuplicates"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}