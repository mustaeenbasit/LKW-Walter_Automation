package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.AppModel;

public class StudioSearchView extends View {
	protected static StudioSearchView view;
	
	public StudioFilterSearch filterSearch;
	public StudioBasicSearch basicSearch;
	public StudioAdvancedSearch advancedSearch;
	
	private StudioSearchView() throws Exception {
		filterSearch = StudioFilterSearch.getInstance();
		basicSearch = StudioBasicSearch.getInstance();
		advancedSearch = StudioAdvancedSearch.getInstance();
	}
	
	public static StudioSearchView getInstance() throws Exception {
		if (view == null)
			view = new StudioSearchView();
		return view;
	}
}