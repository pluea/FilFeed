package com.phicdy.mycuration.view;

import android.support.annotation.NonNull;

import com.phicdy.mycuration.rss.Feed;

import java.util.ArrayList;

public interface FeedListView {
    void showDeleteFeedAlertDialog(int position);
    void showEditTitleDialog(int position, @NonNull String feedTitle);
    void setRefreshing(boolean doScroll);
    void init(ArrayList<Feed> feeds);
    void setTotalUnreadCount(int count);
    void setProgress(int completedCount, int total);
    void onRefreshCompleted();
    void showEditFeedTitleEmptyErrorToast();
    void showEditFeedFailToast();
    void showEditFeedSuccessToast();
    void showDeleteSuccessToast();
    void showDeleteFailToast();
    void showAddFeedSuccessToast();
    void showGenericAddFeedErrorToast();
    void showInvalidUrlAddFeedErrorToast();
    void notifyDataSetChanged();
    void hideAllUnreadView();
}
