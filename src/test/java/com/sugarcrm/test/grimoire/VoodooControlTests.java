package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class VoodooControlTests extends SugarTest {
	AccountRecord myAccount;
	VoodooControl horizontalScrollBar, verticalScrollBar, createdOn, accName;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		horizontalScrollBar = sugar().accounts.listView.getControl("horizontalScrollBar");
		verticalScrollBar = sugar().accounts.recordView.getControl("verticalScrollBar");
		createdOn = new VoodooControl("span", "css", "div.flex-list-view-content > table > thead > tr:nth-child(1) > th.sorting.orderBydate_entered > div > div.ui-draggable > span");
		accName = new VoodooControl("a", "css", "div.flex-list-view-content > table > tbody > tr > td:nth-child(2) > span > div > a");
		contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void exists() throws Exception {
		VoodooUtils.voodoo.log.info("Running exists...");

		sugar().accounts.navToListView();

		// Positive test cases (should pass).
		sugar().accounts.listView.getControl("createButton").assertExists(true);
		sugar().accounts.createDrawer.getEditField("billingAddressStreet").assertExists(false);

		// Negative test cases (should fail, so we call a method that verifies
		// that they do).
		expectFailure(sugar().accounts.listView.getControl("createButton"), false);
		expectFailure(sugar().accounts.createDrawer.getEditField("billingAddressStreet"), true);
	}

	public void expectFailure(VoodooControl control, boolean shouldExist) throws Exception {
		boolean assertFailed = false;

		try {
			control.assertExists(shouldExist);
		} catch (AssertionError e) {
			assertFailed = true;
		}
		if (assertFailed == false) {
			String problem;
			if (shouldExist == true)
				problem = " existed when it did not.";
			else
				problem = " did not exist when it did.";

			fail("VoodooControl reported element with " + control.getStrategyName() + " = "
					+ control.getHookString() + problem);
		}
	}

	@Test
	public void assertContainsFailure() throws Exception {
		VoodooUtils.voodoo.log.info("Running assertContainsFailure...");

		sugar().accounts.navToListView();

		// Positive test cases (should pass).
		VoodooControl nameCtrl = sugar().accounts.listView.getDetailField(1, "name");
		nameCtrl.assertContains(myAccount.getRecordIdentifier(), true);

		// Negative test cases (should fail, so we call a method that verifies
		// that they do).
		expectAssertContainsFailure(nameCtrl, myAccount.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info("assertContainsFailure() test complete.");
	}

	public void expectAssertContainsFailure(VoodooControl control, String string, boolean shouldContain) throws Exception {
		boolean assertContainsFailed = false;

		// Actual string on UI
		String actualString = control.getText();
		try {
			control.assertContains(string, shouldContain);
		} catch (AssertionError e) {
			assertContainsFailed = true;
		}
		if (assertContainsFailed == false) {
			String problem;
			if (shouldContain == true)
				problem =  " does not contain string '" + string + "' when it should. Actual UI string is '" + actualString + "'";
			else
				problem = " contains string '" + string + "' when it should not. Actual UI string is '" + actualString + "'";

			fail("VoodooControl reported element with " + control.getStrategyName() + " = "
					+ control.getHookString() + problem);
		}
	}

	@Test
	public void assertElementContainsFailure() throws Exception {
		VoodooUtils.voodoo.log.info("Running assertElementContainsFailure...");

		sugar().accounts.navToListView();

		// Positive test cases (should pass).
		VoodooControl nameCtrl = sugar().accounts.listView.getDetailField(1, "name");
		nameCtrl.assertElementContains(myAccount.getRecordIdentifier(), true);

		// Negative test cases (should fail, so we call a method that verifies
		// that they do).
		expectAssertElementContainsFailure(nameCtrl, myAccount.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info("assertElementContainsFailure() test complete.");
	}

	public void expectAssertElementContainsFailure(VoodooControl control, String string, boolean shouldContain) throws Exception {
		boolean assertElementContainsFailed = false;

		// Actual string on UI
		String actualString = control.getText();
		try {
			control.assertElementContains(string, shouldContain);
		} catch (AssertionError e) {
			assertElementContainsFailed = true;
		}
		if (assertElementContainsFailed == false) {
			String problem;
			if (shouldContain == true)
				problem =  " does not contain string '" + string + "' when it should. Actual UI string is '" + actualString + "'";
			else
				problem = " contains string '" + string + "' when it should not. Actual UI string is '" + actualString + "'";

			fail("VoodooControl reported element with " + control.getStrategyName() + " = "
					+ control.getHookString() + problem);
		}
	}

	@Test
	public void assertEqualsFailure() throws Exception {
		VoodooUtils.voodoo.log.info("Running assertEqualsFailure...");

		sugar().accounts.navToListView();

		// Positive test cases (should pass).
		VoodooControl nameCtrl = sugar().accounts.listView.getDetailField(1, "name");
		nameCtrl.assertEquals(myAccount.getRecordIdentifier(), true);

		// Negative test cases (should fail, so we call a method that verifies
		// that they do).
		expectAssertEqualsFailure(nameCtrl, myAccount.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info("assertEqualsFailure() test complete.");
	}

	public void expectAssertEqualsFailure(VoodooControl control, String string, boolean shouldEqual) throws Exception {
		boolean assertEqualsFailed = false;

		// Actual string on UI
		String actualString = control.getText();
		try {
			control.assertEquals(string, shouldEqual);
		} catch (AssertionError e) {
			assertEqualsFailed = true;
		}
		if (assertEqualsFailed == false) {
			String problem;
			if (shouldEqual == true)
				problem =  " does not equal '" + string + "' when it should. Actual UI string is '" + actualString + "'";
			else
				problem = " equals '" + string + "' when it should not. Actual UI string is '" + actualString + "'";

			fail("VoodooControl reported element with " + control.getStrategyName() + " = "
					+ control.getHookString() + problem);
		}
	}

	@Test
	public void asserts() throws Exception {
		VoodooUtils.voodoo.log.info("Running asserts() ...");

		sugar().accounts.navToListView();

		// Test assertElementContains()
		VoodooControl nameCtrl = sugar().accounts.listView.getDetailField(1, "name");
		nameCtrl.assertElementContains(myAccount.getRecordIdentifier(), true);

		// Test assertEquals()
		nameCtrl.assertEquals(myAccount.getRecordIdentifier(), true);

		// Test assertContains
		nameCtrl.assertContains(myAccount.getRecordIdentifier(), true);

		// Test assertAttribute()
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "name").assertAttribute("class", "inherit-width required", true);
		sugar().accounts.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info("asserts() completed...");
	}

	@Test
	public void checkboxes() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkboxes...");
		VoodooUtils.voodoo.log.info("Checking in a BWC module..........");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); // focus on iframe document to get
		// access to elements in
		// backwards compatible mode
		VoodooUtils.pause(1000);
		sugar().admin.adminTools.getControl("passwordManagement").click();

		// Confirm default state of checkbox
		assertTrue(
				"This checkbox should be checked!",
				(true == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"passwordSettingOneUpper").getAttribute("checked"))));

		// The following checkbox by default is checked. We will use
		// .set("true") to confirm that the set method doesn't
		// click regardless of desired state.
		sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").set("true");
		assertTrue(
				"This checkbox should be checked!",
				(true == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"passwordSettingOneUpper").getAttribute("checked"))));

		// The same checkbox above should still be checked. We will use
		// .set("false") to confirm that the set method keeps the
		// checkbox in the desired state.
		sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").set("false");
		assertTrue(
				"This checkbox should NOT be checked!",
				(false == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"passwordSettingOneUpper").getAttribute("checked"))));

		// Confirm default state of checkbox
		assertTrue(
				"This checkbox should NOT be checked!",
				(false == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"SystemGeneratedPasswordCheckbox").getAttribute("checked"))));

		// The following checkbox by default is NOT checked. We will use
		// .set("false") to confirm that the set method doesn't
		// click regardless of desired state.
		sugar().admin.passwordManagement.getControl("SystemGeneratedPasswordCheckbox").set("false");
		assertTrue(
				"This checkbox should NOT be checked!",
				(false == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"SystemGeneratedPasswordCheckbox").getAttribute("checked"))));

		// The following checkbox should still NOT be checked. We will use
		// .set("true") to confirm that the set method doesn't
		// click regardless of desired state.
		sugar().admin.passwordManagement.getControl("SystemGeneratedPasswordCheckbox").set("true");
		assertTrue(
				"This checkbox should be checked!",
				(true == Boolean.parseBoolean(sugar().admin.passwordManagement.getControl(
						"SystemGeneratedPasswordCheckbox").getAttribute("checked"))));

		VoodooUtils.focusDefault();

		// Sidecar enabled tests
		VoodooUtils.voodoo.log.info("Checking checkboxes in a sidecar module.......");

		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		if (sugar().contacts.createDrawer.getControl("showMore").queryVisible())
			sugar().contacts.createDrawer.showMore();

		// Confirm default state of checkbox
		assertTrue("This checkbox should not be checked!",
				(false == Boolean.parseBoolean(new VoodooControl("input", "css",
						".fld_do_not_call.edit input").getAttribute("checked"))));

		// The following checkbox by default is not checked. We use
		// .set("false") to confirm that the set method doesn't
		// click regardless of desired state.
		new VoodooControl("input", "css", ".fld_do_not_call.edit input").set("false");
		assertTrue("This checkbox should not be checked!",
				(false == Boolean.parseBoolean(new VoodooControl("input", "css",
						".fld_do_not_call.edit input").getAttribute("checked"))));

		// The above checkbox should still NOT be checked. We use .set("true")
		// to confirm that the set method doesn't
		// click regardless of desired state.
		new VoodooControl("input", "css", ".fld_do_not_call.edit input").set("true");
		assertTrue("This checkbox should be checked!",
				(true == Boolean.parseBoolean(new VoodooControl("input", "css",
						".fld_do_not_call.edit input").getAttribute("checked"))));

		// Confirm default state of checkbox
		assertTrue("This checkbox should be checked!",
				(true == Boolean.parseBoolean(new VoodooControl("input", "css", ".fld_copy.edit input")
				.getAttribute("checked"))));

		// The following checkbox by default is checked. We use .set("true") to
		// confirm that the set method doesn't
		// click regardless of desired state.
		new VoodooControl("input", "css", ".fld_copy.edit input").set("true");
		assertTrue("This checkbox should be checked!",
				(true == Boolean.parseBoolean(new VoodooControl("input", "css", ".fld_copy.edit input")
				.getAttribute("checked"))));

		// The above checkbox should still be checked. We use .set("false") to
		// confirm that the set method doesn't
		// click regardless of desired state.
		new VoodooControl("input", "css", ".fld_copy.edit input").set("false");
		assertTrue("This checkbox should NOT be checked!",
				(false == Boolean
				.parseBoolean(new VoodooControl("input", "css", ".fld_copy.edit input")
				.getAttribute("checked"))));
	}

	@Test
	public void append() throws Exception {
		VoodooUtils.voodoo.log.info("Running append()...");

		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("name").append(" Edited");
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.getDetailField("name").assertEquals(myAccount.getRecordIdentifier() + " Edited", true);

		VoodooUtils.voodoo.log.info("append() test complete.");
	}

	@Test
	public void getXpathChildFromCssParentControl() throws Exception {
		VoodooUtils.voodoo.log.info("Running getXpathChildFromCssParentControl()...");

		VoodooControl parent = new VoodooControl("li", "css", "ul.dashlets li.layout_Home:nth-of-type(2)");
		VoodooControl child = parent.getChildElement("span", "xpath", "//li/div/div/div[2]/div/div[1]/div/div/div/span");
		VoodooUtils.voodoo.log.info(child.toString());
		child.assertAttribute("class", "edit fld_selectedTimePeriod");

		VoodooUtils.voodoo.log.info("getXpathChildFromCssParentControl() test complete.");
	}

	@Test
	public void getXPathChildFromXpathParentControl() throws Exception {
		VoodooUtils.voodoo.log.info("Running getXPathChildFromXpathParentControl()...");

		VoodooControl parent = new VoodooControl("ul", "xpath", "//*[@id='content']/div/div/div/div/ul/li[2]/ul/li[1]/div/ul[2]");
		VoodooControl child = parent.getChildElement("span", "xpath", "//li/div/div/div[2]/div/div[1]/div/div/div/span");
		VoodooUtils.voodoo.log.info(child.toString());
		child.assertAttribute("class", "edit fld_selectedTimePeriod");

		VoodooUtils.voodoo.log.info("getXPathChildFromXpathParentControl() test complete.");
	}

	@Test
	public void getCssChildFromCssParentControl() throws Exception {
		VoodooUtils.voodoo.log.info("Running getCssChildFromCssParentControl()...");

		VoodooControl parent = new VoodooControl("li", "css", "ul.dashlets li.layout_Home:nth-of-type(2)");
		VoodooControl child = parent.getChildElement("ul", "css", "ul");
		VoodooUtils.voodoo.log.info(child.toString());
		child.assertAttribute("class", "dashlet-row");

		VoodooUtils.voodoo.log.info("getCssChildFromCssParentControl() test complete.");
	}

	@Test
	public void getCssChildFromXpathParentControl() throws Exception {
		VoodooUtils.voodoo.log.info("Running getCssChildFromXpathParentControl()...");

		VoodooControl parent = new VoodooControl("li", "xpath", "//*[@id='content']/div/div/div/div/ul/li[2]");
		VoodooControl child = parent.getChildElement("ul", "css", "ul");
		VoodooUtils.voodoo.log.info(child.toString());
		child.assertAttribute("class", "dashlet-row");

		VoodooUtils.voodoo.log.info("getCssChildFromXpathParentControl() test complete.");
	}

	@Test
	public void isDisabled() throws Exception {
		VoodooUtils.voodoo.log.info("Running isDisabled()...");

		// Sidecar module for testing the disabled class
		sugar().accounts.navToListView();
		assertTrue("The action dropdown should be disabled, but isDisabled returned false.",
				sugar().accounts.listView.getControl("actionDropdown").isDisabled());
		assertFalse(
				"The select all checkbox should not be disabled, but isDisabled returned true.",
				sugar().accounts.listView.getControl("selectAllCheckbox").isDisabled());

		// BWC module for testing the disabled attribute.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "bug_tracker").click();
		VoodooUtils.pause(2000);
		VoodooUtils.focusFrame("bwc-frame");
		assertTrue("The list view start button should be disabled, but isDisabled returned false.",
				new VoodooControl("button", "css", "button[name='listViewStartButton']").isDisabled());
		assertFalse("The create button should not be disabled, but isDisabled returned true.",
				new VoodooControl("input", "css", "input[name='New']").isDisabled());
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("isDisabled() test complete.");
	}

	@Test
	public void scrollVerticallyCheck() throws Exception {
		myAccount.navToRecord();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// contactsSubpanel.assertVisible(false);
		verticalScrollBar.scrollVertically(400);
		// contactsSubpanel.assertVisible(true);
		verticalScrollBar.scrollVertically(-400);
		sugar().accounts.api.deleteAll();
	}

	@Test
	public void scrollVerticallyHomeCheck() throws Exception {
		myAccount.navToRecord();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// contactsSubpanel.assertVisible(false);
		verticalScrollBar.scrollVertically(200);
		// contactsSubpanel.assertVisible(true);
		verticalScrollBar.scrollVerticallyHome();
		// contactsSubpanel.assertVisible(false);
		sugar().accounts.api.deleteAll();
	}

	@Test
	public void scrollHorizontallyCheck() throws Exception {
		sugar().accounts.navToListView();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// createdOn.assertVisible(false);
		horizontalScrollBar.scrollHorizontally(140);
		// createdOn.assertVisible(true);
		horizontalScrollBar.scrollHorizontally(-140);
		// createdOn.assertVisible(false);
		sugar().accounts.api.deleteAll();
	}

	@Test
	public void scrollHorizontallyHomeCheck() throws Exception {
		sugar().accounts.navToListView();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// createdOn.assertVisible(false);
		horizontalScrollBar.scrollHorizontally(140);
		// createdOn.assertVisible(true);
		horizontalScrollBar.scrollHorizontallyHome();
		// createdOn.assertVisible(false);
		sugar().accounts.api.deleteAll();
	}

	@Test
	public void scrollIntoViewCheck() throws Exception {
		sugar().accounts.navToListView();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// createdOn.assertVisible(false);
		createdOn.scrollIntoView();
		// createdOn.assertVisible(true);
		sugar().accounts.api.deleteAll();
	}

	@Test
	public void scrollIntoViewIfNeeded() throws Exception {
		sugar().accounts.navToListView();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// createdOn.assertVisible(false);
		createdOn.scrollIntoViewIfNeeded(true);
		// createdOn.assertVisible(true);

		// accName.assertVisible(false);
		accName.scrollIntoViewIfNeeded(false);
		// accName.assertVisible(true);

		accName.click();

		// sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(false);
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).scrollIntoViewIfNeeded(true);
		// sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);

		// sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(false);
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).scrollIntoViewIfNeeded(true);
		// sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
	}

	@Test
	public void scrollIntoViewIfNeededCheck() throws Exception {
		sugar().accounts.navToListView();

		// TODO: CB-236 - Need a function to check status of visible but out of view elements on a page
		// createdOn.assertVisible(false);
		createdOn.scrollIntoViewIfNeeded(horizontalScrollBar, true);
		// createdOn.assertVisible(true);
		VoodooUtils.waitForReady();
		// accName.assertVisible(false);
		accName.scrollIntoViewIfNeeded(horizontalScrollBar, false);
		// accName.assertVisible(true);

		accName.click();

		// sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(false);
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).scrollIntoViewIfNeeded(verticalScrollBar, true);
		// sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertVisible(true);

		// sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(false);
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).scrollIntoViewIfNeeded(verticalScrollBar, true);
		// sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertVisible(true);
	}

	@Test
	public void scrollIntoViewIfNeededWithScrollParentDivCheck() throws Exception {
		myAccount.navToRecord();

		// Edit Email as api create does not fill email field
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("emailAddress").set("abc@xyz.com");
		sugar().accounts.recordView.save();

		sugar().accounts.navToListView();

		sugar().accounts.listView.getDetailField(1, "emailAddress").scrollIntoViewIfNeeded(horizontalScrollBar, false);

		// Click happens only when the field is in clickable view, otherwise it will give an overlapping error
		sugar().accounts.listView.getDetailField(1, "emailAddress").click();
		VoodooUtils.waitForReady();

		// Verify that Compose Email page has appeared
		new VoodooControl("span", "css", ".module-title").assertEquals("Compose Email", true);

		// Cancel Compose Email
		new VoodooControl("a", "css", "div [data-voodoo-name='compose'] a[name='cancel_button']").click();
		VoodooUtils.waitForReady();

		// Back to Accounts List view to check if other non anchor tag elements continue to work ok
		// Scroll to Created on col
		createdOn.scrollIntoViewIfNeeded(false);

		// Scroll to Account Name col and click link
		accName.scrollIntoViewIfNeeded(horizontalScrollBar, true);

		// Verify that recordView has appeared
		new VoodooControl("span", "css", "span.detail.fld_picture").assertExists(true);
		sugar().accounts.recordView.getDetailField("name").assertContains(sugar().accounts.getDefaultData().get("name"), true);
	}

	@Test
	public void getCssAttributeCheck() throws Exception {

		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		new VoodooControl("a", "css", "a[name='save_button']").assertAttribute("name", "save_button");

		sugar().navbar.selectMenuItem(sugar().accounts, "importAccounts");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h2", "css", "div.moduleTitle h2").assertCssAttribute("font-size", "18px");
		VoodooUtils.focusDefault();
	}

	@Test
	public void verifyWaitForVisibleBool() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyWaitForInvisible()...");

		// Test on non-existant alert
		sugar().alerts.getSuccess().waitForVisible(5000,false);

		sugar().accounts.navToListView();

		// Test waiting for the side bar to be invisible
		sugar().accounts.dashboard.getControl("create").assertVisible(true);
		sugar().accounts.listView.toggleSidebar();
		sugar().accounts.dashboard.getControl("create").waitForVisible(false);
		sugar().accounts.dashboard.getControl("create").assertVisible(false);
		sugar().accounts.listView.toggleSidebar();
		sugar().accounts.dashboard.getControl("create").assertVisible(true);

		// Manually create a record so that we can handle the success alert
		sugar().accounts.listView.getControl("createButton").click();
		FieldSet data = new FieldSet();
		data.put("name","Shauna Vayne");
		sugar().accounts.createDrawer.setFields(data);
		sugar().accounts.createDrawer.getControl("saveButton").click();

		// Use waitForInvisible to wait for alert timeouts
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().waitForVisible(25000,false);
		sugar().alerts.getSuccess().assertVisible(false);

		VoodooUtils.voodoo.log.info("verifyWaitForInvisible() test complete");
	}

	@Test
	public void verifyWaitForInvisible() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyWaitForInvisible()...");

		// Test on non-existant alert
		sugar().alerts.getSuccess().waitForInvisible(5000);

		sugar().accounts.navToListView();

		// Test waiting for the side bar to be invisible
		sugar().accounts.dashboard.getControl("create").assertVisible(true);
		sugar().accounts.listView.toggleSidebar();
		sugar().accounts.dashboard.getControl("create").waitForInvisible();
		sugar().accounts.dashboard.getControl("create").assertVisible(false);
		sugar().accounts.listView.toggleSidebar();
		sugar().accounts.dashboard.getControl("create").assertVisible(true);

		// Manually create a record so that we can handle the success alert
		sugar().accounts.listView.getControl("createButton").click();
		FieldSet data = new FieldSet();
		data.put("name","Shauna Vayne");
		sugar().accounts.createDrawer.setFields(data);
		sugar().accounts.createDrawer.getControl("saveButton").click();

		// Use waitForInvisible to wait for alert timeouts
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().waitForInvisible(25000);
		sugar().alerts.getSuccess().assertVisible(false);

		VoodooUtils.voodoo.log.info("verifyWaitForInvisible() test complete");
	}

	@Ignore("VOOD-1677 & CB-255 - Polling fails in WebDriverInterface.focusWindow() if the window doesn't exist yet.")
	@Test
	public void verifyWaitForAttributeMethods() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyWaitForAttributeMethods()...");

		// Ref: Forecasts_27817
		// This test Verifies that quota for both sales rep and manager is populating correctly

		Configuration config;
		String baseUrl;
		DataSource users;
		FieldSet firstUser, secondUser, thirdUser;
		UserRecord myFirstUser, mySecondUser, myThirdUser;
		VoodooControl commitButton;

		// TODO The VoodooControl references in Forecasts will be replaced by
		// VOOD-725 Forecasts Lib
		config = VoodooUtils.getGrimoireConfig();
		baseUrl = new SugarUrl().getBaseUrl();

		firstUser = new FieldSet();
		secondUser = new FieldSet();
		thirdUser = new FieldSet();

		firstUser.put("userName", "sally");
		firstUser.put("firstName", "Sally");
		firstUser.put("lastName", "Smith");
		firstUser.put("fullName", "Sally Smith");
		firstUser.put("emailAddress", "Sallysmith@example.com");
		firstUser.put("newPassword", "Sally123");
		firstUser.put("confirmPassword", "Sally123");
		firstUser.put("password", "Sally123");
		firstUser.put("timeZone", "America/Los Angeles (GMT-7:00)");

		secondUser.put("userName", "jim");
		secondUser.put("firstName", "Jim");
		secondUser.put("lastName", "Jones");
		secondUser.put("fullName", "Jim Jones");
		secondUser.put("emailAddress", "JimJones@example.com");
		secondUser.put("newPassword", "Jim123");
		secondUser.put("confirmPassword", "Jim123");
		secondUser.put("password", "Jim123");
		secondUser.put("timeZone", "America/Los Angeles (GMT-7:00)");

		thirdUser.put("userName", "jack");
		thirdUser.put("firstName", "Jack");
		thirdUser.put("lastName", "Davies");
		thirdUser.put("fullName", "Jack Davies");
		thirdUser.put("emailAddress", "ackDavies@example.com");
		thirdUser.put("newPassword", "Jack123");
		thirdUser.put("confirmPassword", "Jack123");
		thirdUser.put("password", "Jack123");
		thirdUser.put("timeZone", "America/Los Angeles (GMT-7:00)");

		commitButton = new VoodooControl("a", "css", "a[name='commit_button'].btn.btn-primary");

		// Create 3 users and set up 1 as Manager
		// TODO VOOD-444 - API Creating relationships
		myFirstUser = (UserRecord) sugar().users.create(firstUser);

		secondUser.put("reportsTo", firstUser.get("firstName"));
		thirdUser.put("reportsTo", firstUser.get("firstName"));

		mySecondUser = (UserRecord) sugar().users.create(secondUser);
		myThirdUser = (UserRecord) sugar().users.create(thirdUser);

		// Assign Sales Administrator role to Sally
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();

		// Click on 'Sales Administrator'
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a").click();

		// Click Select User button
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a#acl_roles_users_select_button").click();
		// Select Sally
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "xpath", "//td/a[contains(text(),'"+firstUser.get("fullName")+"')]").click();
		VoodooUtils.focusWindow(0);

		VoodooUtils.focusDefault();

		// Enable default Forecast settings
		sugar().navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").waitForVisible(30000);
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").click();
		VoodooUtils.waitForAlertExpiration(); // Required
		sugar().alerts.waitForLoadingExpiration(30000);

		VoodooUtils.focusDefault();

		sugar().logout(); // admin

		// After user creation, now add quota figures to users
		// Cannot add this to csv file as this will affect user creation
		firstUser.put("quota", "102.00");
		secondUser.put("quota", "202.00");
		thirdUser.put("quota", "302.00");
		String total_quota = "606.00";

		// The first user is a Manager so assign Quota options should be visible
		sugar().login(firstUser);

		// TODO: VOOD-929 applicable for all below controls
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary")
		.click();
		new VoodooControl("a", "css", ".fld_assign_quota").assertVisible(true);

		// Set quota for firstUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/input").set(firstUser.get("quota"));

		// Set quota for secondUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/input").set(secondUser.get("quota"));

		// Set quota for thirdUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/input").set(thirdUser.get("quota"));

		// As soon as any quota figure is changed, commit button becomes enabled
		new VoodooControl("h2", "id", "quota").click(); // To remove focus from quota

		assertTrue("Commit Button does not have class attribute", commitButton.hasAttribute("class"));

		commitButton.waitForAttributeNotToContain("class", "disabled");
		commitButton.click();
		commitButton.waitForAttributeToContain("class", "disabled");

		sugar().alerts.getSuccess().waitForVisible();
		sugar().alerts.getSuccess().closeAlert();

		// Goto Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// TODO: VOOD-929 applicable for all below controls
		// Verify that quota figures remain intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(total_quota, true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(firstUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(secondUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(thirdUser.get("quota"), true);

		// Goto firstUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+firstUser.get("fullName")+"')]").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that quota for firstUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(firstUser.get("quota"), true);

		// Goto secondUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+secondUser.get("fullName")+"')]").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that quota for secondUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(secondUser.get("quota"), true);

		// Goto thirdUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+thirdUser.get("fullName")+"')]").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that quota for thirdUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(thirdUser.get("quota"), true);

		sugar().logout(); // firstUser

		sugar().login();

		// Cleanup - Delete user and its private team
		myFirstUser.delete(myFirstUser);
		mySecondUser.delete(mySecondUser);
		myThirdUser.delete(myThirdUser);

		// Disable Forecast settings
		sugar().forecasts.resetForecastSettings();

		VoodooUtils.voodoo.log.info("verifyWaitForAttributeMethods() test complete");
	}

	@Test
	public void isChecked() throws Exception {
		VoodooUtils.voodoo.log.info("Running isChecked()...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("passwordManagement").click();
		VoodooUtils.waitForReady();

		assertTrue("The control was not checked when it should have been",
				sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").isChecked());

		assertTrue("The control was checked when it should NOT have been",
				!sugar().admin.passwordManagement.getControl("SystemGeneratedPasswordCheckbox").isChecked());

		VoodooUtils.voodoo.log.info("isChecked() test complete");
	}

	@Test
	public void assertChecked() throws Exception {
		VoodooUtils.voodoo.log.info("Running assertChecked()...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("passwordManagement").click();
		VoodooUtils.waitForReady();

		sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").assertChecked(true);
		sugar().admin.passwordManagement.getControl("SystemGeneratedPasswordCheckbox").assertChecked(false);

		VoodooUtils.voodoo.log.info("assertChecked() test complete");
	}

	@Test
	public void checkCount() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkCount()...");

		// returns count of the element and its siblings
		// Sibling is defined as elements with same parent and same tag

		// Count of RHS dashlets on home page
		int itemCount = new VoodooControl("li", "css",
				"ul.dashlets.row-fluid > li:nth-of-type(2) > ul > li.row-fluid").count();
		assertTrue("Error in method count(). Expected: 3, Actual: " + itemCount, itemCount == 3);

		itemCount = new VoodooControl("li", "css",
				"ul.dashlets.row-fluid > li:nth-of-type(2) > ul > li").count();
		assertTrue("Error in method count(). Expected: 3, Actual: " + itemCount, itemCount == 3);

		VoodooUtils.voodoo.log.info("checkCount() test complete");
	}

	@Test
	public void checkCountWithClass() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkCountWithClass()...");

		// returns count of the element and its exact siblings
		// Exact Sibling is defined as elements with same parent, same tag and with same class

		// Count of RHS dashlets on home page
		int itemCount = new VoodooControl("li", "xpath",
				"//ul[@class='dashlets row-fluid']/li[2]/ul/li[@class='row-fluid sortable']").countWithClass();
		assertTrue("Error in method checkCountWithClass(). Expected: 2, Actual: " + itemCount, itemCount == 2);

		VoodooUtils.voodoo.log.info("checkCountWithClass() test complete");
	}

	@Test
	public void checkCountAll() throws Exception {
		VoodooUtils.voodoo.log.info("Running checkCountAll()...");

		// returns count of the element, same tag + containing same class(es), on the whole page

		// Count of all dashlets on home page
		int itemCount = new VoodooControl("li", "xpath", "//li[@class='row-fluid']").countAll();
		assertTrue("Error in method checkCountAll(). Expected: 7, Actual: " + itemCount, itemCount == 7);

		VoodooUtils.voodoo.log.info("checkCountAll() test complete");
	}


	public void cleanup() throws Exception {}
}