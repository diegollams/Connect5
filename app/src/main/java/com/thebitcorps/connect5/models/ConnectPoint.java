package com.thebitcorps.connect5.models;

import android.widget.Button;

import com.thebitcorps.connect5.R;

/**
 * Created by diegollams on 11/19/15.
 */
public class ConnectPoint {
	private Button button;
	private int x,y;
	private int fitness;
	private int playerSelection;




	public ConnectPoint(Button button, int x, int y) {
		this.playerSelection = 0;
		this.button = button;
		this.x = x;
		this.y = y;
	}

	public static final int PLAYER_ONE_VALUE = 1;
	public static final int PLAYER_TWO_VALUE = 2;
	public static final int UNSELECTED_VALUE = 0;

	public int getPlayerSelection() {
		return playerSelection;
	}


	public boolean isSelected(){
		return this.getPlayerSelection() != UNSELECTED_VALUE;
	}

	public void setPlayerSelection(int playerSelection) {
		if(playerSelection == UNSELECTED_VALUE){
			this.playerSelection = playerSelection;
			this.button.setBackgroundResource( R.drawable.button_background_default);
			return;
		}
		//Raise custom exception for display error and prevent user turn change
		this.playerSelection = playerSelection;
//		this.button.setText(Integer.toString(this.getPlayerSelection()));
		this.button.setBackgroundResource(playerSelection == PLAYER_ONE_VALUE ? R.color.first_player_color : R.color.second_player_color);
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}
	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

//	public int getPoint() {
//		return point;
//	}
//
//	public void setPoint(int point) {
//		this.point = point;
//	}
}
