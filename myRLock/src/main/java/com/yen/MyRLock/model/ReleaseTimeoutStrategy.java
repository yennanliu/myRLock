package com.yen.MyRLock.model;

import com.yen.MyRLock.handler.exception.MyRlockTimeoutException;
import com.yen.MyRLock.handler.release.ReleaseTimeoutHandler;

// TODO : check if necessary to use enum
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {

  /** 继续执行业务逻辑，不做任何处理 */
  NO_OPERATION() {
    @Override
    public void handle(LockInfo lockInfo) {
      // do nothing
    }
  },
  /** 快速失败 */
  FAIL_FAST() {
    @Override
    public void handle(LockInfo lockInfo) {

      String errorMsg =
          String.format(
              "Found Lock(%s) already been released while lock lease time is %d s",
              lockInfo.getName(), lockInfo.getLeaseTime());
      throw new MyRlockTimeoutException(errorMsg);
    }
  }
}
