package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28391 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify no tags field available for PA modules
	 * @throws Exception
	 */
	@Test
	public void Tags_28391_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tags = testData.get(testName).get(0);

		// Navigate to ProcessDefinition list view
		sugar.processDefinitions.navToListView();

		// Create a filter.
		sugar.processDefinitions.listView.openFilterDropdown();
		sugar.processDefinitions.listView.selectFilterCreateNew();

		// Verify tags filter is not available in Process Definition List view
		// TODO: VOOD-1463
		VoodooControl searchBox = new VoodooControl("span", "css", ".detail.fld_filter_row_name");
		searchBox.click();
		VoodooControl dropdownList = new VoodooControl("ul", "css", "#select2-drop ul");
		dropdownList.assertContains(tags.get("tags"), false);

		// Need to select Name else unable to click any element outside the drop down open
		// TODO:VOOD-629
		new VoodooControl("div", "css", "#select2-drop .select2-result-selectable.select2-highlighted div").click();
		sugar.processDefinitions.listView.filterCreate.cancel();

		// Verify tags filter is not available in Process Business Rules List view
		sugar.processBusinessRules.navToListView();
		sugar.processBusinessRules.listView.openFilterDropdown();
		sugar.processBusinessRules.listView.selectFilterCreateNew();
		searchBox.click();
		dropdownList.assertContains(tags.get("tags"), false);
		VoodooControl name = new VoodooControl("div", "css", "#select2-drop .select2-result-selectable.select2-highlighted div");
		name.click();
		sugar.processBusinessRules.listView.filterCreate.cancel();

		// Verify tags filter is not available in Process Email Templates List view
		sugar.processEmailTemplates.navToListView();
		sugar.processEmailTemplates.listView.openFilterDropdown();
		sugar.processEmailTemplates.listView.selectFilterCreateNew();
		searchBox.click();
		dropdownList.assertContains(tags.get("tags"), false);
		name.click();
		sugar.processEmailTemplates.listView.filterCreate.cancel();

		// Verify tags filter is not available in Processes List view
		sugar.processes.navToListView();
		sugar.processes.listView.openFilterDropdown();
		sugar.processes.listView.selectFilterCreateNew();
		searchBox.click();
		dropdownList.assertContains(tags.get("tags"), false);
		name.click();
		sugar.processes.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}