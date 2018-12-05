package com.sugarcrm.test.grimoire;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for menu dropdown items, listview headers, sortBy, edit/detail list & record hook values,
 * preview pane and subpanels on record view.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class KBModuleTests extends SugarTest {
	KBRecord kbRecord;

	public void setup() throws Exception {
		kbRecord = (KBRecord)sugar().knowledgeBase.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().knowledgeBase.navToListView();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// Verify menu items
		sugar().knowledgeBase.menu.getControl("createArticle").assertVisible(true);
		sugar().knowledgeBase.menu.getControl("createTemplate").assertVisible(true);
		sugar().knowledgeBase.menu.getControl("viewArticles").assertVisible(true);
		sugar().knowledgeBase.menu.getControl("viewTemplates").assertVisible(true);
		sugar().knowledgeBase.menu.getControl("viewCategories").assertVisible(true);
		sugar().knowledgeBase.menu.getControl("configure").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		ArrayList<String> hiddenHeaders = new ArrayList<String>(Arrays.asList("revision", "kbsapprover_name", "assigned_user_name"));
		sugar().knowledgeBase.listView.toggleHeaderColumns(hiddenHeaders);

		// TODO: VOOD-1768 - Once resolved "language" header check should remove it from for loop
		new VoodooControl("th", "css", "th[data-fieldname=language]").assertVisible(true);

		// Verify all sort headers in listview
		for(String header : sugar().knowledgeBase.listView.getHeaders()) {
			if(!header.equals("language")) // language field has no sort feature
				sugar().knowledgeBase.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		// 2 knowledgeBase having 1 with default data and another with custom data
		FieldSet customData = new FieldSet();
		customData.put("name", "Nerd");
		sugar().knowledgeBase.api.create(customData);
		VoodooUtils.refresh(); // to reload data

		// Verify records after sort by 'name ' in descending and ascending order
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		sugar().knowledgeBase.listView.verifyField(1, "name", customData.get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbRecord.getRecordIdentifier());

		sugar().knowledgeBase.listView.sortBy("headerName", true);
		sugar().knowledgeBase.listView.verifyField(1, "name", kbRecord.getRecordIdentifier());
		sugar().knowledgeBase.listView.verifyField(2, "name", customData.get("name"));

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().knowledgeBase.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// name
		sugar().previewPane.getPreviewPaneField("name").assertEquals(kbRecord.getRecordIdentifier(), true);

		// status
		sugar().previewPane.getPreviewPaneField("status").assertVisible(true);

		// tags
		sugar().previewPane.getPreviewPaneField("tags").assertVisible(true);

		// language
		sugar().previewPane.getPreviewPaneField("language").assertVisible(true);

		// revision
		sugar().previewPane.getPreviewPaneField("revision").assertVisible(true);

		// category
		sugar().previewPane.getPreviewPaneField("category").assertVisible(true);

		// TODO: TR-10342 - Once resolved, commented code will work
		/*
		// active Revision (disabled and checked)
		VoodooControl activeRev = sugar().previewPane.getPreviewPaneField("isActiveRevision");
		activeRev.assertVisible(true);
		Assert.assertTrue("ActiveRev is enabled", activeRev.isDisabled());
		Assert.assertTrue("ActiveRev checkbox is unchecked", activeRev.isChecked());

		// created by
		sugar().previewPane.getPreviewPaneField("dateEnteredBy").assertVisible(true);
		 */

		// frequency
		sugar().previewPane.getPreviewPaneField("viewCount").assertVisible(true);

		// team
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(true);

		// author
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertVisible(true);

		// external article
		VoodooControl external = sugar().previewPane.getPreviewPaneField("isExternal");
		external.assertVisible(true);
		Assert.assertTrue("External checkbox is checked", !external.isChecked());

		// date created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// Date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		// approvedBy
		sugar().previewPane.getPreviewPaneField("approvedBy").assertVisible(true);

		// publish date
		sugar().previewPane.getPreviewPaneField("date_publish").assertVisible(true);

		// rel case
		sugar().previewPane.getPreviewPaneField("relCase").assertVisible(true);

		// expiration date
		sugar().previewPane.getPreviewPaneField("date_expiration").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().knowledgeBase.listView.editRecord(1);

		// name
		sugar().knowledgeBase.listView.getEditField(1, "name").assertEquals(kbRecord.getRecordIdentifier(), true);

		// language (read-only)
		VoodooControl language = sugar().knowledgeBase.listView.getEditField(1, "language");
		language.assertVisible(true);
		language.assertAttribute("class", "edit", false);

		// status
		sugar().knowledgeBase.listView.getEditField(1, "status").assertVisible(true);

		// category
		sugar().knowledgeBase.listView.getEditField(1, "category").assertVisible(true);

		// frequency (read-only)
		VoodooControl frequency = sugar().knowledgeBase.listView.getEditField(1, "viewCount");
		frequency.assertVisible(true);
		frequency.assertAttribute("class", "edit", false);

		// date created (read-only)
		VoodooControl dateCreated = sugar().knowledgeBase.listView.getEditField(1, "date_entered_date");
		dateCreated.scrollIntoViewIfNeeded(false);
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// cancel record
		sugar().knowledgeBase.listView.cancelRecord(1);

		// verify detail fields
		// name
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbRecord.getRecordIdentifier(), true);

		// language
		sugar().knowledgeBase.listView.getDetailField(1, "language").assertVisible(true);

		// status
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertVisible(true);

		// category
		sugar().knowledgeBase.listView.getDetailField(1, "category").assertVisible(true);

		// frequency
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertVisible(true);

		// date created
		sugar().knowledgeBase.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();

		// name
		sugar().knowledgeBase.recordView.getEditField("name").assertEquals(kbRecord.getRecordIdentifier(), true);

		// status
		VoodooControl status = sugar().knowledgeBase.recordView.getEditField("status");
		status.set("Approved");
		status.assertEquals("Approved", true);

		// body
		VoodooUtils.focusFrame("mce_0_ifr");
		VoodooControl body = sugar().knowledgeBase.recordView.getEditField("body");
		body.assertVisible(true);
		body.assertContains(kbRecord.get("body"), true);
		VoodooUtils.focusDefault();

		// tags
		sugar().knowledgeBase.recordView.getEditField("tags").assertVisible(true);

		// language (read-only)
		VoodooControl language = sugar().knowledgeBase.recordView.getEditField("language");
		language.assertVisible(true);
		language.assertAttribute("class", "edit", false);

		// revision (read-only)
		VoodooControl revision = sugar().knowledgeBase.recordView.getEditField("revision");
		revision.assertVisible(true);
		revision.assertAttribute("class", "edit", false);

		// category
		sugar().knowledgeBase.recordView.getEditField("category").assertVisible(true);

		// active revision (default disabled and checked)
		VoodooControl activeRev = sugar().knowledgeBase.recordView.getEditField("isActiveRevision");
		activeRev.assertVisible(true);
		Assert.assertTrue("ActiveRev is enabled", activeRev.isDisabled());
		Assert.assertTrue("ActiveRev checkbox is unchecked", activeRev.isChecked());

		// frequency (read-only)
		VoodooControl frequency = sugar().knowledgeBase.recordView.getEditField("viewCount");
		frequency.assertVisible(true);
		frequency.assertAttribute("class", "edit", false);

		// team
		sugar().knowledgeBase.recordView.getEditField("relTeam").assertEquals("Global", true);

		// author
		sugar().knowledgeBase.recordView.getEditField("relAssignedTo").assertVisible(true);

		// external article
		VoodooControl isExternal = sugar().knowledgeBase.recordView.getEditField("isExternal");
		isExternal.click();
		Assert.assertTrue("isActiveRev checkbox is unchecked", isExternal.isChecked());

		// date created (read-only)
		VoodooControl dateCreated = sugar().knowledgeBase.recordView.getEditField("date_entered_date");
		dateCreated.assertVisible(true);
		dateCreated.assertAttribute("class", "edit", false);

		// created by
		sugar().knowledgeBase.recordView.getEditField("dateEnteredBy").assertVisible(true);

		// date modified (read-only)
		VoodooControl dateModified = sugar().knowledgeBase.recordView.getEditField("date_modified_date");
		dateModified.assertVisible(true);
		dateModified.assertAttribute("class", "edit", false);

		// approved by
		sugar().knowledgeBase.recordView.getEditField("approvedBy").assertVisible(true);

		// publish date
		VoodooControl publishDate = sugar().knowledgeBase.recordView.getEditField("date_publish");
		publishDate.assertVisible(true);
		publishDate.assertEquals(kbRecord.get("date_publish"), true);

		// expiration date
		VoodooControl expirationDate = sugar().knowledgeBase.recordView.getEditField("date_expiration");
		expirationDate.assertVisible(true);
		expirationDate.assertEquals(kbRecord.get("date_expiration"), true);

		// related case
		sugar().knowledgeBase.recordView.getEditField("relCase").assertVisible(true);

		// cancel the record
		sugar().knowledgeBase.recordView.cancel();

		// verify detail view fields
		// name
		sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(kbRecord.getRecordIdentifier(), true);

		// status
		sugar().knowledgeBase.recordView.getDetailField("status").assertVisible(true);

		// body
		sugar().knowledgeBase.recordView.getDetailField("body").assertVisible(true);

		// tags
		sugar().knowledgeBase.recordView.getDetailField("tags").assertVisible(true);

		// language
		sugar().knowledgeBase.recordView.getDetailField("language").assertVisible(true);

		// revision
		sugar().knowledgeBase.recordView.getDetailField("revision").assertVisible(true);

		// category
		sugar().knowledgeBase.recordView.getDetailField("category").assertVisible(true);

		// active revision
		VoodooControl isActiveRev = sugar().knowledgeBase.recordView.getDetailField("isActiveRevision");
		isActiveRev.assertVisible(true);
		Assert.assertTrue("ActiveRev is enabled", isActiveRev.isDisabled());
		Assert.assertTrue("ActiveRev checkbox is unchecked", isActiveRev.isChecked());

		// frequency
		sugar().knowledgeBase.recordView.getDetailField("viewCount").assertVisible(true);

		// team
		sugar().knowledgeBase.recordView.getDetailField("relTeam").assertContains("Global", true);

		// author
		sugar().knowledgeBase.recordView.getDetailField("relAssignedTo").assertVisible(true);

		// external article
		VoodooControl externalArticle = sugar().knowledgeBase.recordView.getDetailField("isExternal");
		externalArticle.assertVisible(true);
		Assert.assertTrue("External checkbox is checked", !externalArticle.isChecked());

		// date created
		sugar().knowledgeBase.recordView.getDetailField("date_entered_date").assertVisible(true);

		// created by
		sugar().knowledgeBase.recordView.getDetailField("dateEnteredBy").assertVisible(true);

		// date modified
		sugar().knowledgeBase.recordView.getDetailField("date_modified_date").assertVisible(true);

		// approved by
		sugar().knowledgeBase.recordView.getDetailField("approvedBy").assertVisible(true);

		// publish date
		VoodooControl detailPublishDate = sugar().knowledgeBase.recordView.getDetailField("date_publish");
		detailPublishDate.assertVisible(true);
		detailPublishDate.assertContains(kbRecord.get("date_publish"), true);

		// expiration date
		VoodooControl detailExpirationDate = sugar().knowledgeBase.recordView.getDetailField("date_expiration");
		detailExpirationDate.assertVisible(true);
		detailExpirationDate.assertContains(kbRecord.get("date_expiration"), true);

		// related case
		sugar().knowledgeBase.recordView.getDetailField("relCase").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifySubpanelsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySubpanelsInRecordView()...");

		sugar().knowledgeBase.listView.clickRecord(1);

		// TODO: VOOD-1760 - Once resolved add assertion for Localization and Revision subpanel too
		// Verify subpanels
		sugar().knowledgeBase.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifySubpanelsInRecordView() complete.");
	}

	@Test
	public void apiCreateKBRecordStatusDraft() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateKBRecordStatusDraft()...");

		FieldSet draftKB = new FieldSet();
		draftKB.put("status", "Draft");
		sugar().knowledgeBase.api.create(draftKB);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)");

		VoodooUtils.voodoo.log.info("apiCreateKBRecordStatusDraft() complete.");
	}

	@Test
	public void apiCreateKBRecordStatusPublished() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateKBRecordStatusPublished()...");

		FieldSet publishedKB = new FieldSet();
		publishedKB.put("status", "Published");
		sugar().knowledgeBase.api.create(publishedKB);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(51, 128, 13, 1)");

		VoodooUtils.voodoo.log.info("apiCreateKBRecordStatusPublished() complete.");
	}

	@Test
	public void apiCreateKBRecordStatusInReview() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateKBRecordStatusInReview()...");

		FieldSet inReviewKB = new FieldSet();
		inReviewKB.put("status", "In Review");
		sugar().knowledgeBase.api.create(inReviewKB);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(253, 248, 238, 1)");

		VoodooUtils.voodoo.log.info("apiCreateKBRecordStatusInReview() complete.");
	}

	@Test
	public void apiCreateKBRecordStatusExpired() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateKBRecordStatusExpired()...");

		FieldSet expiredKB = new FieldSet();
		expiredKB.put("status", "Expired");
		sugar().knowledgeBase.api.create(expiredKB);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(85, 85, 85, 1)");

		VoodooUtils.voodoo.log.info("apiCreateKBRecordStatusExpired() complete.");
	}

	@Test
	public void apiCreateKBRecordStatusApproved() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateKBRecordStatusApproved()...");

		FieldSet approvedKB = new FieldSet();
		approvedKB.put("status", "Approved");
		sugar().knowledgeBase.api.create(approvedKB);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(15, 119, 153, 1)");
		VoodooUtils.voodoo.log.info("apiCreateKBRecordStatusApproved() complete.");
	}

	public void cleanup() throws Exception {}
}