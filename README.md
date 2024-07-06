# myRLock

> Custom distributed lock based on Redis

## Code structrue

<details>
<summary>Code structrue</summary>

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

</details>

## Install

## Quick start

## Concept

## Deploy built jar

- step 1) setup ~/.m2/settings.xml 

- Step 2) deploy
```bash
# step 1) setup ~/.m2/settings.xml 

# step 2) 
# copy below cmd, and run in IntellJ "maven input", then can deploy updated project to github maven
# mvn clean deploy -Dmaven.test.skip  -DaltDeploymentRepository=self-mvn-repo::default::file:/Users/yennanliu/myRLock/myRLock

mvn clean package deploy
```

- Step 3) use

- Example usage
```
<dependency>
  <groupId>com.yen</groupId>
  <artifactId>myrlock</artifactId>
  <version>1.0.2-SNAPSHOT</version>
</dependency>
```

- https://github.com/yennanliu/myRLock/packages/2197239

- https://github.com/yennanliu/myLib
- https://github.com/yennanliu/SpringPlayground/pull/158/commits/fc9d7d30d8c8320a10665dfde5da85637130cc20

- https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry


## Todo

## Reference