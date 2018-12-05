package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

import java.util.ArrayList;

import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class SearchSelectViewTests extends SugarTest {
	LeadRecord myLead1, myLead2;
	FieldSet lead1 = new FieldSet(), lead2 = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myLead1 = (LeadRecord)sugar().leads.api.create();

		// custom lead
		lead2 = new FieldSet();
		lead2.put("firstName", "Test");
		lead2.put("lastName", "Lead");
		myLead2 = (LeadRecord)sugar().leads.api.create(lead2);

		sugar().login();

		// Navigate to accounts recordview
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
	}

	@Test
	public void verifyControls() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyControls()...");

		// Leads subpanel
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).clickLinkExisting();

		// Verify basic controls on SSV
		sugar().leads.searchSelect.getControl("moduleTitle").assertVisible(true);
		sugar().leads.searchSelect.getControl("count").assertVisible(true);
		sugar().leads.searchSelect.getControl("cancel").assertVisible(true);
		sugar().leads.searchSelect.getControl("link").assertVisible(true);
		sugar().leads.searchSelect.getControl("toggleSidebar").assertVisible(true);
		sugar().leads.searchSelect.getControl("search").assertVisible(true);
		sugar().leads.searchSelect.getControl("selectInput01").assertVisible(true);// 01 -> first record (checkbox)
		sugar().leads.searchSelect.getControl("preview01").assertVisible(true); // 01 -> first record (preview icon)
		sugar().leads.searchSelect.cancel();

		VoodooUtils.voodoo.log.info("verifyControls() test complete");
	}

	@Test
	public void verifyLinkUnlinkAndPreviewRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyLinkUnlinkAndPreviewRecord()...");

		// Leads subpanel
		StandardSubpanel leadSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubpanel.linkExistingRecord(myLead1);

		// Verify lead1
		lead1 = new FieldSet();
		lead1.put("fullName", myLead1.getRecordIdentifier());
		leadSubpanel.verify(1, lead1, true);

		// unlink lead record
		leadSubpanel.unlinkRecord(1);
		leadSubpanel.expandSubpanel();
		assertTrue("The subpanel is not empty, unlink failed", leadSubpanel.isEmpty());

		// existing leads (i.e lead1 and lead2)
		ArrayList<Record> linkRecords = new ArrayList<Record>();
		linkRecords.add(myLead1);
		linkRecords.add(myLead2);
		leadSubpanel.linkExistingRecords(linkRecords);

		// Verify lead1 and lead2
		lead2.clear();
		lead2.put("fullName", myLead2.getRecordIdentifier());

		// TODO: VOOD-1828
		leadSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		leadSubpanel.verify(1, lead2, true);
		leadSubpanel.verify(2, lead1, true);

		leadSubpanel.clickLinkExisting();

		// TODO: VOOD-1067 Sort list to known order, we need sugar().leads.searchSelect.sortBy() method
		new VoodooControl("span", "css", ".drawer.active .sorting.orderByfull_name span").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify leads preview with index of row and with searching record on search select drawer
		sugar().leads.searchSelect.preview(1);
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl fullName = sugar().previewPane.getPreviewPaneField("fullName");
		fullName.assertContains(lead2.get("fullName"), true);

		sugar().leads.searchSelect.preview(myLead1);
		sugar().alerts.waitForLoadingExpiration();
		fullName.assertContains(lead1.get("fullName"), true);
		sugar().leads.searchSelect.cancel();

		VoodooUtils.voodoo.log.info("verifyLinkUnlinkAndPreviewRecord() test complete.");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).clickLinkExisting();

		// Verify no record on calls SSV
		sugar().calls.searchSelect.getControl("count").assertVisible(true);
		int rowCountForCall = sugar().calls.searchSelect.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", rowCountForCall == 0);
		sugar().calls.searchSelect.cancel();

		// Verify 2 records on leads SSV
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).clickLinkExisting();
		int rowCountForLeads = sugar().leads.searchSelect.countRows();
		Assert.assertTrue("Number of rows did not equal two.", rowCountForLeads == 2);
		sugar().leads.searchSelect.getControl("count").getChildElement("span", "css", " span.count").assertEquals("("+rowCountForLeads+")", true);
		sugar().leads.searchSelect.cancel();

		VoodooUtils.voodoo.log.info("countRows() test complete");
	}

	public void cleanup() throws Exception {}
}