package com.yen.MyRLock.handler.release;

import com.yen.MyRLock.model.LockInfo;

/** interface deal with Lock timeout */
public interface ReleaseTimeoutHandler {

  void handle(LockInfo lockInfo);
}
