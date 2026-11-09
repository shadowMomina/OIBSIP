package com.hana.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private Button btnStart, btnStop, btnReset, btnLap;
    private ArrayList<String> laps = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean running = false;
    private long startBase = 0;      // clock value that corresponds to "time zero"
    private long pausedElapsed = 0;  // elapsed time saved when stopped

    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            updateDisplay();
            if (running) {
                handler.postDelayed(this, 50);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);
        ListView lvLaps = findViewById(R.id.lvLaps);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, laps);
        lvLaps.setAdapter(adapter);

        btnStart.setOnClickListener(v -> startTimer());
        btnStop.setOnClickListener(v -> stopTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btnLap.setOnClickListener(v -> recordLap());

        updateDisplay();
        updateButtons();
    }

    private void startTimer() {
        if (running) return;
        startBase = SystemClock.elapsedRealtime() - pausedElapsed;
        running = true;
        handler.removeCallbacks(ticker);
        handler.post(ticker);
        updateButtons();
    }

    private void stopTimer() {
        if (!running) return;
        pausedElapsed = SystemClock.elapsedRealtime() - startBase;
        running = false;
        handler.removeCallbacks(ticker);
        updateDisplay();
        updateButtons();
    }

    private void resetTimer() {
        running = false;
        handler.removeCallbacks(ticker);
        pausedElapsed = 0;
        laps.clear();
        adapter.notifyDataSetChanged();
        updateDisplay();
        updateButtons();
    }

    private void recordLap() {
        if (!running) return;
        laps.add(0, "Lap " + (laps.size() + 1) + "   " + format(getElapsed(), true));
        adapter.notifyDataSetChanged();
    }

    private long getElapsed() {
        return running ? SystemClock.elapsedRealtime() - startBase : pausedElapsed;
    }

    private void updateDisplay() {
        tvTime.setText(format(getElapsed(), false));
    }

    // Start is disabled while running; Stop is disabled while stopped
    private void updateButtons() {
        btnStart.setEnabled(!running);
        btnStop.setEnabled(running);
        btnLap.setEnabled(running);
        btnReset.setEnabled(running || pausedElapsed > 0);
    }

    private String format(long ms, boolean withHundredths) {
        long totalSeconds = ms / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (withHundredths) {
            long hundredths = (ms % 1000) / 10;
            return String.format(Locale.getDefault(), "%02d:%02d:%02d.%02d",
                    hours, minutes, seconds, hundredths);
        }
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Stop redrawing while the app is in the background
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(ticker);
    }

    // Resume redrawing; elapsed time comes from the system clock, so it is still correct
    @Override
    protected void onResume() {
        super.onResume();
        if (running) {
            handler.post(ticker);
        }
        updateDisplay();
        updateButtons();
    }
}