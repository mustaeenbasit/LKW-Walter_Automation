package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18822 extends SugarTest {
	AccountRecord myAccount, myAccount2;
	FieldSet customData;
	StandardSubpanel contactSubpanel;

	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord)sugar.accounts.api.create();

		sugar.contacts.api.create();
		customData = testData.get(testName).get(0);

		// Account is added with a contact associated
		myAccount.navToRecord();		
		contactSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactSubpanel.clickLinkExisting();
		new VoodooControl("input", "css", "div.layout_Contacts table tbody tr:nth-child(1) td:nth-child(1) input[type='checkbox']").click();
		new VoodooControl("a", "css", "a[name='link_button']").click();

		sugar.alerts.getSuccess().closeAlert();

		myAccount2 = (AccountRecord)sugar.accounts.api.create();
		myAccount2.navToRecord();
		contactSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactSubpanel.clickLinkExisting();
		new VoodooControl("input", "css", "div.layout_Contacts table tbody tr:nth-child(1) td:nth-child(1) input[type='checkbox']").click();
		new VoodooControl("a", "css", "a[name='link_button']").click();
		sugar.alerts.getSuccess().closeAlert();
	}

	/**
	 * Workflow_Trigger_By_MassUpdate
	 * @throws Exception
	 */
	@Test
	public void WorkFlow_18822_execute() throws Exception {
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

		// Create Work-flow with target as Accounts Module
		new VoodooControl("input", "id", "name").set(customData.get("wf_name"));
		new VoodooControl("input", "id", "save_workflow").click();

		// Condition : When a field in the target module contains a specified value 
		// First Name as Sugar
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(2) td:nth-child(1) input[type='radio']").click();
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "save");
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
		new VoodooControl("option", "css", "#selector option:nth-of-type(7)").click();
		new VoodooControl("input", "css", "table tr:nth-of-type(1) td:nth-of-type(3) input:nth-of-type(1)").click();
		//VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "step1_next").click();
		VoodooUtils.focusFrame("selectiframe");
		new VoodooControl("input", "css", "input[type='checkbox'][value= 'assigned_user_id']").click();
		new VoodooControl("input", "css", "input[type='checkbox'][value= 'team_id']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusWindow(1);

		// Set Assigned User
		new VoodooControl("a", "css", "table tr:nth-child(3)  tr:nth-child(5) a").click();
		//VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#basic_options td:nth-of-type(2) select option:nth-of-type(2)").click();
		new VoodooControl("input", "css", "table tr:nth-of-type(1) td table tr:nth-of-type(5) input[title='Save']").click();
		//VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(1);
		//Set Team ID
		new VoodooControl("a", "css", "table tr:nth-child(3)  tr:nth-child(6) a").click();
		//VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#basic_options td:nth-of-type(2) select option:nth-of-type(3)").click();
		new VoodooControl("input", "css", "table tr:nth-of-type(1) td table tr:nth-of-type(5) input[title='Save']").click();
		//VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(1);

		new VoodooControl("input", "css", "body form table tbody tr:nth-child(5) td input:nth-child(2)").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		// Mass update two Accounts
		sugar.accounts.navToListView();
		sugar.accounts.listView.toggleSelectAll();
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Teams", "qauser");
		sugar.accounts.massUpdate.performMassUpdate(massUpdateData);
		sugar.alerts.getSuccess().waitForVisible();
		sugar.alerts.getSuccess().closeAlert();

		// Assert check
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.showMore();
		new VoodooControl("div", "css", "div.record div.record-label[data-name='assigned_user_name']").assertContains("Assigned to", true);
		new VoodooControl("a", "css", "div.record span.fld_assigned_user_name.detail a").assertContains("qauser", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}