package com.yen.MyRLock.model;

import com.yen.MyRLock.handler.exception.MyRlockTimeoutException;
import com.yen.MyRLock.handler.lock.LockTimeoutHandler;
import com.yen.MyRLock.lock.Lock;
import java.util.concurrent.TimeUnit;

/**
 * adopt below code for java 8 syntax (modified by gpt)
 *
 */
public enum LockTimeoutStrategy implements LockTimeoutHandler {

    NO_OPERATION {
        @Override
        public void handle(LockInfo lockInfo, Lock lock) {
            // Do nothing
        }
    },

    FAIL_FAST {
        @Override
        public void handle(LockInfo lockInfo, Lock lock) {
            String errorMsg = String.format("Failed to acquire Lock(%s) with timeout(%ds)", lockInfo.getName(), lockInfo.getWaitTime());
            throw new MyRlockTimeoutException(errorMsg);
        }
    },

    KEEP_ACQUIRE {
        @Override
        public void handle(LockInfo lockInfo, Lock lock) {
            final long DEFAULT_INTERVAL = 100L;
            final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000L;
            long interval = DEFAULT_INTERVAL;

            while (!lock.acquire()) {
                if (interval > DEFAULT_MAX_INTERVAL) {
                    String errorMsg = String.format("Failed to acquire Lock(%s) after too many times, this may be because deadlock occurred.", lockInfo.getName());
                    throw new MyRlockTimeoutException(errorMsg);
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(interval);
                    interval <<= 1; // Equivalent to interval *= 2
                } catch (InterruptedException e) {
                    throw new MyRlockTimeoutException("Failed to acquire Lock", e);
                }
            }
        }
    };

    @Override
    public abstract void handle(LockInfo lockInfo, Lock lock);

}