package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20646 extends SugarTest {
	DataSource ds;
	
	VoodooControl myEmailMenuCaret, myEmailTemplateList, myEmailTemplateCreate, myEmailName, myEmailType, myEmailDesc, myEmailSub, myEmailbody;
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		
		myEmailMenuCaret = new VoodooControl("button", "css", "li[data-module='Emails'] button[data-toggle='dropdown']");
		myEmailTemplateList = new VoodooControl("a", "css", "li[data-module='Emails'] div.dropdown-menu a[data-navbar-menu-item='LNK_EMAIL_TEMPLATE_LIST']");
		myEmailTemplateCreate = new VoodooControl("a", "css", "li[data-module='Emails'] div.dropdown-menu a[data-navbar-menu-item='LNK_NEW_EMAIL_TEMPLATE']");
		myEmailName = new VoodooControl("input", "id", "name");
		myEmailType = new VoodooControl("option", "css", "select[name='type'] option[value='email']");
		myEmailDesc = new VoodooControl("textarea", "id", "description");
		myEmailSub = new VoodooControl("textarea", "id", "subjectfield");
		myEmailbody = new VoodooControl("body", "id", "tinymce");
		sugar.login();		
	}

	/**
	 * Check Email Template can be duplicated successfully
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToModule("Emails");
		myEmailMenuCaret.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// First Create a new Template
		myEmailTemplateCreate.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// name
		myEmailName.set(ds.get(0).get("name"));
		
		// type
		myEmailType.click();
		
		// description
		myEmailDesc.set(ds.get(0).get("description"));

		// subject
		myEmailSub.set(ds.get(0).get("subject"));

		// body
		VoodooUtils.focusFrame("body_text_ifr");
		myEmailbody.set(ds.get(0).get("body"));

		// Now Save the record
		VoodooUtils.focusDefault();

		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "SAVE").click();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusDefault();

		// Now Start
		// First Check Cancel part
		for (int i=1; i<ds.size();i++) {
			// Navigate to an existing Template
			myEmailMenuCaret.click();
			sugar.alerts.waitForLoadingExpiration();
			myEmailTemplateList.click();
	
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'"+ds.get(0).get("name")+"')]/td[3]/b/a").click();
			
			// Copy the existing Template
			VoodooUtils.focusDefault();

			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("span", "css", "#detail_header_action_menu span").click();
			new VoodooControl("li", "css", "ul.subnav.ddopen li:nth-of-type(1)").click();
			
			// name
			myEmailName.set(ds.get(i).get("name"));
			
			// type
			myEmailType.click();
			
			// description
			myEmailDesc.set(ds.get(i).get("description"));
	
			// subject
			myEmailSub.set(ds.get(i).get("subject"));
	
			// body
			VoodooUtils.focusFrame("body_text_ifr");
			myEmailbody.set(ds.get(i).get("body"));
	
			// Now do not Save the record
			VoodooUtils.focusDefault();
			
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "id", "CANCEL").click();
			
			// If cancelled, then this entry should not appear in table
			new VoodooControl("table", "css", "div.listViewBody table.list.view").assertContains(ds.get(i).get("name"), false);

			VoodooUtils.focusDefault();
		}
		
		// Now Check Save
		VoodooUtils.focusDefault();
		
		for (int i=1; i<ds.size();i++) {
			// Navigate to an existing Template
			myEmailMenuCaret.click();
			sugar.alerts.waitForLoadingExpiration();
			myEmailTemplateList.click();
	
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("tr", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'"+ds.get(0).get("name")+"')]/td[3]/b/a").click();
			
			// Copy the existing Template
			VoodooUtils.focusDefault();

			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("span", "css", "#detail_header_action_menu span").click();
			new VoodooControl("li", "css", "ul.subnav.ddopen li:nth-of-type(1)").click();
			
			// name
			myEmailName.set(ds.get(i).get("name"));
			
			// type
			myEmailType.click();
			
			// description
			myEmailDesc.set(ds.get(i).get("description"));
	
			// subject
			myEmailSub.set(ds.get(i).get("subject"));
	
			// body
			VoodooUtils.focusFrame("body_text_ifr");
			myEmailbody.set(ds.get(i).get("body"));
	
			// Now Save the record
			VoodooUtils.focusDefault();

			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "id", "SAVE").click();
			sugar.alerts.waitForLoadingExpiration();

			// Subject and Name truncated to 256 characters
			// By design, Subject and Name contains @ in 257th position and onwards
			// Name
			new VoodooControl("td", "css", "div.detail.view table tr:nth-of-type(2) td:nth-of-type(2)").assertContains("@", false);
			// Subject
			new VoodooControl("td", "css", "div.detail.view table tr:nth-of-type(4) td:nth-of-type(2)").assertContains("@", false);
			
			VoodooUtils.focusDefault();
		}

		// Check if the newly save records exist
		VoodooUtils.focusDefault();
		
		new VoodooControl("button", "css", "li[data-module='Emails'] button[data-toggle='dropdown']").click();
		sugar.alerts.waitForLoadingExpiration();
		myEmailTemplateList.click();

		VoodooUtils.focusFrame("bwc-frame");
		
		for (int i=1; i<ds.size();i++) {
			new VoodooControl("table", "css", "div.listViewBody table.list.view").assertContains(ds.get(i).get("name"), true);
		}

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}