package com.thebitcorps.connect5;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebitcorps.connect5.listeners.ConnectPointListener;
import com.thebitcorps.connect5.models.ConnectPoint;


public class MainActivity extends ActionBarActivity {
	private static final int[] layout_ids = {R.id.first_row,R.id.second_row,R.id.third_row,R.id.fourth_row,R.id.fifth_row,R.id.sixth_row,R.id.seventh_row,R.id.eighth_row};
	private static final int rows = 8;
	private static final int columns = 10;
	private Boolean is_first_player;
	ConnectPoint[][] points = new ConnectPoint[rows][columns];

	public void changePlayer(){
		is_first_player = is_first_player ? false  : true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		is_first_player = true;
		for (int x = 0;x < rows;x++){
			for (int y = 0;y < columns;y++) {
				Button button = new Button(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
				layoutParams.setMargins(0, 0, 0, 0);
				button.setLayoutParams(layoutParams);
				button.setPadding(0, 0, 0, 0);
				points[x][y] = new ConnectPoint(button);
				points[x][y].getButton().setOnClickListener(new ConnectPointListener(is_first_player,points[x][y]){
					@Override
					public void onClick(View v) {
						super.onClick(v);
						connectPoint.setPlayerSelection(is_first_player ? ConnectPoint.PLAYER_ONE_VALUE : ConnectPoint.PLAYER_TWO_VALUE);
						changePlayer();
					}
				});
			}
		}
		for (int x = 0;x <rows;x++) {
			LinearLayout linearLayout = (LinearLayout) findViewById(layout_ids[x]);
			for (int y = 0; y < columns; y++) {
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
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
