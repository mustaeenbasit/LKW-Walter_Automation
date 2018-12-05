package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Processes_30022 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Label id should not display for Process Author module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Processes_30022_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Click on Action drop-down in Navigation bar near search text box
		sugar().navbar.showAllModules();

		// Verify Process Definitions, Processes, Process Business Rules, Process Email Templates
		// TODO: VOOD-784
		new VoodooControl("a", "css", "div.module-list li[data-module='pmse_Project'] .module-list-link").assertContains(processData.get("processDefinitions"), true);
		new VoodooControl("a", "css", "div.module-list li[data-module='pmse_Inbox'] .module-list-link").assertContains(processData.get("processes"), true);
		new VoodooControl("a", "css", "div.module-list li[data-module='pmse_Business_Rules'] .module-list-link").assertContains(processData.get("processBusinessRules"), true);
		new VoodooControl("a", "css", "div.module-list li[data-module='pmse_Emails_Templates'] .module-list-link").assertContains(processData.get("processEmailTemplates"), true);

		// Again click on the show All Modules arrow drop down to hide the opened drop down
		sugar().navbar.showAllModules();

		// Click on Process Definition
		sugar().processDefinitions.navToListView();

		// Verify the Header of the 'Process Definition' list view
		for(String header : sugar().processDefinitions.listView.getHeaders()){
			sugar().processDefinitions.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertContains(processData.get(header), true);
		}

		// Navigates to the create drawer of the Process Definition
		sugar().processDefinitions.listView.create();
		sugar().processDefinitions.createDrawer.showMore();

		// Define controls for the fields display label
		// TODO: VOOD-1931
		VoodooControl nameLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='name']");
		VoodooControl targetModuleLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='prj_module']");
		VoodooControl assignedToLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='assigned_user_name']");
		VoodooControl statusLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='prj_status']");
		VoodooControl descriptionLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='description']");
		VoodooControl dateModifiedLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='date_modified']");
		VoodooControl dateEnteredLblCtrl = new VoodooControl("span", "css", ".record-label[data-name='date_entered']");
		// Define Save And Design Button control
		VoodooControl saveAndDesignButtonCtrl = sugar().processDefinitions.createDrawer.getControl("saveAndDesignButton");

		// Verify the fields display labels on create drawer
		nameLblCtrl.assertEquals(processData.get("name"), true);
		targetModuleLblCtrl.assertEquals(processData.get("prj_module"), true);
		assignedToLblCtrl.assertEquals(processData.get("assigned_user_name"), true);
		statusLblCtrl.assertEquals(processData.get("prj_status"), true);
		descriptionLblCtrl.assertEquals(processData.get("description"), true);
		dateModifiedLblCtrl.assertEquals(processData.get("date_modified"), true);
		dateEnteredLblCtrl.assertEquals(processData.get("date_entered"), true);
		saveAndDesignButtonCtrl.assertEquals(processData.get("saveAndDesignButton"), true);

		// Fill all the information on create drawer of Process Definition and Save
		sugar().processDefinitions.createDrawer.getEditField("name").set(sugar().processDefinitions.getDefaultData().get("name"));
		sugar().processDefinitions.createDrawer.getEditField("description").set(testName);
		saveAndDesignButtonCtrl.click();
		VoodooUtils.waitForReady();
		sugar().alerts.getSuccess().closeAlert();

		// Navigates to the record view of the created Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.clickRecord(1);

		// Verify the fields display labels on create drawer
		targetModuleLblCtrl.assertEquals(processData.get("prj_module"), true);
		assignedToLblCtrl.assertEquals(processData.get("assigned_user_name"), true);
		statusLblCtrl.assertEquals(processData.get("prj_status"), true);
		descriptionLblCtrl.assertEquals(processData.get("description"), true);
		dateModifiedLblCtrl.assertEquals(processData.get("date_modified"), true);
		dateEnteredLblCtrl.assertEquals(processData.get("date_entered"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}