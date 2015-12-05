package com.thebitcorps.connect5;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebitcorps.connect5.listeners.ConnectPointListener;
import com.thebitcorps.connect5.models.ConnectBoard;
import com.thebitcorps.connect5.models.ConnectPoint;
import com.thebitcorps.connect5.models.Point;


/**
 * SharePrefenreces: @CPU_BOOLEAN this key is the one for the cpu mode if set to false two player mode will be acive
 *					CPU_BOOLEAN_MAX this key is the one for the cpumax mode also you need to set false the CPU_BOOLEAN to prevent the cpu make two moves
 */
public class MainActivity extends AppCompatActivity {
	private static final String TAG = "shit";
	private static final int[] layout_ids = {R.id.first_row,R.id.second_row,R.id.third_row,R.id.fourth_row,R.id.fifth_row,R.id.sixth_row,R.id.seventh_row,R.id.eighth_row};
	private Boolean is_first_player;
	private TextView firstPlayerTextView;
	private TextView secondPlayerTextView;
	private static final String SECOND_PLAYER_NAME = "Player 2";
	private static final String CPU_PLAYER_NAME = "CPU";
	/////Preferences keys
	private static final String FIRST_PLAYER_SCORE = "firstPlayer";
	private static final String SECOND_PLAYER_SCORE = "secondPlayer";
	private static final String CPU_BOOLEAN = "CPU";
	private static final String CPU_BOOLEAN_MAX = "CPUMAX";
	////COLORS
	private  final int FIRST_PLAYER_COLOR = Color.parseColor("#ffffff");
	private final int SECOND_PLAYER_COLOR = Color.parseColor("#ffffff");
	private static final int INACTIVE_PLAYER_COLOR = Color.parseColor("#FFCC00");
	private ConnectBoard board;

	private Boolean cpuPlaying;
	private Boolean cpuMax;

	/*
	*	Will change the player turn and the color of the textviews for the current player
	*	if a cpu mode is active will execute the proper generato for the cpu move
	* */
	private void changePlayer(){

		firstPlayerTextView.setTextColor(is_first_player ? FIRST_PLAYER_COLOR : INACTIVE_PLAYER_COLOR);
		secondPlayerTextView.setTextColor(is_first_player ? INACTIVE_PLAYER_COLOR : SECOND_PLAYER_COLOR);
		is_first_player = is_first_player ? false  : true;
		if((!is_first_player) && cpuPlaying ) {
			makeCpuMove();
		}
		else if((!is_first_player) && cpuMax ) {
			makeCpuMaxMove();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//set the type of player
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		cpuPlaying = settings.getBoolean(CPU_BOOLEAN,false);
		cpuMax = settings.getBoolean(CPU_BOOLEAN_MAX,false);
		is_first_player = true;
		//set elements of activity
		firstPlayerTextView = (TextView) findViewById(R.id.first_player_text_view);
		secondPlayerTextView = (TextView) findViewById(R.id.second_player_text_view);
		secondPlayerTextView.setText(cpuPlaying ? CPU_PLAYER_NAME : SECOND_PLAYER_NAME);
		secondPlayerTextView.setTextColor(SECOND_PLAYER_COLOR);
		//initialize the board
		board = new ConnectBoard(this);

		for (int x = 0;x < ConnectBoard.ROWS;x++){
			for (int y = 0;y < ConnectBoard.COLUMNS;y++) {
				//make clicklistener for every button in the board
				board.getButton(x,y).setOnClickListener(new ConnectPointListener(is_first_player, board.getPoint(x, y)) {
					@Override
					public void onClick(View v) {
						super.onClick(v);
						//verify if button clicked can be click
						if(board.isPointSelectable(connectPoint.getX(),connectPoint.getY())){
							connectPoint.setPlayerSelection(is_first_player ? ConnectPoint.PLAYER_ONE_VALUE : ConnectPoint.PLAYER_TWO_VALUE);
							//if the move was a winner one
							SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
							if (board.checkPointsForWin(connectPoint.getPlayerSelection(), connectPoint.getX(), connectPoint.getY())) {
								SharedPreferences.Editor editor = settings.edit();
								//add one game win to the current player
								if (is_first_player) {
									editor.putInt(FIRST_PLAYER_SCORE, settings.getInt(FIRST_PLAYER_SCORE, 0) + 1);
								} else {
									editor.putInt(SECOND_PLAYER_SCORE, settings.getInt(SECOND_PLAYER_SCORE, 0) + 1);
								}
								editor.commit();
								showFinishedGameAlert(true);
							//game finish with no winner
							} else {
								if(board.isFull()){
									showFinishedGameAlert(false);
									return;
								}
								changePlayer();
							}
						}
					}
				});
			}
		}
		//add all buttons to layout
		for (int x = 0;x < ConnectBoard.ROWS;x++) {
			LinearLayout linearLayout = (LinearLayout) findViewById(layout_ids[x]);
			for (int y = 0; y < ConnectBoard.COLUMNS; y++) {
				linearLayout.addView(board.getButton(x, y));
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.restart_game) {
			restartGame();
			return true;
		}
		//clear all the share prefecences
		else if (id == R.id.restore_score){
			SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.clear();
			editor.commit();
			return true;
		}
		//activate cpu mode and if current player two will make a cpu move
		else if (id == R.id.cpu_mode){
			//when they change is trigger if is in second player turn make cpu move
			if(!is_first_player){
				makeCpuMove();
			}
			setCpuPlayer(true);
			return true;
		}
		else if (id == R.id.two_players_mode){
			setCpuPlayer(false);
			return true;
		}
		//activate cpu max mode and if current player two will make a cpu move
		else if (id == R.id.cpu_mode_max) {
			if(!is_first_player){
				makeCpuMaxMove();
			}
			setCpuMaxPlayer(true);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	*	Show a alert that display the current user as the winner of the match or if @param isWinner is false will tie message
	 *	also will display the record of wins of every player
	 *@param isWinner if true will show current user as winner if false will show tie message
	* */
	private void showFinishedGameAlert(boolean isWinner){
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		String title;
		if (isWinner){
			title = "Gano" + (is_first_player ? "Jugador 1" : " Jugador 2");
		}
		else{
			title = "Empate";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
		builder.setTitle(title);
		builder.setMessage("Jugador 1: " + settings.getInt(FIRST_PLAYER_SCORE, 0) + "\nJugador 2: " + settings.getInt(SECOND_PLAYER_SCORE, 0));
		builder.setPositiveButton("OK", null);
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				restartGame();
			}
		});
		builder.show();
	}

	/**
	 * Get a point generate wit a genetic max algorithm al trigger that point button @onClick
	 */
	private void makeCpuMaxMove() {
		Point point = board.generatePointGeneticWithMax();
		board.getButton(point.getX(),point.getY()).performClick();
	}

	/**
	 * Get a point generate wit a genetic max algorithm al trigger that point button @onClick
	 */
	private void makeCpuMove() {
		Point point = board.generatePointGenetic();
		board.getButton(point.getX(),point.getY()).performClick();
	}

	/**
	 * will restart activity so the last game is discarded
	 */
	private void restartGame() {
		finish();
		startActivity(new Intent(getApplicationContext(),MainActivity.class));
	}

	/**
	 * will change sharePreferences, so the cpuMaxmode is active and will make the cpuMode inactive
	 * @param isCpuPlaying is true will set cpuMaxMode active if no will active the twoPlayers mode
	 */
	private void setCpuMaxPlayer(boolean isCpuPlaying){
		secondPlayerTextView.setText(isCpuPlaying ? CPU_PLAYER_NAME : SECOND_PLAYER_NAME);
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(CPU_BOOLEAN,false);
		editor.putBoolean(CPU_BOOLEAN_MAX,isCpuPlaying);
		editor.commit();
		cpuPlaying = isCpuPlaying;
	}

	/**
	 * will change sharePreferences, so the cpumode is active and will make the cpuMaxMode inactive
	 *  @param isCpuPlaying is true will set cpuMode active if no will active the twoPlayers mode
	 */
	private void setCpuPlayer(boolean isCpuPlaying){
		secondPlayerTextView.setText(isCpuPlaying ? CPU_PLAYER_NAME : SECOND_PLAYER_NAME);
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(CPU_BOOLEAN,isCpuPlaying);
		editor.putBoolean(CPU_BOOLEAN_MAX,false);
		editor.commit();
		cpuPlaying = isCpuPlaying;
	}

}
