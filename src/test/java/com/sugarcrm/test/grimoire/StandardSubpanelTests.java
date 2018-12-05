package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.records.*;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import java.util.ArrayList;

public class StandardSubpanelTests extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;
	OpportunityRecord myOpp;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements() ...");

		myContact = (ContactRecord)sugar().contacts.api.create();
		myAccount.navToRecord();
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.linkExistingRecord(myContact);
		contactSubpanel.getControl("subpanelName").assertVisible(true);
		contactSubpanel.getControl("count").assertVisible(true);
		contactSubpanel.getControl("toggleSubpanel").assertVisible(true);
		contactSubpanel.getControl("addRecord").assertVisible(true);
		contactSubpanel.getControl("subpanelStatus").assertVisible(true);
		contactSubpanel.getControl("nameRow01").assertVisible(true);
		contactSubpanel.getControl("favoriteRow01").assertVisible(true);
		contactSubpanel.getControl("previewRow01").assertVisible(true);
		contactSubpanel.getControl("expandActionRow01").click();
		contactSubpanel.getControl("unlinkActionRow01").assertVisible(true);
		contactSubpanel.getControl("editActionRow01").click();
		contactSubpanel.getControl("saveActionRow01").assertVisible(true);
		contactSubpanel.getControl("cancelActionRow01").click();

		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}

	@Test
	public void createRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running createRecord() ...");

		ContactRecord contactMe = (ContactRecord)sugar().contacts.api.create();

		// TODO: VOOD-1655 Once this resolved, this method should be remove it from here. As this is a part of Create operation
		myAccount.navToRecord();
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.toggleSubpanel();
		contactSubpanel.addRecord();
		sugar().contacts.createDrawer.cancel();
		Assert.assertTrue("Record is created.", contactSubpanel.isEmpty());

		FieldSet contactData = new FieldSet();
		contactData.put("firstName", "Mazen");
		contactData.put("lastName", "Louis");
		contactSubpanel.create(contactData);

		contactSubpanel.linkExistingRecord(contactMe);

		// TODO: VOOD-1424 - Commented lines will work after this story resolved
		contactSubpanel.getDetailField(1, "fullName").assertContains(contactData.get("firstName") + " " + contactData.get("lastName"), true);
		contactSubpanel.getDetailField(2, "fullName").assertContains(contactMe.getRecordIdentifier(), true);
		//FieldSet myContactData = new FieldSet();
		//myContactData.put("firstName", myContact.get("firstName"));
		//contactSubpanel.verify(1, myContactData, true);

		VoodooUtils.voodoo.log.info("createRecord() complete.");
	}

	@Test
	public void showMore() throws Exception {
		VoodooUtils.voodoo.log.info("Running showMore()...");

		// Create 6 contacts to relate to account
		for (int i = 0; i < 6; i++) {
			sugar().contacts.api.create();
		}

		myAccount.navToRecord();

		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.clickLinkExisting();

		// TODO: VOOD-1573
		new VoodooControl("input","css",".layout_Contacts.drawer.active .btn.checkall input").click();
		sugar().contacts.searchSelect.link();

		contactSubpanel.showMore();
		contactSubpanel.getControl("moreLink").assertVisible(false);

		VoodooUtils.voodoo.log.info("showMore() complete.");
	}

	@Test
	public void clickRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickRecord()...");

		TaskRecord myTask = (TaskRecord)sugar().tasks.api.create();

		myAccount.navToRecord();
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSub.linkExistingRecord(myTask);
		taskSub.clickRecord(1);
		sugar().tasks.recordView.getDetailField("subject").assertContains(myTask.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("clickRecord() complete.");
	}

	@Ignore("VOOD-1573 Search and Select Drawer needs to be updated to accommodate multi-select vs single select")
	@Test
	public void selectReports() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectReports()...");

		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().targetlists.api.create();
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		// click on Summation Report-> Contacts-> lastName (Group By)
		new VoodooControl("td", "css", "#report_type_div  tr:nth-child(2) td:nth-child(1) table tbody  tr:nth-child(3) td:nth-child(1)").click();
		new VoodooControl("table", "id", sugar().contacts.moduleNamePlural).click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Contacts_last_name").click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div input#nextButton").click();

		// Save and Run Report
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.focusDefault();

		// Go to target list recordview -> Contact Subpanel -> select from reports dropdown
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.selectFromReports(1);

		// TODO: VOOD-1424 - Commented lines will work after this story resolved
		// Verify Contact record linked
		// FieldSet contactFullName = new FieldSet();
		// contactFullName.put("name", myContact.getRecordIdentifier());
		// contactSubpanel.verify(1, contactFullName, true);
		contactSubpanel.getDetailField(1, "fullName").assertContains(myContact.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("selectReports() complete.");
	}

	@Test
	public void clickLink() throws Exception {
		VoodooUtils.voodoo.log.info("Running clickLink()...");

		TaskRecord myTask = (TaskRecord) sugar().tasks.api.create();

		myAccount.navToRecord();
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSub.scrollIntoViewIfNeeded(false);
		taskSub.linkExistingRecord(myTask);
		taskSub.clickLink(myTask.getRecordIdentifier(), 1);
		sugar().tasks.recordView.getDetailField("subject").assertEquals(myTask.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info("clickLink() complete.");
	}

	@Test
	public void countRows() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		TaskRecord myTask = (TaskRecord) sugar().tasks.api.create();
		myAccount.navToRecord();

		// Verify no record count
		StandardSubpanel callsSub = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		int row = callsSub.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		// Verify 1 record count
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSub.linkExistingRecord(myTask);
		row = taskSub.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);
		taskSub.getControl("count").getChildElement("span", "css", " span.count").assertEquals("(" + row + ")", true);

		// Verify no record
		taskSub.unlinkRecord(1);
		row = taskSub.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);

		VoodooUtils.voodoo.log.info("countRows() test complete");
	}

	@Test
	public void editRecordStandard() throws Exception {
		VoodooUtils.voodoo.log.info("Running editRecordStandard()...");

		FieldSet contactData = new FieldSet();
		contactData.put("relAccountName", "Aperture Laboratories");

		myContact = (ContactRecord)sugar().contacts.api.create();
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.setFields(contactData);
		sugar().alerts.getWarning().cancelAlert();
		sugar().contacts.recordView.save();

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		FieldSet editData = new FieldSet();
		editData.put("firstName", "Edited First Name");
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.expandSubpanel();
		contactSubpanel.editRecord(1, editData);
		editData.clear();
		editData.put("firstName", "Edited First Name");
		// TODO: VOOD-1424
		contactSubpanel.verify(1, editData, true);

		VoodooUtils.voodoo.log.info("editRecordStandard() complete.");
	}

	@Test
	public void editRecordBwc() throws Exception {
		VoodooUtils.voodoo.log.info("Running editRecordBwc()...");

		// TODO: VOOD-444: Support creating relationships via API.
		sugar().quotes.create();

		myAccount.navToRecord();
		StandardSubpanel quotesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get("Quotes (Bill To)");
		quotesSubpanel.scrollIntoViewIfNeeded(false);
		VoodooUtils.waitForReady();
		quotesSubpanel.click();
		quotesSubpanel.editRecord(1);

		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("The method editRecord did not place us on the EditView of the Quotes Record.",
				sugar().quotes.editView.getControl("cancelButton").queryVisible());
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("editRecordBwc() complete.");
	}

	@Test
	public void relateTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running relateTest()...");

		LeadRecord myLead = (LeadRecord)sugar().leads.api.create();

		myContact = (ContactRecord)sugar().contacts.api.create();
		myContact.navToRecord();

		StandardSubpanel leadSub = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSub.linkExistingRecord(myLead);

		myLead.navToRecord();

		VoodooUtils.voodoo.log.info("relateTest() complete.");
	}

	@Test
	public void unlinkRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Running unlinkRecord()...");

		myAccount = (AccountRecord)sugar().accounts.api.create();
		TaskRecord myTask = (TaskRecord)sugar().tasks.api.create();

		myAccount.navToRecord();
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get("Tasks");
		taskSub.linkExistingRecord(myTask);

		taskSub.unlinkRecord(1);
		Assert.assertTrue("The Task subpanel is not empty!", taskSub.isEmpty());

		VoodooUtils.voodoo.log.info("unlinkRecord() complete.");
	}

	@Test
	public void verifylinkExistingRecords() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifylinkExistingRecords()...");

		FieldSet lead1 = new FieldSet(), lead2 = new FieldSet();

		LeadRecord myLead1 = (LeadRecord)sugar().leads.api.create();
		lead1.put("fullName", myLead1.getRecordIdentifier());

		// custom lead
		lead2.put("firstName", "Test");
		lead2.put("lastName", "Lead");
		LeadRecord myLead2 = (LeadRecord)sugar().leads.api.create(lead2);
		lead2.clear();
		lead2.put("fullName", myLead2.getRecordIdentifier());

		// Navigate to accounts recordview
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Leads subpanel
		StandardSubpanel leadSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);

		// existing leads (i.e lead1 and lead2)
		ArrayList<Record> linkRecords = new ArrayList<Record>();
		linkRecords.add(myLead1);
		linkRecords.add(myLead2);
		leadSubpanel.linkExistingRecords(linkRecords);

		// TODO: VOOD-1673, VOOD-1828 - reordering issue records on CI (date/time override value does not work here for subpanels)
		leadSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1424
		// Verify lead1 and lead2
		leadSubpanel.verify(1, lead2, true);
		leadSubpanel.verify(2, lead1, true);

		VoodooUtils.voodoo.log.info("Completed verifylinkExistingRecords()...");
	}

	@Test
	public void verifyKBlinkExistingRecords() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyKBlinkExistingRecords()...");

		ArrayList<Record> linkRecords = new ArrayList<Record>();
		ArrayList<FieldSet> fieldSets = new ArrayList<FieldSet>();
		FieldSet kb = new FieldSet();

		kb.put("name", "test1kb");
		linkRecords.add((KBRecord) sugar().knowledgeBase.api.create(kb));
		fieldSets.add(kb);
		kb.clear();
		kb.put("name", "test2kb");
		linkRecords.add((KBRecord) sugar().knowledgeBase.api.create(kb));
		fieldSets.add(kb);
		kb.clear();
		kb.put("name", "test3kb");
		linkRecords.add((KBRecord) sugar().knowledgeBase.api.create(kb));
		fieldSets.add(kb);
		kb.clear();

		sugar().cases.api.create();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Open a Case >> KB subpanel.
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// KB subpanel
		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);

		// Linking Kb records
		kbSubpanel.linkExistingRecords(linkRecords);

		// Verify kbSubpanel
		kbSubpanel.verify(1, fieldSets.get(0), true);
		kbSubpanel.verify(2, fieldSets.get(1), true);
		kbSubpanel.verify(3, fieldSets.get(2), true);

		VoodooUtils.voodoo.log.info("Completed verifyKBlinkExistingRecords()...");
	}

	@Test
	public void verifygetHeader() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifygetHeader()...");
		sugar().accounts.navToListView();
		myAccount.navToRecord();

		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get("Tasks");
		taskSub.expandSubpanel();

		Assert.assertTrue("Get Header ",!taskSub.getHeaders().isEmpty());
		VoodooUtils.voodoo.log.info("debug" + taskSub.getHeaders());

		taskSub.sortBy("headerName", false);
		taskSub.sortBy("headerStatus", false);
		taskSub.sortBy("headerContactname", true);
		taskSub.sortBy("headerDatestart", false);
		taskSub.sortBy("headerDatedue", false);
		taskSub.sortBy("headerAssignedusername", false);

		taskSub.removeHeader("name");
		VoodooUtils.voodoo.log.info("debug" + taskSub.getHeaders());
		Assert.assertTrue("Get Header after remove ", !taskSub.getHeaders().contains("name"));
		VoodooUtils.voodoo.log.info("debug" + taskSub.getHeaders());

		VoodooUtils.voodoo.log.info("Completed verifygetHeader()...");
	}

	@Test
	public void verifyHeaderAllModules() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyHeaderAllModules()...");

		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);

		sugar().accounts.navToListView();
		myAccount.navToRecord();

		StandardSubpanel callsSub = sugar().accounts.recordView.subpanels.get("Calls");
		callsSub.expandSubpanel();

		Assert.assertTrue("Get Header Calls",!callsSub.getHeaders().isEmpty());

		callsSub.sortBy("headerName", false);
		callsSub.sortBy("headerStatus", false);
		callsSub.sortBy("headerDatestart", false);
		callsSub.sortBy("headerDateend", false);
		callsSub.sortBy("headerAssignedusername", false);

		StandardSubpanel casesSub = sugar().accounts.recordView.subpanels.get("Cases");
		casesSub.expandSubpanel();

		Assert.assertTrue("Get Header Case",!casesSub.getHeaders().isEmpty());

		casesSub.sortBy("headerCasenumber", false);
		casesSub.sortBy("headerName", false);
		casesSub.sortBy("headerStatus", false);
		casesSub.sortBy("headerPriority", false);
		casesSub.sortBy("headerDateentered", false);
		casesSub.sortBy("headerAssignedusername", false);

		StandardSubpanel leadsSub = sugar().accounts.recordView.subpanels.get("Leads");
		leadsSub.expandSubpanel();

		Assert.assertTrue("Get Header Leads",!leadsSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		leadsSub.sortBy("headerFullname", false);
		leadsSub.sortBy("headerReferedby", false);
		leadsSub.sortBy("headerLeadsource", false);
		leadsSub.sortBy("headerPhonework", false);
		leadsSub.sortBy("headerEmail", false);
		leadsSub.sortBy("headerAssignedusername", false);

		StandardSubpanel meetingSub = sugar().accounts.recordView.subpanels.get("Meetings");
		meetingSub.expandSubpanel();

		Assert.assertTrue("Get Header meetingSub",!meetingSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		meetingSub.sortBy("headerName", false);
		meetingSub.sortBy("headerStatus", false);
		meetingSub.sortBy("headerDatestart", false);
		meetingSub.sortBy("headerDateend", false);
		meetingSub.sortBy("headerAssignedusername", false);

		StandardSubpanel notesSub = sugar().accounts.recordView.subpanels.get("Notes");
		notesSub.expandSubpanel();

		Assert.assertTrue("Get Header Notes",!notesSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		notesSub.sortBy("headerName", false);
		notesSub.sortBy("headerDatemodified", false);
		notesSub.sortBy("headerDateentered", false);
		notesSub.sortBy("headerAssignedusername", false);

		StandardSubpanel oppSub = sugar().accounts.recordView.subpanels.get("Opportunities");
		oppSub.expandSubpanel();

		Assert.assertTrue("Get Header Opportunities",!oppSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		oppSub.sortBy("headerName", false);
		oppSub.sortBy("headerSalesstatus", false);
		oppSub.sortBy("headerDateclosed", false);
		oppSub.sortBy("headerAmount", false);
		oppSub.sortBy("headerAssignedusername", false);

		StandardSubpanel revSub = sugar().accounts.recordView.subpanels.get("RevenueLineItems");
		revSub.expandSubpanel();

		Assert.assertTrue("Get Header RevenueLineItems",!revSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		revSub.sortBy("headerName", false);
		revSub.sortBy("headerSalesstage", false);
		revSub.sortBy("headerDateclosed", false);
		revSub.sortBy("headerWorstcase", false);
		revSub.sortBy("headerLikelycase", false);
		revSub.sortBy("headerBestcase", false);
		revSub.sortBy("headerQuotename", false);

		StandardSubpanel accountSub = sugar().accounts.recordView.subpanels.get("Accounts");
		accountSub.expandSubpanel();

		Assert.assertTrue("Get Header Account ",!accountSub.getHeaders().isEmpty());

		accountSub.sortBy("headerName", false);
		accountSub.sortBy("headerBillingaddresscity", false);
		accountSub.sortBy("headerBillingaddresscountry", false);
		accountSub.sortBy("headerPhoneoffice", false);

		StandardSubpanel contactsSub = sugar().accounts.recordView.subpanels.get("Contacts");
		contactsSub.expandSubpanel();

		Assert.assertTrue("Get Header Contacts", !contactsSub.getHeaders().isEmpty());

		contactsSub.sortBy("headerFullname", false);
		contactsSub.sortBy("headerPrimaryaddresscity", false);
		contactsSub.sortBy("headerPrimaryaddressstate", false);
		contactsSub.sortBy("headerEmail", false);
		contactsSub.sortBy("headerPhonework", false);

		StandardSubpanel emailsSub = sugar().accounts.recordView.subpanels.get("Emails");
		emailsSub.expandSubpanel();

		Assert.assertTrue("Get Header Emails",!emailsSub.getHeaders().isEmpty());

		VoodooUtils.waitForReady();
		emailsSub.sortBy("headerName", false);
		emailsSub.sortBy("headerStatus", false);
		emailsSub.sortBy("headerDateentered", false);
		emailsSub.sortBy("headerDatemodified", false);
		emailsSub.sortBy("headerAssignedusername", false);

		StandardSubpanel bugsSub = sugar().accounts.recordView.subpanels.get("Bugs");
		bugsSub.expandSubpanel();

		Assert.assertTrue("Get Header Bugs",!bugsSub.getHeaders().isEmpty());

		bugsSub.sortBy("headerBugnumber", false);
		bugsSub.sortBy("headerName", false);
		bugsSub.sortBy("headerStatus", false);
		bugsSub.sortBy("headerType", false);
		bugsSub.sortBy("headerPriority", false);
		VoodooUtils.waitForReady();
		bugsSub.sortBy("headerAssignedusername", false);

		StandardSubpanel quotedLineSub = sugar().accounts.recordView.subpanels.get("Products");
		quotedLineSub.expandSubpanel();

		Assert.assertTrue("Get Header QuotedLineItems",!quotedLineSub.getHeaders().isEmpty());

		quotedLineSub.sortBy("headerName", false);
		quotedLineSub.sortBy("headerStatus", false);
		quotedLineSub.sortBy("headerAccountname", false);
		quotedLineSub.sortBy("headerContactname", false);
		quotedLineSub.sortBy("headerDatepurchased", false);
		quotedLineSub.sortBy("headerDiscountprice", false);
		quotedLineSub.sortBy("headerDatesupportexpires", false);

		StandardSubpanel contractSub = sugar().accounts.recordView.subpanels.get("Contracts");
		contractSub.expandSubpanel();

		Assert.assertTrue("Get Header Contracts ",!contractSub.getHeaders().isEmpty());

		contractSub.sortBy("headerName", false);
		contractSub.sortBy("headerStartdate", false);
		contractSub.sortBy("headerEnddate", false);
		contractSub.sortBy("headerStatus", false);
		contractSub.sortBy("headerTotalcontractvalue", false);

		StandardSubpanel docSub = sugar().accounts.recordView.subpanels.get("Documents");
		docSub.expandSubpanel();

		Assert.assertTrue("Get Header Documents ",!docSub.getHeaders().isEmpty());

		docSub.sortBy("headerDocumentname", false);
		docSub.sortBy("headerFilename", false);
		docSub.sortBy("headerCategoryid", false);
		docSub.sortBy("headerDoctype", false);
		docSub.sortBy("headerStatusid", false);
		docSub.sortBy("headerActivedate", false);

		VoodooUtils.voodoo.log.info("Completed verifyHeaderAllModules()...");
	}

	public void cleanup() throws Exception {}
}