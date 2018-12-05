package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23418 extends SugarTest {
	StandardSubpanel bugSubpanel;
	ArrayList<Record> myBugRecords;

	public void setup() throws Exception {
		sugar().cases.api.create();

		// Two bugs record create with different name
		myBugRecords = new ArrayList<Record>();
		myBugRecords.add(sugar().bugs.api.create());
		FieldSet customFS = new FieldSet();
		customFS.put("name", testName);
		myBugRecords.add(sugar().bugs.api.create(customFS));
		sugar().login();

		// Go to Admin -> Display Modules and SubPanels and drag Bugs from Hidden SubPanel to Displayed SubPanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Link cases with bugs in "bugs" sub-panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		bugSubpanel = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugSubpanel.linkExistingRecords(myBugRecords);
	}

	/**
	 * Sort List_Verify that bugs displayed in "Bugs" sub-panel in "Case" detail view can be sorted by column titles.
	 * @throws Exception
	 */
	@Test
	public void Cases_23418_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		boolean flag = false;
		for(Record record : myBugRecords) {
			// Click on cases detailView > Bugs Sub-Panel > header column title
			bugSubpanel.sortBy("headerName", flag);
			VoodooUtils.waitForReady();
			if (flag == false) flag = true;
			else
				flag = false;

			// Verify that bugs are sorted according to the column title in "Bugs" sub-panel.
			bugSubpanel.getDetailField(1, "name").assertEquals(record.getRecordIdentifier(), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
