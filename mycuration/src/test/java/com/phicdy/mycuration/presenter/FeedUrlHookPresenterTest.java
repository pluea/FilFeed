package com.phicdy.mycuration.presenter;

import android.content.Intent;

import com.phicdy.mycuration.db.DatabaseAdapter;
import com.phicdy.mycuration.rss.Feed;
import com.phicdy.mycuration.rss.RssParser;
import com.phicdy.mycuration.rss.UnreadCountManager;
import com.phicdy.mycuration.task.NetworkTaskManager;
import com.phicdy.mycuration.view.FeedUrlHookView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class FeedUrlHookPresenterTest {

    private NetworkTaskManager networkTaskManager;
    private DatabaseAdapter adapter;
    private UnreadCountManager unreadCountManager;
    private FeedUrlHookPresenter presenter;
    private RssParser parser;

    @Before
    public void setup() {
        networkTaskManager = Mockito.mock(NetworkTaskManager.class);
        adapter = Mockito.mock(DatabaseAdapter.class);
        unreadCountManager = Mockito.mock(UnreadCountManager.class);
        parser = Mockito.mock(RssParser.class);
        presenter = new FeedUrlHookPresenter(
                adapter, unreadCountManager, networkTaskManager, parser);

    }

    @Test
    public void testOnCreate() {
        // For coverage
        presenter.setView(new MockView());
        presenter.create();
        assertTrue(true);
    }

    @Test
    public void testOnResume() {
        // For coverage
        presenter.setView(new MockView());
        presenter.create();
        presenter.resume();
        assertTrue(true);
    }

    @Test
    public void receiverIsUnregisteredInAfterPause() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.pause();
        assertFalse(view.isReceiverRegistered);
    }

    @Test
    public void finishWhenInvalidActionComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle("hogehoge", "http://www.google.com");
        assertTrue(view.isFinished);
    }

    @Test
    public void finishWhenEmptyActionComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle("", "http://www.google.com");
        assertTrue(view.isFinished);
    }

    @Test
    public void registerReceiverWhenActionViewAndUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_VIEW, "http://www.google.com");
        assertTrue(view.isReceiverRegistered);
    }

    @Test
    public void progressDialogShowsWhenActionViewAndUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_VIEW, "http://www.google.com");
        assertTrue(view.isProgressDialogForeground);
    }

    @Test
    public void toastShowsWhenActionViewAndInvalidUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_VIEW, "hogehoge");
        assertTrue(view.isInvalidUrlErrorToastShowed);
    }

    @Test
    public void registerReceiverWhenActionSendAndUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_SEND, "http://www.google.com");
        assertTrue(view.isReceiverRegistered);
    }

    @Test
    public void progressDialogShowsWhenActionSendAndUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_SEND, "http://www.google.com");
        assertTrue(view.isProgressDialogForeground);
    }

    @Test
    public void toastShowsWhenActionSendAndInvalidUrlComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handle(Intent.ACTION_SEND, "hogehoge");
        assertTrue(view.isInvalidUrlErrorToastShowed);
    }

    @Test
    public void viewDoesNotFinishWhenInvalidActionComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish("hogehoge", "http://www.google.com", NetworkTaskManager.REASON_NOT_FOUND);
        assertFalse(view.isFinished);
    }

    @Test
    public void viewFinishesWhenFinishAddFeedActionComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                "http://www.google.com", NetworkTaskManager.REASON_NOT_FOUND);
        assertTrue(view.isFinished);
    }

    @Test
    public void progressDialogDismissesWhenFinishAddFeedActionComes() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                "http://www.google.com", NetworkTaskManager.REASON_NOT_FOUND);
        assertFalse(view.isProgressDialogForeground);
    }

    @Test
    public void toastShowsWhenFinishAddFeedActionComes() {
        DatabaseAdapter adapter = Mockito.mock(DatabaseAdapter.class);
        String testUrl = "http://www.google.com";
        Feed testFeed = new Feed(1, "Google");
        Mockito.when(adapter.getFeedByUrl(testUrl)).thenReturn(testFeed);
        FeedUrlHookPresenter presenter = new FeedUrlHookPresenter(
                adapter, unreadCountManager, networkTaskManager, parser);
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                testUrl, NetworkTaskManager.REASON_NOT_FOUND);
        assertTrue(view.isSuccessToastShowed);
    }

    @Test
    public void toastShowsWhenFinishAddFeedActionComesWithNotRssHtmlError() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                "http://www.google.com", NetworkTaskManager.ERROR_NON_RSS_HTML_CONTENT);
        assertTrue(view.isGenericErrorToastShowed);
    }

    @Test
    public void toastShowsWhenFinishAddFeedActionComesWithUnknownError() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                "http://www.google.com", NetworkTaskManager.ERROR_UNKNOWN);
        assertTrue(view.isGenericErrorToastShowed);
    }

    @Test
    public void toastShowsWhenFinishAddFeedActionComesWithInvalidUrlError() {
        MockView view = new MockView();
        presenter.setView(view);
        presenter.create();
        presenter.resume();
        presenter.handleFinish(NetworkTaskManager.FINISH_ADD_FEED,
                "http://www.google.com", NetworkTaskManager.ERROR_INVALID_URL);
        assertTrue(view.isInvalidUrlErrorToastShowed);
    }

    private class MockView implements FeedUrlHookView {
        private boolean isReceiverRegistered = false;
        private boolean isProgressDialogForeground = false;
        private boolean isSuccessToastShowed = false;
        private boolean isInvalidUrlErrorToastShowed = false;
        private boolean isGenericErrorToastShowed = false;
        private boolean isFinished = false;

        @Override
        public void showProgressDialog() {
            isProgressDialogForeground = true;
        }

        @Override
        public void dismissProgressDialog() {
            isProgressDialogForeground = false;
        }

        @Override
        public void registerFinishAddReceiver() {
            isReceiverRegistered = true;
        }

        @Override
        public void unregisterFinishAddReceiver() {
            isReceiverRegistered = false;
        }

        @Override
        public void showSuccessToast() {
            isSuccessToastShowed = true;
        }

        @Override
        public void showInvalidUrlErrorToast() {
            isInvalidUrlErrorToastShowed = true;
        }

        @Override
        public void showGenericErrorToast() {
            isGenericErrorToastShowed = true;
        }

        @Override
        public void finishView() {
            isFinished = true;
        }
    }
}
