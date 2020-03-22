plugins {
    kotlin("jvm") version "1.3.70"
    java
}

group = "com.github.ryoii"
version = "V0.2.0"

repositories {
    maven { setUrl("https://mirrors.huaweicloud.com/repository/maven") }
    jcenter()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("net.mamoe:mirai-core-jvm:0.29.0")
    compileOnly("net.mamoe:mirai-console:0.3.5")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}