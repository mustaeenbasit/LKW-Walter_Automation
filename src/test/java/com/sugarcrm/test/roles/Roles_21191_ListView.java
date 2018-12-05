package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class Roles_21191_ListView extends SugarTest {
	DataSource roleRecord = new DataSource();
	FieldSet roleReadOwnerWrite = new FieldSet();
	FieldSet roleReadWrite = new FieldSet();
	UserRecord chrisUser;

	public void setup() throws Exception { 
		roleRecord = testData.get(testName);
		roleReadOwnerWrite = roleRecord.get(0);
		roleReadWrite = roleRecord.get(1);
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verifying field_permissions_multiple_roles_Read/OwnerWrite & Read/Write
	 * @throws Exception
	 */
	@Test
	public void Roles_21191_ListView_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		chrisUser = (UserRecord) sugar().users.create();
		FieldSet qaUser = sugar().users.getQAUser();
		FieldSet accountData = sugar().accounts.getDefaultData();
		FieldSet contactsData = sugar().contacts.getDefaultData();
		FieldSet usersData = sugar().users.getDefaultData();

		// TODO: VOOD-856
		VoodooControl evenCell, optionReadOwnerWrite, optionReadWrite, saveRole, contactsLink;
		VoodooSelect selectDropDown;
		saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		contactsLink = new VoodooControl("a", "css", ".edit tr:nth-child(7) a");

		// Create RoleReadOwnerWrite with permission ReadOwnerWrite for all the fields in Contacts Module
		AdminModule.createRole(roleReadOwnerWrite);
		VoodooUtils.focusFrame("bwc-frame");
		contactsLink.click();
		VoodooUtils.waitForReady();

		// Count number of Rows in the fieldPermission table
		int rowCount = new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr").count();

		// Count number of Columns in the fieldPermission table
		int columnsInRow[] = new int[rowCount];
		for(int i=0;i<rowCount;i++){
			int columnCount = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(i+1)+") "+" td").count();
			columnsInRow[i] = columnCount;
		}

		// TODO: VOOD-856
		// Set the field permission for all the fields to Read/OwnerWrite
		for(int matrixRow=1; matrixRow<=rowCount; matrixRow++ ){
			for(int matrixCol=1; matrixCol<=columnsInRow[(matrixRow-1)]; matrixCol+=2){
				evenCell = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") div:nth-child(2)");
				selectDropDown = new VoodooSelect("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select");
				optionReadOwnerWrite = new VoodooControl("option", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+matrixRow+") "
						+ "td:nth-child("+(matrixCol+1)+") select option:nth-child(3)");
				evenCell.click();
				if(!(selectDropDown).queryVisible()){
					evenCell.click();
				}
				selectDropDown.click();				
				optionReadOwnerWrite.click();
			}
		}

		// Click Save button to save the roleReadOwnerWrite 
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleReadWrite to qaUser
		AdminModule.assignUserToRole(qaUser);
		AdminModule.assignUserToRole(chrisUser);

		// Create RoleReadOwnerWrite with permission ReadWrite for all the fields in Contacts Module
		AdminModule.createRole(roleReadWrite);
		VoodooUtils.focusFrame("bwc-frame");
		contactsLink.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-856
		// Set the field permission for all the fields to ReadWrite
		for(int matrixRow=1; matrixRow<=rowCount; matrixRow++ ){
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

		// Click Save button to save the roleReadWrite 
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleReadWrite to qaUser
		AdminModule.assignUserToRole(qaUser);
		AdminModule.assignUserToRole(chrisUser);

		// Create a Contact record assigned to qaUser and Team is Global
		FieldSet contactData = new FieldSet();
		contactData.put("relAssignedTo", qaUser.get("userName"));
		contactData.put("emailAddress", usersData.get("emailAddress"));
		sugar().contacts.create(contactData);

		// TODO:VOOD-1724
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "relAccountName").set(accountData.get("name"));
		sugar().alerts.getWarning().cancelAlert();
		sugar().contacts.listView.saveRecord(1);

		// Logout from Admin user & login from qaUser
		sugar().logout();
		sugar().login(qaUser);

		//Navigate to Contacts ListView
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", contactsData.get("fullName"));
		sugar().contacts.listView.verifyField(1, "title", contactsData.get("title"));
		sugar().contacts.listView.verifyField(1, "relAccountName", accountData.get("name"));
		sugar().contacts.listView.verifyField(1, "emailAddress", usersData.get("emailAddress"));
		sugar().contacts.listView.verifyField(1, "phoneWork", contactsData.get("phoneWork"));
		sugar().contacts.listView.verifyField(1, "relAssignedTo", qaUser.get("userName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
