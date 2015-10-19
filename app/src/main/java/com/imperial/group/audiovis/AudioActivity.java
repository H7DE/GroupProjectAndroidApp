package com.imperial.group.audiovis;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.nio.Buffer;
import java.nio.ShortBuffer;

/**
 * Created by rick on 19/10/2015.
 */
public class AudioActivity extends Activity {

    public static final String DEBUG = "DEBUG";
    private static final long REPEAT_INTERVAL = 40;
    private AudioRecord recorder;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private Thread recordingThread = null;
    private boolean isRecording = false;
    private TextView textView;

    private Handler handler;

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

    static int counter = 0;
    private void startRecording() {
        final int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
        int BytesPerElement = 2; // 2 bytes in 16bit format
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
//        int counter = 0;
        Runnable updateVisualizer = new Runnable() {
            @Override
            public void run() {

                if (isRecording) // if we are already recording
                {
                    textView.setText("Counter:" + ++counter);
                    // update in 40 milliseconds
                    handler.postDelayed(this, REPEAT_INTERVAL);
                }
            }
        };

        handler.post(updateVisualizer);
//        recordingThread = new Thread(new Runnable() {
//            public void run() {
//                Log.d(DEBUG, "RECORDING");
//                short[] audioData = new short[BufferElements2Rec];
//                int counter = 0;
////                while(isRecording){
////                    int bufferReadResult = recorder.read(audioData, 0, audioData.length);
////                    if(bufferReadResult == BufferElements2Rec){
////                        Buffer realAudioData1024 = ShortBuffer.wrap(audioData, 0, 1024);
//////                        textView.setText("Counter:" + 0);
////                    }
////
////                }
//            }
//        }, "AudioRecorder Thread");
//        runOnUiThread(recordingThread);
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
            Log.d(DEBUG, "STOPPED RECORDING");
        }
    }


    @Override
    protected void onPause() {
        stopRecording();
        super.onPause();
    }
}
