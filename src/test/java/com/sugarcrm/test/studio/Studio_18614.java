package com.sugarcrm.test.studio;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_18614 extends SugarTest {
	DataSource customData;
	VoodooControl fieldCtrl, descriptionCtrl, calculatedCtrl, saveFieldCtrl, studioFooterCtrl; 
	String subPanelStr;
	AccountRecord myAccount;
	FieldSet data;

	public void setup() throws Exception {
		customData = testData.get(testName+"_formula");
		data = testData.get(testName).get(0);
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio - module - field - description - edit formula
		sugar().admin.adminTools.getControl("studio").click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		descriptionCtrl = new VoodooControl("a", "id", "description");
		calculatedCtrl = new VoodooControl("input", "id", "calculated"); 
		saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		for(int i=0; i< customData.size(); i++){
			if(i==0){
				// Accounts
				subPanelStr = "studiolink_Accounts";
			}else if(i==1){
				// Contacts
				subPanelStr = "studiolink_Contacts";
			}else if(i==2){
				// Cases
				subPanelStr = "studiolink_Cases";
			}
			new VoodooControl("a", "id", subPanelStr).click();
			fieldCtrl.click();
			VoodooUtils.waitForReady();
			descriptionCtrl.click();
			VoodooUtils.waitForReady();
			calculatedCtrl.click();
			new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
			new VoodooControl("textarea", "css", "#formulaInput").set(customData.get(i).get("formula_field_description"));
			new VoodooControl("input", "id", "fomulaSaveButton").click();
			VoodooUtils.waitForReady();
			saveFieldCtrl.click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-999
			studioFooterCtrl.click();
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the SugarBean operations API 
	 * @throws Exception
	 */
	@Ignore("TR-4832")
	@Test
	public void Studio_18614_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link Accounts with Contacts module
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		String relAcc = myAccount.getRecordIdentifier();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(relAcc);
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Link Cases with Contact module
		sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural).addRecord();
		sugar().cases.createDrawer.getEditField("name").set(data.get("cases_name"));

		// verifying description in cases module (i.e Account Name)
		sugar().cases.createDrawer.getEditField("description").assertEquals(relAcc, true);
		sugar().cases.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.recordView.showMore();

		// verifying description in contacts module (i.e Accounts Name + Cases Name)
		String contactDesc = String.format("%s%s", relAcc,data.get("cases_name"));
		sugar().contacts.recordView.getDetailField("description").assertEquals(contactDesc, true);

		// verifying description in accounts module (i.e Case Name + Contacts description)
		// TODO: TR-4832 (Account description showing incorrect i.e case name + accounts name)
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		String accountDesc = String.format("%s%s",data.get("cases_name"),contactDesc);
		sugar().accounts.recordView.getDetailField("description").assertEquals(accountDesc, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}