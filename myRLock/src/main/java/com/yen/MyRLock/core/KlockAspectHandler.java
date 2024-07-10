 package com.yen.MyRLock.core;

 import com.yen.MyRLock.annotation.MyRLock;

 import com.yen.MyRLock.handler.exception.MyRlockInvocationException;
 import com.yen.MyRLock.lock.Lock;
 import com.yen.MyRLock.lock.LockFactory;
 import com.yen.MyRLock.model.LockInfo;
 import org.aspectj.lang.JoinPoint;
 import org.aspectj.lang.ProceedingJoinPoint;
 import org.aspectj.lang.annotation.AfterReturning;
 import org.aspectj.lang.annotation.AfterThrowing;
 import org.aspectj.lang.annotation.Around;
 import org.aspectj.lang.annotation.Aspect;
 import org.aspectj.lang.reflect.MethodSignature;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;

 import org.springframework.core.annotation.Order;
 import org.springframework.stereotype.Component;
 import org.springframework.util.StringUtils;

 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.util.Map;
 import java.util.Objects;
 import java.util.concurrent.ConcurrentHashMap;

 @Aspect
 @Component
 @Order(0) // TODO : check it
 public class KlockAspectHandler {

     private static final Logger logger = LoggerFactory.getLogger(KlockAspectHandler.class);

     @Autowired
     private LockFactory lockFactory;

     @Autowired
     private LockInfoProvider lockInfoProvider;

     private final Map<String, LockRes> currentThreadLock = new ConcurrentHashMap<>();

     @Around(value = "@annotation(myRlock)")
     public Object around(ProceedingJoinPoint joinPoint, MyRLock myRlock) throws Throwable {
         LockInfo lockInfo = lockInfoProvider.get(joinPoint, myRlock);
         String currentLock = getCurrentLockId(joinPoint, myRlock);
         currentThreadLock.put(currentLock, new LockRes(lockInfo, false));
         Lock lock = lockFactory.getLock(lockInfo);
         boolean lockResult = lock.acquire();

         if (!lockResult) {
             handleLockTimeout(myRlock, joinPoint);
         }

         currentThreadLock.get(currentLock).setLock(lock);
         currentThreadLock.get(currentLock).setRes(true);

         return joinPoint.proceed();
     }

     @AfterReturning(value = "@annotation(myRlock)")
     public void afterReturning(JoinPoint joinPoint, MyRLock myRlock) throws Throwable {
         String currentLock = getCurrentLockId(joinPoint, myRlock);
         releaseLock(myRlock, joinPoint, currentLock);
         cleanUpThreadLocal(currentLock);
     }

     @AfterThrowing(value = "@annotation(myRlock)", throwing = "ex")
     public void afterThrowing(JoinPoint joinPoint, MyRLock myRlock, Throwable ex) throws Throwable {
         String currentLock = getCurrentLockId(joinPoint, myRlock);
         releaseLock(myRlock, joinPoint, currentLock);
         cleanUpThreadLocal(currentLock);
         throw ex;
     }

     private void handleLockTimeout(MyRLock myRlock, ProceedingJoinPoint joinPoint) throws Throwable {
         if (logger.isWarnEnabled()) {
             logger.warn("Timeout while acquiring Lock({})", myRlock.name());
         }

         if (!StringUtils.isEmpty(myRlock.customLockTimeoutStrategy())) {
             handleCustomTimeout(myRlock.customLockTimeoutStrategy(), joinPoint);
         } else {
             myRlock.lockTimeoutStrategy().handle(lockInfoProvider.get(joinPoint, myRlock), lockFactory.getLock(lockInfoProvider.get(joinPoint, myRlock)));
         }
     }

     private Object handleCustomTimeout(String timeoutHandler, ProceedingJoinPoint joinPoint) throws Throwable {
         Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
         Object target = joinPoint.getTarget();
         Method handleMethod;
         try {
             handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(timeoutHandler, currentMethod.getParameterTypes());
             handleMethod.setAccessible(true);
         } catch (NoSuchMethodException e) {
             throw new IllegalArgumentException("Illegal annotation param customLockTimeoutStrategy", e);
         }

         try {
             return handleMethod.invoke(target, joinPoint.getArgs());
         } catch (IllegalAccessException e) {
             throw new MyRlockInvocationException("Fail to invoke custom lock timeout handler: " + timeoutHandler, e);
         } catch (InvocationTargetException e) {
             throw e.getTargetException();
         }
     }

     private void releaseLock(MyRLock klock, JoinPoint joinPoint, String currentLock) throws Throwable {
         LockRes lockRes = currentThreadLock.get(currentLock);
         if (Objects.isNull(lockRes)) {
             throw new NullPointerException("Please check whether the input parameter used as the lock key value has been modified in the method, which will cause the acquire and release locks to have different key values and throw null pointers. currentLockKey:" + currentLock);
         }
         if (lockRes.getRes()) {
             boolean releaseRes = currentThreadLock.get(currentLock).getLock().release();
             lockRes.setRes(false);
             if (!releaseRes) {
                 handleReleaseTimeout(klock, lockRes.getLockInfo(), joinPoint);
             }
         }
     }

     private void handleReleaseTimeout(MyRLock klock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {
         if (logger.isWarnEnabled()) {
             logger.warn("Timeout while release Lock({})", lockInfo.getName());
         }

         if (!StringUtils.isEmpty(klock.customReleaseTimeoutStrategy())) {
             handleCustomReleaseTimeout(klock.customReleaseTimeoutStrategy(), joinPoint);
         } else {
             klock.releaseTimeoutStrategy().handle(lockInfo);
         }
     }

     private void handleCustomReleaseTimeout(String releaseTimeoutHandler, JoinPoint joinPoint) throws Throwable {
         Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
         Object target = joinPoint.getTarget();
         Method handleMethod;
         try {
             handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(releaseTimeoutHandler, currentMethod.getParameterTypes());
             handleMethod.setAccessible(true);
         } catch (NoSuchMethodException e) {
             throw new IllegalArgumentException("Illegal annotation param customReleaseTimeoutStrategy", e);
         }

         try {
             handleMethod.invoke(target, joinPoint.getArgs());
         } catch (IllegalAccessException e) {
      throw new MyRlockInvocationException("Fail to invoke custom release timeout handler: " + releaseTimeoutHandler, e);
         } catch (InvocationTargetException e) {
             throw e.getTargetException();
         }
     }

     private void cleanUpThreadLocal(String currentLock) {
         currentThreadLock.remove(currentLock);
     }

     private String getCurrentLockId(JoinPoint joinPoint, MyRLock klock) {
         LockInfo lockInfo = lockInfoProvider.get(joinPoint, klock);
         return Thread.currentThread().getId() + lockInfo.getName();
     }

     private static class LockRes {

         private final LockInfo lockInfo;
         private Lock lock;
         private boolean res;

         LockRes(LockInfo lockInfo, boolean res) {
             this.lockInfo = lockInfo;
             this.res = res;
         }

         LockInfo getLockInfo() {
             return lockInfo;
         }

         Lock getLock() {
             return lock;
         }

         void setLock(Lock lock) {
             this.lock = lock;
         }

         boolean getRes() {
             return res;
         }

         void setRes(boolean res) {
             this.res = res;
         }
     }
 }
