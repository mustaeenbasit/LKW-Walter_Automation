package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Calls_20089 extends SugarTest {
	ContactRecord myCon;
	LeadRecord myLead;
	AccountRecord myAcc;
	ArrayList<Record> myCalls;
	DataSource ds, ds1;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		ds1= testData.get(testName+"_1");
		myCalls = sugar.calls.api.create(ds);
		myLead = (LeadRecord) sugar.leads.api.create();
		myCon = (ContactRecord) sugar.contacts.api.create();
		myAcc =  (AccountRecord) sugar.accounts.api.create();	
		int i=0;
		for(Record mycall:myCalls) {			
			mycall.navToRecord();
//			sugar.calls.detailView.edit();
			VoodooUtils.focusFrame("bwc-frame");
//			sugar.calls.editView.getEditField("relatedToParentType").set(ds1.get(i).get("module"));
			i=i+1;
			// TODO VOOD-764
			new VoodooControl("button", "id", "btn_parent_name").click();
			VoodooUtils.focusWindow("SugarCRM");
			new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 a").click();
			VoodooUtils.focusWindow(0);
//			sugar.calls.editView.save();
		}		
		sugar.calls.navToListView();
	}

	/**
	 * Advanced search does not search on "related to" field if not specified.
	 * 
	 * @throws Exception
	 */
	@Ignore ("This test is written as per old BWC Calls module. Story of test need to be updated for SideCar Module")
	@Test
	public void Calls_20089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		VoodooUtils.focusFrame("bwc-frame");
		sugar.calls.listView.getControl("advancedSearchLink").click();
		
		// TODO VOOD-975
		new VoodooControl("input", "id", "search_form_clear_advanced").click();
		new VoodooControl("input", "id", "current_user_only_advanced").click();
		new VoodooControl("input", "id", "search_form_submit_advanced").click();
		
		for(int i=1;i<=ds.size();i++) {
			String link = String.format("link%02d", i);
			sugar.calls.listView.getControl(link).assertVisible(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}