package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25740 extends SugarTest {
	VoodooControl accountsSubPanelCtrl;
	VoodooControl relationshipCtrl;
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * 5.1_Studio_Add a "One to Many" type relationship with non-default sub-panel 
	 * @throws Exception
	 */
	@Test
	public void Studio_25740_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		relationshipCtrl = new VoodooControl("td", "id", "relationshipsBtn");
		relationshipCtrl.click();

		// TODO: VOOD-938
		// Add relationship and save
		new VoodooControl("input", "css", "input[name=addrelbtn]").click();
		new VoodooControl("select", "css", "#relationship_type_field option:nth-of-type(2)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rhs_mod_field option:nth-of-type(6)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rhs_subpanel option:nth-of-type(2)").click();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.waitForReady();
		
		// below code is needed, as on saving relationship, error occurs: Failed to retrieve data
		new VoodooControl("input", "css", "input[value=Studio]").click();
		accountsSubPanelCtrl.click();
		relationshipCtrl.click();

		// verify new relationship saved in list
		String newRelationshipStr = String.format("//*[@id='relGrid']/div[3]/table/tbody[2]/tr[contains(.,'%s')]", customData.get("relationship_name"));
		String relationNameStr = String.format("%s//td[1]//div",newRelationshipStr);
		String primaryModuleStr = String.format("%s//td[2]//div",newRelationshipStr);
		String relationTypeStr = String.format("%s//td[3]//div",newRelationshipStr);
		String relatedModuleStr = String.format("%s//td[4]//div",newRelationshipStr);
		// xpath used for assert values, becoz new relationship is random in order
		new VoodooControl("div", "xpath", relationNameStr).assertEquals(customData.get("relationship_name"), true);
		new VoodooControl("div", "xpath", primaryModuleStr).assertEquals(customData.get("primary_module"), true);
		new VoodooControl("div", "xpath", relationTypeStr).assertEquals(customData.get("relationship_type"), true);
		new VoodooControl("div", "xpath", relatedModuleStr).assertEquals(customData.get("related_module"), true);
		VoodooUtils.focusDefault();
		
		// Accounts module
		FieldSet newData = new FieldSet();
		newData.put("name", customData.get("account_name"));
		AccountRecord myAccount = (AccountRecord)sugar().accounts.create(newData);
		myAccount.navToRecord();

		// verifying the existence of second position of Cases subpanel
		new VoodooControl("div", "xpath", "//*[@id='content']/div/div/div[1]/div/div[3]/div[2]/div/div[@data-voodoo-name='Cases'][position()=2]").assertExists(true);
		
		// verifying Cases accounts default field and newly relate-field 
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).addRecord();
		new VoodooControl("div", "css", "#drawers > div > div > div.main-pane.span8 > div > div > div > div.record > div:nth-child(2) > div:nth-child(1) > div").assertEquals(customData.get("account_name_Lbl"), true);
		sugar().cases.createDrawer.getEditField("relAccountName").assertEquals(customData.get("account_name"), true);
		new VoodooControl("div", "css", "#drawers > div > div > div.main-pane.span8 > div > div > div > div.record > div:nth-child(5) > div:nth-child(2) > div").assertEquals(customData.get("account_related_field_lbl"), true);
		new VoodooControl("span", "css", ".fld_accounts_cases_1_name.edit div a span.select2-chosen").assertEquals(customData.get("account_name"), true);
		sugar().cases.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}