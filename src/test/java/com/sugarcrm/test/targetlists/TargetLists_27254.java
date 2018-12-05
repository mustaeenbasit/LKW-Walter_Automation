package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27254 extends SugarTest {
	TargetListRecord myTargetListRecord;
	ContactRecord myContactRecord;
	FieldSet fs = new FieldSet(); 

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();
		myTargetListRecord = (TargetListRecord) sugar().targetlists.api.create();
		myContactRecord= (ContactRecord) sugar().contacts.api.create();

		// VOOD-643
		// navigate to report module 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		
		// click onRows and Columns Report
		new VoodooControl("td", "css", "#report_type_div > table > tbody > tr:nth-child(2) > td:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(1)").click();
		
		// click on Contacts module 
		new VoodooControl("table", "id", "Contacts").click();

		// select Contacts first name
		VoodooControl firstNameCtrl = new VoodooControl("tr", "id", "Contacts_first_name");
		firstNameCtrl.click();
		new VoodooControl("input", "css", "tr:nth-child(1) > td:nth-child(4) td:nth-child(1) > input[type='text']").set(myContactRecord.get("firstName"));
		
		// select Contacts last name
		VoodooControl lastNameCtrl = new VoodooControl("tr", "id", "Contacts_last_name");
		lastNameCtrl.click();

		new VoodooControl("input", "css", "tr:nth-child(2) > td:nth-child(4) td:nth-child(1) > input[type='text']").set(myContactRecord.get("lastName"));

		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		firstNameCtrl.click();
		lastNameCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "save_report_as").set(fs.get("reportName"));

		new VoodooControl("input", "id", "saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

	}

	/**
	 * Verify that add Contact from report in Targetlist
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_27254_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTargetListRecord.navToRecord();

		// TODO: VOOD-1147 -Need Lib support for sub panels drop down menu option "select from Reports"
		new VoodooControl("span", "css", "#content .layout_Contacts span.actions.btn-group.pull-right.panel-top  a  span").click();
		VoodooUtils.pause(100);
		new VoodooControl("li", "css", "[data-subpanel-link='contacts'] .pull-right ul.dropdown-menu li:nth-child(2)").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify "Contact Report1" is automatically show up in the list. "Contact' reports" is appearing in Filter header.  The filter define is like "Module is any of Contacts".  
		new VoodooControl("tr", "css", ".search-and-select tbody tr").assertVisible(true);
		new VoodooControl("span", "css", ".filter-view.search.layout_Reports span.choice-filter-label").assertEquals(fs.get("filterName"), true);

		// TODO: VOOD-999
		new VoodooControl("span", "css", "div[data-filter='field'] div a span.select2-chosen").assertEquals(fs.get("field"), true);
		new VoodooControl("span", "css", "div[data-filter='operator'] div a span.select2-chosen").assertEquals(fs.get("operator"), true);
		new VoodooControl("div", "css", "div[data-filter='value'] .fld_module div ul li div").assertEquals(fs.get("value"), true);
		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that Contact record appears in the sub panel of targetlist > Contacts 
		FieldSet targetsData = new FieldSet();
		targetsData.put("name", myContactRecord.getRecordIdentifier());
		targetsData.put("phoneWork", myContactRecord.get("phoneWork"));
		
		// Expanding Contacts subpanel before verification
		StandardSubpanel contactsSubpanel = sugar.targetlists.recordView.subpanels.get
				(sugar().contacts.moduleNamePlural);
		contactsSubpanel.expandSubpanel(); 
		sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural).verify(1, targetsData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}