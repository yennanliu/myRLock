package com.yen.MyRLock.lock;

public class ReentrantReadWriteLock implements Lock {

    @Override
    public boolean acquire() {
        return false;
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public void info() {

    }

}
