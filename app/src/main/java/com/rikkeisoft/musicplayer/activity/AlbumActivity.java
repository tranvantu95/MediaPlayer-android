package com.rikkeisoft.musicplayer.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.rikkeisoft.musicplayer.R;
import com.rikkeisoft.musicplayer.activity.base.BaseFragment;
import com.rikkeisoft.musicplayer.activity.base.MyActivity;
import com.rikkeisoft.musicplayer.activity.fragment.SongsFragment;
import com.rikkeisoft.musicplayer.model.SongsModel;
import com.rikkeisoft.musicplayer.model.item.AlbumItem;
import com.rikkeisoft.musicplayer.model.item.SongItem;
import com.rikkeisoft.musicplayer.utils.Loader;

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
        setTitle("");
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SongsFragment songsFragment = (SongsFragment) fragmentManager.findFragmentByTag("SongsFragment");
        if(songsFragment == null) songsFragment = SongsFragment.newInstance(BaseFragment.ACTIVITY_MODEL);

        if(!songsFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, songsFragment, "SongsFragment")
                    .commit();
        }
    }

    @Override
    protected void onReceiverMediaChange() {
        super.onReceiverMediaChange();

        album = null;
        loadData();
    }

    @Override
    protected void loadData() {
        int id = getIntent().getIntExtra(ID, -1);
        if(id == -1) {
            finish();
            return;
        }

        if(album == null) album = Loader.getInstance().findAlbum(id);
        if(album == null) {
            finish();
            return;
        }

        setTitle(album.getName());

        if(album.getBitmap() != null) appbarImage.setImageBitmap(album.getBitmap());

        //
        getModel(SongsModel.class).getItems().setValue(album.getSongs());
    }

    @Override
    protected void onChangeTypeView(int typeView) {
        super.onChangeTypeView(typeView);

        getModel(SongsModel.class).getTypeView().setValue(typeView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
