package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29633 extends SugarTest {
	int recordsCount = 0;
	String qaUserName = "";
	FieldSet testFS = new FieldSet();

	public void setup() throws Exception {
		// Login as admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a user
		UserRecord chrisUser = (UserRecord) sugar().users.create();

		// Logout admin
		sugar().logout();

		// Login as Max (chrisUser), assign records to  Sally (qauser)
		chrisUser.login();

		// Create 3 KB records , assign to  Sally (qauser)
		sugar().knowledgeBase.navToListView();
		qaUserName = sugar().users.getQAUser().get("userName");

		// 1. Max create a non-Approved KB1. assign to Sally (Author), leave "Approved By" as blank.
		createKB(qaUserName,"");
		// 2. Max create a non-Approved KB2, assign to Sally, "Approved By" is Sally too.
		createKB(qaUserName, qaUserName);
		// 3. Max create a non-Approved KB3, leave Author and "Approved By" as blank.
		createKB("", "");

		// Logout admin
		sugar().logout();
	}
	/**
	 * Verify that Approved By is automatically assigned by the user who has changed status to Approved
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29633_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// 1. Sally (qauser) user log in and open above KB.
		sugar().login(sugar().users.getQAUser());

		// 2. Sally change status=Approved.
		// 3. Check "Approved By" field.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		editAndAssertKBApprovedBy(qaUserName);

		// 4. Remove above KB and re-create same set KB  assign to Sally (qauser)
		// TODO: VOOD-2050 Exception after deleting KB record(s) from list view action dropdown. OK manually
		sugar().knowledgeBase.navToListView();
		testFS = testData.get(testName).get(0);
		recordsCount = Integer.parseInt(testFS.get("recordCount"));
		for (int i = 0; i < recordsCount; i++) {
			sugar().knowledgeBase.listView.deleteRecord(1);
			sugar().alerts.getWarning().confirmAlert();
		}

		// Re-create same set KB  assign to Sally (qauser)
		createKB(qaUserName,"");
		createKB(qaUserName, qaUserName);
		createKB("", "");
		sugar().logout();

		// 5. Chris (admin) user log in and open above KB.
		// Login as admin
		sugar().login();

		// 6. Chris user change status=Approved.
		// 7. Check "Approved By" field. 
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		editAndAssertKBApprovedBy(testFS.get("adminName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	// Create KB record with custom assignedTo, approvedBy fields
	private void createKB (String assignedTo,String approvedBy) throws Exception {

		// Create KB record
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.showMore();

		// Set Assigned To
		// TODO: VOOD-806 Click on Clear Value (x) button
		if (assignedTo != "") 
			sugar().knowledgeBase.createDrawer.getEditField("relAssignedTo").set(assignedTo);
		else 
			new VoodooControl("abbr", "css", ".fld_assigned_user_name.edit abbr").click();

		// Set Approved To. No else condition since approvedBy is blank by default
		if (approvedBy != "") 
			sugar().knowledgeBase.createDrawer.getEditField("approvedBy").set(approvedBy);

		sugar().knowledgeBase.createDrawer.save();
	}

	// Update KB record and Verify updated field 
	private void editAndAssertKBApprovedBy (String approvedBy) throws Exception {

		for (int i = 0; i < recordsCount; i++) {
			// Update KB record status
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.getEditField("status").set(testFS.get("statusApproved"));
			sugar().knowledgeBase.recordView.save();
			sugar().alerts.getWarning().confirmAlert();

			// Verify updated field
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getDetailField("approvedBy").assertEquals(approvedBy, true);
			sugar().knowledgeBase.recordView.gotoNextRecord();
		}
	}

	public void cleanup() throws Exception {}
}