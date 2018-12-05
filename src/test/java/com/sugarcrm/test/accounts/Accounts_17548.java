package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Accounts_17548 extends SugarTest {
	DataSource customDataSource = new DataSource();

	public void setup() throws Exception {
		customDataSource = testData.get(testName);
		sugar().login();
		
		// TODO: VOOD-517, VOOD-542
		VoodooControl accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		
		// TODO: VOOD-542
		// Go to Admin > Studio > Accounts > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
				
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("select", "id", "type").set(customDataSource.get(0).get("dataType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customDataSource.get(0).get("fieldName"));
		new VoodooControl("input", "css", "input[name='required']").set(Boolean.toString(true));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
				
		// Add created custom field to Record view
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
				
		// Add one row
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
				
		// Add Custom TextArea field to Record View
		String dataNameDraggableField = String.format("div[data-name=%s_c]",customDataSource.get(0).get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify custom text area content can be expanded or collapsed.
	 * @throws Exception
	 */
	@Test
	public void Accounts_17548_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(customDataSource.get(0).get("name"));
		
		// TODO: VOOD-1036
		VoodooControl customTextField = new VoodooControl("textarea", "css", "textarea[name='"+customDataSource.get(0).get("fieldName")+"_c']");
		customTextField.set(customDataSource.get(0).get("custom_val"));
		sugar().accounts.createDrawer.save();
		sugar().accounts.listView.clickRecord(1);
		
		// Verify description will truncate ~ 450 words
		VoodooControl customFieldTextContent = new VoodooControl("span", "css", ".fld_customfield_c.detail div");
		customFieldTextContent.assertEquals(customDataSource.get(1).get("custom_val") + "\n" + customDataSource.get(2).get("custom_val"), true);
		
		// TODO: VOOD-594
		VoodooControl moreLessLink = new VoodooControl("span", "css", ".detail.fld_customfield_c .toggle-text");
		
		// Verify more link is present.
		moreLessLink.assertVisible(true);
		
		// Verify when more link is clicked all the description content is displayed
		moreLessLink.click();
		customFieldTextContent.assertEquals(customDataSource.get(3).get("custom_val") + "\n" + customDataSource.get(4).get("custom_val"), true);
		
		// Verify when less link is clicked then description will truncate ~ 450 words
		moreLessLink.click();
		customFieldTextContent.assertEquals(customDataSource.get(1).get("custom_val") + "\n" + customDataSource.get(2).get("custom_val"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}