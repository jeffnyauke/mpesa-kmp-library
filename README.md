# <img src="https://kotlinlang.org/assets/images/favicon.svg" height="23"/> Mpesa
[![Build](https://github.com/jeffnyauke/mpesa-kmp-library/actions/workflows/check.yml/badge.svg)](https://github.com/jeffnyauke/mpesa-kmp-library/actions/workflows/check.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jeffnyauke/mpesa-kmp-library?color=blue)](https://search.maven.org/search?q=g:io.github.jeffnyauke.mpesa)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-mac](http://img.shields.io/badge/platform-macos-111111.svg?style=flat)
![badge-watchos](http://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat)
![badge-tvos](http://img.shields.io/badge/platform-tvos-808080.svg?style=flat)
![badge-jvm](http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat)
![badge-linux](http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat)
![badge-windows](http://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat)
![badge-nodejs](https://img.shields.io/badge/platform-jsNode-F8DB5D.svg?style=flat)
![badge-browser](https://img.shields.io/badge/platform-jsBrowser-F8DB5D.svg?style=flat)

_A Kotlin Multiplatform SDK for the Safaricom M-Pesa Daraja 2.0 API._

## Features:

- 🤳 Dynamic QR
- 💶 STK Push - Lipa na M-Pesa Online API (M-PESA express)
- ⏳ STK Push query
- 📝 C2B register
- 💶 C2B
- 💶 B2C
- ⏳ Transaction status
- 🏦 Account balance
- 🔁 Transaction reversal

## At a glace

```kotlin
val mpesa = Mpesa("86smaD2TEnlXLVp9yOGvBiA9Znd3iHh3", "utbzOaE5a0LZFGB2")
val response = mpesa.stkPush(
    "174379",
    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
    CustomerPayBillOnline,
    "1",
    "254708374149",
    "254708374149",
    "174379",
    "https://mydomain.com/path",
    "CompanyXLTD",
    "Payment of X",
)
```

## Adding to your project

The library is available on Maven Central.

### Gradle

Add the Maven Central repository if it is not already there.

```kotlin
repositories { 
  mavenCentral()
    // ...  
}
```

To use the library in a single-platform project, add a dependency. Latest version [![Maven Central](https://img.shields.io/maven-central/v/io.github.jeffnyauke/mpesa-kmp-library?color=blue)](https://search.maven.org/search?q=g:io.github.jeffnyauke.mpesa)

```kotlin
dependencies {
    // ...
    implementation("io.github.jeffnyauke:mpesa:<version>")
}
```

In Kotlin Multiplatform projects, add the dependency to your `commonMain` source-set dependencies.

```kotlin
sourceSets {
  val commonMain by getting { 
    dependencies { 
      // ...
      implementation("io.github.jeffnyauke:mpesa:<version>") 
    } 
  }
}
```

## Contributing 🤝
Please feel free to [open an issue](https://github.com/jeffnyauke/mpesa-kmp-library/issues/new/choose) if you have any questions or suggestions. Or participate in the [discussion](https://github.com/jeffnyauke/mpesa-kmp-library/discussions). If you want to contribute, please read the [contribution guidelines](https://github.com/jeffnyauke/mpesa-kmp-library/blob/main/CONTRIBUTING.md) for more information.

## License

**mpesa-kmp-library** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.

## Trademarks

M-PESA is a trademark of Vodafone Group Plc. and is not affiliated with this project.