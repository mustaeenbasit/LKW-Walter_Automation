package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18824 extends SugarTest {
	ContactRecord myContact;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Api needed for RLI
		sugar.opportunities.api.create();
		sugar.revLineItems.api.create();
		sugar.login();
	}

	/**
	 * Workflow use mathematical operator to modify field value
	 * @throws Exception
	 */
	@Test
	public void WorkFlow_18824_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042
		// Workflow
		new VoodooControl("a", "id", "workflow_management").click();
		VoodooUtils.focusDefault();
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();
		new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Create workflow with target as RLI Module
		new VoodooControl("input", "id", "name").set(customData.get("wf_name"));
		new VoodooControl("select", "css", "[name='base_module']").set("Revenue Line Items");
		new VoodooControl("input", "id", "save_workflow").click();

		// Condition: When the target module changes 
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(2) td:nth-child(1) input[type='radio']").click();
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "save");
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Action: Update fields in the target module
		// Total Discount Amount with advanced search
		new VoodooControl("a", "id", "NewWorkFlowActionShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(1) td:nth-child(1) input[type='radio']").click();
		new VoodooControl("input", "id", "step1_next").click();
		VoodooUtils.focusFrame("selectiframe");
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(12) td:nth-child(1) input[type='checkbox']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "body form table tr:nth-child(3) td table tr:nth-child(13) td a").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("a", "id", "href_set_type_basic").click();
		new VoodooControl("option", "css", "#adv_options td:nth-child(2) select option:nth-of-type(3)").click();
		new VoodooControl("input", "css", "#adv_options td:nth-child(2) input[type='text']").set(customData.get("multiplier"));		
		new VoodooControl("input", "css", "body table tr td table tr:nth-child(5) td input:nth-child(2)").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body form tr:nth-child(5) td input:nth-child(2)").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		
		// RLI record
		// first-case with Integer value
		sugar.revLineItems.navToListView();
		sugar.revLineItems.listView.clickRecord(1);
		sugar.revLineItems.recordView.edit();
		sugar.revLineItems.recordView.getEditField("discountPrice").set(customData.get("discountPrice_int"));
		sugar.revLineItems.recordView.getEditField("relOpportunityName").set(customData.get("relOpportunityName"));
		sugar.revLineItems.recordView.save();
		sugar.alerts.waitForLoadingExpiration();

		// Verifying discount price with integer should be its double value
		int triggeredDiscountPriceWithIntVal = Integer.parseInt(customData.get("discountPrice_int")) * Integer.parseInt(customData.get("multiplier"));
		String resultStr1  = String.format("$%s.00", Integer.toString(triggeredDiscountPriceWithIntVal));
		sugar.revLineItems.recordView.getDetailField("discountPrice").assertEquals(resultStr1, true);

		// second-case with Float value
		sugar.revLineItems.recordView.edit();
		sugar.revLineItems.recordView.getEditField("discountPrice").set(customData.get("discountPrice_float"));
		sugar.revLineItems.recordView.save();
		sugar.alerts.waitForLoadingExpiration();

		// Verifying discount price with float should be its double value 
		float triggeredDiscountPriceWithFloatVal = Float.parseFloat(customData.get("discountPrice_float")) * Float.parseFloat(customData.get("multiplier"));
		String resultStr2  = String.format("$%s", Float.toString(triggeredDiscountPriceWithFloatVal));
		sugar.revLineItems.recordView.getDetailField("discountPrice").assertEquals(resultStr2, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}