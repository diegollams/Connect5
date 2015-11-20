package com.thebitcorps.connect5;


import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebitcorps.connect5.listeners.ConnectPointListener;
import com.thebitcorps.connect5.models.ConnectPoint;


public class MainActivity extends ActionBarActivity {
	private static final String TAG = "shit";
	private static final int[] layout_ids = {R.id.first_row,R.id.second_row,R.id.third_row,R.id.fourth_row,R.id.fifth_row,R.id.sixth_row,R.id.seventh_row,R.id.eighth_row};
	private static final int ROWS = 8;
	private static final int COLUMNS = 10;
	private Boolean is_first_player;
	private TextView firstPlayerTextView;
	private TextView secondPlayerTextView;
	private static final int ACTIVE_PLAYER_COLOR = Color.BLUE;
	private static final int INACTIVE_PLAYER_COLOR = Color.BLACK;
	private static final int  WIN_NUMBER_CONNECTED = 5;
	ConnectPoint[][] points = new ConnectPoint[ROWS][COLUMNS];


	private void changePlayer(){
		is_first_player = is_first_player ? false  : true;
		firstPlayerTextView.setTextColor(is_first_player ? ACTIVE_PLAYER_COLOR : INACTIVE_PLAYER_COLOR);
		secondPlayerTextView.setTextColor(is_first_player ? INACTIVE_PLAYER_COLOR : ACTIVE_PLAYER_COLOR);
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
		if(checkPointsForWinRecursive(playerPoint,x,y,  0 ,-1,1)){
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		firstPlayerTextView = (TextView) findViewById(R.id.first_player_text_view);
		firstPlayerTextView.setTextColor(ACTIVE_PLAYER_COLOR);
		secondPlayerTextView = (TextView) findViewById(R.id.second_player_text_view);
		is_first_player = true;
		for (int x = 0;x < ROWS;x++){
			for (int y = 0;y < COLUMNS;y++) {
				Button button = new Button(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
				layoutParams.setMargins(0, 0, 0, 0);
				button.setLayoutParams(layoutParams);
				button.setPadding(0, 0, 0, 0);
				points[x][y] = new ConnectPoint(button,x,y);
				points[x][y].getButton().setOnClickListener(new ConnectPointListener(is_first_player,points[x][y]){
					@Override
					public void onClick(View v) {
						super.onClick(v);
						if (!connectPoint.is_selected()){
							connectPoint.setPlayerSelection(is_first_player ? ConnectPoint.PLAYER_ONE_VALUE : ConnectPoint.PLAYER_TWO_VALUE);
							if(checkPointsForWin(connectPoint.getPlayerSelection(),connectPoint.getX(),connectPoint.getY())){

								AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AppCompatAlertDialogStyle);
								builder.setTitle("Gano");
								builder.setMessage("Lorem ipsum dolor ....");
								builder.setPositiveButton("OK", null);
								builder.show();
							}
							changePlayer();
						}

					}
				});
			}
		}
		for (int x = 0;x < ROWS;x++) {
			LinearLayout linearLayout = (LinearLayout) findViewById(layout_ids[x]);
			for (int y = 0; y < COLUMNS; y++) {
				linearLayout.addView(points[x][y].getButton());
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
			finish();
			startActivity(new Intent(getApplicationContext(),MainActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
