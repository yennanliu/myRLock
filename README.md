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

```bash
# step 1) setup ~/.m2/settings.xml 

# step 2) 
# copy below cmd, and run in IntellJ "maven input", then can deploy updated project to github maven
mvn clean deploy -Dmaven.test.skip  -DaltDeploymentRepository=self-mvn-repo::default::file:/Users/yennanliu/myRLock/myRLock
```

- https://github.com/yennanliu/myLib


## Todo

## Reference