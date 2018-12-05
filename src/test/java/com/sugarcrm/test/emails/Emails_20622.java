package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20622 extends SugarTest {
	DataSource dsEmail;
	FieldSet emailSetup;
	AccountRecord myAccount;
	VoodooControl emailComposeButtonCtrl,emailToCtrl,emailSubjectCtrl,emailBodyCtrl,emailsendCtrl,selectRelatedToCtrl;
	VoodooControl pickRelatedToRecCtrl,searchNameCtrl,submitForSearchCtrl,selectFirstResultRowCtrl;
	
	public void setup() throws Exception {
		dsEmail = testData.get(testName);
		emailSetup = testData.get(testName+"_emailsetup").get(0);
		myAccount = (AccountRecord) sugar.accounts.api.create();

		sugar.login();
		
		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup);
				
		// TODO: VOOD-672
		emailComposeButtonCtrl = new VoodooControl("button", "id", "composeButton");
		emailToCtrl = new VoodooControl("input", "css", "input[title='To']");
		emailSubjectCtrl = new VoodooControl("input", "css", "#emailtabs > div > div:nth-child(2) > div > div > div > div > div > div:nth-child(2) > form > table > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(6) > td.emailUIField > div > input");
		emailBodyCtrl = new VoodooControl("body", "id", "tinymce");
 		emailsendCtrl = new VoodooControl("button", "css", "#emailtabs > div > div:nth-child(2) > div > div > div > div > div > div:nth-child(2) > form > table > tbody > tr:nth-child(1) > th > table > tbody > tr > td:nth-child(1) > button:nth-child(1)");
 		
		selectRelatedToCtrl = new VoodooControl("select", "xpath", "//table[@class='list']/tbody/tr[1]/th/table/tbody/tr/td[2]/select");
 		pickRelatedToRecCtrl = new VoodooControl("button", "xpath", "//table[@class='list']/tbody/tr[1]/th/table/tbody/tr/td[3]/button");
		searchNameCtrl = new VoodooControl("input", "id", "name_advanced");
		submitForSearchCtrl = new VoodooControl("input", "id", "search_form_submit");
		selectFirstResultRowCtrl = new VoodooControl("a", "css", "body > table.list.view > tbody > tr:nth-of-type(3) > td:nth-child(1) > a");
 		
		// Go to Emails module
 		sugar.navbar.navToModule("Emails");
 		sugar.alerts.waitForLoadingExpiration();
 		VoodooUtils.focusFrame("bwc-frame");
 		
 		// Send 7 dummy mails
 		for (int i=0; i<dsEmail.size(); i++) {
 			emailComposeButtonCtrl.click();
	 		
 			emailToCtrl.set(dsEmail.get(i).get("to"));
 			emailSubjectCtrl.set(dsEmail.get(i).get("subject"));

 			// Select related to Dropdown - Accounts 
 			selectRelatedToCtrl.set("Account");
 			pickRelatedToRecCtrl.click();
 			VoodooUtils.focusWindow(1);
 			searchNameCtrl.set(myAccount.getRecordIdentifier());
 			submitForSearchCtrl.click();
 			selectFirstResultRowCtrl.click();
 			VoodooUtils.focusWindow(0);
 	 		VoodooUtils.focusFrame("bwc-frame");

 			VoodooUtils.focusFrame(1);
	 		emailBodyCtrl.set(dsEmail.get(i).get("body"));

	 		VoodooUtils.focusDefault();	
	 		VoodooUtils.focusFrame("bwc-frame");
	 		emailsendCtrl.click();
	 		VoodooUtils.pause(5000); // pause need here to send email.
 		}
	}

	/**
	 *  Verify that search in Emails module working properly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20622_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-1051
		// click on search tab  
		new VoodooControl("li", "css", "#searchTab").click();
		// click on "More" option in search window
		new VoodooControl("a", "css", "#advancedSearchTable > tbody > tr.toggleClass.visible-search-option > td:nth-child(1) > a").click();
		
		VoodooControl searchsubjectCtrl = new VoodooControl("input", "id", "searchSubject");
		VoodooControl searchemailFrom = new VoodooControl("input", "id", "searchFrom");
		VoodooControl searchemailTo = new VoodooControl("input", "id", "searchTo");
		VoodooControl inputdateFromCtrl= new VoodooControl("input", "id", "searchDateFrom");
		VoodooControl inputdateUntilCtrl= new VoodooControl("input", "id", "searchDateTo");
		VoodooControl attachmentCtrl= new VoodooControl("select", "id", "attachmentsSearch");
		VoodooControl relatedToCtrl= new VoodooControl("select", "id", "data_parent_type_search");
		VoodooControl assignedTo = new VoodooControl("input", "id", "assigned_user_name");
		VoodooControl inputSelectRecord = new VoodooControl("input", "id", "data_parent_name_search");
		VoodooControl searchResult = new VoodooControl("div", "css", "#emailGrid > div.yui-dt-bd");
		
		VoodooControl dateFromCtrl= new VoodooControl("img", "id", "searchDateFrom_trigger");
		VoodooControl todayDateCtl = new VoodooControl("a", "id", "callnav_today");
		VoodooControl dateToCtrl = new VoodooControl("img", "id", "searchDateTo_trigger");
		VoodooControl closeToDateCtrl = new VoodooControl("a", "css", "#container_searchDateTo_trigger > a");
		VoodooControl advSearchCtrl= new VoodooControl("input", "id", "advancedSearchButton");
		VoodooControl clearCtrl = new VoodooControl("input", "css", "#advancedSearchTable tr:nth-child(12) > td > input:nth-child(4)");
		
		for (int i=0; i<dsEmail.size(); i++) {
			searchsubjectCtrl.set(dsEmail.get(i).get("subject_to_assert"));
			searchemailFrom.set(dsEmail.get(0).get("email_from"));
			searchemailTo.set(dsEmail.get(i).get("to"));
			// inputdateFromCtrl.set(dsEmail.get(0).get("date_from")); // commented due to bug 'SC-2765' i.e. Backslash problem in CI.
			// inputdateUntilCtrl.set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy")); // commented due to bug 'SC-2765' i.e. Backslash problem in CI.
			assignedTo.set(dsEmail.get(0).get("assigned_to"));
			new VoodooControl("li", "css", ".yui-ac-highlight").waitForVisible();
			new VoodooControl("li", "css", ".yui-ac-highlight").click();
			attachmentCtrl.set("No");
			relatedToCtrl.set(sugar.accounts.moduleNameSingular);
			inputSelectRecord.set(myAccount.getRecordIdentifier());
			new VoodooControl("div", "css", "#advancedSearchForm_data_parent_name_search_results .yui-ac-content").waitForVisible();
			new VoodooControl("div", "css", "#advancedSearchForm_data_parent_name_search_results .yui-ac-content").click();
			
			advSearchCtrl.click();
			VoodooUtils.pause(3000);
			VoodooUtils.waitForAlertExpiration();
			
			// Verify, the emails that match the condition are displayed in the list.
			if (i==0 || i==1){
				searchResult.assertContains(dsEmail.get(0).get("subject"), true);
				searchResult.assertContains(dsEmail.get(1).get("subject"), true);
				searchResult.assertContains(dsEmail.get(2).get("subject"), true);
			}
			if (i==2 || i==4 || i==6){
				searchResult.assertContains(dsEmail.get(i-1).get("subject"), true);
				searchResult.assertContains(dsEmail.get(i).get("subject"), true);
			}
			if (i==3 || i==5){
				searchResult.assertContains(dsEmail.get(i).get("subject"), true);
			}
			clearCtrl.click();
			searchsubjectCtrl.assertContains("", true);
			searchemailFrom.assertContains("", true);
		}
		
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-1176
		// Change the Date Format in User Profile.
		sugar.navbar.navToProfile();
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.userPref.getControl("tab4").click();
		new VoodooControl("select", "css", "[name='dateformat']").click();
		new VoodooControl("option", "css", "[name='dateformat'] [value='Y/m/d']").click();
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		
		// Go to Emails module
 		sugar.navbar.navToModule("Emails");
 		sugar.alerts.waitForLoadingExpiration();
 		VoodooUtils.focusFrame("bwc-frame");
		
 		// TODO: VOOD-1051
		// click on search tab  
		new VoodooControl("li", "css", "#searchTab").click();
		// click on "More" option in search window
		new VoodooControl("a", "css", "#advancedSearchTable > tbody > tr.toggleClass.visible-search-option > td:nth-child(1) > a").click();
 			
		for (int i=0; i<dsEmail.size(); i++) {
			searchsubjectCtrl.set(dsEmail.get(i).get("subject_to_assert"));
			searchemailFrom.set(dsEmail.get(0).get("email_from"));
			searchemailTo.set(dsEmail.get(i).get("to"));
			
			// Select date from time selector.
			dateFromCtrl.click();
			new VoodooControl("a", "css", "#searchDateFrom_trigger_div_t tr:nth-child(1) a.calnav").click();
			new VoodooControl("input", "id", "searchDateFrom_trigger_div_nav_year").set(dsEmail.get(0).get("year_from"));
			new VoodooControl("button", "id", "searchDateFrom_trigger_div_nav_submit").click();
			new VoodooControl("td", "id", "searchDateFrom_trigger_div_t_cell17").click();
			
			// Select date from time selector.
			dateToCtrl.click();
			todayDateCtl.click();			
			closeToDateCtrl.click();
			
			assignedTo.set(dsEmail.get(0).get("assigned_to"));
			new VoodooControl("li", "css", ".yui-ac-highlight").waitForVisible();
			new VoodooControl("li", "css", ".yui-ac-highlight").click();
			attachmentCtrl.set("No");
			relatedToCtrl.set(sugar.accounts.moduleNameSingular);
			inputSelectRecord.set(myAccount.getRecordIdentifier());
			new VoodooControl("div", "css", "#advancedSearchForm_data_parent_name_search_results .yui-ac-content").waitForVisible();
			new VoodooControl("div", "css", "#advancedSearchForm_data_parent_name_search_results .yui-ac-content").click();

			advSearchCtrl.click();
			VoodooUtils.pause(3000);
			VoodooUtils.waitForAlertExpiration();
			
			// Verify, the emails that match the condition are displayed in the list.
			if (i==0 || i==1){
				searchResult.assertContains(dsEmail.get(0).get("subject"), true);
				searchResult.assertContains(dsEmail.get(1).get("subject"), true);
				searchResult.assertContains(dsEmail.get(2).get("subject"), true);
			}
			if (i==2 || i==4 || i==6){
				searchResult.assertContains(dsEmail.get(i-1).get("subject"), true);
				searchResult.assertContains(dsEmail.get(i).get("subject"), true);
			}
			if (i==3 || i==5){
				searchResult.assertContains(dsEmail.get(i).get("subject"), true);
			}
			
			// Click "Clear" button.
			clearCtrl.click();
			
			// Verify, the search conditions are cleared.
			searchsubjectCtrl.assertContains("", true);
			searchemailFrom.assertContains("", true);
		}
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
