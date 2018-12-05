package com.sugarcrm.sugar.views;

public class StudioConvertLeadView extends View {
	protected static StudioConvertLeadView view;
	
	private StudioConvertLeadView() throws Exception {}
	
	public static StudioConvertLeadView getInstance() throws Exception {
		if (view == null)
			view = new StudioConvertLeadView();
		return view;
	}
 }