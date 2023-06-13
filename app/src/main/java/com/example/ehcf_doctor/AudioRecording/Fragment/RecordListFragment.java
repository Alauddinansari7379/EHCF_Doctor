package com.example.ehcf_doctor.AudioRecording.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ehcf_doctor.AudioRecording.Adapter.Rec_list_adapter;
import com.example.ehcf_doctor.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;


public class RecordListFragment extends AppCompatActivity implements Rec_list_adapter.onItemList_click {

    private ConstraintLayout audio_playersheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView record_list;
    private File[] allFiles;
    private Rec_list_adapter rec_list_adapter;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private File file_toPlay;
    private ImageButton play_btn;
    private ImageButton play_prev_btn;
    private ImageButton play_forw_btn;
    private TextView player_file_name;
    private TextView player_title;
    private ImageView imgBack;
    private SeekBar seekBar;
    private Handler seekbarHandler;
    private Runnable updateseekbar;


    public RecordListFragment() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record_list);


        audio_playersheet = findViewById(R.id.playersheet);
        bottomSheetBehavior = BottomSheetBehavior.from(audio_playersheet);
        record_list = findViewById(R.id.record_recycler_list);

        play_btn = findViewById(R.id.play_btn);
        play_prev_btn = findViewById(R.id.play_back_btn);
        play_forw_btn = findViewById(R.id.play_forward_btn);
        player_file_name = findViewById(R.id.player_file_name);
        player_title = findViewById(R.id.player_title);
        seekBar = findViewById(R.id.seekbar_player);
        imgBack = findViewById(R.id.imgBack);


        //Storing recording files into Files array

        String rec_path_files = this.getExternalFilesDir("/").getAbsolutePath();
        File dir = new File(rec_path_files);
        allFiles = dir.listFiles();

        rec_list_adapter = new Rec_list_adapter(allFiles,this,this);
        record_list.setHasFixedSize(true);
        record_list.setLayoutManager(new LinearLayoutManager(this));
        record_list.setAdapter(rec_list_adapter);
        rec_list_adapter.getItemCount();


       // record_list.removeItemDecorationAt();
      //  Log.e("Auidro list", rec_list_adapter.);
       // Log.e("Auidro list", record_list.getScrollBarSize());
      //  Log.e("Auidro list",rec_list_adapter.getItemId());



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying)
                {
                    audio_playersheet.setVisibility(View.VISIBLE);
                    pauseAudio();
                }
                else {
                    if(file_toPlay!= null)
                    {
                        audio_playersheet.setVisibility(View.VISIBLE);
                        resumeAudio();
                    }
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });

        play_prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAudio();
                int curr_pos = mediaPlayer.getCurrentPosition();
                mediaPlayer.seekTo(curr_pos-1000);
                int seek_prog = seekBar.getProgress();
                seekBar.setProgress(seek_prog-10);
                updateRunnable();
                resumeAudio();
            }
        });

        play_forw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAudio();
                int curr_pos = mediaPlayer.getCurrentPosition();
                mediaPlayer.seekTo(curr_pos+1000);
                int seek_prog = seekBar.getProgress();
                seekBar.setProgress(seek_prog+10);
                updateRunnable();
                resumeAudio();

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick_Listener(File file, int position)
    {
        file_toPlay = file;
        if(isPlaying)
        {
            stopAudio();
            playAudio(file_toPlay);
        }
        else
        {
            playAudio(file_toPlay);
        }
    }

    private void stopAudio()
    {
        play_btn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_play_btn,null));
        player_title.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
    }

    private void playAudio(File file_toPlay)
    {

        audio_playersheet.setVisibility(View.VISIBLE);

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(file_toPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play_btn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_pause_btn,null));
        player_file_name.setText(file_toPlay.getName());
        player_title.setText("Playing");
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                player_title.setText("Finsihed");
            }
        });

        seekBar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateseekbar,0);
    }

    private void updateRunnable() {
        updateseekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this,500);
            }
        };
    }

    private void pauseAudio()
    {
        mediaPlayer.pause();
        play_btn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_play_btn,null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateseekbar);
    }

    private void resumeAudio()
    {
        mediaPlayer.start();
        play_btn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_pause_btn,null));
        isPlaying = true;

        updateRunnable();
        seekbarHandler.postDelayed(updateseekbar,0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isPlaying)
        {
            stopAudio();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}