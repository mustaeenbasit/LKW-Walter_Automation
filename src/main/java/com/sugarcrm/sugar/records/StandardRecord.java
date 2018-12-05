package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Base class from which the Record objects for all standard modules extend.
 * Data and methods that are common to records in all standard modules are
 * stored here.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * @author David Safar <dsafar@sugarcrm.com>
 */
public abstract class StandardRecord extends Record {
	protected StandardModule module;

	/**
	 * Pass-through constructor.
	 * 
	 * @param	data	A FieldSet containing the data for the new record. 
	 * @throws Exception
	 */
	public StandardRecord(FieldSet data) throws Exception {
		super(data);
	}

	/**
	 * Edit the calling Record with the supplied FieldSet data.
	 * 
	 * @param	editedData	Data to edit in the record.
	 * @throws Exception
	 */
	public void edit(FieldSet editedData) throws Exception {
		navToRecord();

		module.recordView.edit();
		module.recordView.showMore();
		module.recordView.setFields(editedData);

		putAll(editedData);

		module.recordView.save();
	} // edit

	/**
	 * Verifies the current record using its stored data.
	 * 
	 * @throws Exception
	 */
	public void verify() throws Exception {
		verify(this);
	} // verify()

	/**
	 * Verifies the current record using the specified data.
	 * 
	 * @param verifyThis
	 * @throws Exception
	 */
	public void verify(FieldSet verifyThis) throws Exception {
		navToRecord();

		module.recordView.showMore();

		if(module.moduleNamePlural.equalsIgnoreCase("Users")) {
			VoodooUtils.focusFrame("bwc-frame");
		}

		for(String controlName : verifyThis.keySet()) {
			if(verifyThis.get(controlName) != null) {
				if(module.recordView.getDetailField(controlName) == null) {
					continue;
				}
				VoodooUtils.voodoo.log.info("Verifying field " + controlName);
				String toVerify = verifyThis.get(controlName);
				// TODO: TR-13042, VOOD-1746 Try and figure out a proper way to verify Teams field with subelements (Primary) using .assertEquals
				if(controlName.equals("relTeam") || controlName.startsWith("date") || controlName.startsWith("description")) {
					module.recordView.getDetailField(controlName).assertContains(
							toVerify, true);
				}else if(controlName.startsWith("check")){
					module.recordView.getDetailField(controlName).assertChecked(Boolean.parseBoolean(toVerify));
				}
				else {					
					module.recordView.getDetailField(controlName).assertEquals(
							toVerify, true);
				}
			}
		}
		if(module.moduleNamePlural.equalsIgnoreCase("Users")) {
			VoodooUtils.focusDefault();
		}
	}

	/**
	 * Verifies the preview pane of the current record using its stored data.
	 * 
	 * @throws Exception
	 */
	public void verifyPreview() throws Exception {
		verifyPreview(this);
	}

	/**
	 * Verifies the preview pane of the current record using the specified data.
	 * 
	 * @throws Exception
	 */
	public void verifyPreview(FieldSet verifyThis) throws Exception {
		sugar().previewPane.setModule(module);
		sugar().previewPane.showMore();
		VoodooUtils.voodoo.log.info("KeySet keys: " + verifyThis.keySet());

		for(String controlName : verifyThis.keySet()) {
			if(verifyThis.get(controlName) != null) {
				if(module.getField(controlName).previewPaneControl == null) {
					VoodooUtils.voodoo.log.warning("The control " + controlName + " does not have a defined preview pane element value");
					continue;
				}
				VoodooUtils.voodoo.log.info("Verifying field " + controlName);
				String toVerify = verifyThis.get(controlName);
				// TODO: TR-13042, VOOD-1746 Try and figure out a proper way to verify Teams field with subelements (Primary) using .assertEquals
				if(controlName.equals("relTeam") || controlName.startsWith("date") || controlName.startsWith("description")) {
					module.getField(controlName).previewPaneControl.assertContains(
							toVerify, true);
				}
				else if(controlName.startsWith("check")) {
					module.getField(controlName).previewPaneControl.assertChecked(Boolean.parseBoolean(toVerify));
				}
				else{
					module.getField(controlName).previewPaneControl.assertEquals(toVerify, true);
				}
			}
		}
	}

	/**
	 * Verify this record's data
	 * 
	 * @throws Exception
	 */
	public void verifyPreviewListView() throws Exception {
		module.navToListView();
		module.listView.setSearchString(getRecordIdentifier());
		module.listView.previewRecord(01);
		verifyPreview(this);
	}

	/**
	 * Verifies this record's data on this record's module subpanel.
	 * <p>
	 * 
	 * @param parentRecord	the related record, in a StandardModule, which is expected to display this record on its subpanel
	 * @throws Exception
	 */
	public void verifyPreviewSubpanel(StandardRecord parentRecord) throws Exception {
		// Navigate to parentRecord recordView
		parentRecord.navToRecord();

		// Get this record's subpanel object
		StandardSubpanel subpanel = parentRecord.module.recordView.subpanels.get(module.moduleNamePlural);
		subpanel.expandSubpanel(); // Expose the subpanel

		// Search for this record in all subpanels by its identifier
		parentRecord.module.listView.setSearchString(this.getRecordIdentifier());

		// Click on the first result in the subpanel list
		subpanel.clickPreview(1);

		// Verify preview pane of record in subpanel
		verifyPreview(this);
	}

	/**
	 * saveDraft will start the process to send an email but save it as a draft
	 * instead. All data is contained in the passed in DataSource
	 * 
	 * @param draftOptions
	 *            - values for fields in email draft
	 * @throws Exception
	 */
	public void saveDraft(DataSource draftOptions) throws Exception {
		// TODO: Need logic to check if you are in the correct view to compose
		// an email
		// if not, need to navigate to the correct view. For now, force
		// navigation

		// Click on Compose Email
		module.recordView.getControl("activityOption").click();
		module.recordView.getControl("composeEmail").click();

		// Accept Popup Dialog warning that no outbound server information is
		// present, if needed
		if(new VoodooControl("div", "id", "sugarMsgWindow").getAttribute("visible").equals("true")) {
			module.recordView.getControl("acceptOk").click();
		}

		// Enter subject and body.
		module.recordView.getControl("subject").set(draftOptions.get(0).get("subject"));
		module.recordView.getControl("emailBody").set(draftOptions.get(0).get("body"));

		// Click Save Draft
		module.recordView.getControl("saveDraft").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Navigates to the record's detailView and deletes the record
	 */
	public void delete() throws Exception {
		VoodooUtils.voodoo.log.info("Deleting record " + getRecordIdentifier() +".");

		navToRecord();
		VoodooUtils.pause(7000);
		module.recordView.delete();
		VoodooUtils.pause(500);
		sugar().alerts.getAlert().confirmAlert();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Navigates to this record by searching for it on the ListView.
	 * @throws Exception 
	 */
	public void navToRecord() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating to " + getRecordIdentifier() + ".");

		// Navigate to ListView of this record's module
		module.navToListView();
		module.listView.setSearchString(getRecordIdentifier());
		sugar().alerts.waitForLoadingExpiration();
		module.listView.clickRecord(1);
	}

	/**
	 * Returns the Record's name by default but has to
	 * be overridden when the Identifier isn't a name but subject or title
	 * 
	 * @return - String of the records Identification (name, subject, title
	 *         etc.)
	 */
	public String getRecordIdentifier() {
		return StandardRecord.this.get("name");
	}

	/**
	 * Creates Comment in Activity Stream on a Record.
	 * 
	 * This method leaves you on the Activity Stream view of this Record.
	 * 
	 * @param content Content of the Entry
	 * @throws Exception
	 */
	public void createComment(String content) throws Exception {
		navToRecord();
		// If the Comment button is NOT visible we are not on the Activity Stream view, so click on the Show Activity Stream button
		if(!(new VoodooControl("button","css","div[data-voodoo-name='activitystream-omnibar'] button").queryVisible())){
			module.recordView.showActivityStream();
			VoodooUtils.pause(1000);
		}
		module.recordView.activityStream.createComment(content);
	}

	/**
	 * Creates a Reply to an Entry.
	 * 
	 * This method leaves you on the Activity Stream view of this Record.
	 * 
	 * @param content String content of Reply
	 * @param rowNum int index of Entry
	 * @throws Exception
	 */
	public void createReply(String content, int rowNum) throws Exception {
		navToRecord();
		// If the Comment button is NOT visible we are not on the Activity Stream view, so click on the Show Activity Stream button
		if(!(new VoodooControl("button","css","div[data-voodoo-name='activitystream-omnibar'] button").queryVisible())) {
			module.recordView.showActivityStream();
			VoodooUtils.pause(1000);
		}
		module.recordView.activityStream.createReply(content, rowNum);
	}

	/**
	 * Asserts the comment does or does not contain the string content.
	 * 
	 * There must already be a comment in the Activity Stream.
	 * This assert leaves you on the Activity Stream view of this Record.
	 * 
	 * @param content content to be asserted
	 * @param entryRow 1-based index of the comment
	 * @param shouldContain true for positive assert, false for negative "assertnot"
	 * @throws Exception
	 */
	public void assertCommentContains(String content, int entryRow, boolean shouldContain) throws Exception {
		navToRecord();
		// If the Comment button is NOT visible we are not on the Activity Stream view, so click on the Show Activity Stream button
		if(!(new VoodooControl("button","css","div[data-voodoo-name='activitystream-omnibar'] button").queryVisible())){
			module.recordView.showActivityStream();
			VoodooUtils.pause(1000);
		}
		new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(" + entryRow + ") div .tagged").assertElementContains(content, shouldContain);
	}

	/**
	 * Asserts the reply does or does not contain the string content
	 * 
	 * There must already be a reply in the Activity Stream.
	 * This assert leaves you on the Activity Stream view of this Record.
	 * 
	 * @param content content to be asserted
	 * @param entryRow 1-based index of the comment where a reply exists
	 * @param replyRow 1-based index of the reply
	 * @param shouldContain true for positive assert, false for negative "assertnot"
	 * @throws Exception
	 */
	public void assertReplyContains(String content, int entryRow, int replyRow, boolean shouldContain) throws Exception {
		navToRecord();
		// If the Comment button is NOT visible we are not on the Activity Stream view, so click on the Show Activity Stream button
		if(!(new VoodooControl("button","css","div[data-voodoo-name='activitystream-omnibar'] button").queryVisible())){
			module.recordView.showActivityStream();
			VoodooUtils.pause(1000);
		}
		new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(" + entryRow + ") .comments li:nth-of-type(" + replyRow + ") .tagged").assertElementContains(content, shouldContain);
	}

	/**
	 * Copy this record to a new one with the specified fields changed.
	 * 
	 * @param	edits	FieldSet of fields to be changed on the new record. 
	 * 
	 * @return	a Record representing the newly-created copy.
	 * 
	 * @throws Exception
	 */
	@Override
	public StandardRecord copy(FieldSet edits) throws Exception {
		FieldSet recordData = module.defaultData.deepClone();
		recordData.putAll(edits);

		navToRecord();
		module.recordView.copy();
		module.createDrawer.showMore();
		module.createDrawer.setFields(edits);
		module.createDrawer.save();
		// TODO: This sucks, but waitForAlertExpiration() doesn't work here.
		VoodooUtils.pause(3000);

		return (StandardRecord) Class.forName(this.getClass().getName())
				.getConstructor(FieldSet.class).newInstance(recordData);
	}

	/**
	 * Navigate to this record in portal.
	 * <p>
	 * Must be in portal to use.<br>
	 * When used you will be taken to this records record view in portal.
	 * 
	 * @throws Exception
	 */
	public void navToPortalRecord() throws Exception {
		module.navToPortalListView();
		// TODO: Update to use portal specific listview
		module.listView.setSearchString(getRecordIdentifier());
		VoodooUtils.pause(2000);
		module.listView.clickRecord(1);
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Verifies this record in portal, using its stored data.
	 * 
	 * @throws Exception
	 */
	public void verifyInPortal() throws Exception {
		verifyInPortal(this);
	}

	/**
	 * Verifies the current record in portal, using the specified data.
	 * 
	 * @param verifyThis FieldSet of data to verify
	 * @throws Exception
	 */
	public void verifyInPortal(FieldSet verifyThis) throws Exception {
		navToPortalRecord();

		for(String controlName : verifyThis.keySet()) {
			if(verifyThis.get(controlName) != null) {
				if(module.recordView.getDetailField(controlName) == null) {
					continue;
				}
				VoodooUtils.voodoo.log.info("Verifying field " + controlName);
				String toVerify = verifyThis.get(controlName);
				// TODO: TR-13042, VOOD-1746 Try and figure out a proper way to verify Teams field with subelements (Primary) using .assertEquals
				if(controlName.equals("relTeam") || controlName.startsWith("date")) {
					module.recordView.getDetailField(controlName).assertContains(toVerify, true);
				} else {					
					module.recordView.getDetailField(controlName).assertEquals(toVerify, true);
				}
			}
		}
	}
} // Record