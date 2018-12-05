package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30653 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Status should be Draft when create a new KB during convert Lead
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30653_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		String kbModuleName = sugar().knowledgeBase.moduleNamePlural;

		// TODO: VOOD-542, VOOD-1506
		// Go to studio->Leads->Layouts->Lead Convert
		new VoodooControl("a", "id", "studiolink_Leads").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("select", "css", "#convertSelectNewModule").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", "#convertSelectNewModule [value='" + kbModuleName + "']").click();
		new VoodooControl("input", "css", "[name='addModule']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='" + kbModuleName + "-required']").click();
		new VoodooControl("input", "css", "[name='saveLayout']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Leads recordView
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Convert lead
		sugar().leads.recordView.openPrimaryButtonDropdown();
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: VOOD-695
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-585
		// In Account panel
		new VoodooControl("input", "css", "#collapseAccounts .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", ".accordion-heading.enabled.active .convert-panel-header.fld_associate_button").click();
		VoodooUtils.waitForReady();

		// In Opportunity panel
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", ".accordion-heading.enabled.active .convert-panel-header.fld_associate_button").click();
		VoodooUtils.waitForReady();

		// Verify status should be Draft by default
		new VoodooControl("a", "css", "#collapse" + kbModuleName + " .fld_status.edit a").assertContains(customFS.get("status"), true);

		// In KB panel
		new VoodooControl("b", "css", "#collapse" + kbModuleName + " .fld_status.edit a span.select2-arrow b").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".select2-drop-active ul.select2-results li:nth-child(3)").click();
		new VoodooControl("input", "css", "#collapse" + kbModuleName + " .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", ".accordion-heading.enabled.active .convert-panel-header.fld_associate_button").click();
		VoodooUtils.waitForReady();

		// Verify that the yellow warn message bar appears "Schedule this article to be published by specifying the Publish Date. Do you wish to continue without entering a Publish Date"
		sugar().alerts.getWarning().assertContains(customFS.get("warningMessage"), true);

		// Click on No.  The Calendar appears at Publish Date, allow you select tomorrow date. Then save the new KB
		sugar().alerts.cancelAllWarning();
		VoodooUtils.waitForReady();
		String pubLishDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");

		// TODO: VOOD-585
		new VoodooControl("input", "css", ".fld_active_date.edit input").set(pubLishDate);
		VoodooUtils.waitForReady();

		// Click on create KB button
		new VoodooControl("a", "css", ".accordion-heading.enabled.active .convert-panel-header.fld_associate_button").click();
		VoodooUtils.waitForReady();

		// Click on Save and Convert button
		new VoodooControl("a", "css", ".convert-headerpane.fld_save_button a").click();
		VoodooUtils.waitForReady(30000);

		// Go to listView
		sugar().leads.navToListView();

		// Verify that the Lead is converted
		sugar().leads.listView.getDetailField(1, "status").assertContains(customFS.get("statusConverted"), true);

		// Go to KB recordView
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Verify that the status, name, and other fields that you have entered in KB are correct. 
		sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName, true);
		sugar().knowledgeBase.recordView.getDetailField("status").assertEquals(customFS.get("approvedStatus"), true);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.getDetailField("date_publish").assertContains(pubLishDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 