package com.sugarcrm.test.activitystream;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_21182 extends SugarTest {
	UserRecord qauser;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		
		// Login as QAUser
		sugar.login(sugar.users.getQAUser());					
	}

	/**
	 * Verify submit bottom is enabled when copy/paste an string
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_21182_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.showActivityStream();
		VoodooControl accountActivitySubmitBtn = sugar.accounts.recordView.activityStream.getControl("submit");
		
		// Verify that activity stream button is disabled
		Assert.assertTrue("The activity submit button is already enabled.", accountActivitySubmitBtn.isDisabled());
		sugar.accounts.recordView.activityStream.getControl("streamInput").set(testName);
		VoodooUtils.waitForReady();
		
		// Verify that submit button is enabled
		Assert.assertFalse("The activity submit button is still disabled.", accountActivitySubmitBtn.isDisabled());
		
		// Submit Activity stream
		sugar.accounts.recordView.activityStream.clickSubmit();
		VoodooUtils.waitForReady();
		
		// Verify that the comment is posted.
		sugar.accounts.recordView.activityStream.assertCommentContains(testName, 1, true);
	
		// Go to contacts recordView
		sugar.contacts.navToListView();
		sugar.contacts.listView.showActivityStream();
		VoodooControl contactActivitySubmitBtn = sugar.contacts.recordView.activityStream.getControl("submit");
		contactActivitySubmitBtn.waitForVisible();
		
		// Verify that activity stream button is disabled
		Assert.assertTrue("The activity submit button is already enabled.", contactActivitySubmitBtn.isDisabled());
		sugar.accounts.recordView.activityStream.getControl("streamInput").set(testName);
		VoodooUtils.waitForReady();
		
		// Verify that submit button is enabled
		Assert.assertFalse("The activity submit button is still disabled.", contactActivitySubmitBtn.isDisabled());
		
		// Submit Activity stream
		sugar.accounts.recordView.activityStream.clickSubmit();
		VoodooUtils.waitForReady();
		
		// Verify that the comment is posted.
		sugar.contacts.listView.activityStream.assertCommentContains(testName, 1, true);
		
		// Check call create activity on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		
		ActivityStream stream = new ActivityStream();
		VoodooControl homeActivitySubmitBtn = stream.getControl("submit");
		
		// Verify that activity stream button is disabled
		Assert.assertTrue("The activity submit button is already enabled.", homeActivitySubmitBtn.isDisabled());
		stream.getControl("streamInput").set(testName);
		VoodooUtils.waitForReady();
		
		// Verify that submit button is enabled
		Assert.assertFalse("The activity submit button is still disabled.", homeActivitySubmitBtn.isDisabled());
		
		// Submit Activity stream
		stream.clickSubmit();
		VoodooUtils.waitForReady();
		
		// Verify that the comment is posted.
		stream.assertContains(testName, true);
				
		// TODO: VOOD-954 Clean up will fail if not on dashboard page after navigation to home
		sugar.navbar.clickModuleDropdown(sugar.home);
		
		// TODO: VOOD-953 need defined control of My Dashboard menu item under home tab
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 