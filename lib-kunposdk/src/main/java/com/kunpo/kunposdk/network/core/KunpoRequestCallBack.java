package com.kunpo.kunposdk.network.core;

public interface KunpoRequestCallBack {
    void success(KunpoResponse response);
    void error(KunpoResponse response);
}
