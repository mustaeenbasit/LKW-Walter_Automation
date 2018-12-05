package com.sugarcrm.test.studio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_27182 extends SugarTest {
	VoodooControl leadsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, clickOnLastName;
	FieldSet customData;
	DataSource ds;
	String newTime, newDate, amPm;
	
	public void setup() throws Exception {
	
		// Store current date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		newDate = sdf.format(dt);
		
		int hour = c.get(Calendar.HOUR); // Get current hours
		int min = c.get(Calendar.MINUTE); // Get current minutes
		int booleanAmPm = c.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		amPm = "am";
		if(booleanAmPm > 0) amPm = "pm";
		if(hour <= 0) hour = 12;
		
		// Set hours and minutes as per requirement
		if(min >= 0 && min < 15) {			
			newTime = hour+":15"+amPm;
		} else if(min >= 15 && min < 30)
			newTime = hour+":30"+amPm;
		else if(min >= 30 && min < 45)
			newTime = hour+":45"+amPm;
		else if(min >= 45 && min < 59) {
			if(hour != 12) hour = (hour+1);
			newTime = hour+":00pm";
		}
		
		VoodooUtils.voodoo.log.info("log current time: " + hour+" >> Min: "+min);
		VoodooUtils.voodoo.log.info("log set time: " + newTime + ">> current date: "+newDate+ ">> current Am/PM : "+amPm);
		
		ds = testData.get(testName+"_fields");
		customData = testData.get(testName).get(0);
		 
		leadsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		clickOnLastName = new VoodooControl("input", "name", "last_name");
		sugar().login();
	}

	/**
	 * Verify that hoursUntil function is working properly 
	 * 
	 * @throws Exception
	 */
	@Ignore("Blocked in Testopia")
	@Test
	public void Studio_27182_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938 -Need library support for studio subpanel
		sugar().admin.adminTools.getControl("studio").click();
		leadsSubPanelCtrl.click();
		fieldCtrl.click();
		for(int i = 0; i < ds.size(); i++) {
			VoodooUtils.waitForReady();
			new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
			new VoodooControl("select", "css", "#type").set(ds.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			new VoodooControl("input", "id", "field_name_id").set(ds.get(i).get("module_field_name"));			
			if(i == 2 || i == 3) {
				new VoodooControl("input", "id", "calculated").click();		
				new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
				VoodooUtils.waitForReady();
				new VoodooControl("textarea", "css", "#formulaInput").set(ds.get(i).get("formula"));
				new VoodooControl("input", "id", "fomulaSaveButton").click();
			}
			
			VoodooUtils.waitForReady();
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady();
			sugar().alerts.waitForLoadingExpiration();
		}
		
		// TODO: VOOD-999	
		studioFooterCtrl.click();
		leadsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		// Record view
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		for(int i = 0; i < ds.size(); i++) {
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			moveToLayoutPanelCtrl.waitForVisible();
			VoodooUtils.pause(3000);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		
		// List view
		studioFooterCtrl.click();
		leadsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click();
		for(int i = 0; i < ds.size(); i++) {
			VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
			new VoodooControl("li", "css", ".draggable[data-name='"+ds.get(i).get("module_field_name")+"_c").dragNDrop(moveHere);
		}
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create lead record
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl dateField1 = new VoodooControl("input", "css", ".fld_"+customData.get("field_name")+"_c.edit .datepicker");
		VoodooControl dateField2 = new VoodooControl("input", "css", ".fld_"+customData.get("field_name2")+"_c.edit .datepicker");
		VoodooControl timeField = new VoodooControl("input", "css", ".fld_"+customData.get("field_name2")+"_c.edit .ui-timepicker-input");
		dateField2.waitForVisible();
		dateField2.set(newDate);
		dateField1.waitForVisible();
		dateField1.set(newDate);
		timeField.waitForVisible();
		timeField.set(newTime);
		dateField2.set(newDate);
		dateField2.waitForVisible();
		clickOnLastName.set(customData.get("lead_name"));
		clickOnLastName.waitForVisible();
		sugar().leads.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.pause(5000);
		VoodooUtils.focusDefault();

		// Verify that hours should be 24 
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl hoursRes = new VoodooControl("div", "css", ".detail.disabled[data-voodoo-name='"+ds.get(2).get("module_field_name")+"_c'] div");
		VoodooControl hoursRes2 = new VoodooControl("div", "css", ".detail.disabled[data-voodoo-name='"+ds.get(3).get("module_field_name")+"_c'] div");
		hoursRes.waitForVisible(3000);
		VoodooUtils.voodoo.log.info("check hours:"+hoursRes.getText());
		VoodooUtils.voodoo.log.info(">>> check hours:"+hoursRes2.getText());
		
		hoursRes.assertContains(customData.get("currentDateResult"),true);
		hoursRes2.assertContains(customData.get("currentDateResult"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}