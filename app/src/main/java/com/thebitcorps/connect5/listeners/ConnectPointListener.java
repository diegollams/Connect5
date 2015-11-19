package com.thebitcorps.connect5.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.thebitcorps.connect5.MainActivity;
import com.thebitcorps.connect5.models.ConnectPoint;

/**
 * Created by diegollams on 11/19/15.
 */
public class ConnectPointListener implements View.OnClickListener {

	protected Boolean isFirstPlayer;
	protected ConnectPoint connectPoint;

	public ConnectPointListener(Boolean isFirstPlayer, ConnectPoint connectPoint) {
		this.isFirstPlayer = isFirstPlayer;
		this.connectPoint = connectPoint;

	}

	@Override
	public void onClick(View v) {


	}
}
