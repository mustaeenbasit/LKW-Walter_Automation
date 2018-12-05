package com.sugarcrm.sugar;

import com.sugarcrm.candybean.datasource.FieldSet;

import java.util.HashMap;

public class SugarField extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	
	public VoodooControl editControl, detailControl, massUpdateControl,
							previewPaneControl, listViewDetailControl, listViewEditControl,
							portalEditControl, portalDetailControl,
							filterCreateControl;

	public SugarField(FieldSet values) throws Exception {
		// Change string "null"s to object nulls.
		for(String column : values.keySet()) {
			if(values.get(column).equalsIgnoreCase("null")) {
				values.put(column, null);
			}
		}

		// Save the parameters to this object.
		// TODO: Consider simplifying this with a putAll with standard keys.
		put("field_name", values.get("sugarField"));
		put("detail_hook_tag", values.get("detailHookTag"));
		put("detail_hook_strategy", values.get("detailHookStrategy"));
		put("detail_hook_value", values.get("detailHookValue"));
		put("edit_hook_tag", values.get("editHookTag"));
		put("edit_hook_strategy", values.get("editHookStrategy"));
		put("edit_hook_value", values.get("editHookValue"));
		put("rest_name", values.get("restName"));
		put("mass_update_tag", values.get("massUpdateTag"));
		put("mass_update_strategy", values.get("massUpdateStrategy"));
		put("mass_update_value", values.get("massUpdateValue"));
		put("default_value", values.get("defaultValue"));
		put("type", values.get("type"));
		put("preview_tag", values.get("previewTag"));
		put("preview_strategy", values.get("previewStrategy"));
		put("preview_value", values.get("previewValue"));
		put("listview_detail_tag", values.get("listViewDetailTag"));
		put("listview_detail_strategy", values.get("listViewDetailStrategy"));
		put("listview_detail_value", values.get("listViewDetailValue"));
		put("listview_edit_tag", values.get("listViewEditTag"));
		put("listview_edit_strategy", values.get("listViewEditStrategy"));
		put("listview_edit_value", values.get("listViewEditValue"));

		// Portal specific fields
		put("portal_edit_tag", values.get("portalEditTag"));
		put("portal_edit_strategy", values.get("portalEditStrategy"));
		put("portal_edit_value", values.get("portalEditValue"));
		put("portal_detail_tag", values.get("portalDetailTag"));
		put("portal_detail_strategy", values.get("portalDetailStrategy"));
		put("portal_detail_value", values.get("portalDetailValue"));

		// ListView Filter Create
		put("filter_view_tag", values.get("filterViewTag"));
		put("filter_view_strategy", values.get("filterViewStrategy"));
		put("filter_view_value", values.get("filterViewValue"));

		// Save a detail-mode control for later use if the hook def is complete. 
		if(get("detail_hook_tag") != null && get("detail_hook_strategy") != null &&
				get("detail_hook_value") != null) {
				detailControl = new VoodooControl(get("detail_hook_tag"),
					get("detail_hook_strategy"), get("detail_hook_value"));
		}
		else
			detailControl = null;

		// Save an edit-mode control for later use if the hook def is complete.
		if(get("edit_hook_tag") != null && get("edit_hook_strategy") != null
				&& get("edit_hook_value") != null && get("type") != null) {

			// Use a VoodooSelect if it's a select field, VoodooBWCRelate
			// if it's a bwcrelate field, VoodooControl otherwise.
			switch(get("type")) {
				case "select":
					editControl = new VoodooSelect(get("edit_hook_tag"),
							get("edit_hook_strategy"), get("edit_hook_value"));
					break;
				case "bwcrelate":
					editControl = new VoodooBWCRelate(get("edit_hook_tag"),
							get("edit_hook_strategy"), get("edit_hook_value"));
					break;
				case "date":
					editControl = new VoodooDate(get("edit_hook_tag"),
							get("edit_hook_strategy"), get("edit_hook_value"));
					break;
				case "tag":
					editControl = new VoodooTag(get("edit_hook_tag"),
							get("edit_hook_strategy"), get("edit_hook_value"));
					break;
				default:
					editControl = new VoodooControl(get("edit_hook_tag"),
							get("edit_hook_strategy"), get("edit_hook_value"));
			}
		}

		// Save a mass update control for later use if the hook def is complete. 
		if(get("mass_update_tag") != null && get("mass_update_strategy") != null &&
				get("mass_update_value") != null) {

			// Use a VoodooSelect if it's a select field, VoodooBWCRelate
			// if it's a bwcrelate field, VoodooControl otherwise.
			if(get("type") != null && get("type").equals("select"))
				massUpdateControl = new VoodooSelect(get("mass_update_tag"),
						get("mass_update_strategy"), get("mass_update_value"));
			else if(get("type") != null && get("type").equals("bwcrelate"))
				massUpdateControl = new VoodooBWCRelate(get("mass_update_tag"),
						get("mass_update_strategy"), get("mass_update_value"));
			else
				massUpdateControl = new VoodooControl(get("mass_update_tag"),
						get("mass_update_strategy"), get("mass_update_value"));
		}
		else
			massUpdateControl = null;

		// Save a mass preview pane control for later use if the hook def is complete. 
		if(get("preview_tag") != null && get("preview_strategy") != null &&
				get("preview_value") != null) {

			// Use a VoodooSelect if it's a select field, VoodooBWCRelate
			// if it's a bwcrelate field, VoodooControl otherwise.
			if(get("type") != null && get("type").equals("select"))
				previewPaneControl = new VoodooSelect(get("preview_tag"),
						get("preview_strategy"), get("preview_value"));
			else if(get("type") != null && get("type").equals("bwcrelate"))
				previewPaneControl = new VoodooBWCRelate(get("preview_tag"),
						get("preview_strategy"), get("preview_value"));
			else
				previewPaneControl = new VoodooControl(get("preview_tag"),
						get("preview_strategy"), get("preview_value"));
		}
		else
			previewPaneControl = null;
		
		// Save a listViewDetail control for later use if the hook def is complete. 
		if(get("listview_detail_tag") != null && get("listview_detail_strategy") != null &&
				get("listview_detail_value") != null) {

			listViewDetailControl = new VoodooControl(
					get("listview_detail_tag"),
					get("listview_detail_strategy"),
					get("listview_detail_value"));
		}
		else
			listViewDetailControl = null;
		
		// Save a listViewEdit control for later use if the hook def is complete. 
		if(get("listview_edit_tag") != null && get("listview_edit_strategy") != null &&
				get("listview_edit_value") != null) {

			// Use a VoodooSelect if it's a select field, VoodooBWCRelate
			// if it's a bwcrelate field, VoodooControl otherwise.
			if(get("type") != null && get("type").equals("select"))
				listViewEditControl = new VoodooSelect(get("listview_edit_tag"),
						get("listview_edit_strategy"), get("listview_edit_value"));
			else if(get("type") != null && get("type").equals("bwcrelate"))
				listViewEditControl = new VoodooBWCRelate(get("listview_edit_tag"),
						get("listview_edit_strategy"), get("listview_edit_value"));
			else
				listViewEditControl = new VoodooControl(get("listview_edit_tag"),
						get("listview_edit_strategy"), get("listview_edit_value"));
		}
		else
			listViewEditControl = null;
		
		// PORTAL SPECIFIC!
		// Save a portal-detail-mode control for later use if the hook def is complete. 
		if(get("portal_detail_tag") != null && get("portal_detail_strategy") != null && get("portal_detail_value") != null) {
			portalDetailControl = new VoodooControl(get("portal_detail_tag"), get("portal_detail_strategy"), get("detail_hook_value"));	
		}
		else
			portalDetailControl = null;

		// Save an portal-edit-mode control for later use if the hook def is complete.
		if(get("portal_edit_tag") != null && get("portal_edit_strategy") != null && get("portal_edit_value") != null) {
			// Use a VoodooSelect if it's a select field
			if(get("type") != null && get("type").equals("select"))
				portalEditControl = new VoodooSelect(get("portal_edit_tag"), get("portal_edit_strategy"), get("portal_edit_value"));
			else
				portalEditControl = new VoodooControl(get("portal_edit_tag"), get("portal_edit_strategy"), get("portal_edit_value"));
		} 
		else
			portalEditControl = null;

		// Save a filterCreate view control for later use if the hook def is complete.
		if(get("filter_view_tag") != null && get("filter_view_strategy") != null &&
				get("filter_view_value") != null) {

			// Use a VoodooSelect if it's a select field, VoodooBWCRelate
			// if it's a bwcrelate field, VoodooControl otherwise.
			if(get("type") != null && get("type").equals("select"))
				filterCreateControl = new VoodooSelect(get("filter_view_tag"),
						get("filter_view_strategy"), get("filter_view_value"));
			else if(get("type") != null && get("type").equals("bwcrelate"))
				filterCreateControl = new VoodooBWCRelate(get("filter_view_tag"),
						get("filter_view_strategy"), get("filter_view_value"));
			else
				filterCreateControl = new VoodooControl(get("filter_view_tag"),
						get("filter_view_strategy"), get("filter_view_value"));
		}
		else
			filterCreateControl = null;
	}
	
	/**
	 * Return a VoodooControl or VoodooSelect representing this fields edit mode control on the listView
	 * @param rowNum 1-based index of the row
	 * @return	a VoodooControl representing this field as rendered on the ListView in edit mode.
	 * @throws Exception
	 */
	public VoodooControl getListViewEditControl(int rowNum) throws Exception {
		if(get("type").equals("select")){
			return new VoodooSelect(get("listview_edit_tag"), "css", "tbody tr:nth-of-type(" + rowNum + ") " + get("listview_edit_value"));
		}
		else
			return new VoodooControl(get("listview_edit_tag"), "css", "tbody tr:nth-of-type(" + rowNum + ") " + get("listview_edit_value"));
	}
	
	/**
	 * Return a VoodooControl representing this fields detail mode control on the listView
	 * @param rowNum 1-based index of the row
	 * @return	a VoodooControl representing this field as rendered on the ListView in detail mode.
	 * @throws Exception
	 */
	public VoodooControl getListViewDetailControl(int rowNum) throws Exception {
		return new VoodooControl(get("listview_detail_tag"), "css", "tbody tr:nth-of-type(" + rowNum + ") " + get("listview_detail_value"));
	}
	
	/**
	 * BWC Version 
	 * Return a VoodooControl representing this fields detail mode control on the listView
	 * @param rowNum 1-based index of the row
	 * @return	a VoodooControl representing this field as rendered on the ListView in detail mode.
	 * @throws Exception
	 */
	public VoodooControl getListViewDetailControlBwc(int rowNum) throws Exception {
		return new VoodooControl(get("listview_detail_tag"), "css", "table.list.view tbody tr:nth-of-type(" + (rowNum+2) + ") " + get("listview_detail_value"));
	}
}