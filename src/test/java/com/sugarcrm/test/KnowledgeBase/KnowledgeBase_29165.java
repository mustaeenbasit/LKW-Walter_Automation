package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.sugar.views.CreateDrawer;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29165 extends SugarTest {
	DataSource kbData = new DataSource();
	String popUpErrorMessage = "";
	String tooltipValidationMessage = "";
	String currentDate = "";
	String kbCreateSuccesMessage = "";
	CreateDrawer kbCreateDrawer;
	VoodooControl kbNameEditField, kbStatusEditField, kbPublishDateEditField, toolTipIcon, toolTip;
	Alert successAlert, errorAlert;

	public void setup() throws Exception {
		kbData = testData.get(testName);
		popUpErrorMessage = kbData.get(0).get("popUpError");
		tooltipValidationMessage = kbData.get(0).get("toolTipError");
		kbCreateSuccesMessage = kbData.get(0).get("kbCreateSuccessMessage");
		currentDate = DateTime.now().toString("MM/dd/yyyy");
		kbCreateDrawer = sugar().knowledgeBase.createDrawer;
		kbNameEditField = kbCreateDrawer.getEditField("name");
		kbStatusEditField = kbCreateDrawer.getEditField("status");
		kbPublishDateEditField = kbCreateDrawer.getEditField("date_publish");
		successAlert = sugar().alerts.getSuccess();
		errorAlert = sugar().alerts.getError();
		toolTipIcon = new VoodooControl("i", "css", ".fld_active_date.edit.error .fa-exclamation-circle");
		toolTip = new VoodooControl("div", "css", ".tooltip.top .tooltip-inner");

		// Login as admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings (previously 'Configure') option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding two new localization languages de - Deutsch and fr - French 
		// VOOD: 1762 : Need library support for adding/removing languages in Knowledge Base
		for (int i = 0; i < kbData.size(); i++) {
			new VoodooControl("button", "css", ".btn.first").click();
			new VoodooControl("input", "css", ".fld_languages div:nth-child(" + (i+2) + ").control-group [name='key_languages']").set(kbData.get(i).get("langKey"));
			new VoodooControl("input", "css", ".fld_languages div:nth-child(" + (i+2) + ").control-group [name='value_languages']").set(kbData.get(i).get("langValue"));
		}

		// Save the languages
		new VoodooControl("a", "css", ".fld_main_dropdown [name='save_button']").click();
		VoodooUtils.waitForReady();

		// Logout from Admin
		sugar().logout();
	}

	// Method to create the Revision/Localization with publishDate = Today and Asserting the publishDate Validation Errors
	private void assertPublishDateValidationErrors(String kbName, String statusApproved) throws Exception {
		// Enter the Revision/Localization Name
		kbNameEditField.set(kbName);

		// Enter the status = Approved
		kbStatusEditField.set(statusApproved);
		kbCreateDrawer.showMore();

		// Click Save button
		kbCreateDrawer.save();

		// Assert that the publish date takes the current date
		kbPublishDateEditField.scrollIntoViewIfNeeded(false);
		kbPublishDateEditField.assertEquals(currentDate, true);

		// Assert the error in the Alert pop up
		errorAlert.assertEquals(popUpErrorMessage, true);

		// Close the Alert
		errorAlert.closeAlert();

		// Hover on the tooltip of Publish Date
		// TODO: VOOD-1292 - Need lib support to assert tooltip text inside edit fields for all sidecar modules in case of error message
		toolTipIcon.hover();

		// Assert the error message in the tooptip
		toolTip.assertEquals(tooltipValidationMessage, true);
	}

	// Method to change the publishDate to futureDate and Save the Record. Also assert the record creation
	private void changePublishDateAndSaveApprovedKb(String kbName, String futureDate) throws Exception {
		// Change the publish date to a future date
		kbPublishDateEditField.set(futureDate);

		// Click Save
		kbCreateDrawer.save();

		// Assert that the success message appears for kb record successful creation
		successAlert.assertContains(String.format("%s %s", kbCreateSuccesMessage, kbName), true);
		if(successAlert.queryVisible() == true) {
			successAlert.closeAlert();
		}
	}

	/**
	 * Verify that published date is validated correctly when create a status=Approved revision or localization of a Published KB
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29165_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qaUser
		sugar().login(sugar().users.getQAUser());

		// qaUser creates a Published KB record
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.listView.create();
		kbNameEditField.set(sugar().knowledgeBase.getDefaultData().get("name"));
		kbStatusEditField.set(kbData.get(0).get("status"));
		kbCreateDrawer.save();
		if(successAlert.queryVisible() == true) {
			successAlert.closeAlert();
		}

		// Initialize the String variables that would be used more than once
		String statusApproved = kbData.get(1).get("status");
		String revision1 = kbData.get(0).get("revisionName");
		String revision2 = kbData.get(1).get("revisionName");
		String localization1 = kbData.get(0).get("localizationName");
		String localization2 = kbData.get(1).get("localizationName");
		String futureDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");

		// In the listView of KB, Choose "Create Revision" from the action list
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		new VoodooControl("span", "css", ".list.fld_create_revision_button").click();

		// Create a KB revision with status = Approved and Publish date = current date. Assert validation error message in the Alert pop-up and in the tooptip of the publishDate
		assertPublishDateValidationErrors(revision1, statusApproved);
		// Change the publishDate to Future Date and click Save. Assert that the Revision gets created.
		changePublishDateAndSaveApprovedKb(revision1, futureDate);

		// Create a localization
		// Navigate the the record view of the KB article created above
		sugar().knowledgeBase.listView.clickRecord(1);
		// Open the action list
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();

		// Click "Create Localization" option
		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		new VoodooControl("a", "css", ".detail.fld_create_localization_button a").click();
		VoodooUtils.waitForReady();

		// Create a KB Localization with status = Approved and Publish date = current date. Assert validation error message in the Alert pop-up and in the tooptip of the publishDate
		assertPublishDateValidationErrors(localization1, statusApproved);
		// Change the publishDate to Future Date and click Save. Assert that the Localization gets created.
		changePublishDateAndSaveApprovedKb(localization1, futureDate);

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		VoodooControl createLocalizationsCtrl = new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']");

		// In the Revision Subpanel, click the "+" icon to create another revision
		new VoodooControl("a", "css", "[data-subpanel-link='revisions'] [name='create_button']").click();
		VoodooUtils.waitForReady();

		// Create a KB revision with status = Approved and Publish date = current date. Assert validation error message in the Alert pop-up and in the tooptip of the publishDate
		assertPublishDateValidationErrors(revision2, statusApproved);
		// Change the publishDate to Future Date and click Save. Assert that the Revision gets created.
		changePublishDateAndSaveApprovedKb(revision2, futureDate);

		// In the Localization subpanel, click the "+" icon to create another Localization
		createLocalizationsCtrl.click();
		VoodooUtils.waitForReady();

		// Create a KB Localization with status = Approved and Publish date = current date. Assert validation error message in the Alert pop-up and in the tooptip of the publishDate
		assertPublishDateValidationErrors(localization2, statusApproved);
		// Change the publishDate to Future Date and click Save. Assert that the Localization gets created.
		changePublishDateAndSaveApprovedKb(localization2, futureDate);

		// Assert the Localizations and Revisions created in the subpanel
		// Assert the revisions created and their status = Approved
		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		new VoodooControl("a", "css", "div[data-subpanel-link='revisions'] .single .list.fld_name a").assertEquals(revision2, true);
		new VoodooControl("a", "css", "div[data-subpanel-link='revisions'] .single:nth-child(2) .list.fld_name a").assertEquals(revision1, true);
		new VoodooControl("span", "css", "div[data-subpanel-link='revisions'] .single .list.fld_status").assertEquals(statusApproved, true);
		new VoodooControl("span", "css", "div[data-subpanel-link='revisions'] .single:nth-child(2) .list.fld_status").assertEquals(statusApproved, true);

		// Assert the localizations created and their status = Approved
		new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .single .list.fld_name a").assertEquals(localization2, true);
		new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .single:nth-child(2) .list.fld_name a").assertEquals(localization1, true);
		new VoodooControl("span", "css", "div[data-subpanel-link='localizations'] .single .list.fld_status").assertEquals(statusApproved, true);
		new VoodooControl("span", "css", "div[data-subpanel-link='localizations'] .single:nth-child(2) .list.fld_status").assertEquals(statusApproved, true);

		// Click on the "+" icon in the Localization subpanel again
		createLocalizationsCtrl.click();
		VoodooUtils.waitForReady();

		// Assert that the warning message appears "Unable to create a new localization as a localization version exists for all available languages.", as we have only 2 languages other than English
		// and have already created 2 localizations for them
		sugar().alerts.getWarning().assertEquals(kbData.get(0).get("warningMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
