package com.phicdy.mycuration.view;


public interface TopActivityView {
    void closeSearchView();
    void goToFeedSearch();
    void goToAddCuration();
    void goToAddFilter();
    void goToSetting();
    void goToLicense();
    int currentTabPosition();
}