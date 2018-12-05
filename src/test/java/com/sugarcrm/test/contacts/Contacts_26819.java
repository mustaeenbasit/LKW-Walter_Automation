package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_26819 extends SugarTest {

	public void setup() throws Exception {
		AccountRecord accountRecord = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);

		// TODO:VOOD-1444
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl QLI = new VoodooControl("div","xpath", "//*[contains(@class,'add_subpanels')]//div[.='Quoted Line Items']");
		VoodooControl displaySubpanel = new VoodooControl("div", "id", "enabled_subpanels_div");
		QLI.dragNDrop(displaySubpanel);
		VoodooControl Projects = new VoodooControl("div","xpath", "//*[contains(@class,'add_subpanels')]//div[.='Projects']");
		Projects.dragNDrop(displaySubpanel);
		sugar().admin.configureTabs.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Creating a Contact and relating it to an Account
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.getEditField("relAccountName").set(accountRecord.getRecordIdentifier());
		sugar().alerts.cancelAllWarning();
		sugar().contacts.createDrawer.save();
	}

	/**
	 * Verify that correct account information is populated by default when create records via sub-panels in Contacts record view
	 * @throws Exception
	 */
	@Test
	public void Contacts_26819_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.listView.clickRecord(1);

		// Selecting Calls Subpanel and asserting the Account in Related Fields
		StandardSubpanel Calls = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		Calls.addRecord();
		sugar().calls.createDrawer.getEditField("relatedToParentType").assertContains(sugar().accounts.moduleNameSingular,true);
		sugar().calls.createDrawer.getEditField("relatedToParentName").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().calls.createDrawer.cancel();

		// Selecting Meetings Subpanel and asserting the Account in Related Fields
		StandardSubpanel Meetings = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		Meetings.addRecord();
		sugar().meetings.createDrawer.getEditField("relatedToParentType").assertContains(sugar().accounts.moduleNameSingular,true);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().meetings.createDrawer.cancel();

		// Selecting Tasks Subpanel and asserting the Account in Related Fields
		StandardSubpanel tasks = sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasks.addRecord();
		sugar().tasks.createDrawer.getEditField("relRelatedToParentType").assertContains(sugar().accounts.moduleNameSingular,true);
		sugar().tasks.createDrawer.getEditField("relRelatedToParent").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().tasks.createDrawer.cancel();

		// Selecting Notes Subpanel and asserting the Account in Related Fields
		StandardSubpanel notes = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notes.addRecord();
		sugar().notes.createDrawer.getEditField("relRelatedToModule").assertContains(sugar().accounts.moduleNameSingular,true);
		sugar().notes.createDrawer.getEditField("relRelatedToValue").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().notes.createDrawer.cancel();

		// Selecting Opportunities Subpanel and asserting the Account in Related Fields
		StandardSubpanel opp = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opp.addRecord();
		sugar().opportunities.createDrawer.getEditField("relAccountName").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().opportunities.createDrawer.cancel();

		// Selecting Direct Reports Subpanel and asserting the Account in Related Fields
		StandardSubpanel directReports = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReports.addRecord();
		sugar().contacts.createDrawer.getEditField("relAccountName").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		sugar().contacts.createDrawer.cancel();

		// Selecting QLI Subpanel and asserting the Account in Related Fields
		VoodooControl QLI =  new VoodooControl("span", "css", ".layout_Products span[data-voodoo-name='create_button'] a");
		QLI.click();
		sugar().quotedLineItems.createDrawer.getEditField("accountName").assertContains(sugar().accounts.getDefaultData().get("name"),true);

		// TODO:Product Bug SC-4191 The Expected Result(i.e 3c) can't be executed/verified

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
