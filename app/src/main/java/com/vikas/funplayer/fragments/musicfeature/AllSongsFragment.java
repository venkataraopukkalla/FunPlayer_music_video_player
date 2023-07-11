package com.vikas.funplayer.fragments.musicfeature;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vikas.funplayer.Adpaters.AllsongsAdapter;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.MusicFunctionality;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.fragments.MusicFeatureFragment;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.MyMusic;

import java.io.IOException;
import java.util.ArrayList;

public class AllSongsFragment extends Fragment  {

  private RecyclerView allsongRecycleview;
  private Toolbar currentSongShortCut_toolbar;
  private ImageView displayCurrentSongShortCutArtPic_img,currentSongShortCutPlay_imgBtn;
  private TextView displayCurrentSongShortCutTile_txt;
    ArrayList<SongsDetails> songsArrayList;
    MediaPlayer mediaPlayer = MyMusic.instanceOfMyMusic();
    FragmentActivity activity;

    private MusicFunctionality musicFunctionality;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        allsongRecycleview=view.findViewById(R.id.allsongs_recycleview);
        currentSongShortCut_toolbar=view.findViewById(R.id.currentSongDisplay_toolbar);
        displayCurrentSongShortCutArtPic_img=view.findViewById(R.id.currentSongAlbumArt_img);
        currentSongShortCutPlay_imgBtn=view.findViewById(R.id.cureentSongpausebtn_img);
        displayCurrentSongShortCutTile_txt=view.findViewById(R.id.currentSongTitle_txt);
        songsArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        allsongRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        allsongRecycleview.setAdapter(new AllsongsAdapter(songsArrayList,getContext()));
       // demo=view.findViewById(R.id.allsongfragmetChecking);

         activity= getActivity();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(getActivity()!=null){

                    getActivity().runOnUiThread( ()->{
                        if(mediaPlayer!=null &&mediaPlayer.isPlaying() ){
                            cusumOnstart();

                        }

                        new Handler().postDelayed(this,1000);
                    });
                }
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();


        currentSongShortCutPlay_imgBtn.setOnClickListener(e->{

            if(mediaPlayer.isPlaying() && mediaPlayer!=null) {
                mediaPlayer.pause();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }else {
                mediaPlayer.start();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }

        });

        currentSongShortCut_toolbar.setOnClickListener(e->{
            try {
                Intent intent = new Intent(getContext(), MusicPlayDesign.class);
                intent.putExtra("KEY_VALUE",songsArrayList);
                intent.putExtra("VIKAS",true);
                getContext().startActivity(intent);
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }


        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        allsongRecycleview.setAdapter(new AllsongsAdapter(songsArrayList,getContext()));
    }

    public  static AllSongsFragment instance(ArrayList<SongsDetails> songsDetailsArrayList){
        AllSongsFragment allSongsFragment = new AllSongsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.KEY_VALUE,songsDetailsArrayList);
        allSongsFragment.setArguments(bundle);
        return  allSongsFragment;
    }



    private void cusumOnstart(){
        if(MyMusic.instanceOfMyMusic()!=null && MyMusic.currentSongNumber!=-1)
        {
            if(mediaPlayer.isPlaying()){
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }else{
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }
            String songTitle = songsArrayList.get(MyMusic.currentSongNumber).getSongTitle();
            currentSongShortCut_toolbar.setVisibility(View.VISIBLE);
            displayCurrentSongShortCutTile_txt.setText(songTitle);
            byte[] albumArt = AlbumArt.getAlbumArt(songsArrayList.get(MyMusic.currentSongNumber).getSongData());
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);

            if(albumArt!=null){

                Glide.with(this).asBitmap().load(albumArt).into(displayCurrentSongShortCutArtPic_img);
                displayCurrentSongShortCutArtPic_img.startAnimation(rotateAnimation);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.music_logo).into(displayCurrentSongShortCutArtPic_img);
            }


            // Toast.makeText(this,MyMusst.LENGTH_ic.currentSongNumber+"",ToaLONG).show();
        }
    }



}

