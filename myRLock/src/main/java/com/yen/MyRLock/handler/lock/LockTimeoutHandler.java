package com.yen.MyRLock.handler.lock;

import com.yen.MyRLock.lock.Lock;
import com.yen.MyRLock.model.LockInfo;

/** get lock timeout interface */
public interface LockTimeoutHandler {

  // TODO : check if JoinPoint is necessary
  // void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
  void handle(LockInfo lockInfo, Lock lock);
}
