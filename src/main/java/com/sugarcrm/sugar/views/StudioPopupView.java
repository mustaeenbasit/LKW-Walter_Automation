package com.sugarcrm.sugar.views;

public class StudioPopupView extends View {
	protected static StudioPopupView view;
	
	public StudioPopupListView popupListView;
	public StudioPopupSearch popupSearch;    		
	
	private StudioPopupView() throws Exception {
		popupListView = StudioPopupListView.getInstance();
		popupSearch = StudioPopupSearch.getInstance();
	}
	
	public static StudioPopupView getInstance() throws Exception {
		if (view == null)
			view = new StudioPopupView();
		return view;
	}
}