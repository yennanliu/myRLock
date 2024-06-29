package com.yen.MyRLock.model;

/**
 * Lock type
 */
public enum LockType {

    /**
     * Reentrant lock
     */
    Reentrant,

    /**
     * fair lock
     */
    Fair,

    /**
     * read lock
     */
    Read,

    /**
     * write lock
     */
    Write;

    LockType() {
    }

}
