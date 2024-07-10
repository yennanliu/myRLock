package com.yen.MyRLock.lock;

import com.yen.MyRLock.core.LockInfoProvider;
import com.yen.MyRLock.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ReentrantLock implements Lock {

  private static final Logger logger = LoggerFactory.getLogger(LockInfoProvider.class);

  private final LockInfo lockInfo;
  private final RedissonClient redissonClient;
  private RLock rLock;

  public ReentrantLock(RedissonClient redissonClient, LockInfo lockInfo) {
    this.redissonClient = redissonClient;
    this.lockInfo = lockInfo;
    logger.debug(String.valueOf(this.lockInfo));
  }

  @Override
  public boolean acquire() {
    try {
      rLock = redissonClient.getLock(lockInfo.getName());
      return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      return false;
    }
  }

  @Override
  public boolean release() {
    if (rLock.isHeldByCurrentThread()) {
      try {
        return rLock.forceUnlockAsync().get();
      } catch (InterruptedException e) {
        return false;
      } catch (ExecutionException e) {
        return false;
      }
    }
    return false;
  }

  public String getKey() {
    return this.lockInfo.getName();
  }

  @Override
  public void info() {

  }

}
