package com.thebitcorps.connect5.models;

import android.util.Pair;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by diegollams on 11/19/15.
 */
public class ConnectPoint {
	private Button button;
	public static final int PLAYER_ONE_VALUE = 1;
	public static final int PLAYER_TWO_VALUE = 2;
	public static final int UNSELECTED_VALUE = 0;

	public int getPlayerSelection() {
		return playerSelection;
	}

	public void setPlayerSelection(int playerSelection) {
		//Raise custom exception for display error and prevent user turn change
		if( this.getPlayerSelection() != UNSELECTED_VALUE) return;
		this.playerSelection = playerSelection;
		this.button.setText(Integer.toString(this.getPlayerSelection()));
	}

	private int playerSelection;

	private ArrayList<Point> neighbors;

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public ConnectPoint(Button button) {
		this.playerSelection = 0;
		this.neighbors = new ArrayList<Point>();
		this.button = button;
	}

	public ArrayList<Point> getNeighbors() {
		return neighbors;
	}

}
