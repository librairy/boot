# Librairy-Boot  [![Release Status](https://travis-ci.org/librairy/boot.svg?branch=master)](https://travis-ci.org/librairy/boot) [![Dev Status](https://travis-ci.org/librairy/boot.svg?branch=develop)](https://travis-ci.org/librairy/boot) [![Doc](https://raw.githubusercontent.com/librairy/resources/master/figures/interface.png)](https://rawgit.com/librairy/boot/doc/report/index.html)


Librairy-Boot makes it easy to create **librairy-powered** applications and services with minimum fuss. It takes the UDM (Unified Data Model) view of **librairy** so that new and existing users can quickly get to the features they need.

You can use it to create both plugins and/or stand-alone applications that can be linked into the **librairy** ecosystem.

## Import the maven library

First of all, you should include the following maven repository in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>librairy-mvn-repo</id>
        <url>https://raw.github.com/librairy/boot/mvn-repo/</url>
    </repository>
</repositories>
```

Then, add the `librairy-boot` dependency:

```xml
<dependency>
    <groupId>org.librairy</groupId>
    <artifactId>boot</artifactId>
    <version>${latest.version}</version>
</dependency>
```

## (or) Include directly the jar library

If you are not using Maven or simply prefer to directly add the jar library, download it from the following link:

```html
https://github.com/librairy/boot/tree/mvn-repo/org/librairy/boot
```

## Get Start!

And that's all!!

In our opinion, the best documentation are samples, take a look at some address samples:

