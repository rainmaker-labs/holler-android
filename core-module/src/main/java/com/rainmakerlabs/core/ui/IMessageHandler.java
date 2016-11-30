package com.rainmakerlabs.core.ui;


import com.rainmakerlabs.ui.dialog.IConfirmMessage;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;
import com.rainmakerlabs.ui.dialog.IMessage;

/**
 * Created by thanhtritran on 9/13/16.
 */
public interface IMessageHandler {

    ILoadingMessage getLoading();

    IConfirmMessage getConfirm();

    IMessage getMessage();

    void hideMessage();

    void hideConfirm();

    void hideLoading();

}
