package com.example.toki.billiardscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView pl1;
    private TextView pl2;
    private int player1points = 0;
    private int player2points = 0;
    private CheckBox whiteOnYellow;
    private CheckBox whiteOnRed;
    private CheckBox whiteOnYellowOnRed;
    private CheckBox yellowOnWhite;
    private CheckBox yellowOnRed;
    private CheckBox yellowOnWhiteOnRed;
    private CheckBox whiteOnPins;
    private CheckBox yellowOnPins;
    private CheckBox white_pin[];
    int whitePinsFalled = 0;
    private CheckBox red_pin;
    int shotNumber = 1;
    private RadioButton player1_turn;
    private RadioButton player2_turn;
    private Button player1shot;
    private Button player2shot;
    private int player1_setsWon = 0;
    private TextView p1_setsWon;
    private int player2_setsWon = 0;
    private TextView p2_setsWon;
    private int pointsPerSet = 0;
    private int setNumberLimit = 0;
    private int setNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rulesBtn = findViewById(R.id.rules);
        rulesBtn.setOnClickListener(rules);

        pl1 = findViewById(R.id.player1_name);
        pl2 = findViewById(R.id.player2_name);

        p1_setsWon = findViewById(R.id.player1_sets_won);
        p2_setsWon = findViewById(R.id.player2_sets_won);

        getPreferencesUpdate();

        player1_turn = findViewById(R.id.player1_turn);
        player1_turn.setChecked(true);

        player2_turn = findViewById(R.id.player2_turn);
        player2_turn.setChecked(false);

        whiteOnYellow = findViewById(R.id.white_on_yellow);
        whiteOnYellow.setChecked(false);
        whiteOnYellow.setOnClickListener(whiteOnYellowListener);

        whiteOnRed = findViewById(R.id.white_on_red);
        whiteOnRed.setChecked(false);
        whiteOnRed.setOnClickListener(whiteOnRedListener);

        whiteOnYellowOnRed = findViewById(R.id.white_on_yellow_on_red);
        whiteOnYellowOnRed.setChecked(false);
        whiteOnYellowOnRed.setOnClickListener(whiteOnYellowOnRedListener);

        yellowOnWhite = findViewById(R.id.yellow_on_white);
        yellowOnWhite.setChecked(false);
        yellowOnWhite.setOnClickListener(yellowOnWhiteListener);

        yellowOnRed = findViewById(R.id.yellow_on_red);
        yellowOnRed.setChecked(false);
        yellowOnRed.setOnClickListener(yellowOnRedListener);

        yellowOnWhiteOnRed = findViewById(R.id.yellow_on_white_on_red);
        yellowOnWhiteOnRed.setChecked(false);
        yellowOnWhiteOnRed.setOnClickListener(yellowOnWhiteOnRedListener);

        whiteOnPins = findViewById(R.id.white_on_pins);
        whiteOnPins.setChecked(false);

        yellowOnPins = findViewById(R.id.yellow_on_pins);
        yellowOnPins.setChecked(false);

        white_pin = new CheckBox[4];
        white_pin[0] = findViewById(R.id.white_pin0);
        white_pin[1] = findViewById(R.id.white_pin1);
        white_pin[2] = findViewById(R.id.white_pin2);
        white_pin[3] = findViewById(R.id.white_pin3);
        for (int i = 0; i < 4; i++) {
            white_pin[i].setChecked(false);
        }

        red_pin = findViewById(R.id.red_pin);
        red_pin.setChecked(false);

        player1shot = findViewById(R.id.add_button_player_1);
        player1shot.setOnClickListener(score);
        player2shot = findViewById(R.id.add_button_player_2);
        player2shot.setOnClickListener(score);

        Button matchReset = findViewById(R.id.matchReset);
        matchReset.setOnClickListener(mReset);

        setTurnVisibility();
        display();

    }

    @Override
    // Method called to update preferences linked values after preference update
    protected void onResume() {
        super.onResume();
        getPreferencesUpdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Collect the values of preferences selected by the user
     */
    public void getPreferencesUpdate() {
        PreferenceManager.setDefaultValues(this, R.xml.settings_main, false);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String player1_name = sharedPrefs.getString(getString(R.string.settings_player_1_name_key), getString(R.string.settings_player_1_name_default));
        String player2_name = sharedPrefs.getString(getString(R.string.settings_player_2_name_key), getString(R.string.settings_player_2_name_default));
        String pPS = sharedPrefs.getString(getString(R.string.settings_points_per_set_key), getString(R.string.settings_points_per_set_default));
        String sNL = sharedPrefs.getString(getString(R.string.settings_set_number_key), getString(R.string.settings_set_number_default));
        pl1.setText(player1_name);
        pl2.setText(player2_name);
        pointsPerSet = Integer.valueOf(pPS);
        setNumberLimit = Integer.valueOf(sNL);
        display();

    }

    View.OnClickListener rules = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rules_url)));
            startActivity(browserIntent);
        }
    };

    View.OnClickListener score = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == player1shot) {
                calculatePlayer_1_shot();
                player1_turn.setChecked(false);
                player2_turn.setChecked(true);
                checkPoints();
                setTurnVisibility();
                resetFields();
                display();
            } else if (v == player2shot) {
                calculatePlayer_2_shot();
                player2_turn.setChecked(false);
                player1_turn.setChecked(true);
                checkPoints();
                setTurnVisibility();
                resetFields();
                display();
            }
        }
    };

    View.OnClickListener mReset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            confirmMatchReset();
        }
    };

    View.OnClickListener whiteOnYellowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (whiteOnYellow.isChecked()) {
                whiteOnYellowOnRed.setVisibility(View.VISIBLE);
            } else {
                whiteOnYellowOnRed.setVisibility(View.INVISIBLE);
            }
        }
    };

    View.OnClickListener whiteOnRedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (whiteOnRed.isChecked()) {
                whiteOnYellowOnRed.setVisibility(View.INVISIBLE);
            } else {
                whiteOnYellowOnRed.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener whiteOnYellowOnRedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (whiteOnYellowOnRed.isChecked()) {
                whiteOnRed.setVisibility(View.INVISIBLE);
            } else {
                whiteOnRed.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener yellowOnWhiteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (yellowOnWhite.isChecked()) {
                yellowOnWhiteOnRed.setVisibility(View.VISIBLE);
            } else {
                yellowOnWhiteOnRed.setVisibility(View.INVISIBLE);
            }
        }
    };

    View.OnClickListener yellowOnRedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (yellowOnRed.isChecked()) {
                yellowOnWhiteOnRed.setVisibility(View.INVISIBLE);
            } else {
                yellowOnWhiteOnRed.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener yellowOnWhiteOnRedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (yellowOnWhiteOnRed.isChecked()) {
                yellowOnRed.setVisibility(View.INVISIBLE);
            } else {
                yellowOnRed.setVisibility(View.VISIBLE);
            }
        }
    };

    /*
    Checks if any player won a set or even the match, calling appropriate message show method
     */
    public void checkPoints() {
        if (player1points >= pointsPerSet) {
            player1_setsWon += 1;
            if (player1_setsWon >= ((setNumberLimit + 1) / 2)) {
                showMessageMatchWon();
            } else {
                showMessageSetWon();
            }
            setNumber += 1;
            newSet();
            display();
            newSetTurn();
        } else if (player2points >= pointsPerSet) {
            player2_setsWon += 1;
            if (player2_setsWon >= ((setNumberLimit + 1) / 2)) {
                showMessageMatchWon();
            } else {
                showMessageSetWon();
            }
            setNumber += 1;
            newSet();
            display();
            newSetTurn();
        }
    }

    /*
    Set start turn for every new set. First set is started by white ball. Then, the turn is alternate
     */
    public void newSetTurn() {
        for (int i = 1; i < setNumberLimit; i = i + 2) {
            if (setNumber == i) {
                player1_turn.setChecked(true);
                player2_turn.setChecked(false);
            } else if (setNumber == (i + 1)) {
                player2_turn.setChecked(true);
                player1_turn.setChecked(false);
            }
        }
    }

    /*
    verify how many white pins are falled after shot
     */
    public void verifyWhitePinFalled() {
        for (int i = 0; i < 4; i++) {
            if (white_pin[i].isChecked()) {
                whitePinsFalled += 1;
            }
        }
    }

    /*
    * calculate the points on player 1 shot
    */
    public void calculatePlayer_1_shot() {
        // On very first shot of each set, it's forbidden score points
        verifyWhitePinFalled();
        if (shotNumber == 1) {
            // add points for each white pin falled
            if (whiteOnYellow.isChecked()) {
                if (whitePinsFalled != 0) {
                    player2points += 2 * whitePinsFalled;
                }

                // add points when red pin falls
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player2points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player2points += 4;
                }

                // add points when the white ball touches the red one after it touched the yellow one
                if (whiteOnRed.isChecked()) {
                    player2points += 4;
                } else {
                    // add points when the yellow ball, touched by the white one, touches the red one
                    if (whiteOnYellowOnRed.isChecked()) {
                        player2points += 3;
                    }
                }

            } else {
                // fault points
                player2points += 2;

                // points for every white pin falled
                if (whitePinsFalled != 0) {
                    player2points += 2 * whitePinsFalled;
                }

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player2points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player2points += 4;
                }

                // points for touching red ball first
                if (whiteOnRed.isChecked()) {
                    player2points += 4;
                }

            }
        } else {
            // add points for each white pin falled
            if (whiteOnYellow.isChecked()) {
                int points = 0;

                if (whitePinsFalled != 0) {
                    points += 2 * whitePinsFalled;
                }

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    points += 4;
                }

                // add points when the white ball touches the red one after it touched the yellow one
                if (whiteOnRed.isChecked()) {
                    points += 4;
                } else {
                    // add points when the yellow ball, touched by the white one, touches the red one
                    if (whiteOnYellowOnRed.isChecked()) {
                        points += 3;
                    }

                }

                // if white ball strikes pins, all the points are loss and gained by the opponent player
                if (whiteOnPins.isChecked()) {
                    player2points += points;
                } else {
                    player1points += points;
                }

            } else {
                // fault points
                player2points += 2;

                // points for every white pin falled
                if (whitePinsFalled != 0) {
                    player2points += 2 * whitePinsFalled;
                }

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player2points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player2points += 4;
                }

                // points for touching red ball first
                if (whiteOnRed.isChecked()) {
                    player2points += 4;
                }
            }
        }

        shotNumber += 1;
        whitePinsFalled = 0;
    }

    /*
    * calculate the points on player 2 shot
    */
    public void calculatePlayer_2_shot() {
        verifyWhitePinFalled();
        if (shotNumber == 1) {
            if (yellowOnWhite.isChecked()) {

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player1points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player1points += 4;
                }

                // points for every white pin falled
                if (whitePinsFalled != 0) {
                    player1points += 2 * whitePinsFalled;
                }

                // add points when the yellow ball touches the red one after it touched the white one
                if (yellowOnRed.isChecked()) {
                    player1points += 4;
                } else {
                    // add points when the white ball, touched by the yellow one, touches the red one
                    if (yellowOnWhiteOnRed.isChecked()) {
                        player1points += 3;
                    }
                }

            } else {
                // fault points
                player1points += 2;

                // points for every white pin falled
                if (whitePinsFalled != 0) {
                    player1points += 2 * whitePinsFalled;
                }

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player1points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player1points += 4;
                }

                // points for touching red ball first
                if (yellowOnRed.isChecked()) {
                    player1points += 4;
                }
            }
        } else {

            if (yellowOnWhite.isChecked()) {
                int points = 0;

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    points += 4;
                }

                // add points for each white pin falled
                if (whitePinsFalled != 0) {
                    points += 2 * whitePinsFalled;
                }

                // points for touching red ball after have touched white one
                if (yellowOnRed.isChecked()) {
                    points += 4;
                } else {
                    // points for whitw ball, touched from yellow one, touching red ball
                    if (yellowOnWhiteOnRed.isChecked()) {
                        points += 3;
                    }
                }

                // if yellow ball strikes pins, all the points are loss and gained by the opponent player
                if (yellowOnPins.isChecked()) {
                    player1points += points;
                } else {
                    player2points += points;
                }

            } else {
                // fault points
                player1points += 2;

                // points for every white pin falled
                if (whitePinsFalled != 0) {
                    player1points += 2 * whitePinsFalled;
                }

                // points for red pin falled alone or with white ones
                if (red_pin.isChecked() && whitePinsFalled == 0) {
                    player1points += 10;
                } else if (red_pin.isChecked() && whitePinsFalled != 0) {
                    player1points += 4;
                }

                // points for touching red ball first
                if (yellowOnRed.isChecked()) {
                    player1points += 4;
                }
            }
        }

        shotNumber += 1;
        whitePinsFalled = 0;
    }

    /*
    Sets the checkboxes visibility, for every turn change
     */
    public void setTurnVisibility() {
        if (player1_turn.isChecked()) {
            player1shot.setVisibility(View.VISIBLE);
            whiteOnYellow.setVisibility(View.VISIBLE);
            whiteOnRed.setVisibility(View.VISIBLE);
            whiteOnYellowOnRed.setVisibility(View.INVISIBLE);
            whiteOnPins.setVisibility(View.VISIBLE);
            player2shot.setVisibility(View.INVISIBLE);
            yellowOnWhite.setVisibility(View.INVISIBLE);
            yellowOnRed.setVisibility(View.INVISIBLE);
            yellowOnWhiteOnRed.setVisibility(View.INVISIBLE);
            yellowOnPins.setVisibility(View.INVISIBLE);
        } else if (player2_turn.isChecked()) {
            player2shot.setVisibility(View.VISIBLE);
            yellowOnWhite.setVisibility(View.VISIBLE);
            yellowOnRed.setVisibility(View.VISIBLE);
            yellowOnWhiteOnRed.setVisibility(View.INVISIBLE);
            yellowOnPins.setVisibility(View.VISIBLE);
            player1shot.setVisibility(View.INVISIBLE);
            whiteOnYellow.setVisibility(View.INVISIBLE);
            whiteOnRed.setVisibility(View.INVISIBLE);
            whiteOnYellowOnRed.setVisibility(View.INVISIBLE);
            whiteOnPins.setVisibility(View.INVISIBLE);
        }
    }

    /*
    Resets checkboxes checked state, setting all of them to unchecked
     */
    public void resetFields() {
        for (int i = 0; i < 4; i++) {
            white_pin[i].setChecked(false);
        }

        red_pin.setChecked(false);
        whiteOnRed.setChecked(false);
        whiteOnYellow.setChecked(false);
        whiteOnYellowOnRed.setChecked(false);
        whiteOnPins.setChecked(false);
        yellowOnWhite.setChecked(false);
        yellowOnRed.setChecked(false);
        yellowOnWhiteOnRed.setChecked(false);
        yellowOnPins.setChecked(false);
    }

    /*
    Reset variables and checkboxes to onCreate settings
     */
    public void newMatch() {
        player1_turn.setChecked(true);
        player2_turn.setChecked(false);
        player1points = 0;
        player2points = 0;
        player1_setsWon = 0;
        player2_setsWon = 0;
        resetFields();
        setTurnVisibility();
        shotNumber = 1;
        setNumber = 1;
        display();
    }

    /*
    Reset only some variables and all of checkboxes, at every set start shot
     */
    public void newSet() {
        player1points = 0;
        player2points = 0;
        resetFields();
        shotNumber = 1;
        display();

    }

    /*
    AlertDialog to get confirm of match reset, after the relative button is clicked
     */
    public void confirmMatchReset() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.warning));
        alertDialog
                .setMessage(getString(R.string.match_reset_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newMatch();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.canc), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    /*
    Alert dialog to show set won message
     */
    public void showMessageSetWon() {
        if (player1points >= pointsPerSet) {
            String message = pl1.getText().toString() + getString(R.string.won_set_n) + setNumber;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.set_won));
            alertDialog
                    .setMessage(message)
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

        if (player2points >= pointsPerSet) {
            String message = pl2.getText().toString() + getString(R.string.won_set_n) + setNumber;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.set_won));
            alertDialog
                    .setMessage(message)
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    /*
    Alert dialog to show match won message
     */
    public void showMessageMatchWon() {
        if (player1_setsWon >= ((setNumberLimit + 1) / 2)) {
            String message = pl1.getText().toString() + getString(R.string.won_set_n) + setNumber + ".\n";
            message = message + pl1.getText().toString() + getString(R.string.won_match);
            message = message + getString(R.string.play_another_match);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.match_won));
            alertDialog
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            newMatch();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

        if (player2_setsWon >= ((setNumberLimit + 1) / 2)) {
            String message = pl2.getText().toString() + getString(R.string.won_set_n) + setNumber + ".\n";
            message = message + pl2.getText().toString() + getString(R.string.won_match);
            message = message + getString(R.string.play_another_match);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.match_won));
            alertDialog
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            newMatch();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    /*
    Display the values of text views after every event
     */
    public void display() {

        TextView player1_points = findViewById(R.id.player1_points);
        String p1points = Integer.toString(player1points);
        player1_points.setText(p1points);

        TextView player2_points = findViewById(R.id.player2_points);
        String p2points = Integer.toString(player2points);
        player2_points.setText(p2points);

        TextView shotNum = findViewById(R.id.shot_number);
        String shotN = Integer.toString(shotNumber);
        shotNum.setText(shotN);

        String p1sw = Integer.toString(player1_setsWon);
        p1_setsWon.setText(p1sw);

        String p2sw = Integer.toString(player2_setsWon);
        p2_setsWon.setText(p2sw);

    }
}