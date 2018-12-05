package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class KnowledgeBase_30519 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that "Language" field is unavailable in mass update in KB
	 *
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30519_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();
		sugar().accounts.listView.checkRecord(1);
		// Select mass update action
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").click();

		// TODO: VOOD-1463: Need a lib support to assert values in dropdown field's on BWC/Sidecar modules.
		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("ul", "css", "#select2-drop .select2-results").assertContains(fs.get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}