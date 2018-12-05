package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_18104 extends SugarTest {
	DataSource caseNameAndStatus = new DataSource();
	AccountRecord acc;

	public void setup() throws Exception {
		caseNameAndStatus = testData.get(testName);

		// Create an Account
		acc = (AccountRecord)sugar().accounts.api.create();

		// Create 6 cases with 6 different statuses 
		sugar().cases.api.create(caseNameAndStatus);
		sugar().login();

		// Associating all the six cases to the Account using Mass Update.
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("selectAllCheckbox").click();
		sugar().cases.listView.getControl("actionDropdown").click();
		sugar().cases.listView.massUpdate();
		sugar().cases.massUpdate.getControl("massUpdateField02").set("Account Name");
		sugar().cases.massUpdate.getControl("massUpdateValue02").set(acc.getRecordIdentifier());
		sugar().cases.massUpdate.update();
	}

	/**
	 * Verify Cases summary dashlet in Account RHS pane
	 * @throws Exception
	 */
	@Test
	public void Accounts_18104_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		int totalNumberOfCases = caseNameAndStatus.size();
		acc.navToRecord();
		StandardSubpanel casesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);

		// Click on Show More in order to view the 6th record
		casesSubpanel.expandSubpanel();
		casesSubpanel.showMore();
		VoodooUtils.waitForReady();

		// Count number of Cases in the Subpanel
		int numberOfCasesInSubpanel = casesSubpanel.countRows();

		// Asserting that all the 6 cases got associated with the account
		Assert.assertTrue("No cases found in the subpanel",numberOfCasesInSubpanel == totalNumberOfCases);

		// Toggle to My Dashboard
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if (!dashboardTitle.getText().contains("My Dashboard"))
			sugar().dashboard.chooseDashboard("My Dashboard");
		VoodooUtils.waitForReady();

		// TODO: VOOD-670
		//Assert the visibility of the Cases Summary Dashlet
		new VoodooControl("div", "css", "div[data-voodoo-name='casessummary']").assertVisible(true);

		// Verify Summary number is the same as the total number of Cases listed under Cases subpanel.
		int summaryCountInDashlet = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='Summary'] .count").getText());

		Assert.assertTrue("The Case count does not match",summaryCountInDashlet==numberOfCasesInSubpanel);

		// Calculated the number of Opened and Closed Cases in the Cases Summary Dashlet
		int openCasesCountInDashlet=0,closedCasesCountInDashlet=0,newCase=0,pendingCase=0,assignedCase=0;
		pendingCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='Pending Input'] .count").getText().toString().trim());
		assignedCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='Assigned'] .count").getText().toString().trim());	
		newCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='New'] .count").getText().toString().trim());
		openCasesCountInDashlet = pendingCase + assignedCase + newCase;
		closedCasesCountInDashlet = summaryCountInDashlet - openCasesCountInDashlet;

		// Calculated the number of Opened and Closed Cases in the Cases Subpanel
		int openCasesCountInSubpanel=0,closedCasesCountInSubpanel=0;
		for(int i=1; i<=numberOfCasesInSubpanel; i++){
			String statusText = casesSubpanel.getDetailField(i, "status").getText().trim();
			if (statusText.equals(caseNameAndStatus.get(0).get("status")) || statusText.equals(caseNameAndStatus.get(1).get("status")) || statusText.equals(caseNameAndStatus.get(2).get("status")) ){
				openCasesCountInSubpanel++;
			}
			else if (statusText.equals(caseNameAndStatus.get(3).get("status")) || statusText.equals(caseNameAndStatus.get(4).get("status")) || statusText.equals(caseNameAndStatus.get(5).get("status"))) {
				closedCasesCountInSubpanel++;     				
			}
		}

		// Verify number of open cases status (New, Assigned, Pending Input) listed in dashlet has the same number that listed under Cases subpanel.
		Assert.assertTrue("The Open Cases Count in Dashlet and Subpanel does not Match.", openCasesCountInDashlet==openCasesCountInSubpanel);

		// Verify total number of Closed Cases (Closed, Rejected, Duplicate) listed in dashlet has the same number that listed under Cases subpanel.
		Assert.assertTrue("The Closed Cases Count in Dashlet and Subpanel does not Match", closedCasesCountInDashlet==closedCasesCountInSubpanel);

		//  Inline edit one case record as to change status to different status and Save (Here, Changed Rejected to Assigned)
		for(int j=1;j<=numberOfCasesInSubpanel;j++){
			if((casesSubpanel.getDetailField(j, "status").getText().trim()).equals(caseNameAndStatus.get(4).get("status"))){
				casesSubpanel.editRecord(j);
				casesSubpanel.getEditField(j, "status").set(caseNameAndStatus.get(1).get("status"));
				casesSubpanel.saveAction(j);
			}
		}

		// Refresh the Dashlet
		new VoodooControl("a", "css", "li:nth-child(2) div div > a").click();
		new VoodooControl("a", "css", "li:nth-child(2) li:nth-child(2) span a").click();
		VoodooUtils.waitForReady();

		// Re-Calculated the number of Opened and Closed Cases in the Cases Summary Dashlet
		pendingCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='Pending Input'] .count").getText().toString().trim());
		assignedCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='Assigned'] .count").getText().toString().trim());	
		newCase = Integer.parseInt(new VoodooControl("span", "css", "div[data-voodoo-name='casessummary'] a[data-original-title='New'] .count").getText().toString().trim());
		openCasesCountInDashlet = pendingCase + assignedCase + newCase;
		closedCasesCountInDashlet = summaryCountInDashlet - openCasesCountInDashlet;
		String newCaseSubjectSubpanel = "";

		// Re-Calculated the number of Opened and Closed Cases in the Cases Subpanel
		int updatedOpenCasesCountInSubpanel=0,updatedClosedCasesCountInSubpanel=0;
		for(int k=1; k<=numberOfCasesInSubpanel; k++){
			String statusText = casesSubpanel.getDetailField(k, "status").getText().trim();
			if (statusText.equals(caseNameAndStatus.get(0).get("status")) || statusText.equals(caseNameAndStatus.get(1).get("status")) || statusText.equals(caseNameAndStatus.get(2).get("status")) ){
				updatedOpenCasesCountInSubpanel++;
				if (statusText.equals(caseNameAndStatus.get(0).get("status"))){
					newCaseSubjectSubpanel = casesSubpanel.getDetailField(k, "name").getText().trim();
				}
			}
			else if (statusText.equals(caseNameAndStatus.get(3).get("status")) || statusText.equals(caseNameAndStatus.get(4).get("status")) || statusText.equals(caseNameAndStatus.get(5).get("status"))) {
				updatedClosedCasesCountInSubpanel++;     				
			}
		}

		// Re-Verify total number of open cases status (New, Assigned, Pending Input) listed in dashlet has the same number that listed under Cases subpanel.
		Assert.assertTrue("The Open Cases Count in Dashlet and Subpanel does not Match.", openCasesCountInDashlet==updatedOpenCasesCountInSubpanel);

		// Re-Verify total number of Closed Cases (Closed, Rejected, Duplicate) listed in dashlet has the same number that listed under Cases subpanel.
		Assert.assertTrue("The Closed Cases Count in Dashlet and Subpanel does not Match", closedCasesCountInDashlet==updatedClosedCasesCountInSubpanel);

		// Clicking the New tab under the Cases Summary Dashlet
		new VoodooControl("a", "css", ".cases-summary-wrapper a[data-original-title='New']").click();

		// Getting the Case Subject i.e with status New
		VoodooControl subjectLink = new VoodooControl("a", "css", "#myTabContent .tab-pane.active a");
		String newCaseSubjectDashlet = subjectLink.getText().trim();

		// Asserting the visibility and text of the SubjectLink.
		subjectLink.assertVisible(true);
		Assert.assertTrue("The SubjectLink text does not match.", newCaseSubjectDashlet.equals(newCaseSubjectSubpanel));
		System.out.println(newCaseSubjectDashlet+ "    and   "  +newCaseSubjectSubpanel);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}