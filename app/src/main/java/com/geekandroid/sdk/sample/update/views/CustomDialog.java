package com.geekandroid.sdk.sample.update.views;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog{

private	 Context context;

	    public CustomDialog(Context context) {
	        super(context);
	        // TODO Auto-generated constructor stub
	        this.context = context;
	    }
	    public CustomDialog(Context context, int theme){
	    	  super(context, theme);
	        this.context = context;
	    }

	}

