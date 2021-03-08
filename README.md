# ![Multipay Logo](https://docs.multinet.com.tr/multipay/theme/uploads/logo.png)

[![MavenCentral](https://img.shields.io/maven-central/v/tr.com.inventiv.multipaysdk/multipaysdk)](https://search.maven.org/artifact/tr.com.inventiv.multipaysdk/multipaysdk)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://developer.android.com/studio/releases/platforms#4.4)
[![AndroidX](https://img.shields.io/badge/androidX-✓-blueviolet)](#)
[![GitHub license](https://img.shields.io/github/license/multinetinventiv/MultiPay-Android-Sdk)](https://github.com/multinetinventiv/MultiPay-Android-Sdk/blob/main/LICENSE)

MultiPay Sdk kütüphanesidir. Multinet cüzdan ile ödeme almak için kullanılır.

## Sistem Gereksinimleri

Entegre edecek uygulamada;

- Minimum SDK versyionu 19 ve üzeri olmalı

- AndroidX desteği olmalı

## Gradle Entegrasyonu

**build.gradle(:app)** altına aşağıdaki dependency eklenmeli

```Groovy
dependencies {
    implementation "tr.com.inventiv.multipaysdk:multipaysdk:$latest_version"
}
```

Ekli değilse **build.gradle(:project)** altına **mavenCentral** reposunun eklenmesi gerekir.

```Groovy
buildscript {
    repositories {
        google()
        mavenCentral()
        ..
    }
}
```

## Doküman

Entegrasyon dokümanı için :

[Multinet Sdk Doküman](https://docs.multinet.com.tr/multipay/integration)

sitesini ziyaret edebilirsiniz.
