package com.thebitcorps.connect5.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebitcorps.connect5.R;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by diegollams on 11/27/15.
 */
public class ConnectBoard {
	private static final String TAG = "shit";
	public static final int COLUMNS = 7;
	public static final int ROWS = 8;
	private static final int FITNESS_MEDIA = 12;
	private static final int NUMBER_OF_GENERATIONS = 1000;
	private static final int  WIN_NUMBER_CONNECTED = 5;
	private static final float MUTATE_RANDOM_CONSTANT = 0.8f;
	private static final int NUMER_RANDOM_ELEMENTS = 5;
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

	public int checkCountForFitness(int x,int y){
		int fitness = 0;
		fitness += checkCountForFitnessRecursive(x,y,1,0,0);
		fitness += checkCountForFitnessRecursive(x,y,-1,0,0);
		fitness += checkCountForFitnessRecursive(x,y,0,1,0);
		fitness += checkCountForFitnessRecursive(x,y,0,-1,0);
		fitness += checkCountForFitnessRecursive(x,y,1,1,0);
		fitness += checkCountForFitnessRecursive(x,y,-1,-1,0);
		fitness += checkCountForFitnessRecursive(x,y,-1,1,0);
		fitness += checkCountForFitnessRecursive(x,y,1,-1,0);
		return fitness;
	}

	private int checkCountForFitnessRecursive(int x,int y,int adderX,int adderY,int connectedPoints){
		final int newX = x + adderX;
		final int newY = y + adderY;
		if(newX >= ROWS ||newX < 0 || newY>= COLUMNS ||newY < 0){
			return connectedPoints;
		}
		else if( !points[newX][newY].isSelected()){
			return connectedPoints;
		}
		else{
			return checkCountForFitnessRecursive(newX, newY, adderX, adderY, connectedPoints +1);
		}
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
	public boolean isFull(){
		for (int x = 0; x <ROWS ; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				if(!getPoint(x,y).isSelected()) return false;
			}
		}
		return true;
	}


	private static void shufflePoints(Point[] points){
		Random random = new Random();
		for (int i = points.length - 1; i > 0 ; i--) {
			int index = random.nextInt( i +1);
			Point a = points[index];
			points[index] = points[i];
			points[i] = a;
		}
	}

	public Point generatePointGenetic(){
		Point firstPoint = getPoints();
		Point secondPoint = getPoints();
		Random mutateRandom = new Random();
		for (int i = 0; i < NUMBER_OF_GENERATIONS; i++) {
			firstPoint = getPoints(firstPoint);
			Log.e(TAG,firstPoint.getX() +" "+ secondPoint.getX() +" "+firstPoint.getFitness());
			secondPoint = getPoints(secondPoint);
			if(isPointSelectable(firstPoint.getX(),secondPoint.getY()) && isPointSelectable(secondPoint.getX(),secondPoint.getY())) {
				if(mutateRandom.nextFloat() > MUTATE_RANDOM_CONSTANT){
//					firstPoint = new Point(firstPoint.getX(), secondPoint.getY());
//					secondPoint = new Point(secondPoint.getX(), firstPoint.getY());
					Log.v(TAG,"MUTATE");
				}
			}

		}
		return firstPoint;
	}


	public boolean isPointSelectable(int x, int y) {
		if(x < 0 || x >= ROWS || y < 0 || y >= COLUMNS ) return false;
		return !points[x][y].isSelected();
	}

	private Point getPoints() {
		Point[] newPoints = new Point[NUMER_RANDOM_ELEMENTS];
		for (int i = 0; i < NUMER_RANDOM_ELEMENTS; i++) {
			newPoints[i] = getRandomPosition();
			newPoints[i].setFitness(checkCountForFitness(newPoints[i].getX(), newPoints[i].getY()));
		}
		shufflePoints(newPoints);
		Random random = new Random();
		final int randonFitness =  random.nextInt(FITNESS_MEDIA);
		int summatoryFitness = 0;
		for (int i = 0; i < NUMER_RANDOM_ELEMENTS; i++) {
			summatoryFitness += newPoints[i].getFitness();
			if(summatoryFitness >= randonFitness){
				return newPoints[i];

			}
		}
		//if the summatory dint reach the fitness assign the last one arbitrary
		return newPoints[NUMER_RANDOM_ELEMENTS -1];

	}
	private Point getPoints(Point healthiest) {
		Point[] newPoints = new Point[NUMER_RANDOM_ELEMENTS];
		newPoints[0] = healthiest;
		for (int i = 1; i < NUMER_RANDOM_ELEMENTS; i++) {
			newPoints[i] = getRandomPosition();
			newPoints[i].setFitness(checkCountForFitness(newPoints[i].getX(), newPoints[i].getY()));
		}
		shufflePoints(newPoints);
		Random random = new Random();
		final int randonFitness =  random.nextInt(FITNESS_MEDIA);
		int summatoryFitness = 0;
		for (int i = 0; i < NUMER_RANDOM_ELEMENTS; i++) {
			summatoryFitness += newPoints[i].getFitness();
			if(summatoryFitness >= randonFitness){
				return newPoints[i];

			}
		}
		//if the summatory dint reach the fitness assign the last one arbitrary
		return newPoints[NUMER_RANDOM_ELEMENTS -1];

	}

	public Point getRandomPosition(){
//		TODO raise exeption
		if(isFull()){
			return null;
		}
		Point randomPoint;
		Random random =  new Random();
		do {
			randomPoint = new Point(random.nextInt(ROWS),random.nextInt(COLUMNS));
			randomPoint.setFitness(checkCountForFitness(randomPoint.getX(),randomPoint.getY()));
		}while(points[randomPoint.getX()][randomPoint.getY()].isSelected() || randomPoint.getFitness() == 0 );
		return randomPoint;
	}

	private int min(int player,int xPos , int yPos){
		if(checkPointsForWin(ConnectPoint.PLAYER_TWO_VALUE,xPos,yPos)){return  1;}
		if(isFull()){return 0;}
		int auxiliar,better = 9999;
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				if(!getPoint(x,y).isSelected()){
					getPoint(x,y).setPlayerSelection(ConnectPoint.PLAYER_ONE_VALUE);
					auxiliar = max(player,x,y);
					if(auxiliar < better){
						better = auxiliar;
//						getPoint(x,y).setPlayerSelection(ConnectPoint.UNSELECTED_VALUE);
//						return auxiliar;

					}
					getPoint(x,y).setPlayerSelection(ConnectPoint.UNSELECTED_VALUE);
				}
			}
		}
		return better ;
	}


	private int max(int player,int xPos , int yPos){
		if(checkPointsForWin(ConnectPoint.PLAYER_ONE_VALUE,xPos,yPos)){return  -1;}
		if(isFull()){return 0;}
		int auxiliar,better = -9999;
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				if(!getPoint(x,y).isSelected()){
					getPoint(x,y).setPlayerSelection(ConnectPoint.PLAYER_TWO_VALUE);
					auxiliar = min(player, x, y);
					if(auxiliar > better){
						better = auxiliar;
//						getPoint(x,y).setPlayerSelection(ConnectPoint.UNSELECTED_VALUE);
//						return auxiliar;
					}
					getPoint(x,y).setPlayerSelection(ConnectPoint.UNSELECTED_VALUE);

				}
			}
		}
		return better;
	}

	@Deprecated
	public Point generateMinMaxPLay(int player){
		Point point =null;
		int auxiliar,better = -9999;
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				if(!getPoint(x,y).isSelected()){
					getPoint(x,y).setPlayerSelection(ConnectPoint.PLAYER_TWO_VALUE);
					auxiliar = min(player,x,y);
					if(auxiliar > better){
						better = auxiliar;
						point = new Point(x,y);
					}
					getPoint(x,y).setPlayerSelection(ConnectPoint.UNSELECTED_VALUE);
				}
			}
		}
		return  point;
	}
}
