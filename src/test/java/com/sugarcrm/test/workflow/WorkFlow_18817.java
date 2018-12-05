package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18817 extends SugarTest {
	ContactRecord myContact;
	FieldSet customData;
	StandardSubpanel taskSubpanel;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar.contacts.api.create();
		customData = testData.get(testName).get(0);
		sugar.login();
		// Contact is added with task associated
		myContact.navToRecord();
		taskSubpanel = sugar.contacts.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		taskSubpanel.addRecord();
		sugar.tasks.createDrawer.getEditField("subject").set(customData.get("task_name"));
		sugar.tasks.createDrawer.save();
		sugar.alerts.getSuccess().waitForVisible();
		sugar.alerts.getSuccess().closeAlert();
	}

	/**
	 * Workflow_WhenRecordSaved_Action_"Update fields in a related module"
	 * @throws Exception
	 */
	@Test
	public void WorkFlow_18817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		if(sugar.alerts.getWarning().queryVisible()){
			sugar.alerts.getWarning().confirmAlert();
		}
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042
		// Workflow
		new VoodooControl("a", "id", "workflow_management").click();
		VoodooUtils.focusDefault();
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();
		new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Create workflow with target as Contacts Module
		new VoodooControl("input", "id", "name").set(customData.get("wf_name"));
		new VoodooControl("option", "css", "select[name=base_module] option:nth-of-type(6)").click();
		new VoodooControl("input", "id", "save_workflow").click();

		// Condition : When a field in the target module contains a specified value 
		// First Name as Sugar
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(4) td:nth-child(1) input[type='radio']").click();
		new VoodooControl("input", "id", "next").click();
		new VoodooControl("a", "css", "#lang_trigger td a").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "select#lhs_field option:nth-of-type(19)").click();
		new VoodooControl("input", "id", "trigger__field_value").set(customData.get("first_name"));
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "save");
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(1);
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Action: Update fields in related tasks module
		// Modify status field with value "Completed"
		new VoodooControl("a", "id", "NewWorkFlowActionShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(2) td:nth-child(1) input[type='radio']").click();
		new VoodooControl("a", "css", "#lang_update_rel td a").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#selector [value='tasks']").click();
		new VoodooControl("input", "css", "table tr:nth-of-type(1) td:nth-of-type(3) input:nth-of-type(1)").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "step1_next").click();
		VoodooUtils.focusFrame("selectiframe");
		new VoodooControl("input", "css", ".edit.view [value='status']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "table tr:nth-child(3) tr:nth-child(8) a").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#basic_options td:nth-of-type(2) select option:nth-of-type(1)").click();
		new VoodooControl("input", "css", "table tr:nth-of-type(1) td table tr:nth-of-type(5) input[title='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body form table tbody tr:nth-child(5) td input:nth-child(2)").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		// Contacts
		myContact.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		sugar.contacts.recordView.edit();
		sugar.contacts.recordView.getEditField("firstName").set(customData.get("first_name"));
		sugar.contacts.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verifying status field which was set in workflow, after action trigerred
		FieldSet newData = new FieldSet();
		newData.put("status", customData.get("status"));
		taskSubpanel.verify(1, newData, true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}