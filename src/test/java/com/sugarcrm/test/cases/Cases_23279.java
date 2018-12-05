package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Cases_23279 extends SugarTest {
	FieldSet myBug;

	public void setup() throws Exception {
		myBug = testData.get(testName).get(0);
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().cases.api.create();
	}

	/**
	 * Report Bug_Verify that bug can be reported for case in "Bugs" sub-panel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23279_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.showDataView();

		StandardSubpanel bugsSubpanel = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.scrollIntoView();
		bugsSubpanel.addRecord();
		sugar().bugs.createDrawer.setFields(myBug);
		sugar().bugs.createDrawer.save();

		bugsSubpanel.scrollIntoView();
		// Verify the Bug record is created and displayed in sub panel
		bugsSubpanel.verify(1, myBug, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
