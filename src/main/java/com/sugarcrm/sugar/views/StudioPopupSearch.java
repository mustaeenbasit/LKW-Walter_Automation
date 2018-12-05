package com.sugarcrm.sugar.views;

public class StudioPopupSearch extends View {
	protected static StudioPopupSearch view;
	
	private StudioPopupSearch() throws Exception {}
	
	public static StudioPopupSearch getInstance() throws Exception {
		if (view == null)
			view = new StudioPopupSearch();
		return view;
	}
}