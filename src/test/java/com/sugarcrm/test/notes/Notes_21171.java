package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Notes_21171 extends SugarTest {
	FieldSet customData = new FieldSet();
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// 1 contact and note record created via API
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().notes.api.create();
		sugar().login();

		// TODO: VOOD-1505 - studio relationship support
		// Studio Controls for Contacts
		VoodooControl contactModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		VoodooControl relationshipBtn = new VoodooControl("td", "id", "relationshipsBtn");
		VoodooControl addRelationshipBtn = new VoodooControl("input", "css", "[name='addrelbtn']");
		VoodooControl relTypeSelect = new VoodooControl("select", "id", "relationship_type_field");
		VoodooControl rightModuleField = new VoodooControl("select", "id", "rhs_mod_field");
		VoodooControl saveAndDeployButton = new VoodooControl("input", "css", "input[name=saverelbtn]");

		// Studio Controls for Notes
		// TODO: VOOD-1507 - studio listview layout support
		VoodooControl notesModuleCtrl = new VoodooControl("a", "id", "studiolink_Notes");
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl listViewBtnCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		VoodooControl contactsNotesFieldCtrl = new VoodooControl("li", "css", "[data-name='"+customData.get("customContactField")+"_name']");
		VoodooControl contactsNotesDropCtrl = new VoodooControl("li", "css", "[data-name='contact_name']");
		VoodooControl saveAndDeployButton2 = new VoodooControl("input", "id", "savebtn");

		// TODO: VOOD-1509, VOOD-1510 - search view and layout
		// Studio controls for Search
		VoodooControl searchBtn = new VoodooControl("td", "id", "searchBtn");
		VoodooControl searchBtn2 = new VoodooControl("td", "id", "FilterSearchBtn");

		// Creating a custom One-to-Many relationship Contacts-Notes
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		contactModuleCtrl.click();
		VoodooUtils.waitForReady();

		relationshipBtn.click();
		VoodooUtils.waitForReady();

		addRelationshipBtn.click();
		VoodooUtils.waitForReady();

		relTypeSelect.set(customData.get("relType"));
		VoodooUtils.waitForReady();

		rightModuleField.set(sugar().notes.moduleNamePlural);
		VoodooUtils.waitForReady();

		saveAndDeployButton.click();
		VoodooUtils.waitForReady(30000);

		// Navigate back to Studio (Footer Pane) > Notes > Layouts > ListView
		sugar().admin.studio.clickStudio(); // hack, this will be deleted after VOOD-542 resolved
		notesModuleCtrl.click();
		VoodooUtils.waitForReady();

		layoutCtrl.click();
		VoodooUtils.waitForReady();

		listViewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Adding contacts to ListView fields
		contactsNotesFieldCtrl.scrollIntoView();
		contactsNotesFieldCtrl.dragNDrop(contactsNotesDropCtrl);

		VoodooUtils.waitForReady();
		saveAndDeployButton2.click();

		// Navigate to Notes > Layouts > Search, move Contacts (contacts_notes_name) to Default. 
		sugar().admin.studio.clickStudio(); // hack, this will be deleted after VOOD-542 resolved
		notesModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtn.click();
		VoodooUtils.waitForReady();
		searchBtn2.click();
		VoodooUtils.waitForReady();

		// Adding contacts to filter search fields
		contactsNotesFieldCtrl.scrollIntoView();
		contactsNotesFieldCtrl.dragNDropViaJS(contactsNotesDropCtrl);
		VoodooUtils.waitForReady();
		saveAndDeployButton2.click();
		VoodooUtils.pause(3000); // Required pause to save the settings

		VoodooUtils.focusDefault();
	}
	/**
	 * Custom field created by Contacts-Notes relationship does not respect the name format in My Setting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21171_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1036: Need library support for Accounts/any sidecar module for newly created custom fields
		VoodooSelect contactsNotesDropDownCtrl = new VoodooSelect("span", "css", ".fld_"+customData.get("customContactField")+"_name.edit");
		VoodooControl customContactOnListView = new VoodooControl("span", "css", ".list.fld_"+customData.get("customContactField")+"_name");
		VoodooControl contactOnListView = sugar().notes.listView.getDetailField(1, "contact");

		// Setting contact names here
		String salutation = myContact.get("salutation");
		String firstName = myContact.get("firstName");
		String lastName = myContact.get("lastName");
		String formatDrDavidLivingstone = salutation +" "+ firstName +" "+ lastName ; 
		String formatLivingstoneDavidDr = lastName +" "+ firstName +" "+ salutation ;

		// Navigates to notes listview -> contact name -> notes dropdown
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		String contactName = myContact.getRecordIdentifier();
		sugar().notes.recordView.getEditField("contact").set(contactName);
		contactsNotesDropDownCtrl.set(contactName);
		sugar().notes.recordView.save();

		sugar().notes.navToListView();
		// Verifying that the default Contact field displays the contact on specified name format
		contactOnListView.assertEquals(formatDrDavidLivingstone, true);

		// Verifying that the custom Contact field displays the contact on specified name format
		customContactOnListView.assertEquals(formatDrDavidLivingstone, true);

		// Changing the name format of the user to 'Livingstone David Dr.'
		FieldSet nameFormatFS = new FieldSet();
		nameFormatFS.put("advanced_nameFormat", customData.get("customNameFormat"));
		sugar().users.setPrefs(nameFormatFS);

		sugar().notes.navToListView();
		// Verifying that the default Contact field displays the contact as per the changed name format
		contactOnListView.assertEquals(formatLivingstoneDavidDr, true);

		// Verifying that the custom Contact field displays the contact as per the changed name format
		customContactOnListView.assertEquals(formatLivingstoneDavidDr, true);

		// Creating a custom filter for Zhou Ran on Notes List View
		sugar().notes.listView.openFilterDropdown();
		sugar().notes.listView.selectFilterCreateNew();

		// TODO: VOOD-1462: Enhance Filter Create to allow use of Custom Fields from Studio
		// Setting filter for Notes
		new VoodooSelect("a", "css", "[data-filter='field'] [data-voodoo-type='field'] a").set(customData.get("type"));
		new VoodooSelect("a", "css", "[data-filter='operator'] [data-voodoo-type='field'] a").set(customData.get("operator"));
		new VoodooControl("input", "css", "div[data-filter='value'] .select2-search-field input").set(lastName);
		new VoodooControl("li", "css", ".select2-results li").click();
		VoodooUtils.waitForReady();

		// Verifying that the note with custom Contact field is displayed
		customContactOnListView.assertEquals(formatLivingstoneDavidDr, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}