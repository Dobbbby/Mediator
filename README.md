# Mediator

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/49560cef2d864b9fb95bb21c4b75dfaf)](https://www.codacy.com/app/Dobbbby/Mediator?utm_source=github.com&utm_medium=referral&utm_content=Dobbbby/Mediator&utm_campaign=badger)

Mediator is a tool that helps with **Android permissions** at run time.

## 1. Getting Started

The repo has been uploaded to jcenter().

### 1.1 Gradle

```gradle
dependencies {
    compile 'com.dobbby.mediator:mediator:0.0.4'
}
```

### 1.2 Maven

```xml
<dependency>
    <groupId>com.dobbby.mediator</groupId>
    <artifactId>mediator</artifactId>
    <version>0.0.4</version>
    <type>pom</type>
</dependency>
```

## 2. Java

Simply add this to your Application's `onCreate()` method:

```java
Mediator.init(this);
Mediator.setDefaultOnRejected(new Mediator.OnRejected() {
    @Override
    public void onRejected() {
        // custom notifications when permission denied.
    }
});
```

And request permissions at run time can be easy as pie:

```java
Mediator.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .onPerform(new Mediator.OnPerform() {
            @Override
            public void onPerform() {
                saveSomeFiles();
            }
        })
        .onRejected(new Mediator.OnRejected() {
            @Override
            public void onRejected() {
                Log.i(TAG, "Permission denied");
            }
        })
        .delegate();
```

## 3. Kotlin

Simply add this to your Application's `onCreate()` method:

```kotlin
Mediator.init(this)
Mediator.setDefaultOnRejected { toast("Rejected") }
```

And request permissions at run time can be easy as pie:

```kotlin
Mediator.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .onPerform { saveSomeFiles() }
        .onRejected { toast("Rejected") }
        .delegate()
```

