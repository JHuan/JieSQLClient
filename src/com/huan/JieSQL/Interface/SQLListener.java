package com.huan.JieSQL.Interface;

import java.util.List;
import java.util.Map;

/**
 * Created by barryjiang on 2015/3/4.
 */
public interface SQLListener {

    public void onBeforeConnectDB();

    public void onGetConnectResult(Boolean isConnected);

    public void onSendingCommit();

    public void onGetCommitResult(List<Map<String, Object>> resltSet);
}
