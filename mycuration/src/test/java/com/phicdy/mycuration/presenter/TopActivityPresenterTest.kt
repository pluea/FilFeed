package com.phicdy.mycuration.presenter

import com.phicdy.mycuration.task.NetworkTaskManager
import com.phicdy.mycuration.view.TopActivityView
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times


class TopActivityPresenterTest {

    private lateinit var networkTaskManager: NetworkTaskManager
    private lateinit var mockView: TopActivityView

    @Before
    fun setup() {
        networkTaskManager = Mockito.mock(NetworkTaskManager::class.java)
        mockView = Mockito.mock(TopActivityView::class.java)
    }

    @Test
    fun changeTabIsCalledWhenOnCreate() {
        val presenter = TopActivityPresenter(false, networkTaskManager, 0)
        presenter.setView(mockView)
        presenter.create()
        Mockito.verify(mockView, times(1)).changeTab(0)
    }
}