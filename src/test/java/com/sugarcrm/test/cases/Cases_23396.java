package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23396 extends SugarTest {
	BugRecord relBug;
	StandardSubpanel bugsSubpanel;
	FieldSet newData, bugsDefaultData;

	public void setup() throws Exception {
		sugar().cases.api.create();
		relBug = (BugRecord) sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Relate bug relate to a case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		bugsSubpanel = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanel.scrollIntoView();
		bugsSubpanel.linkExistingRecord(relBug);
	}

	/**
	 * Edit Bug Report_Verify that modification of related bug of a case can be canceled
	 * @throws Exception
	 */
	@Test
	public void Cases_23396_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		newData = testData.get(testName).get(0);

		// Click "edit" link for an existing bug in "Bugs" sub-panel
		bugsSubpanel.editRecord(1);

		// Modify values in all the fields
		for (String controlName : newData.keySet()) {
			sugar().cases.getField(controlName).listViewEditControl.scrollIntoViewIfNeeded(false);
			sugar().cases.getField(controlName).listViewEditControl.set(newData.get(controlName));
		}

		// Cancel editing the bug
		bugsSubpanel.cancelAction(1);

		bugsDefaultData = new FieldSet();
		bugsDefaultData.put("name", sugar().bugs.getDefaultData().get("name"));
		bugsDefaultData.put("status", sugar().bugs.getDefaultData().get("status"));
		bugsDefaultData.put("priority", sugar().bugs.getDefaultData().get("priority"));

		// Verify that bug is displayed in "Bugs" sub-panel without any change
		bugsSubpanel.verify(1, bugsDefaultData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
