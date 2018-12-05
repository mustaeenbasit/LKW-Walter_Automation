package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29439 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Currency drop down is not showing blank while creating Process Rules Builder
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29439_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Business Rules
		sugar().processBusinessRules.navToListView();

		// Create new rule and click on 'Save & Design' button
		sugar().processBusinessRules.listView.create();
		sugar().processBusinessRules.createDrawer.getEditField("name").set(testName);
		VoodooUtils.waitForReady();
		sugar().processBusinessRules.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();

		// Click in the rectangle just under the Conditions title and Select an item from the list
		// TODO: VOOD-1539, VOOD-1936
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("select", "css", "#businessruledesigner select").set(customData.get("fieldName"));

		// Enter any condition.
		new VoodooControl("td", "css", ".decision-table-operator").click();

		// Click in the rectangle just under the Conclusions title to expose the select list.
		new VoodooControl("span", "css", ".decision-table-conclusions .expression-container").click();
		new VoodooControl("h4", "css", ".adam.field-panel div:nth-child(4) div h4").click();

		// Select Currency
		new VoodooControl("a", "css", ".adam.field-panel div:nth-child(4) div ul li:nth-child(5) a").click();

		// Currency dropdown should show the default currency.
		VoodooControl currenyDropdownCtrl = new VoodooSelect("select", "css", "[name='currency']");
		currenyDropdownCtrl.assertEquals(customData.get("defaultCurrency"), true);

		currenyDropdownCtrl.click();

		// Verify available currency list in drop down
		new VoodooControl("option", "css", "div.adam-formpaneldropdown span span select option").assertEquals(customData.get("defaultCurrency"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}