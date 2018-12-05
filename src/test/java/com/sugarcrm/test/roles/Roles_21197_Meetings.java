package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21197_Meetings extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);

		// Create Account and Task records because it is required while creating and editing meeting record
		sugar().accounts.api.create();
		sugar().tasks.api.create();
		sugar().meetings.api.create();

		sugar().login();
	}
	/**
	 * Field Permissions read_write_with_access_permission_enable
	 * @throws Exception
	 */
	@Test
	public void Roles_21197_Meetings_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-856
		VoodooControl evenCell, optionReadWrite;
		VoodooSelect selectDropDown;
		VoodooControl saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		VoodooControl meetingsLink = new VoodooControl("a", "css", ".edit tr:nth-child(13) a");

		// Create Role with  Field Permissions read_write_with_access_permission_enable
		AdminModule.createRole(meetingData);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#ACLEditView_Access_Meetings_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Meetings_access div select").set(meetingData.get("moduleAccess"));
		saveRole.click();
		VoodooUtils.waitForReady();
		meetingsLink.click();
		VoodooUtils.waitForReady();

		// Count number of Rows in the fieldPermission table
		// TODO: VOOD-1526
		int columns, row = 0;
		while (new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(row+1)+")").queryExists()){
			row++;
		}

		// Count number of Columns in the fieldPermission table
		// TODO: VOOD-1526
		int columnsInRow[] = new int[row];

		for(int i=0;i<row;i++){
			columns=0;
			while(new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(i+1)+") "
					+ "td:nth-child("+(columns+1)+")").queryExists()){
				columns++;
			}
			columnsInRow[i] = columns;
		}

		// Set the field permission for all the fields to Read/Write
		for(int matrixRow=1; matrixRow<=row; matrixRow++ ){
			for(int matrixCol=1; matrixCol<=columnsInRow[(matrixRow-1)]; matrixCol+=2){
				evenCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select");
				optionReadWrite = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select option:nth-child(2)");

				evenCell.click();
				if(!(selectDropDown).queryVisible()){
					evenCell.click();
				}
				selectDropDown.click();				
				optionReadWrite.click();
			}
		}

		// Click Save button to save the roleNone
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleNone to qaUser
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Updating the meeting record with additional information missing in default csv	
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("relatedToParentType").set(meetingData.get("relatedToParentType"));
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().meetings.recordView.getEditField("assignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().meetings.recordView.getEditField("remindersPopup").set(meetingData.get("remindersPopup"));
		sugar().meetings.recordView.getEditField("remindersEmail").set(meetingData.get("remindersEmail"));
		sugar().meetings.recordView.save();

		// Logout from Admin
		sugar().logout();

		// Login as qaUser
		sugar().login(sugar().users.getQAUser());
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		DataSource fieldsData = testData.get(testName+"_editFields");
		int fieldCount = fieldsData.size();

		// Asserting the values for Team and AssignedTo field 
		sugar().meetings.recordView.getDetailField("teams").assertContains(meetingData.get("teams"), true);
		sugar().meetings.recordView.getDetailField("assignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		for (int i = 0; i < fieldCount; i++) {
			sugar().meetings.recordView.getDetailField(fieldsData.get(i).get("sugarField")).assertVisible(true);
		}

		// Editing the fields on meetings record view
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();
		for (int j = 0; j < fieldCount; j++) {
			sugar().meetings.recordView.getEditField(fieldsData.get(j).get("sugarField")).set(fieldsData.get(j).get("value"));
		}

		// Asserting the field values on meetings record view 
		sugar().meetings.recordView.save();
		sugar().meetings.recordView.showMore();
		for (int k = 0; k < fieldCount; k++) {
			sugar().meetings.recordView.getDetailField(fieldsData.get(k).get("sugarField")).assertContains(fieldsData.get(k).get("value"), true);
		}

		// Searching the record in list view
		String meetingName = sugar().meetings.getDefaultData().get("name");
		sugar().meetings.navToListView();
		sugar().meetings.listView.setSearchString(meetingName);
		sugar().meetings.listView.getDetailField(1, "name").assertEquals(meetingName, true);

		// Updating meeting record using massupdate panel
		FieldSet massUp = new FieldSet();
		massUp.put(meetingData.get("massAssigned"),sugar().users.getQAUser().get("userName"));
		sugar().meetings.listView.toggleSelectAll();
		sugar().meetings.massUpdate.performMassUpdate(massUp);

		// Verifying meeting gets updated by massupdate
		sugar().meetings.listView.getDetailField(1,"assignedTo").assertContains(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}