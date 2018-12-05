package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests different BWCSubpanel class methods in SugarCRM.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class BWCSubpanelTests extends SugarTest {

	public void setup() throws Exception {
		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.api.create();
		sugar().login();
		myQuote.navToRecord();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		BWCSubpanel activitySubpanel = sugar().quotes.detailView.subpanels.get(sugar().tasks.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl actionMenu = activitySubpanel.getControl("subpanelActionsMenu");
		actionMenu.assertVisible(true);
		VoodooUtils.focusDefault();
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_schedulemeeting_button_create_");
		sugar().meetings.createDrawer.cancel();
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_logcall_button_create_");
		sugar().calls.createDrawer.cancel();
		activitySubpanel.expandSubpanelActionsMenu();
		VoodooUtils.focusFrame("bwc-frame");
		activitySubpanel.getControl("composeEmail").assertVisible(true);
		actionMenu.click();
		VoodooUtils.focusDefault();

		// TODO: VOOD-444 
		// Create dependency of meeting record in activities subpanel
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_schedulemeeting_button_create_");
		sugar().meetings.createDrawer.getEditField("name").set("Sugar meeting");
		sugar().meetings.createDrawer.save();

		VoodooUtils.focusFrame("bwc-frame");
		int row = 1;
		activitySubpanel.getControl(String.format("viewRecordRow%02d", row)).assertVisible(true);
		activitySubpanel.getControl(String.format("recordNameRow%02d", row)).assertVisible(true);
		activitySubpanel.getControl(String.format("expandActionRow%02d", row)).click();
		activitySubpanel.getControl(String.format("unlinkRecordRow%02d", row)).assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}

	@Test
	public void verifyRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyRecord()...");

		// TODO: VOOD-444
		// Create dependency of call record in activities subpanel
		FieldSet callFS = new FieldSet();
		callFS.put("name", sugar().calls.getDefaultData().get("name"));
		callFS.put("status", sugar().calls.getDefaultData().get("status"));

		BWCSubpanel activitySubpanel = sugar().quotes.detailView.subpanels.get(sugar().tasks.moduleNamePlural);
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_logcall_button_create_");
		sugar().calls.createDrawer.getEditField("name").set(callFS.get("name"));
		sugar().calls.createDrawer.save();

		// Verify record
		activitySubpanel.verify(1, callFS, true);

		VoodooUtils.voodoo.log.info("verifyRecord() complete.");
	}

	@Test
	public void verifyClickRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickRecord()...");

		// TODO: VOOD-444
		// Create dependency of meeting record in activities subpanel
		String meetingName = sugar().meetings.getDefaultData().get("name");
		BWCSubpanel activitySubpanel = sugar().quotes.detailView.subpanels.get(sugar().tasks.moduleNamePlural);
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_schedulemeeting_button_create_");
		sugar().meetings.createDrawer.getEditField("name").set(meetingName);
		sugar().meetings.createDrawer.save();

		// Verify click on the subject
		activitySubpanel.clickRecord(1);
		sugar().meetings.recordView.getDetailField("name").assertEquals(meetingName, true);

		VoodooUtils.voodoo.log.info("verifyClickRecord() complete.");
	}

	@Test
	public void verifyViewRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyViewRecord()...");

		// TODO: VOOD-444
		// Create dependency of call record in activities subpanel
		String callSubject = sugar().calls.getDefaultData().get("name");
		BWCSubpanel activitySubpanel = sugar().quotes.detailView.subpanels.get(sugar().tasks.moduleNamePlural);
		activitySubpanel.expandSubpanelActionsMenu();
		activitySubpanel.subpanelAction("#Activities_logcall_button_create_");
		sugar().calls.createDrawer.getEditField("name").set(callSubject);
		sugar().calls.createDrawer.save();

		// Verify view record
		activitySubpanel.viewRecord(1);
		sugar().calls.recordView.getDetailField("name").assertEquals(callSubject, true);

		VoodooUtils.voodoo.log.info("verifyViewRecord() complete.");
	}

	@Test
	public void verifyUnlinkRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyUnlinkRecord()...");

		// TODO: VOOD-444
		// Create dependency of document record in doc subpanel
		BWCSubpanel documentsSubpanel = sugar().quotes.detailView.subpanels.get(sugar().documents.moduleNamePlural);
		documentsSubpanel.expandSubpanelActionsMenu();
		documentsSubpanel.subpanelAction("#documents_quotes_create_button");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.editView.getEditField("documentName").set(sugar().documents.getDefaultData().get("documentName"));

		// TODO: VOOD-2028, VOOD-826
		new VoodooFileField("input", "id", "filename_file").set("src/main/resources/data/DocumentsModuleFields.csv");
		new VoodooControl("input", "id", "Documents_subpanel_save_button").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Verify record is successfully unlinked from document subpanel
		documentsSubpanel.unlinkRecord(1);
		assertTrue("The Document Subpanel is not empty, when it should be", documentsSubpanel.isEmpty()); 

		VoodooUtils.voodoo.log.info("verifyUnlinkRecord() complete.");
	}

	public void isEmpty() throws Exception {
		VoodooUtils.voodoo.log.info("Running isEmpty()...");

		BWCSubpanel documentsSubpanel = sugar().quotes.detailView.subpanels.get(sugar().documents.moduleNamePlural);
		assertTrue("The Document Subpanel is not empty, when it should be", documentsSubpanel.isEmpty()); 

		VoodooUtils.voodoo.log.info("isEmpty() complete.");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifySortBy() throws Exception {
		VoodooUtils.voodoo.log.severe("BWCSubpanel sortBy() not implemented yet!");
	}

	@Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
	public void verifyHeader() throws Exception {
		VoodooUtils.voodoo.log.info("BWCSubpanel getHeaders() not implemented yet!");
	}

	public void cleanup() throws Exception {}
}