package com.truekenyan.whatsappstatusdownloader.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.truekenyan.whatsappstatusdownloader.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by password
 * on 2/4/18.
 * WhatsAppStatusDownloader
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<File> fileArrayList;
    private Activity activity;
    private String LOCATION_TO_SAVE_TO = "/storage/emulated/0/WhatsAppStatus/";

    public RecyclerViewAdapter(ArrayList<File> fileArrayList, Activity activity){
        this.fileArrayList = fileArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_status, parent, false);
        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        File currentStatus = fileArrayList.get(position);

        holder.imageStatusDownload.setOnClickListener(downloadMedia(currentStatus));
        holder.videoStatusDownload.setOnClickListener(downloadMedia(currentStatus));

        if (currentStatus.getAbsolutePath().endsWith(".mp4")){
            holder.videoStatusCard.setVisibility(View.VISIBLE);
            holder.imageStatusCard.setVisibility(View.GONE);
            Uri video = Uri.parse(currentStatus.getAbsolutePath());
            holder.videoStatusData.setVideoURI(video);
            holder.videoStatusData.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    holder.videoStatusData.start();
                }
            });
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(currentStatus.getAbsolutePath());
            holder.imageStatusData.setImageBitmap(bitmap);
        }
    }

    private View.OnClickListener downloadMedia(final File currentFile) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(activity, "Saving ...", Toast.LENGTH_SHORT).show();
                            saveFile(currentFile, new File(LOCATION_TO_SAVE_TO + currentFile.getName()));
                        } catch (IOException e) {
                            Toast.makeText(activity, "Error! Could not save file.", Toast.LENGTH_SHORT).show();
                        } finally {
                            Toast.makeText(activity, "Done saving", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.run();
            }
        };
    }

    private void saveFile(File source, File destination) throws IOException {
        if (!destination.getParentFile().exists())
            destination.getParentFile().mkdirs();

        if (!destination.exists())
            destination.createNewFile();

        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null)
                sourceChannel.close();

            if (destinationChannel != null)
                destinationChannel.close();
        }
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageStatusData;
        VideoView videoStatusData;
        Button videoStatusDownload;
        Button imageStatusDownload;
        CardView imageStatusCard;
        CardView videoStatusCard;

        private MyViewHolder(View itemView) {
            super(itemView);

            imageStatusData = itemView.findViewById(R.id.imageStatusView);
            videoStatusData = itemView.findViewById(R.id.videoStatusView);
            videoStatusDownload = itemView.findViewById(R.id.downloadVideoButton);
            imageStatusDownload = itemView.findViewById(R.id.downloadImageButton);
            imageStatusCard = itemView.findViewById(R.id.imageCard);
            videoStatusCard = itemView.findViewById(R.id.videoCard);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){

        }
    }
}
