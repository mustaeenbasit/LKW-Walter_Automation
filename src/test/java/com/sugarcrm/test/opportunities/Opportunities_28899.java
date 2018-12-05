package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28899 extends SugarTest {
	public void setup() throws Exception {
		// Login as Admin
		sugar().login();
	}

	/**
	 * Verify the Color coding for likely field in Opportunity module
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28899_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity Module -> Click on Create button -> Without entering any value in any field, Click on Save button
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.getControl("saveButton").click();

		// Close the error message
		sugar().alerts.getError().closeAlert();

		FieldSet errorMessageData = testData.get(testName).get(0);

		// Verify that the Likely label should be displayed in red color at Likely field like others
		// TODO: VOOD-1445
		VoodooControl likelyFieldCtrl = new VoodooControl("span", "css", ".record .fld_amount.edit");
		likelyFieldCtrl.assertAttribute("class", errorMessageData.get("error"), true);

		// Check Color coding at Likely field
		// TODO: VOOD-1445
		new VoodooControl("div", "css", "div[data-name='amount'] .record-label").assertCssAttribute("color", errorMessageData.get("color"), true);
		new VoodooControl("span", "css", likelyFieldCtrl.getHookString() + " .fld_currency_id").assertCssAttribute("color", errorMessageData.get("color"), true);
		new VoodooControl("span", "css", likelyFieldCtrl.getHookString() + " > .select2-addon > .input-append .error-tooltip").assertCssAttribute("color", errorMessageData.get("color"), true);

		// Cancel the Opportunity create drawer
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}