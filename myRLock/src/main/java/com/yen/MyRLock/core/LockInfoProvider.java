 package com.yen.MyRLock.core;

 import com.yen.MyRLock.annotation.MyRLock;
 import com.yen.MyRLock.config.MyRLockConfig;
 import com.yen.MyRLock.model.LockInfo;
 import com.yen.MyRLock.model.LockType;
 import org.aspectj.lang.JoinPoint;
 import org.aspectj.lang.reflect.MethodSignature;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;

 public class LockInfoProvider {

     private static final String LOCK_NAME_PREFIX = "lock";
     private static final String LOCK_NAME_SEPARATOR = ".";

     @Autowired
     private MyRLockConfig klockConfig;

     @Autowired
     private BusinessKeyProvider businessKeyProvider;

     private static final Logger logger = LoggerFactory.getLogger(LockInfoProvider.class);

     public LockInfo get(JoinPoint joinPoint, MyRLock klock) {
         MethodSignature signature = (MethodSignature) joinPoint.getSignature();
         LockType type = klock.lockType();
         String businessKeyName = businessKeyProvider.getKeyName(joinPoint, klock);
         String lockName = buildLockName(klock.name(), signature, businessKeyName);
         long waitTime = getWaitTime(klock);
         long leaseTime = getLeaseTime(klock);

         warnIfNoExpiration(klock, lockName, leaseTime);

         return new LockInfo(type, lockName, waitTime, leaseTime);
     }

     private String buildLockName(String annotationName, MethodSignature signature, String businessKeyName) {
         return LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR +
                 (annotationName.isEmpty() ? String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName()) : annotationName) +
                 businessKeyName;
     }

     private void warnIfNoExpiration(MyRLock klock, String lockName, long leaseTime) {
         if (leaseTime == -1 && logger.isWarnEnabled()) {
             logger.warn("Trying to acquire Lock({}) with no expiration, " +
                     "Klock will keep prolonging the lock expiration while the lock is still held by the current thread. " +
                     "This may cause deadlock in some circumstances.", lockName);
         }
     }

     private long getWaitTime(MyRLock klock) {
         return klock.waitTime() == Long.MIN_VALUE ?
                 klockConfig.getWaitTime() : klock.waitTime();
     }

     private long getLeaseTime(MyRLock klock) {
         return klock.leaseTime() == Long.MIN_VALUE ?
                 klockConfig.getLeaseTime() : klock.leaseTime();
     }

 }