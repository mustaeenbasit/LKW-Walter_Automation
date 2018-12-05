package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models the ActivityStream for SugarCRM. 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ActivityStream extends View {
	public ActivityStream() throws Exception {
		// Common controls for activity stream comments
		addControl("submit","button","css","div[data-voodoo-name='activitystream-omnibar'] button");
		addControl("streamInput","div","css","div[data-voodoo-name='activitystream-omnibar'] .inputwrapper div");
	}
	
	/**
	 * Clicks the Submit button for an Activity Stream.
	 * If content is provided clicking this button will submit the content to the
	 * Activity Stream. You will be left on the Activity Stream view with a new message
	 * in the Stream.
	 * 
	 * If you click this button with no content in the input field, then 
	 * this button becomes unusable until content is present.
	 * 
	 * TODO: The graying out of the Submit button may be a bug. Filed SFA-1497 to track.
	 * 
	 * You must be on the RecordView or ListView with the Activity Stream View open
	 * to use this button.
	 * 
	 * @throws Exception
	 */
	public void clickSubmit() throws Exception {
		getControl("submit").click();
		VoodooUtils.pause(800);
	}
	
	/**
	 * Creates a comment in the Activity Stream associated with a Module.
	 * 
	 * You must be on the Record View or List View with the Activity Stream view open
	 * to use this method.
	 * 
	 * When done this method will leave you in the same place as you started and will post
	 * the content provided to the Activity Stream.
	 * 
	 * @param content String content of the Entry
	 * @throws Exception
	 */
	public void createComment(String content) throws Exception {
		getControl("streamInput").set(content);
		VoodooUtils.pause(500);
		clickSubmit();
		VoodooUtils.waitForAlertExpiration();
	}
	
	/**
	 * Asserts the comment does or does not contain the string content.
	 * 
	 * You must be on the Record View or List View with the Activity Stream view open
	 * to use this method and have content already posted to the Activity Stream.
	 * 
	 * This method only asserts and therefore does not change the page.
	 * 
	 * @param content content to be asserted
	 * @param entryRow 1-based index of the comment
	 * @param shouldContain true for positive assert, false for negative "assertnot"
	 * @throws Exception
	 */
	public void assertCommentContains(String content, int entryRow, boolean shouldContain) throws Exception {
		new VoodooControl("span", "css", ".activitystream-list.results > li:nth-of-type(" + entryRow + ") div .tagged").assertElementContains(content, shouldContain);
	}
	
	/**
	 * Asserts the reply does or does not contain the string content.
	 * 
	 * You must be on the Record View or List View with the Activity Stream view open
	 * to use this method and have content posted to the Activity Stream with a reply.
	 * 
	 * This method only asserts and therefore does not change the page.
	 * 
	 * @param content content to be asserted
	 * @param entryRow 1-based index of the comment where a reply exists
	 * @param replyRow 1-based index of the reply
	 * @param shouldContain true for positive assert, false for negative "assertnot"
	 * @throws Exception
	 */
	public void assertReplyContains(String content, int entryRow, int replyRow, boolean shouldContain) throws Exception {
		new VoodooControl("span", "css", ".activitystream-list.results > li:nth-of-type(" + entryRow + ") .comments li:nth-of-type(" + replyRow + ") .tagged").assertContains(content, shouldContain);
	}
	
	/**
	 * Toggles the Preview button of the Entry.
	 * 
	 * This will open the Preview Pane (right hand side) view of this Entry's Associated Record.
	 * There must already be an entry/comment already in the Activity Stream.
	 * 
	 * Clicking this button again will close the Preview Pane.
	 * 
	 * @param entryRowNum 1-based index of the row for the entry
	 * @throws Exception
	 */
	public void togglePreviewButton(int entryRowNum) throws Exception {
		new VoodooControl("a","css",".activitystream-list.results > li:nth-of-type(" + entryRowNum + ") .actions.btn-group a.preview-btn").click();
		VoodooUtils.pause(1000);
	}
	
	/**
	 * Clicks the Comment button an Entry in the Activity Stream.
	 * 
	 * This will display an input field to be used to enter in text as content for the Comment.
	 * 
	 * Clicking this button without content in the input field will do nothing.
	 * 
	 * @param entryRowNum 1-based index of the row for the entry
	 * @throws Exception
	 */
	public void clickCommentButton(int entryRowNum) throws Exception {
		new VoodooControl("a","css",".activitystream-list.results > li:nth-of-type(" + entryRowNum + ") .actions.btn-group a.comment-btn").click();
		VoodooUtils.pause(500);
	}
	
	/**
	 * Clicks the Reply button in this Entry.
	 * 
	 * You must be on the Activity Stream View in ListView or RecordView to use.
	 * Must have content in the input field.
	 * There must already be an entry/comment already in the Activity Stream.
	 * 
	 * @param entryRowNum 1-based index of the row for the entry  
	 * @throws Exception
	 */
	public void clickReplyButton(int entryRowNum) throws Exception {
		new VoodooControl("button", "css", ".activitystream-list.results > li:nth-of-type(" + entryRowNum + ") .comments .reply-input button").click();
		VoodooUtils.pause(500);
	}
	
	/**
	 * Creates a Reply on a given Entry.
	 * 
	 * You must be on the Activity Stream View in ListView or RecordView to use.
	 * There must already be an entry/comment already in the Activity Stream.
	 * 
	 * @param content string content in reply
	 * @param entryRowNum 1-based index of entry to reply to
	 * @throws Exception
	 */
	public void createReply(String content, int entryRowNum) throws Exception {		
		clickCommentButton(entryRowNum);
		VoodooUtils.pause(500);
		new VoodooControl("div","css",".activitystream-list.results > li:nth-of-type(" + entryRowNum + ") .comments .reply-input div").set(content);
		VoodooUtils.pause(500);
		clickReplyButton(entryRowNum);
		VoodooUtils.waitForAlertExpiration();
	}
}
