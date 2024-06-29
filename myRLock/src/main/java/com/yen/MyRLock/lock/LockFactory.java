package com.yen.MyRLock.lock;

import com.yen.MyRLock.model.LockInfo;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockFactory {

  Logger logger = LoggerFactory.getLogger(getClass());

  // @Autowired
  private RedissonClient redissonClient;

  public Lock getLock(LockInfo lockInfo) {
    switch (lockInfo.getType()) {
      case Reentrant:
        return new ReentrantLock(redissonClient, lockInfo);
      case Fair:
        return new FairLock(redissonClient, lockInfo);
      case Read:
        return new ReadLock(redissonClient, lockInfo);
      case Write:
        return new WriteLock(redissonClient, lockInfo);
      default:
        return new ReentrantLock(redissonClient, lockInfo);
    }
  }
}
