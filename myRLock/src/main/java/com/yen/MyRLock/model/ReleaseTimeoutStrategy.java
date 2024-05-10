package com.yen.MyRLock.model;

import com.yen.MyRLock.handler.release.ReleaseTimeoutHandler;

// TODO : check if necessary to use enum
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {
    ;

    @Override
    public void handle(LockInfo lockInfo) {

        System.out.println("(ReleaseTimeoutStrategy) handle method ...");
    }

    // TODO : fix below
//    /**
//     * do nothing, proceed to following code logic
//     */
//    NO_OPERATION() {
//        @Override
//        public void handle(LockInfo lockInfo) {
//            // do nothing
//        }
//    },
//    /**
//     * fail fast
//     */
//    FAIL_FAST() {
//        @Override
//        public void handle(LockInfo lockInfo) {
//
//            String errorMsg = String.format("Found Lock(%s) already been released while lock lease time is %d s", lockInfo.getName(), lockInfo.getLeaseTime());
//            throw new KlockTimeoutException(errorMsg);
//        }
//    }


}
