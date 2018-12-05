package com.sugarcrm.test.activitystream;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;


public class ActivityStream_26232 extends SugarTest {

	public void setup() throws Exception {
		sugar.leads.api.create();
		sugar.login();
		
		// Set lead record as follow
		sugar.leads.navToListView();
		sugar.leads.listView.toggleFollow(1);
	}

	/**
	 * Linked Activity Stream is generated when add a Note or an attachment in Notes sub panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_26232_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customDS = testData.get(testName);
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		StandardSubpanel noteSubPanel = sugar.leads.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		noteSubPanel.scrollIntoView();
		noteSubPanel.addRecord();
		
		// Create Notes
		sugar.notes.createDrawer.getEditField("subject").set(testName);
		sugar.notes.createDrawer.save();
		String noteModuleBadge = String.format(customDS.get(0).get("linkedRecord"), testName,sugar.leads.getDefaultData().get("fullName"));
		String leadModuleBadge = String.format(customDS.get(1).get("linkedRecord"), sugar.leads.getDefaultData().get("fullName"));
		String noteModuleListView = String.format(customDS.get(1).get("linkedRecord"), testName)+" Note.";
		
		// Click on show activity stream
		sugar.leads.recordView.showActivityStream();
		
		// Verify that A Nt badge AS message about "Linked [NoteName] to [recordName]" in recordView, recordListView.
		sugar.leads.recordView.activityStream.assertCommentContains(noteModuleBadge, 1, true);
		
		// TODO: VOOD-977 need defined control for the module icon on activity stream
		VoodooControl noteLabelCtrl = new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(1) div.label-module");
		VoodooControl leadLabelCtrl = new VoodooControl("div", "css", ".activitystream-list.results > li:nth-of-type(2) div.label-module");
		noteLabelCtrl.assertEquals(customDS.get(0).get("moduleBadge"), true);
		
		// Check call create activity on home activity stream page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		
		ActivityStream stream = new ActivityStream();
		
		// Verify that A Le badge AS message about "Created [NoteName]" in Home AS listView. 
		stream.assertCommentContains(noteModuleListView, 1, true);
		
		// Verify that A Nt badge AS message about "Created [NoteName]" in Home AS listView. 
		leadLabelCtrl.assertEquals(customDS.get(1).get("moduleBadge"), true);
		stream.assertCommentContains(leadModuleBadge, 2, true);
		
		// Go to leads list view
		sugar.notes.navToListView();
		sugar.notes.listView.showActivityStream();
		
		// Verify that A Nt badge AS message about "Created [NoteName]" in [NoteName] recordView, notesListView.
		sugar.notes.recordView.activityStream.assertCommentContains(noteModuleListView, 1, true);
		
		// Go to notes recordView
		sugar.notes.listView.showListView();
		sugar.notes.listView.clickRecord(1);
		
		// Verify that A Le badge AS message about "Linked [NoteName] to [recordName]" in [NoteName] recordView.
		sugar.notes.recordView.activityStream.assertCommentContains(noteModuleListView, 1, true);
		sugar.notes.recordView.activityStream.assertCommentContains(noteModuleBadge, 2, true);
		
		// TODO: MAR-780, once this bug is resolved need ot verify below comment
		// A Fi badge AS message about "Added file [NoteName] to [recordName]" in recordView and Home AS listView..
		
		// TODO: VOOD-954 Clean up will fail if not on dashboard page after navigation to home
		sugar.navbar.clickModuleDropdown(sugar.home);
		
		// TODO: VOOD-953 need defined control of My Dashboard menu item under home tab
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}