package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28457 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that KB record view has correct fields 
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28457_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet kbRecordViewData = testData.get(testName).get(0);

		// Open one record view in KB
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Verify that KB record view Header should have these fields: Name, favorite, follow, status
		// TODO: VOOD-1931 and VOOD-828
		new VoodooControl("div", "css", ".headerpane .fld_name.detail div").assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);
		new VoodooControl("i", "css", ".headerpane .fa-favorite").assertVisible(true);
		new VoodooControl("a", "css", ".headerpane [name='follow']").assertEquals(kbRecordViewData.get("following"), true);
		new VoodooControl("span", "css", ".headerpane [data-name='status'] span").assertContains(kbRecordViewData.get("status"), true);

		// Verify that KB record view Short form should have these fields: body, attachments, tags, useful/not useful buttons, 'show more' link
		// TODO: VOOD-1931 and VOOD-828
		new VoodooControl("div", "css", ".panel_body div[data-name='kbdocument_body_set'] .record-label").assertEquals(kbRecordViewData.get("body"), true);
		new VoodooControl("div", "css", ".panel_body div[data-name='attachment_list'] .record-label").assertEquals(kbRecordViewData.get("attachments"), true);
		new VoodooControl("div", "css", ".panel_body div[data-name='tag'] .record-label").assertEquals(kbRecordViewData.get("tags"), true);
		sugar().knowledgeBase.recordView.getControl("showMore").assertEquals(kbRecordViewData.get("showMore"), true);
		// TODO: VOOD-1783
		VoodooControl usefulnessBtnCtrl = new VoodooControl("a", "css", "a[data-action='useful']");
		VoodooControl notUsefulnessBtnCtrl = new VoodooControl("a", "css", "a[data-action='notuseful']");
		usefulnessBtnCtrl.assertContains(kbRecordViewData.get("useful"), true);
		notUsefulnessBtnCtrl.assertContains(kbRecordViewData.get("notUseful"), true);

		// Look at the Long form
		sugar().knowledgeBase.recordView.showMore();

		// Verify that KB record view Long form should see these fields: all fields from short form and: language, revision, category, active revision, View Count, teams, Assigned to, external article, date created, created by, date modified, approved by, publish date, related case, expiration date, show less
		// TODO: VOOD-1931 and VOOD-828
		new VoodooControl("div", "css", "div[data-name='language'] .record-label").assertEquals(kbRecordViewData.get("language"), true);
		new VoodooControl("div", "css", "div[data-name='revision'] .record-label").assertEquals(kbRecordViewData.get("revision"), true);
		new VoodooControl("div", "css", "div[data-name='category_name'] .record-label").assertEquals(kbRecordViewData.get("categoryName"), true);
		new VoodooControl("div", "css", "div[data-name='active_rev'] .record-label").assertEquals(kbRecordViewData.get("activeRevision"), true);
		new VoodooControl("div", "css", "div[data-name='viewcount'] .record-label").assertEquals(kbRecordViewData.get("viewCount"), true);
		new VoodooControl("div", "css", "div[data-name='team_name'] .record-label").assertEquals(kbRecordViewData.get("teams"), true);
		new VoodooControl("div", "css", "div[data-name='assigned_user_name'] .record-label").assertEquals(kbRecordViewData.get("assignedTo"), true);
		new VoodooControl("div", "css", "div[data-name='is_external'] .record-label").assertEquals(kbRecordViewData.get("externalArticle"), true);
		new VoodooControl("div", "css", "div[data-name='date_entered'] .record-label").assertEquals(kbRecordViewData.get("dateEntered"), true);
		new VoodooControl("div", "css", "div[data-name='created_by_name'] .record-label").assertEquals(kbRecordViewData.get("createdBy"), true);
		new VoodooControl("div", "css", "div[data-name='date_modified'] .record-label").assertEquals(kbRecordViewData.get("dateModified"), true);
		new VoodooControl("div", "css", "div[data-name='kbsapprover_name'] .record-label").assertEquals(kbRecordViewData.get("approvedBy"), true);
		new VoodooControl("div", "css", "div[data-name='active_date'] .record-label").assertEquals(kbRecordViewData.get("publishedDate"), true);
		new VoodooControl("div", "css", "div[data-name='kbscase_name'] .record-label").assertEquals(kbRecordViewData.get("relatedCase"), true);
		new VoodooControl("div", "css", "div[data-name='exp_date'] .record-label").assertEquals(kbRecordViewData.get("expirationDate"), true);
		usefulnessBtnCtrl.assertContains(kbRecordViewData.get("useful"), true);
		notUsefulnessBtnCtrl.assertContains(kbRecordViewData.get("notUseful"), true);
		sugar().knowledgeBase.recordView.getControl("showLess").assertEquals(kbRecordViewData.get("showLess"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}