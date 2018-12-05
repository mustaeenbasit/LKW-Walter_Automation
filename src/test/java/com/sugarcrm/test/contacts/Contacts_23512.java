package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23512 extends SugarTest {
	FieldSet massUpdateValues = new FieldSet();
	ArrayList<ContactRecord> contacts = new ArrayList<ContactRecord>();

	public void setup() throws Exception {
		massUpdateValues = testData.get(testName).get(0);
		FieldSet  fs = new FieldSet();
		fs.put("lastName", testName);
		contacts.add((ContactRecord)sugar().contacts.api.create());
		contacts.add((ContactRecord)sugar().contacts.api.create(fs));
		sugar().login();
	}

	/**
	 * Verify that the selected contacts can be updated by "Mass Update"
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23512_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-660 Mass Update should identify fields by VoodooGrimoire internal names
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.massUpdate();
		sugar().contacts.massUpdate.setMassUpdateFields(massUpdateValues);
		sugar().alerts.getWarning().confirmAlert(); // For Reports to field
		sugar().contacts.massUpdate.update();
		sugar().alerts.getWarning().confirmAlert(); // For Donot call to field

		// in the Contacts_23512.csv I use field names as "Reports to", "Do Not Call", to make it work with setMassUpdateFields()
		// but in order to verify changes with contact.verify() method the field names should be as they are defined in ContactsModuleFields.csv: reportsTo, checkDoNotCall, etc
		// that is why I defined replaceKeys() method to change field names like "Assigned to" to "relAssignedTo"
		// I store this name map in Contacts_23512_field_names_map.csv
		DataSource fieldNamesMap = testData.get(testName + "_field_names_map");
		FieldSet fieldSetToVerify = new FieldSet();
		fieldSetToVerify = replaceKeys(massUpdateValues, fieldNamesMap.get(0));

		// c.verify() verifies the same contact twice
		for(ContactRecord c : contacts)
			c.verify(fieldSetToVerify);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	FieldSet replaceKeys(FieldSet initialSet, FieldSet keysMap) {
		FieldSet newSet = new FieldSet();

		for(String k : initialSet.keySet()) {
			// TODO: VOOD-1003
			// there are no some fields in ContactsModuleFields.csv:"Opt out Primary Email", etc
			// that is why I can't verify them with contact.verify()
			// I use "skip!" value in keysMap to skip these fields

			// I even skip relTeam/Teams field in spite of it is in the ContactsModuleFields.csv
			// contact.verify() fails to verify this field as there is "qauser(primary)" text on the page but not just qauser.

			if(keysMap.get(k).equals("skip!"))
				continue;

			newSet.put(keysMap.get(k), initialSet.get(k));
		}

		return newSet;
	}

	public void cleanup() throws Exception {}
}