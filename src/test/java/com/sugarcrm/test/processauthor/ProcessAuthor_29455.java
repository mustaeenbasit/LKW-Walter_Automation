package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29455 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can define the value for "Best" and "Worst" field as conditions or conclusions for a Process Business Rule
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29455_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Business Rules
		sugar().processBusinessRules.navToListView();

		// Create new rule and click on 'Save & Design' button
		sugar().processBusinessRules.listView.create();
		sugar().processBusinessRules.createDrawer.getEditField("name").set(testName);
		VoodooUtils.waitForReady();
		sugar().processBusinessRules.createDrawer.getEditField("targetModule").set(sugar().opportunities.moduleNamePlural);
		VoodooUtils.waitForReady();
		sugar().processBusinessRules.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1936
		FieldSet customFS = testData.get(testName).get(0);
		new VoodooControl("span", "css", ".decision-table thead tr th:nth-child(2) button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#businessruledesigner tbody tr th table tr th select").set(customFS.get("selectBest"));
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#businessruledesigner tbody tr th table tr th:nth-child(2) select").set(customFS.get("selectWorst"));
		VoodooUtils.waitForReady();

		// any condition and enter Best string number any condition.
		VoodooControl selectConditionCtrl1 = new VoodooControl("td", "css", ".decision-table-operator");
		selectConditionCtrl1.click();
		VoodooUtils.waitForReady();
		selectConditionCtrl1.click();
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", ".decision-table-operator select option:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1936
		// Click in the rectangle just under the Conclusions title to expose the select list.
		VoodooControl expressionContainer = new VoodooControl("span", "css", "#businessruledesigner tbody tr:nth-child(2) td table tr td .expression-container");
		expressionContainer.click();
		VoodooUtils.waitForReady();
		new VoodooControl("h4", "css", ".adam.field-panel div:nth-child(4) div h4").click();
		VoodooUtils.waitForReady();
		VoodooControl constantStringCtrl = new VoodooControl("a", "css", ".adam.field-panel div:nth-child(4) div ul li:nth-child(1) a");
		constantStringCtrl.click();
		VoodooUtils.waitForReady();

		// set best input value
		VoodooControl inputValueCtrl = new VoodooControl("input", "css", "[name='value']");
		inputValueCtrl.set(customFS.get("bestValue"));

		// Click on Add button
		VoodooControl addButonCtrl = new VoodooControl("input", "css", "[value='add number']");
		addButonCtrl.click();
		expressionContainer.click();

		// TODO: VOOD-1936
		// Click in the rectangle just under the Conclusions title to expose the select list.
		VoodooControl selectConditionCtrl2 = new VoodooControl("td", "css", ".decision-table-conditions tr td.decision-table-operator:nth-child(3)");
		selectConditionCtrl2.click();
		VoodooUtils.waitForReady();
		selectConditionCtrl2.click();
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", ".decision-table-conditions tr td.decision-table-operator:nth-child(3) select option:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Click in the rectangle just under the Conclusions title to expose the select list.
		VoodooControl expressionContainer2 = new VoodooControl("span", "css", "#businessruledesigner tbody tr:nth-child(2) td table tr td:nth-child(4) .expression-container");
		expressionContainer2.click();
		VoodooUtils.waitForReady();
		new VoodooControl("h4", "css", ".adam.field-panel div:nth-child(4) div h4").click();
		VoodooUtils.waitForReady();
		constantStringCtrl.click();
		VoodooUtils.waitForReady();

		// set worst input value
		inputValueCtrl.set(customFS.get("worstValue"));

		// Click on Add number button
		addButonCtrl.click();
		VoodooUtils.waitForReady();
		expressionContainer2.click(); // Click to close container

		// TODO: VOOD-1936
		// Click in the rectangle just under the Conclusions title to expose the select list.
		new VoodooControl("span", "css", "#businessruledesigner tbody tr:nth-child(2) td:nth-child(3) table tr td .expression-container").click();
		VoodooUtils.waitForReady();
		new VoodooControl("h4", "css", ".adam.field-panel div:nth-child(5) div h4").click();
		VoodooUtils.waitForReady();

		// Select Opportunity
		new VoodooControl("a", "css", ".adam.field-panel div:nth-child(5) div:nth-child(2) ul li a").click();

		// Select Best Currency
		new VoodooControl("li", "css", ".adam.field-panel div:nth-child(5) div:nth-child(2) .adam.multiple-panel-content ul li:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Save design & exit
		new VoodooControl("a", "css", ".fld_project_finish_button").click();
		VoodooUtils.waitForReady();

		// Verify that the Process Business Rules is created successfully
		sugar().processBusinessRules.listView.verifyField(1, "name", testName);

		// Go to Process Business desing view
		sugar().processBusinessRules.listView.openRowActionDropdown(1);
		sugar().processBusinessRules.listView.getControl("design01").click();
		VoodooUtils.waitForReady();

		// Verify, Should be able to enter a number for Best and Worst fields and submit
		expressionContainer.assertContains(customFS.get("bestValue"), true);
		expressionContainer2.assertContains(customFS.get("worstValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}