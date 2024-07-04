# myRLock

> Custom distributed lock based on Redis

## Code structrue
```
myRLock/src/main/java/com/yen/MyRLock
├── annotation
│   ├── MyRLock.java
│   └── MyRLockKey.java
├── config
│   └── MyRLockConfig.java
├── core
│   ├── BusinessKeyProvider.java
│   ├── KlockAspectHandler.java
│   └── LockInfoProvider.java
├── handler
│   ├── exception
│   │   ├── MyRlockInvocationException.java
│   │   └── MyRlockTimeoutException.java
│   ├── lock
│   │   └── LockTimeoutHandler.java
│   └── release
│       └── ReleaseTimeoutHandler.java
├── lock
│   ├── FairLock.java
│   ├── Lock.java
│   ├── LockFactory.java
│   ├── ReadLock.java
│   ├── ReentrantLock.java
│   └── WriteLock.java
└── model
    ├── LockInfo.java
    ├── LockTimeoutStrategy.java
    ├── LockType.java
    └── ReleaseTimeoutStrategy.java
```

## Install

## Quick start

## Concept

## Deploy built jar
- https://github.com/yennanliu/myLib


## Todo

## Reference