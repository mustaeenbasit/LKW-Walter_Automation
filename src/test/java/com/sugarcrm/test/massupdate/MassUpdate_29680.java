package com.sugarcrm.test.massupdate;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class MassUpdate_29680 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().productCatalog.api.create();
		sugar().login();
	}

	/**
	 * Verify that ID # is not appearing in place of related record names in drop-down of Mass Update panel
	 * @throws Exception
	 */
	@Test
	public void MassUpdate_29680_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to RLI list view and selecting checkbox for first record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleRecordCheckbox(1);

		// Selecting mass update from action dropdown
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();

		// Filling in value for first row of mass update and click '+' to add new row to mass update panel
		FieldSet revData = testData.get(testName).get(0);
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set(revData.get("massUpdateField"));
		VoodooControl massValue = sugar().revLineItems.massUpdate.getControl("massUpdateValue02");
		massValue.set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.addRow(2);

		// Clicking '-' sign to remove first row from mass update panel
		// TODO: VOOD-1949
		VoodooControl removeButton = new VoodooControl("button", "css", "div[data-voodoo-name='RevenueLineItems'] .filter-body.clearfix:nth-of-type(2) .filter-actions [data-action='remove']");
		removeButton.click();
		sugar().revLineItems.massUpdate.addRow(2);

		// Asserting Place holder text in mass update panel.
		massValue.queryContains(revData.get("placeHolder2"), true);

		// Again add and remove rows from mass update panel
		removeButton.click();
		sugar().revLineItems.massUpdate.addRow(2);

		// Asserting Place holder text in mass update panel.
		massValue.queryContains(revData.get("placeHolder2"), true);
		sugar().revLineItems.massUpdate.getControl("massUpdateValue03").queryContains(revData.get("placeHolder3"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}