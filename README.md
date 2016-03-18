# Librairy Boot  [![Build Status](https://travis-ci.org/librairy/boot.svg?branch=develop)](https://travis-ci.org/librairy/boot)

Librairy Boot makes it easy to create **librairy-powered** applications and services with minimum fuss. It takes the UDM (Unified Data Model) view of the **librairy** platform so that new and existing users can quickly get to the features they need.

You can use **librairy-boot** to create both plugins and/or stand-alone applications that can be linked into the **librairy** ecosystem.

## Import the maven library

First of all, you should include the following maven repository:

```xml
<repositories>
    <repository>
        <id>librairy-mvn-repo</id>
        <url>https://raw.github.com/librairy/boot/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

Then, add the `librairy-boot` dependency in you `pom.xml`:

```xml
<dependency>
    <groupId>org.librairy</groupId>
    <artifactId>librairy-boot</artifactId>
    <version>${librairy.version}</version>
</dependency>
```

## (or) Include the jar library

If you are not using Maven or simply prefer to directly add the jar library, download it from the following link:

```html
https://github.com/librairy/boot/tree/mvn-repo/org/librairy/librairy-boot
```

## Get Start!

In our opinion, the best documentation are samples, take a look at some address samples:

