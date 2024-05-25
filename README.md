# path-parse

This is a small, dependency-free Java library to parse paths based on pre-configured path specifications.

It is heavily inspired on the path routing specs of projects like Express and Oak for JS/TS, and I learned
a lot from reading the code for `path-to-regexp`, also in TypeScript. I'm working on making the experince
of working on Lambda Proxy Integrations less awful for myself, this is sort of the first step.

### How to use

**Note: this library is 0.1.0 because I wrote like 1 unit test. But it's small enough that you should be
able to read it for yourself.**

Get it on [Maven Central](https://central.sonatype.com/artifact/com.joaonmatos/path-parse).

In your Java code:

```java
PathParser parser = ParseParser.create("/users/:userid/blog-posts");
ParseResult res = parser.parse("/users/joao/blog-posts/");

String userId = res.parameterValue("userid").value; // joao
```

(c) 2024 João N. Matos. Licensed under Apache-2.0, go read NOTICE and LICENSE.
