package com.yen.MyRLock.lock;

import com.yen.MyRLock.model.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WriteLock implements Lock {

  private final LockInfo lockInfo;
  private final RedissonClient redissonClient;
  private RReadWriteLock rLock;

  public WriteLock(RedissonClient redissonClient, LockInfo info) {
    this.redissonClient = redissonClient;
    this.lockInfo = info;
  }

  @Override
  public boolean acquire() {
    try {
      rLock = redissonClient.getReadWriteLock(lockInfo.getName());
      return rLock
          .writeLock()
          .tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      return false;
    }
  }

  @Override
  public boolean release() {
    if (rLock.writeLock().isHeldByCurrentThread()) {
      try {
        return rLock.writeLock().forceUnlockAsync().get();
      } catch (InterruptedException e) {
        return false;
      } catch (ExecutionException e) {
        return false;
      }
    }

    return false;
  }
}
