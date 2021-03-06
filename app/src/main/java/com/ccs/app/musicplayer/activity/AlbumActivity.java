package com.ccs.app.musicplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;

import com.ccs.app.musicplayer.R;
import com.ccs.app.musicplayer.activity.base.BaseFragment;
import com.ccs.app.musicplayer.activity.base.MyActivity;
import com.ccs.app.musicplayer.activity.fragment.SongsFragment;
import com.ccs.app.musicplayer.model.PlayerModel;
import com.ccs.app.musicplayer.model.SongsModel;
import com.ccs.app.musicplayer.model.item.AlbumItem;
import com.ccs.app.musicplayer.model.item.SongItem;
import com.ccs.app.musicplayer.utils.Loader;
import com.ccs.app.musicplayer.player.PlaylistPlayer;

import java.util.List;

public class AlbumActivity extends MyActivity {

    public static final String ID = "id";

    private AlbumItem album;

    public static Intent createIntent(Context context, int id) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void init() {
        super.init();

        addFragment();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        showHomeButton();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SongsFragment fragment = (SongsFragment) fragmentManager.findFragmentByTag("SongsFragment");
        if(fragment == null) fragment = SongsFragment.newInstance(BaseFragment.ACTIVITY_MODEL);

        if(!fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, "SongsFragment")
                    .commit();
        }
    }

    @Override
    protected void beforeReloadData() {
        album = null;
    }

    @Override
    protected void onDataLoaded() {
        int id = getIntent().getIntExtra(ID, -1);
        if(id == -1) {
            finish();
            return;
        }

        if(album == null) {
            album = Loader.getInstance().findAlbum(id);

            if(album == null) {
                finish();
                return;
            }

            setTitle(album.getName());

            if(album.getBitmap() != null) appbarImage.setImageBitmap(album.getBitmap());

            //
            getModel(SongsModel.class).getItems().setValue(album.getSongs());
        }
    }

    @Override
    protected void onChangeTypeView(int typeView) {
        super.onChangeTypeView(typeView);

        getModel(SongsModel.class).getTypeView().setValue(typeView);
    }

    @Override
    protected void onPlayerModelCreated(@NonNull PlayerModel playerModel) {
        super.onPlayerModelCreated(playerModel);

        getModel(SongsModel.class).getPayerModel().setValue(playerModel);
    }

    @Override
    protected void onPlaylistPlayerCreated(@Nullable PlaylistPlayer playlistPlayer) {
        super.onPlaylistPlayerCreated(playlistPlayer);

        getModel(SongsModel.class).getPlaylistPlayer().setValue(playlistPlayer);
    }

    @Override
    protected List<SongItem> getPlaylistDefault() {
        if(album != null) return album.getSongs();
        return null;
    }

    @Override
    protected int calculateForBottomPlayerController(CoordinatorLayout parent, View child, View dependency) {
        return -dependency.getTop() * child.getHeight()
                / (dependency.getHeight() - dependency.findViewById(R.id.toolbar).getHeight());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
