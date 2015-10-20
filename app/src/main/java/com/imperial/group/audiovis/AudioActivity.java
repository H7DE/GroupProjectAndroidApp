package com.imperial.group.audiovis;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class AudioActivity extends Activity {

    public static final String DEBUG = "DEBUG";
    private static final long REPEAT_INTERVAL_IN_MILLI = 80;
    private AudioRecord recorder;
    private static final int RECORDER_SAMPLERATE = 41000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private boolean isRecording = false;
    private TextView textView;
    private Handler handler;
    public static final int ONE_KB = 1024;
    public static final int BYTES_IN_SHORT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);
        textView = (TextView) findViewById(R.id.textView2);
        textView.setText("----------------------------------------");
        handler = new Handler();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.recordingCheckBox:
                if (checked) {
                    Log.d(DEBUG, "TRUE");
                    startRecording();
                } else {
                    stopRecording();
                    Log.d(DEBUG, "FALSE");
                }
                break;
        }
    }


    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, ONE_KB * BYTES_IN_SHORT);

        recorder.startRecording();
        isRecording = true;
        final short sData[] = new short[ONE_KB];

        //TODO: find better concurrency mechanism
        Runnable updateVisualizer = new Runnable() {
            @Override
            public void run() {
                final StringBuilder sb = new StringBuilder();
                if (isRecording) {
                    recorder.read(sData, 0, ONE_KB);
                    for (int i = 0; i < ONE_KB; i += 32) {
                        sb.append(intToString(sData[i])).append("\n");
                    }
                    textView.setText(sb.toString());

                    handler.postDelayed(this, REPEAT_INTERVAL_IN_MILLI);
                }
            }
        };

        handler.post(updateVisualizer);
    }

    //Convert an integer value into a number of '*' for audio visualisation
    String intToString(int x) {
        String str = "";
        x = Math.abs(x);
        x /= 16;
        for (int i = 0; i <= x; ++i) {
            str += "*";
        }
        return str;
    }

    private void stopRecording() {
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
//            recordingThread = null;
            Log.d(DEBUG, "STOPPED RECORDING");
        }
    }


    @Override
    protected void onPause() {
        stopRecording();
        super.onPause();
    }
}
