package com.ccs.app.musicplayer.activity.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.ccs.app.musicplayer.R;
import com.ccs.app.musicplayer.model.PlayerModel;
import com.ccs.app.musicplayer.service.PlayerService;

import java.util.Collections;

public class SortPlaylistFragment extends PlaylistFragment implements View.OnClickListener {

    public static SortPlaylistFragment newInstance(int modelOwner) {
        SortPlaylistFragment fragment = new SortPlaylistFragment();

        Bundle args = new Bundle();
        args.putInt("modelOwner", modelOwner);
        fragment.setArguments(args);

        return fragment;
    }

    private Toolbar toolbar;

    private ItemTouchHelper touchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            int fromPos, toPos;
            boolean startMove;

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                int fromPos = viewHolder.getAdapterPosition();
//                int toPos = target.getAdapterPosition();
//                Log.d("debug", "onMoved " + fromPos + " - " + toPos);
//
//                if(startMove) {
//                    startMove = false;
//                    this.fromPos = fromPos;
//                }
//                this.toPos = toPos;
//
//                if (fromPos < toPos) {
//                    for (int i = fromPos; i < toPos; i++) {
//                        Collections.swap(recyclerAdapter.getItems(), i, i + 1);
//                    }
//                } else {
//                    for (int i = fromPos; i > toPos; i--) {
//                        Collections.swap(recyclerAdapter.getItems(), i, i - 1);
//                    }
//                }
//
//                recyclerAdapter.notifyItemMoved(fromPos, toPos);

                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
//                Log.d("debug", "onMoved " + fromPos + " - " + toPos);

                if(startMove) {
                    startMove = false;
                    this.fromPos = fromPos;
                }
                this.toPos = toPos;

                if (fromPos < toPos) {
                    for (int i = fromPos; i < toPos; i++) {
                        Collections.swap(recyclerAdapter.getItems(), i, i + 1);
                    }
                } else {
                    for (int i = fromPos; i > toPos; i--) {
                        Collections.swap(recyclerAdapter.getItems(), i, i - 1);
                    }
                }

                recyclerAdapter.notifyItemMoved(fromPos, toPos);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
//                Log.d("debug", "onSwiped " + position);

                recyclerAdapter.getItems().remove(position);
                recyclerAdapter.notifyItemRemoved(position);

                update(500);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
//                Log.d("debug", "onSelectedChanged " + actionState);

                switch (actionState) {
                    case ItemTouchHelper.ACTION_STATE_IDLE:
                        if(fromPos != toPos) {
                            fromPos = toPos = 0;
                            update(300);
                        }
                        break;


                    case ItemTouchHelper.ACTION_STATE_SWIPE:

                        break;

                    case ItemTouchHelper.ACTION_STATE_DRAG:
                        startMove = true;
                        break;
                }
            }

        };

        touchHelper = new ItemTouchHelper(callback);
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(playlistPlayer != null) playlistPlayer.setPlaylist(PlayerService.CURRENT_LISTING,
                    playlistPlayer.getPlaylist(), playlistPlayer.findCurrentIndex(), false);
        }
    };

    private void update(int delay) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPlayerModelCreated(@NonNull PlayerModel playerModel) {
        super.onPlayerModelCreated(playerModel);

        playerModel.getPlaylistName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(toolbar != null) toolbar.setTitle(s);
            }
        });
    }

    @Override
    protected void gotoCurrentSong(int delay) {}

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_sort_playlist;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
