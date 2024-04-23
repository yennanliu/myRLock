package com.yen.MyRLock.lock;


/**
 * lock interface
 */
public interface Lock {

    boolean acquire();

    boolean release();
}
