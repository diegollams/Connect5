package com.thebitcorps.connect5.models;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebitcorps.connect5.R;

/**
 * Created by diegollams on 11/27/15.
 */
public class ConnectBoard {
	public static final int ROWS = 8;
	public static final int COLUMNS = 10;
	private static final int  WIN_NUMBER_CONNECTED = 5;
	ConnectPoint[][] points = new ConnectPoint[ROWS][COLUMNS];

	public ConnectBoard(Context context) {
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				Button button = new Button(context);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
				layoutParams.setMargins(0, 0, 0, 0);
				button.setLayoutParams(layoutParams);
				button.setPadding(0, 0, 0, 0);
				button.setBackgroundResource(R.drawable.button_background_default);
				points[x][y] = new ConnectPoint(button,x,y);
			}
		}
	}

	public Button getButton(int x , int y){
		if (x > ROWS || y > COLUMNS || x < 0 || y < 0) return null;
		return points[x][y].getButton();
	}

	public ConnectPoint getPoint(int x , int y){
		if (x > ROWS || y > COLUMNS || x < 0 || y < 0) return null;
		return points[x][y];
	}

	public boolean checkPointsForWin(int playerPoint,int x,int y){
		if(checkPointsForWinRecursive(playerPoint,x,y,  1 ,0,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  -1 ,0,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  0 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  0 ,-1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  1 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  -1 ,-1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  -1 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(playerPoint,x,y,  1 ,-1,1)){
			return true;
		}
//		add diagonal  checks
		return false;
	}

	private boolean checkPointsForWinRecursive(int playerPoint,int x,int y,int adderX,int adderY,int connectedPoints){
		final int newX = x + adderX;
		final int newY = y + adderY;
		if(newX >= ROWS ||newX < 0 || newY>= COLUMNS ||newY < 0){
			return false;
		}
		else if(points[x][y].getPlayerSelection() != points[newX][newY].getPlayerSelection()){
			return false;
		}
		else if(connectedPoints + 1 == WIN_NUMBER_CONNECTED){
			return true;
		}
		else{
			return checkPointsForWinRecursive(playerPoint,newX,newY,adderX,adderY,connectedPoints +1);
		}
	}
}
