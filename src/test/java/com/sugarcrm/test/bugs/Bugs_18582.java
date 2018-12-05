package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18582 extends SugarTest {
	DataSource bugsData = new DataSource();

	public void setup() throws Exception {
		bugsData = testData.get(testName);
		sugar().bugs.api.create(bugsData);

		// Login as a valid user
		sugar().login();

		// Enable Bugs module		
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/**
	 * 18582 Verify all field values are shown correctly on merge bugs page
	 * @throws Exception
	 */
	@Test
	public void Bugs_18582_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Bugs" navigation tab
		sugar().bugs.navToListView();

		// Select two bugs in bug list and open action drop down
		sugar().bugs.listView.toggleSelectAll();
		sugar().bugs.listView.openActionDropdown();

		// Click "Merge" link
		// TODO: VOOD-689
		new VoodooControl("a", "name", "merge_button").click();

		// Verify all field values are shown for the both records 
		// TODO: VOOD-681 and VOOD-721
		VoodooControl firstRecordDiv = new VoodooControl("div", "css", "[data-container='merge-record']:nth-of-type(2) .box");
		VoodooControl secondRecordDiv = new VoodooControl("div", "css", "[data-container='merge-record'] .box");

		// Verify the information for merging is displayed as filled for creating bugs
		for(String fieldValue : bugsData.get(0).values())
			firstRecordDiv.assertElementContains(fieldValue, true);

		// Verify the information for merging is displayed as filled for creating bugs
		// TODO: VOOD-721
		new VoodooControl("input", "css", ".fld_name.edit input").assertAttribute("value", bugsData.get(1).get("name"), true);
		FieldSet dataExceptNameField = bugsData.get(1);
		dataExceptNameField.remove("name");
		for(String fieldValue : dataExceptNameField.values())
			secondRecordDiv.assertElementContains(fieldValue, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}