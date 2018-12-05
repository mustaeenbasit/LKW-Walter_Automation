package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17146 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		customData = testData.get(testName);
		sugar().login();
		// Need RLI with Opp data
		sugar().opportunities.create();
	}

	/**
	 * Verify integer field validation on edit view
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17146_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to "Revenue Line Items" module to display list view.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		VoodooControl qtyDetailCtrl = sugar().revLineItems.recordView.getDetailField("quantity");
		VoodooControl qtyEditCtrl = sugar().revLineItems.recordView.getEditField("quantity");
		for (int i = 0; i < customData.size() - 5; i++) {
			// Click edit button to put entire record in edit view.
			sugar().revLineItems.recordView.edit();
			// Change the value of the "Quantity" field to Integer number/Decimal number
			qtyEditCtrl.set(customData.get(i).get("quantity"));
			sugar().revLineItems.recordView.save();
			// Verify that the QLI is saved without any error
			qtyDetailCtrl.assertContains(customData.get(i).get("quantity"), true);
		}

		// Click edit button to put entire record in edit view.
		sugar().revLineItems.recordView.edit();
		// TODO: VOOD-1292: Need lib support to assert tooltip text inside edit fields for meeting modules in case of error message
		VoodooControl errorFieldCtrl = new VoodooControl("div", "css", ".input-append.error.input");
		VoodooControl tooltipCtrl = new VoodooControl("span", "css", ".error-tooltip.add-on");
		for (int i = customData.size() - 5; i < customData.size(); i++) {
			// Change the value of the "Quantity" field to negative number/alphabetical string/Alphanumeric string
			qtyEditCtrl.set(customData.get(i).get("quantity"));
			sugar().revLineItems.recordView.save();
			// Verify that the Quantity field displays a red outline with an exclamation mark and the error message
			errorFieldCtrl.assertAttribute("class", "error");
			if (i == 3) {
				tooltipCtrl.assertAttribute("data-original-title", customData.get(0).get("errorText"));
			} else {
				tooltipCtrl.assertAttribute("data-original-title", customData.get(1).get("errorText"));
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}