package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25738 extends SugarTest {
	VoodooControl accountsSubPanelCtrl;
	VoodooControl relationshipCtrl;
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify delete a "One to One" type relationship works correctly 
	 * @throws Exception
	 */
	@Test
	public void Studio_25738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938		
		sugar().admin.adminTools.getControl("studio").click(); // studio
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		relationshipCtrl = new VoodooControl("td", "id", "relationshipsBtn");
		relationshipCtrl.click();

		// TODO: VOOD-938
		// Add relationship and save
		new VoodooControl("input", "css", "input[name=addrelbtn]").click();
		new VoodooControl("select", "css", "#relationship_type_field option:nth-of-type(1)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#rhs_mod_field option:nth-of-type(6)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.waitForReady();
		
		// below code is needed, as on saving relationship, error occurs: Failed to retrieve data
		new VoodooControl("input", "css", "input[value=Studio]").click();
		accountsSubPanelCtrl.click();
		relationshipCtrl.click();

		// verify new relationship saved in list
		String newRelationshipStr = String.format("//*[@id='relGrid']/div[3]/table/tbody[2]/tr[contains(.,'%s')]", customData.get("relationship_name"));
		String relationNameStr = String.format("%s//td[1]//div",newRelationshipStr);
		
		// xpath used for assert values, becoz new relationship is random in order		
		// Assert check custom field created
		new VoodooControl("div", "xpath", relationNameStr).assertEquals(customData.get("relationship_name"), true);
		
		// Delete custom relationship
		new VoodooControl("div", "xpath", relationNameStr).click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='deleterelbtn']").click();
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > button.default").waitForVisible();
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > button.default").click();
		VoodooUtils.waitForReady();
		
		// below code is needed, as on saving relationship, error occurs: Failed to retrieve data
		new VoodooControl("input", "css", "input[value=Studio]").click();
		accountsSubPanelCtrl.click();
		relationshipCtrl.click();
		
		// Assert check custom field created
		new VoodooControl("div", "xpath", relationNameStr).assertExists(false);
		
		// Try to delete default relationship
		VoodooUtils.waitForReady();
		new VoodooControl("div", "xpath", "//*[@id='relGrid']/div[3]/table/tbody[2]/tr[1]/td[1]/div").click();
		VoodooUtils.waitForReady();
		// Assert check default relationship not deleted
		new VoodooControl("input", "css", "input[name='deleterelbtn']").assertExists(false);
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}