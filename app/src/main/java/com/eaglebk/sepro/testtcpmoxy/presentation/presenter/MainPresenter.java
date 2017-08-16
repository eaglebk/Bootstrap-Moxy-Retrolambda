package com.eaglebk.sepro.testtcpmoxy.presentation.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.eaglebk.sepro.testtcpmoxy.App;
import com.eaglebk.sepro.testtcpmoxy.TCPService;
import com.eaglebk.sepro.testtcpmoxy.presentation.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IMainPresenter {

    TCPService tcpService;

    @Override
    public void onClickedShowMessage() {
        getViewState().showMessage("Click");
        tcpService = App.getInstanceServiceTCP();
        tcpService.sendCommand("HELLO");
    }
}
