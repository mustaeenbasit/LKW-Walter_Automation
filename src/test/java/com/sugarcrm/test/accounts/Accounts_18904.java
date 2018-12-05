package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18904 extends SugarTest {
  VoodooControl accountsSubPanelCtrl;
  
  public void setup() throws Exception {
    sugar().login();
  }

  /**
   * Verify List View Filter should work with custom fields
   * @throws Exception
   */
  @Test
  public void Accounts_18904_execute() throws Exception {
    VoodooUtils.voodoo.log.info("Running " + testName + "...");
    
    FieldSet customData = testData.get(testName).get(0);
    sugar().navbar.navToAdminTools();
    VoodooUtils.focusFrame("bwc-frame");
    
    // TODO: VOOD-938
    // studio
    sugar().admin.adminTools.getControl("studio").click();
    accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
    accountsSubPanelCtrl.click();
    VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
    fieldCtrl.click();

    // Add field and save
    new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
    new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
    new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
    VoodooUtils.waitForReady();
    sugar().admin.studio.clickStudio();
    accountsSubPanelCtrl.click();
    
    // layout subpanel
    VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
    layoutSubPanelCtrl.click();
    
    // List view
    new VoodooControl("td", "id", "viewBtnlistview").click();
    VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
    defaultSubPanelCtrl.waitForVisible();
    String dataNameDraggableLi = String.format("li[data-name=%s]",customData.get("module_field_name")); 
    new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
    VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
    saveBtnCtrl.click();
    VoodooUtils.waitForReady();
    sugar().admin.studio.clickStudio();
    accountsSubPanelCtrl.click();
    layoutSubPanelCtrl.click();
    
    // Record view
    VoodooControl recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
    recordViewSubPanelCtrl.click(); 
    VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
    VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
    new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
    moveToLayoutPanelCtrl.waitForVisible();
    String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("module_field_name")); 
    new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
    new VoodooControl("input", "id", "publishBtn").click();
    VoodooUtils.waitForReady();
    sugar().admin.studio.clickStudio();
    accountsSubPanelCtrl.click();
    layoutSubPanelCtrl.click();
    
    // Search view
    new VoodooControl("td", "id", "searchBtn").click();
    new VoodooControl("td", "id", "FilterSearchBtn").click();
    VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
    defaultSearchCtrl.waitForVisible();
    new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSearchCtrl);
    saveBtnCtrl.click();
    VoodooUtils.waitForReady();
    VoodooUtils.focusDefault();
    
    // Accounts module
    sugar().accounts.navToListView();
    sugar().accounts.listView.create();
    sugar().accounts.createDrawer.getEditField("name").set(customData.get("name"));
    sugar().accounts.createDrawer.showMore();
    
    // TODO: VOOD-935
    String dataNameInput = String.format(".fld_%s.edit input[name=%s]",customData.get("module_field_name"),customData.get("module_field_name")); 
    VoodooControl testFieldCtrl = new VoodooControl("input", "css", dataNameInput);
    testFieldCtrl.set(customData.get("input_custom_field"));
    sugar().accounts.createDrawer.save();
    
    // Expected result check
    sugar().accounts.listView.openFilterDropdown();
    sugar().accounts.listView.selectFilterCreateNew();
    new VoodooControl("a", "css", ".fld_filter_row_name.detail .select2-container.select2 a").click();
    
    // Xpath used for dynamic field
    new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[contains(.,'test c')]").click();
    new VoodooControl("a", "css", ".fld_filter_row_operator.detail .select2-container.select2 a").click();
    new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-of-type(1)").click();
    new VoodooControl("input", "css", ".fld_"+customData.get("module_field_name")+".detail input").set(customData.get("input_custom_field"));
    
    // Verify that created Account shows up in result list
    String dataName = String.format(".fld_%s.list .ellipsis_inline",customData.get("module_field_name")); 
    new VoodooControl("div", "css", dataName).assertEquals(customData.get("input_custom_field"), true);
    new VoodooControl("a", "css", ".btn-invisible.filter-close").click(); // for cancel search
    
    VoodooUtils.voodoo.log.info(testName + " complete.");
  }

  public void cleanup() throws Exception {}
}