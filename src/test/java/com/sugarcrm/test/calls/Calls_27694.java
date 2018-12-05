package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27694 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord) sugar().users.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().calls.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that cancel of editing one child call and it removes /edit/all-recurrences
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27694_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Calls listview and click on parent call 
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();

		FieldSet customData = testData.get(testName).get(0);
		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", customData.get("repeat_type"));
		repeatData.put("repeatOccur", customData.get("repeat_count"));

		sugar().calls.recordView.repeatOccurrences(repeatData);

		// In Guests fields, add Contact as invitees.
		VoodooControl relatedToParentTypeCtrl = sugar().calls.recordView.getEditField("relatedToParentType");
		VoodooControl relatedToParentNameCtrl = sugar().calls.recordView.getEditField("relatedToParentName");
		relatedToParentTypeCtrl.set(sugar().contacts.moduleNameSingular);
		relatedToParentNameCtrl.set(sugar().contacts.getDefaultData().get("firstName"));

		// In Guests fields, add Lead as invitees.
		relatedToParentTypeCtrl.set(sugar().leads.moduleNameSingular);
		relatedToParentNameCtrl.set(sugar().leads.getDefaultData().get("firstName"));
		VoodooUtils.waitForReady(); // Wait needed

		// add user as invitee
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myUser);
		sugar().calls.recordView.save();

		// Open one of the child call.
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);

		// Click on action drop down, select "Edit All Recurrences".
		sugar().calls.recordView.openPrimaryButtonDropdown();
		sugar().calls.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();

		// It routes to the parent call edit view. Verify in URL that "edit/all-recurrences" is exist.
		String currentUrl1 = VoodooUtils.getUrl();
		Assert.assertTrue("URL does not contain string edit/all-recurrences when it should", currentUrl1.contains(customData.get("editAllRecurrence")));

		// In the parent call  record, change one field, such as remove one invitee.
		sugar().calls.recordView.getControl(String.format("removeInvitee%02d", 3)).click();
		sugar().calls.recordView.cancel();

		// After clicking cancel, notice the URL doesn't have 'edit/all-recurrences' any more.
		String currentUrl2 = VoodooUtils.getUrl();
		Assert.assertFalse("URL contains string edit/all-recurrences when it should not", currentUrl2.contains(customData.get("editAllRecurrence")));

		// Verify that updated changes is reflecting in all children call records.
		// Go to Listview, open other calls and verify.
		sugar().calls.navToListView();
		// TODO: VOOD-1837 - Unsaved Alert appears when a Call/Meeting record is created via API and then edited via UI for relatedToParent fields
		if (sugar().alerts.getWarning().queryVisible()) {
			sugar().alerts.getWarning().confirmAlert();
		}
		sugar().calls.listView.clickRecord(2);

		FieldSet userRecord = new FieldSet();
		userRecord.put("user", myUser.get("lastName"));
		// Verify that 'user' invitee is not removed.
		sugar().calls.recordView.verifyInvitee(3, userRecord);

		// Click on third child call and verify.
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(3);

		// Verify that 'user' invitee is not removed.
		sugar().calls.recordView.verifyInvitee(3, userRecord);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}