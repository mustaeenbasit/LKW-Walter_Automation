package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21193_usera extends SugarTest {
	DataSource roleRecord, contactData = new DataSource();
	UserRecord Chris;

	public void setup() throws Exception {
		roleRecord = testData.get(testName);
		contactData = testData.get(testName+"_contactData");
		sugar().contacts.api.create(contactData);
		sugar().login();
		Chris = (UserRecord)sugar().users.create();
	}

	/**
	 * Verify field permissions in multiple roles for Read Write and Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21193_usera_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Role1 < field_permissions_Not_Set>
		AdminModule.createRole(roleRecord.get(0));
		// Assign Not Set role to Chris
		AdminModule.assignUserToRole(Chris);

		// Create Role2 < field_permission_read_write> 
		AdminModule.createRole(roleRecord.get(1));
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580
		VoodooControl fieldCell, readWritePermission, saveRole, contactsCtrl;
		contactsCtrl = new VoodooControl("a", "css", ".edit tr:nth-child(6) a");
		contactsCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-856
		int columns, tableRow, tableCol, row = 0;
		// Count number of Rows in the fieldPermission table
		while (new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(row+1)+")").queryExists()){
			row++;
		}
		// Count number of Columns in the fieldPermission table
		int columnsInRow[] = new int[row];
		for(int i=0;i<row;i++){
			columns=0;
			while(new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(i+1)+") "
					+ "td:nth-child("+(columns+1)+")").queryExists()){
				columns++;
			}
			columnsInRow[i] = columns;
		}

		saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		contactsCtrl = new VoodooControl("a", "css", ".edit tr:nth-child(6) a");
		VoodooSelect selectDropDown;
		// Set the field permission for all the fields to Read/Write
		for(tableRow = 1 ; tableRow <= row ; tableRow++ ){
			for(tableCol = 1; tableCol <= columnsInRow[(tableRow-1)] ; tableCol+=2){
				fieldCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+tableRow+") "
						+ "td:nth-child("+(tableCol+1)+") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+tableRow+") "
						+ "td:nth-child("+(tableCol+1)+") select");
				readWritePermission = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+tableRow+") "
						+ "td:nth-child("+(tableCol+1)+") select option:nth-child(2)");
				fieldCell.click();
				if(!(selectDropDown).queryVisible()){
					fieldCell.click();
				}
				selectDropDown.click();
				readWritePermission.click();
			}
		}
		// Save Role2
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Assign roleReadWrite to Chris
		AdminModule.assignUserToRole(Chris);

		// Assign the records to user <mutliple_acl_NotSet_rw> i.e. Chris 
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		DataSource massUpdateData = testData.get(testName+"_data");
		sugar().contacts.massUpdate.performMassUpdate(massUpdateData.get(1));
		sugar().logout();

		// Login as Chris
		Chris.login();
		sugar().contacts.navToListView();
		// Verify fields for the records in list view are visible by user <mutliple_acl_NotSet_rw>.
		VoodooControl nameFieldCtrl = sugar().contacts.listView.getDetailField(1, "fullName");
		nameFieldCtrl.assertVisible(true);
		sugar().contacts.listView.getDetailField(1, "title").assertVisible(true);
		sugar().contacts.listView.getDetailField(1, "phoneWork").assertVisible(true);
		VoodooControl assignedToCtrl = sugar().contacts.listView.getDetailField(1, "relAssignedTo");
		assignedToCtrl.assertVisible(true);

		sugar().contacts.listView.clickRecord(1);
		// Verify fields for the records in detail view are visible by user <mutliple_acl_NotSet_rw>.
		sugar().contacts.recordView.getDetailField("fullName").assertVisible(true);
		sugar().contacts.recordView.getDetailField("title").assertVisible(true);
		sugar().contacts.recordView.getDetailField("phoneMobile").assertVisible(true);
		sugar().contacts.recordView.getDetailField("department").assertVisible(true);

		sugar().contacts.recordView.edit();
		// Verify fields for the records in edit view view are visible by user <mutliple_acl_NotSet_rw>.
		sugar().contacts.recordView.getEditField("firstName").assertVisible(true);
		sugar().contacts.recordView.getEditField("lastName").assertVisible(true);
		sugar().contacts.recordView.getEditField("title").assertVisible(true);
		sugar().contacts.recordView.getEditField("phoneMobile").assertVisible(true);
		sugar().contacts.recordView.getEditField("department").assertVisible(true);

		sugar().contacts.recordView.cancel();
		sugar().contacts.navToListView();
		// Verify "MassUpdate" by user
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.massUpdate.performMassUpdate(massUpdateData.get(0));
		FieldSet userData = sugar().users.getQAUser();
		assignedToCtrl.assertEquals(userData.get("userName"), true);
		sugar().contacts.listView.getDetailField(2, "relAssignedTo").assertEquals(userData.get("userName"), true);

		// Verify Records can be searched out using fields
		sugar().contacts.listView.setSearchString(contactData.get(0).get("lastName"));
		Assert.assertTrue("Number of Records in search result", sugar().contacts.listView.countRows() == 1);
		nameFieldCtrl.assertContains(contactData.get(0).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}