package com.dd.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.app.util.UiUtils;
import com.dd.app.widget.BebuWidgetProvider;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.dd.app.R;
import com.dd.app.model.Video;
import com.dd.app.ui.activity.VideoPageActivity;
import com.dd.app.util.ResponsivenessUtils;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dd.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.dd.app.network.APIConstants.Params.IMAGE;
import static com.dd.app.network.APIConstants.Params.ORIGINAL;
import static com.dd.app.network.APIConstants.Params.PARENT_ID;
import static com.dd.app.network.APIConstants.Params.SEASON_ID;
import static com.dd.app.network.APIConstants.Params.TITLE;
import static com.dd.app.util.UiUtils.animationObject;

public class VideoTileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = VideoTileAdapter.class.getSimpleName();

    public static final int VIDEO_SECTION_TYPE_ORIGINALS = 1;
    public static final int VIDEO_SECTION_TYPE_NORMAL = 2;
    //public static final int VIDEO_SECTION_TYPE_NORMAL_VERTICAL = 0;
    public static final int VIDEO_SECTION_TYPE_NORMAL_HORIZONTAL = 0;

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Video> videos;
    private int viewType;
    private int currentPosition=0;
    private boolean isInSinglePage;

    public VideoTileAdapter(Context context, ArrayList<Video> videos, int viewType, boolean isInSinglePage) {
        this.context = context;
        this.videos = videos;
        this.viewType = viewType;
        this.isInSinglePage = isInSinglePage;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (viewType) {
            case VIDEO_SECTION_TYPE_ORIGINALS:
                view = inflater.inflate(R.layout.item_video_long, viewGroup, false);
                return new OriginalsViewHolder(view);
            case VIDEO_SECTION_TYPE_NORMAL_HORIZONTAL:
                view = inflater.inflate(R.layout.item_video_horizontal, viewGroup, false);
                return new NormalVideoViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_video_vertical, viewGroup, false);
                return new NormalVideoViewHolder(view);
        }

    }

    /*@Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        switch (viewType) {
            case VIDEO_SECTION_TYPE_ORIGINALS:
                return  VIDEO_SECTION_TYPE_ORIGINALS;
            case VIDEO_SECTION_TYPE_NORMAL:
                return position % 2 * 2;
        }
        return  -1;
    }*/

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Video video = videos.get(position);
        //UiUtils.log(TAG, "Title: "+ video.getTitle().toUpperCase());
        switch (viewType) {
            case VIDEO_SECTION_TYPE_ORIGINALS:
                OriginalsViewHolder originalsViewHolder = (OriginalsViewHolder) viewHolder;
                Glide.with(context).load(video.getBrowseImage()).thumbnail(0.5f)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .apply(new RequestOptions().placeholder(R.drawable.bebu_placeholder_vertical).error(R.drawable.bebu_placeholder_vertical).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(originalsViewHolder.item);
                originalsViewHolder.videoTileRoot.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                            .setOnFinishListener(new ElasticFinishListener() {
                                @Override
                                public void onFinished() {
                                    // Do something after duration time
                                    takeToVideoPage(originalsViewHolder.item, originalsViewHolder.tvMovieTitle, video);
                                }
                            }).doAction();

                });
              /*  normalVideoViewHolder.videoTileRoot.setOnLongClickListener(v -> {
                    UiUtils.showShortToast(context, video.getTitle());
                    return true;
                });*/
                //setRating(originalsViewHolder.imgRating,video.getA_record());
                originalsViewHolder.tvMovieTitle.setText(video.getTitle());
                originalsViewHolder.item.setLayoutParams(ResponsivenessUtils.getLayoutParamsForOrignalAdapter(context, true));
                checkWidth(originalsViewHolder.tvMovieTitle, originalsViewHolder.imgRating);
                break;
            case VIDEO_SECTION_TYPE_NORMAL_HORIZONTAL:
                NormalVideoViewHolder normalHorizontalVideoViewHolder = (NormalVideoViewHolder) viewHolder;
                Glide.with(context).load(video.getThumbNailUrl()).thumbnail(0.5f)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .apply(new RequestOptions().placeholder(R.drawable.bebu_placeholder_horizontal).error(R.drawable.bebu_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(normalHorizontalVideoViewHolder.item);
                normalHorizontalVideoViewHolder.videoTileRoot.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                            .setOnFinishListener(new ElasticFinishListener() {
                                @Override
                                public void onFinished() {
                                    // Do something after duration time
                                    takeToVideoPage(normalHorizontalVideoViewHolder.item, normalHorizontalVideoViewHolder.tvMovieTitle, video);
                                }
                            }).doAction();

                });
              /*  normalVideoViewHolder.videoTileRoot.setOnLongClickListener(v -> {
                    UiUtils.showShortToast(context, video.getTitle());
                    return true;
                });*/
                //setRating(normalHorizontalVideoViewHolder.imgRating,video.getA_record());
                normalHorizontalVideoViewHolder.tvMovieTitle.setText(video.getTitle());
                normalHorizontalVideoViewHolder.item.setLayoutParams(ResponsivenessUtils.getLayoutParamsForHorizontalAdapter(context, false));
                checkWidth(normalHorizontalVideoViewHolder.tvMovieTitle, normalHorizontalVideoViewHolder.imgRating);
                break;
            default:
                NormalVideoViewHolder normalVideoViewHolder = (NormalVideoViewHolder) viewHolder;
                Glide.with(context).load(video.getBrowseImage()).thumbnail(0.5f)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .apply(new RequestOptions().placeholder(R.drawable.bebu_placeholder_vertical).error(R.drawable.bebu_placeholder_vertical).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(normalVideoViewHolder.item);
                normalVideoViewHolder.videoTileRoot.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                            .setOnFinishListener(new ElasticFinishListener() {
                                @Override
                                public void onFinished() {
                                    // Do something after duration time
                                    takeToVideoPage(normalVideoViewHolder.item, normalVideoViewHolder.tvMovieTitle, video);
                                }
                            }).doAction();

                });
              /*  normalVideoViewHolder.videoTileRoot.setOnLongClickListener(v -> {
                    UiUtils.showShortToast(context, video.getTitle());
                    return true;
                });*/
                //setRating(normalVideoViewHolder.imgRating,video.getA_record());
                normalVideoViewHolder.tvMovieTitle.setText(video.getTitle());
                normalVideoViewHolder.item.setLayoutParams(ResponsivenessUtils.getLayoutParamsForHorizontalAdapter(context, true));
                checkWidth(normalVideoViewHolder.tvMovieTitle, normalVideoViewHolder.imgRating);
                break;
        }
    }

    private void takeToVideoPage(ImageView imageView, TextView textView, Video video) {

        // this will send the broadcast to update the appwidget
        ArrayList<String> widgetData = new ArrayList<>();
        widgetData.add("Hello 1");
        widgetData.add("Hello 2");
        widgetData.add("Hello 3");
        widgetData.add("Hello 4");
        widgetData.add("Hello 5");
        widgetData.add("Hello 6");
        BebuWidgetProvider.sendRefreshBroadcast(context, widgetData);
        UiUtils.log("Widget","BroadCast Sent");

        if (video.isTrailerVideo()) {
            String url = "";
            if (video.getResolutions() != null)
                url = video.getResolutions().get(ORIGINAL);
            switch (video.getVideoType()) {
                case VIDEO_MANUAL:
                case VIDEO_OTHER:
                    if (url != null && Uri.parse(url) != null) {
                        Intent toVideo = new Intent(context, VideoPageActivity.class);
                        toVideo.putExtra(ADMIN_VIDEO_ID, video.getAdminVideoId());
                        toVideo.putExtra(PARENT_ID, video.getParentId());
                        toVideo.putExtra(SEASON_ID, video.getSeasonId());
                        toVideo.putExtra(IMAGE, video.getThumbNailUrl());
                        toVideo.putExtra(TITLE, video.getTitle());
                        context.startActivity(toVideo);

                        /*Intent toPlayer = new Intent(context, PlayerActivity.class);
                        toPlayer.putExtra(PlayerActivity.VIDEO_ID, video.getAdminVideoId());
                        toPlayer.putExtra(PlayerActivity.VIDEO_URL, url);
                        toPlayer.putExtra(PlayerActivity.VIDEO_SUBTITLE, video.getSubTitleUrl());
                        toPlayer.putExtra(PlayerActivity.IS_TRAILER_VIDEO, video.isTrailerVideo());
                        context.startActivity(toPlayer);*/
                    } else {
                        Toast.makeText(context, "Something wrong with the trailer video. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                /*case VIDEO_YOUTUBE:
                    Intent toYouTubePlayer = new Intent(context, YouTubePlayerActivity.class);
                    toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_ID, video.getAdminVideoId());
                    toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_URL, url);
                    context.startActivity(toYouTubePlayer);
                    break;*/
            }
        } else {
            Intent toVideo = new Intent(context, VideoPageActivity.class);
            toVideo.putExtra(ADMIN_VIDEO_ID, video.getAdminVideoId());
            toVideo.putExtra(PARENT_ID, video.getParentId());
            toVideo.putExtra(SEASON_ID, video.getSeasonId());
            toVideo.putExtra(IMAGE, video.getThumbNailUrl());
            toVideo.putExtra(TITLE, video.getTitle());
            context.startActivity(toVideo);
            /*Pair<View, String> p1 = Pair.create(imageView, context.getResources().getString(R.string.transition_image));
            Pair<View, String> p2 = Pair.create(textView, context.getResources().getString(R.string.transition_title));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, p1, p2);
            context.startActivity(toVideo, options.toBundle());*/

        }
    }

    private void setRating(ImageView imgRating, int rating)
    {
        imgRating.setVisibility(View.VISIBLE);
        switch (rating)
        {
            case 1:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.a,null));
                break;
            case 2:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ua,null));
                break;
            case 3:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.u,null));
                break;
            case 4:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.kids,null));
                break;
            default:
                imgRating.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class NormalVideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView item;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.videoTileRoot)
        ViewGroup videoTileRoot;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;

        NormalVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OriginalsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView item;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.videoTileRoot)
        ViewGroup videoTileRoot;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;
        OriginalsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void checkWidth(TextView title, ImageView imgRating)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: "+ width);
        UiUtils.log(TAG, "Width dp: "+ dp);

        if(dp>500 && dp<600)
        {
            UiUtils.log(TAG, "margin(500-600)");
            title.setTextSize(14);
        }else if(dp>600 && dp<700)
        {
            UiUtils.log(TAG, "margin(600-700)");
            title.setTextSize(14);
        }else if(dp>700 && dp<800)
        {
            UiUtils.log(TAG, "margin(700-800)");
            title.setTextSize(14);
        }else if(dp>800)
        {
            UiUtils.log(TAG, "margin(800)");
            title.setTextSize(16);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgRating.getLayoutParams();
            params.width = 60;
            params.height = 60;
            params.setMargins(0, 30, 40, 0);
            imgRating.setLayoutParams(params);
        }
    }
}
