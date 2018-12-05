package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28273 extends SugarTest {
	DataSource leadData;
	FieldSet callData;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		callData = testData.get(testName + "_callData").get(0);
		sugar().leads.api.create(leadData);

		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that subpanel limit is respected in Guests field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().calls.navToListView();		
		sugar().calls.listView.create();

		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		for (int i=0; i<6; i++){
			sugar().calls.createDrawer.getEditField("relatedToParentName").set(leadData.get(i).get("lastName"));
		}
		sugar().calls.createDrawer.save();

		sugar().calls.listView.clickRecord(1);
		// Verify 5 guests in the guest field
		for (int i=0; i<5; i++){
			sugar().calls.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}	

		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more").click();
		sugar().alerts.waitForLoadingExpiration();
		// Verify 2 more guests in the guest field
		sugar().calls.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(5).get("lastName"), true);
		sugar().calls.recordView.getControl("invitees").assertContains(sugar().users.getQAUser().get("userName"), true);

		sugar().calls.navToListView();
		// qaUser create a repeat call and has 7 guests
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(callData.get("name"));
		sugar().calls.createDrawer.getEditField("repeatType").set(callData.get("repeatType"));
		sugar().calls.createDrawer.getEditField("repeatEndType").set(callData.get("repeatEndType"));
		sugar().calls.createDrawer.getEditField("repeatOccur").set(callData.get("repeatOccur"));
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		for (int i=0; i<6; i++){
			sugar().calls.createDrawer.getEditField("relatedToParentName").set(leadData.get(i).get("lastName"));
		}
		sugar().calls.createDrawer.save();

		sugar().calls.listView.clickRecord(1);
		// Verify 5 guests in the guest field
		for (int i=0; i<5; i++){
			sugar().calls.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}	

		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more").click();
		sugar().alerts.waitForLoadingExpiration();
		// Verify 2 more guests in the guest field
		sugar().calls.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(5).get("lastName"), true);
		sugar().calls.recordView.getControl("invitees").assertContains(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}