package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17146 extends SugarTest {
	public void setup() throws Exception {
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar.quotedLineItems);
	}

	/**
	 * Verify integer field validation on edit view
	 *
	 * @throws Exception
	 */
	@Test
	public void ListView_17146_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);

		// Navigate to "Quoted Line Items" module to display list view.
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);
		VoodooControl qtyDetailCtrl = sugar().quotedLineItems.recordView.getDetailField("quantity");
		VoodooControl qtyEditCtrl = sugar().quotedLineItems.recordView.getEditField("quantity");
		for (int i = 0; i < customData.size() - 5; i++) {
			// Click edit button to put entire record in edit view
			sugar().quotedLineItems.recordView.edit();
			// Change the value of the "Quantity" field to Integer number/Decimal number
			qtyEditCtrl.set(customData.get(i).get("quantity"));
			sugar().quotedLineItems.recordView.save();
			// Verify that the QLI is saved without any error
			qtyDetailCtrl.assertContains(customData.get(i).get("quantity"), true);
		}

		// Click edit button to put entire record in edit view.
		sugar().quotedLineItems.recordView.edit();
		// TODO: VOOD-1292 : Need lib support to assert tooltip text inside edit fields for meeting modules in case of error message
		VoodooControl errorFieldCtrl = new VoodooControl("div", "css", ".input-append.error.input");
		VoodooControl tooltipCtrl = new VoodooControl("span", "css", ".error-tooltip.add-on");
		for (int i = customData.size() - 5; i < customData.size(); i++) {
			// Change the value of the "Quantity" field to negative number/alphabetical string/Alphanumeric string
			qtyEditCtrl.set(customData.get(i).get("quantity"));
			sugar().quotedLineItems.recordView.save();
			// Verify that the Quantity field displays a red outline with an exclamation mark and the error message
			errorFieldCtrl.assertAttribute("class", "error");
			if (i == 3) {
				tooltipCtrl.assertAttribute("data-original-title", customData.get(0).get("errorText"));
			} else {
				tooltipCtrl.assertAttribute("data-original-title", customData.get(1).get("errorText"));
			}

			sugar().alerts.getError().closeAlert();
		}

		sugar().quotedLineItems.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}