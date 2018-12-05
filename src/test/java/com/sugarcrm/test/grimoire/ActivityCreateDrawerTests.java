package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class ActivityCreateDrawerTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().calls.navToListView();
		sugar().calls.listView.create();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		sugar().calls.createDrawer.openPrimaryButtonDropdown();

		// Verify save element actions
		sugar().calls.createDrawer.getControl("saveAndSendInvites").assertVisible(true);
		// Verify invitees guest list and '+' invitee buttons
		sugar().calls.createDrawer.getControl("invitees").assertVisible(true);
		sugar().calls.createDrawer.getControl("addInviteeButton").assertVisible(true);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}

	@Test
	public void clickAddInvitee() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickAddInvitee()...");

		sugar().calls.createDrawer.clickAddInvitee();

		// Verify remove invitee icon and search invitee for very next record
		sugar().calls.createDrawer.getControl("removeInvitee02").assertVisible(true);
		sugar().calls.createDrawer.getControl("searchInvitee").assertVisible(true);

		VoodooUtils.voodoo.log.info("clickAddInvitee() complete.");
	}

	@Test
	public void selectInvitee() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectInvitee()...");

		LeadRecord myLead = (LeadRecord)sugar().leads.api.create();
		sugar().calls.createDrawer.clickAddInvitee();

		// Verifying invitees record in list after adding invitee 
		sugar().calls.createDrawer.selectInvitee(sugar().users.getQAUser().get("userName"));
		VoodooControl inviteeList = sugar().calls.createDrawer.getControl("invitees");
		inviteeList.assertContains(sugar().users.getQAUser().get("userName"), true);
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(myLead);
		inviteeList.assertContains(myLead.getRecordIdentifier(), true);		
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("selectInvitee() complete.");
	}

	@Test
	public void saveAndSendInvites() throws Exception {
		VoodooUtils.voodoo.log.info("Running saveAndSendInvites()...");

		FieldSet testData = new FieldSet();
		testData.put("name", "Call Aperture Laboratories");

		sugar().calls.createDrawer.getEditField("name").set(testData.get("name"));
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(sugar().users.getQAUser().get("userName"));
		sugar().calls.createDrawer.saveAndSendInvites();

		// Verify record is saved on listview after save and send invitee
		sugar().calls.listView.verifyField(1, "name", testData.get("name"));

		VoodooUtils.voodoo.log.info("saveAndSendInvites() complete.");
	}

	public void cleanup() throws Exception {}
}