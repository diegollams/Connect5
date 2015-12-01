package com.thebitcorps.connect5;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "shit";
	private static final int[] layout_ids = {R.id.first_row,R.id.second_row,R.id.third_row,R.id.fourth_row,R.id.fifth_row,R.id.sixth_row,R.id.seventh_row,R.id.eighth_row};
	private Boolean is_first_player;
	private TextView firstPlayerTextView;
	private TextView secondPlayerTextView;
	/////Preferences keys
	private static final String FIRST_PLAYER_SCORE = "firstPlayer";
	private static final String SECOND_PLAYER_SCORE = "secondPlayer";
	////COLORS
	private  final int FIRST_PLAYER_COLOR = Color.parseColor("#ffffff");
	private final int SECOND_PLAYER_COLOR = Color.parseColor("#ffffff");
	private static final int INACTIVE_PLAYER_COLOR = Color.parseColor("#FFCC00");
	private ConnectBoard board;

	private Boolean cpuPlaying;

	private void changePlayer(){

		firstPlayerTextView.setTextColor(is_first_player ? FIRST_PLAYER_COLOR : INACTIVE_PLAYER_COLOR);
		secondPlayerTextView.setTextColor(is_first_player ? INACTIVE_PLAYER_COLOR : SECOND_PLAYER_COLOR);
		is_first_player = is_first_player ? false  : true;
		if((!is_first_player) && cpuPlaying ) {
//			Point point = board.generateMinMaxPLay(ConnectPoint.PLAYER_TWO_VALUE);
			Point point = board.generatePointGenetic();
			board.getButton(point.getX(),point.getY()).performClick();
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cpuPlaying = true	;
		firstPlayerTextView = (TextView) findViewById(R.id.first_player_text_view);
		secondPlayerTextView = (TextView) findViewById(R.id.second_player_text_view);
		secondPlayerTextView.setTextColor(FIRST_PLAYER_COLOR);
		is_first_player = true;
		board = new ConnectBoard(this);
		for (int x = 0;x < ConnectBoard.ROWS;x++){
			for (int y = 0;y < ConnectBoard.COLUMNS;y++) {
				board.getButton(x,y).setOnClickListener(new ConnectPointListener(is_first_player, board.getPoint(x, y)) {
					@Override
					public void onClick(View v) {
						super.onClick(v);
						if(board.isPointSelectable(connectPoint.getX(),connectPoint.getY())){
							if(board.isFull()){
								restartGame();
								return;
							}
							connectPoint.setPlayerSelection(is_first_player ? ConnectPoint.PLAYER_ONE_VALUE : ConnectPoint.PLAYER_TWO_VALUE);
							if (board.checkPointsForWin(connectPoint.getPlayerSelection(), connectPoint.getX(), connectPoint.getY())) {
								SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = settings.edit();
								if (is_first_player) {
									editor.putInt(FIRST_PLAYER_SCORE, settings.getInt(FIRST_PLAYER_SCORE, 0) + 1);
								} else {
									editor.putInt(SECOND_PLAYER_SCORE, settings.getInt(SECOND_PLAYER_SCORE, 0) + 1);
								}
								editor.commit();
								String scoreFirstPLayer = Integer.toString(settings.getInt(FIRST_PLAYER_SCORE, 0));
								String scoreSecondPLayer = Integer.toString(settings.getInt(SECOND_PLAYER_SCORE, 0));
								AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AppCompatAlertDialogStyle);
								builder.setTitle("Gano" + (is_first_player ? "Jugador 1" : " Jugador 2"));
								builder.setMessage("Jugador 1: " + scoreFirstPLayer + "\nJugador 2: " + scoreSecondPLayer);
								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
								builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
									@Override
									public void onDismiss(DialogInterface dialog) {
										restartGame();
									}
								});
								builder.show();

							} else {
								changePlayer();
							}
						}
					}
				});
			}
		}
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
		else if (id == R.id.restore_score){
			SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.clear();
			editor.commit();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void restartGame() {
		finish();
		startActivity(new Intent(getApplicationContext(),MainActivity.class));
	}
}
