# ![Multipay Logo](https://docs.multinet.com.tr/multipay/theme/uploads/logo.png)

[ ![Download](https://api.bintray.com/packages/inventiv/MultiPay-Android-SDK/com.inventiv.multipaysdk/images/download.svg?version=1.0.6) ](https://bintray.com/inventiv/MultiPay-Android-SDK/com.inventiv.multipaysdk/1.0.6/link)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://developer.android.com/studio/releases/platforms#4.4)
![AndroidX](https://img.shields.io/badge/androidX-✓-blueviolet)
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
    implementation "com.inventiv.multipaysdk:multipaysdk:$latest_version"
}
```

Ekli değilse **build.gradle(:project)** altına *jcenter* reposunun eklenmesi gerekir.

```Groovy
buildscript {
    repositories {
        google()
        jcenter()
        ..
    }
}
```

## Doküman

Entegrasyon dokümanı için :

[Multinet Sdk Doküman](https://docs.multinet.com.tr/multipay)

sitesini ziyaret edebilirsiniz.
