# Mediator

Mediator is a tool that helps with **Android permissions** at run time.

## Get Started

### Gradle

```gradle
dependencies {
    compile 'com.dobbby.mediator:mediator:0.0.3'
}
```

### Maven

```xml
<dependency>
    <groupId>com.dobbby.mediator</groupId>
    <artifactId>mediator</artifactId>
    <version>0.0.3</version>
    <type>pom</type>
</dependency>
```

## Java

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

##Kotlin

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

