package com.thebitcorps.connect5.models;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebitcorps.connect5.R;

import java.util.Random;

/**
 * Created by diegollams on 11/27/15.
 *
 */
public class ConnectBoard {
	private static final String TAG = "shit";
	public static final int COLUMNS = 7;
	//WARNING only 8 columns supported now
	public static final int ROWS = 8;
	private static final int FITNESS_MEDIA = 14;
	private static final int NUMBER_OF_GENERATIONS = 1000;
	private static final int  WIN_NUMBER_CONNECTED = 5;
	private static final float MUTATE_RANDOM_CONSTANT = 0.99f;
	private static final int NUMER_RANDOM_ELEMENTS = 30;
	ConnectPoint[][] points = new ConnectPoint[ROWS][COLUMNS];

	/**
	 * will create the matrix of buttons the size will be COLUMNS x ROWS
	 * @param context the context that the buttons will be living
	 */
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

	/**
	 * @throws ArrayIndexOutOfBoundsException if x or y is negative or is bigger than the matrixx
	 * @param x position in the x axis of the Button grid to get
	 * @param y position in the y axis of the Button grid to get
	 * @return return  the proper Button of points[x][y[
	 */
	public Button getButton(int x , int y){
		if (x > ROWS || y > COLUMNS || x < 0 || y < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return points[x][y].getButton();
	}

	/**
	 * @throws ArrayIndexOutOfBoundsException if x or y is negative or is bigger than the matrixx
	 * @param x position in the x axis of the ConnectPoint grid to get
	 * @param y position in the y axis of the ConnectPoint grid to get
	 * @return return the proper ConnectPoint of points[x][y[
	 */
	public ConnectPoint getPoint(int x , int y){
		if (x > ROWS || y > COLUMNS || x < 0 || y < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return points[x][y];
	}

	/**
	 * giving a starting x and y position will count every connected point of the same playerType
	 * in every posible valid move
	 * @param x x position in the board where it will stard
	 * @param y y position in the board where it will stard
	 * @return true if the any of the posible connected situation sums 5
	 */
	public boolean checkPointsForWin(int playerType,int x,int y){
		if(checkPointsForWinRecursive(x,y,  1 ,0,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  -1 ,0,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  0 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  0 ,-1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  1 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  -1 ,-1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  -1 ,1,1)){
			return true;
		}
		else if(checkPointsForWinRecursive(x,y,  1 ,-1,1)){
			return true;
		}
//		add diagonal  checks
		return false;
	}

	/**
	 *
	 * @param x current x position in the board  will be starting to count
	 * @param y current y position in the board  will be starting to count
	 * @param adderX this will be add to the x position so when counting the next position will start in the x + adderX
	 * @param adderY this will be add to the y position so when counting the next position will start in the y + adderY
	 * @param connectedPoints the current count of same id of ConnectPoint is selected
	 * @return true if the connectedPoints sum is equals 5 return false if the x or y is outofbound or if the next point is no the same as the current
	 */
	private boolean checkPointsForWinRecursive(int x,int y,int adderX,int adderY,int connectedPoints){
		final int newX = x + adderX;
		final int newY = y + adderY;

		if(connectedPoints == WIN_NUMBER_CONNECTED)
			return true;
		if(newX >= ROWS ||newX < 0 || newY>= COLUMNS ||newY < 0){
			return false;
		}
		else if(points[x][y].getPlayerSelection() != points[newX][newY].getPlayerSelection()){
			return false;
		}
		else{
			return checkPointsForWinRecursive(newX,newY,adderX,adderY,connectedPoints +1);
		}
	}

	/**
	 * count from the starting poin [x][y] for the sum of fitnes that point gets
	 * @param player the player that call this action
	 * @param x x position where will start count
	 * @param y x position where will start count
	 * @return the sum of all point selected by a pleyer that are conected with the starting point [x][y] lokking diagonal horizontal and verticall
	 */
	public int checkCountForFitness(int player,int x,int y){
		int fitness = 0;
		fitness += checkCountForFitnessRecursive(player,x,y,1,0,0);
		fitness += checkCountForFitnessRecursive(player,x,y,-1,0,0);
		fitness += checkCountForFitnessRecursive(player,x,y,0,1,0);
		fitness += checkCountForFitnessRecursive(player,x,y,0,-1,0);
		fitness += checkCountForFitnessRecursive(player,x,y,1,1,0);
		fitness += checkCountForFitnessRecursive(player,x,y,-1,-1,0);
		fitness += checkCountForFitnessRecursive(player,x,y,-1,1,0);
		fitness += checkCountForFitnessRecursive(player,x,y,1,-1,0);
		return fitness;
	}

	/**
	 * Recursive call to same for the next position until reaches a unselected point or the end of the board
	 * @param player the player that call this action
	 * @param x x position where will start count
	 * @param y y position where will start count
	 * @param adderX this will be add to the x position so when counting the next position will start in the x + adderX
	 * @param adderY this will be add to the y position so when counting the next position will start in the y + adderY
	 * @param connectedPoints connectedPoints the current count of same id of ConnectPoint is selected
	 * @return the count of connected points in all valid moves  add to if th next point is the same status of the player param
	 */
	private int checkCountForFitnessRecursive(int player,int x,int y,int adderX,int adderY,int connectedPoints){
		final int newX = x + adderX;
		final int newY = y + adderY;
		if(newX >= ROWS ||newX < 0 || newY>= COLUMNS ||newY < 0){
			return connectedPoints;
		}
		else if( !points[newX][newY].isSelected()){
			return connectedPoints;
		}
		else{
			if(player == getPoint(newX,	newY).getPlayerSelection())
				return checkCountForFitnessRecursive(player,newX, newY, adderX, adderY, connectedPoints +1);
			else
				return checkCountForFitnessRecursive(player,newX, newY, adderX, adderY, connectedPoints +2);
		}
	}



	/**
	 * @return true if all the postions in the board are selected by a plaeyer false if no
	 */
	public boolean isFull(){
		for (int x = 0; x <ROWS ; x++) {
			for (int y = 0; y < COLUMNS; y++) {
				if(!getPoint(x,y).isSelected()) return false;
			}
		}
		return true;
	}


	/**
	 * check if the given position in the board can be set by a player
	 * @param x position in the x axis
	 * @param y  position in the y axis
	 * @return whether can be set or not
	 */
	public boolean isPointSelectable(int x, int y) {
		if(x < 0 || x >= ROWS || y < 0 || y >= COLUMNS ) return false;
		return !points[x][y].isSelected();
	}


	/**
	 * shufles an array of point with the Fisher–Yates alogorithm
	 * @param points the array of point to be shuffle
	 */
	private static void shufflePoints(Point[] points){
		if(points == null) return;
		Random random = new Random();
		for (int i = points.length - 1; i > 0 ; i--) {
			int index = random.nextInt( i +1);
			Point a = points[index];
			points[index] = points[i];
			points[i] = a;
		}
	}

	/**
	 *	Generate NUMBER_OF_GENERATIONS of generation then calls @getPoints with true to use max selection to get one fitness selected point
	 *	to then cross it with other fitness selected point
	 * @return the last point generated by the algforithm
	 */
	public Point generatePointGeneticWithMax() {
		Point firstPoint = getPoints();
		Point secondPoint = getPoints();
//		Log.e(TAG,firstPoint.getX() +" "+ secondPoint.getX() +" "+firstPoint.getFitness());
		Random mutateRandom = new Random();
		for (int i = 0; i < NUMBER_OF_GENERATIONS; i++) {
			firstPoint = getPoints(firstPoint,true);
			secondPoint = getPoints(secondPoint,true);
			if(isPointSelectable(firstPoint.getX(),secondPoint.getY()) && isPointSelectable(secondPoint.getX(),secondPoint.getY())) {
				if(mutateRandom.nextFloat() > MUTATE_RANDOM_CONSTANT){
					firstPoint = new Point(firstPoint.getX(), secondPoint.getY());
					secondPoint = new Point(secondPoint.getX(), firstPoint.getY());
					Log.v(TAG,"MUTATE");
				}
			}

		}
		return firstPoint;
	}
	/**
	 *	Generate NUMBER_OF_GENERATIONS of generation then calls @getPoints to get one fitness selected point
	 *	to then cross it with other fitness selected point
	 * @return the last point generated by the algforithm
	 */
	public Point generatePointGenetic(){
		Point firstPoint = getPoints();
		Point secondPoint = getPoints();
//		Log.e(TAG,firstPoint.getX() +" "+ secondPoint.getX() +" "+firstPoint.getFitness());
		Random mutateRandom = new Random();
		for (int i = 0; i < NUMBER_OF_GENERATIONS; i++) {
			firstPoint = getPoints(firstPoint,false);
			secondPoint = getPoints(secondPoint,false);
			if(isPointSelectable(firstPoint.getX(),secondPoint.getY()) && isPointSelectable(secondPoint.getX(),secondPoint.getY())) {
				if(mutateRandom.nextFloat() > MUTATE_RANDOM_CONSTANT){
					firstPoint = new Point(firstPoint.getX(), secondPoint.getY());
					secondPoint = new Point(secondPoint.getX(), firstPoint.getY());
					Log.v(TAG,"MUTATE");
				}
			}

		}
		return firstPoint;
	}

	/**
	 *
	 * @return
	 */
	private Point getPoints() {
		Point[] newPoints = new Point[NUMER_RANDOM_ELEMENTS];
		for (int i = 0; i < NUMER_RANDOM_ELEMENTS; i++) {
			newPoints[i] = getRandomPosition();
			newPoints[i].setFitness(checkCountForFitness(ConnectPoint.PLAYER_TWO_VALUE,newPoints[i].getX(), newPoints[i].getY()));
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
//		Log.e(TAG,"no llego " + randonFitness);
		return newPoints[NUMER_RANDOM_ELEMENTS -1];

	}

	/**
	 *
	 * @param healthiest
	 * @param getMax
	 * @return
	 */
	private Point getPoints(Point healthiest,boolean getMax) {
		Point[] newPoints = new Point[NUMER_RANDOM_ELEMENTS];
		newPoints[0] = healthiest;
		for (int i = 1; i < NUMER_RANDOM_ELEMENTS; i++) {
			newPoints[i] = getRandomPosition();
			newPoints[i].setFitness(checkCountForFitness(ConnectPoint.PLAYER_TWO_VALUE,newPoints[i].getX(), newPoints[i].getY()));
		}
		if(getMax){
			int max = 0;
			int maxPos = 0;
			for (int i = 0; i < NUMER_RANDOM_ELEMENTS; i++) {
				if(newPoints[i].getFitness() > max)
				{
					max = newPoints[i].getFitness();
					maxPos = i;
				}
			}
			return newPoints[maxPos];
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
//		Log.e(TAG,"no llego " + randonFitness + " "+summatoryFitness);

		return healthiest;

	}

	/**
	 *
	 * @return
	 */
	public Point getRandomPosition(){
		if(isFull()){
			throw new Error("Board is full");
		}
		Point randomPoint;
		Random random =  new Random();
		do {
			randomPoint = new Point(random.nextInt(ROWS),random.nextInt(COLUMNS));
			randomPoint.setFitness(checkCountForFitness(ConnectPoint.PLAYER_TWO_VALUE,randomPoint.getX(),randomPoint.getY()));
		}while(points[randomPoint.getX()][randomPoint.getY()].isSelected() || randomPoint.getFitness() == 0 );
		return randomPoint;
	}

	/**
	 *
	 * @param player
	 * @param xPos
	 * @param yPos
	 * @return
	 */
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

	/**
	 *
	 * @param player
	 * @param xPos
	 * @param yPos
	 * @return
	 */
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
	/**
	 * Deprecated only will work for small 
	 */
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
