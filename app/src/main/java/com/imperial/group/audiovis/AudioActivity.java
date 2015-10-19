package com.imperial.group.audiovis;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by rick on 19/10/2015.
 */
public class AudioActivity extends Activity {

    public static final String DEBUG = "DEBUG";
    private AudioRecord ar;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private Thread recordingThread = null;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);
//        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
//                RECORDER_SAMPLERATE, RECORDER_AUDIO_ENCODING, AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING));

        TextView msg = (TextView) findViewById(R.id.textView2);
        msg.setText("Foo");
//        Log.d("DEBUG", "" + ar.getState());

        //CONFIGURE BUTTONS
        //START/STOP recording

        //Visualize audio data
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.recordingCheckBox:
                if (checked) {
                    Log.d(DEBUG, "TRUE");
                } else {
                    Log.d(DEBUG, "FALSE");
                }
                break;
        }
    }

}
