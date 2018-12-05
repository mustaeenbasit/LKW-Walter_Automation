package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27697 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord) sugar().users.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().calls.api.create();
		sugar().login(sugar().users.getQAUser());		
	}

	/**
	 * Verify that edit parent call and it displays /edit/all_recurrences.
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27697_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();

		FieldSet customData = testData.get(testName).get(0);

		// Fill Repeat Type = Yearly
		sugar().calls.recordView.getEditField("repeatType").set(customData.get("repeat_type"));

		// Fill Repeat Occurrences = 3 times
		sugar().calls.recordView.getEditField("repeatOccurType").set(customData.get("repeatOccurType"));
		sugar().calls.recordView.getEditField("repeatOccur").set(customData.get("repeat_count"));

		// In Guests fields, add Contact as invitees.
		VoodooControl relatedToParentTypeCtrl = sugar().calls.recordView.getEditField("relatedToParentType");
		VoodooControl relatedToParentNameCtrl = sugar().calls.recordView.getEditField("relatedToParentName");
		relatedToParentTypeCtrl.set(sugar().contacts.moduleNameSingular);
		relatedToParentNameCtrl.set(sugar().contacts.getDefaultData().get("firstName"));

		// In Guests fields, add Lead as invitees.
		relatedToParentTypeCtrl.set(sugar().leads.moduleNameSingular);
		relatedToParentNameCtrl.set(sugar().leads.getDefaultData().get("firstName"));

		sugar().calls.recordView.save();

		// Open the parent call.
		sugar().calls.navToListView();
		// Using XPath to select calls by unique date.
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/span/div/div[2]/div[2]/div[2]/div[3]/div[1]/table/tbody/tr[contains(.,'" + sugar().calls.getDefaultData().get("date_start_date") + "')]/td[2]/span/div/a").click();
		VoodooUtils.waitForReady();
		sugar().calls.recordView.openPrimaryButtonDropdown();
		sugar().calls.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();

		// Verify in URL that "edit/all_recurrences" is exist.
		String currentUrl1 = VoodooUtils.getUrl();
		Assert.assertTrue("URL does not contain string edit/all-recurrence when it should", currentUrl1.contains(customData.get("editAllRecurrences")));

		// In Guests fields, add user as invitees.
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myUser);

		// Save
		sugar().calls.recordView.save();

		// After saving, notice the URL doesn't have 'edit/all_recurrences' any more.
		String currentUrl2 = VoodooUtils.getUrl();
		Assert.assertFalse("URL contains string edit/all-recurrence when it should not", currentUrl2.contains(customData.get("editAllRecurrences")));

		// Go to other calls and verify.
		sugar().calls.navToListView();

		// Click on first child call.
		// Using XPath to select calls by unique date.
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/span/div/div[2]/div[2]/div[2]/div[3]/div[1]/table/tbody/tr[contains(.,'" + customData.get("date1") + "')]/td[2]/span/div/a").click();
		VoodooUtils.waitForReady();

		FieldSet userInviteeData = new FieldSet();
		userInviteeData.put("fullName", myUser.get("fullName"));
		// Verify that updated changes is reflecting in all children call records.
		sugar().calls.recordView.verifyInvitee(3, userInviteeData);

		// Click on second child call.
		// Using XPath to select calls by unique date.
		sugar().calls.navToListView();
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/span/div/div[2]/div[2]/div[2]/div[3]/div[1]/table/tbody/tr[contains(.,'" + customData.get("date2") + "')]/td[2]/span/div/a").click();
		VoodooUtils.waitForReady();

		// Verify that updated changes is reflecting in all children call records.
		sugar().calls.recordView.verifyInvitee(3, userInviteeData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}