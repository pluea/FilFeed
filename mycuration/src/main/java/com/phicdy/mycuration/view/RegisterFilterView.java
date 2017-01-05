package com.phicdy.mycuration.view;


public interface RegisterFilterView {
    String filterKeyword();
    String filterUrl();
    String filterTitle();
    void handleEmptyTitle();
    void handleEmptyCondition();
    void handlePercentOnly();
    void finish();
    void showSaveSuccessToast();
    void showSaveErrorToast();
    void trackEdit();
    void trackRegister();
}
