# simpleauth

Simple Authentication based in HMAC and Time (like TOTP), can run in a servlet container like Tomcat as a Filter. Open Source Java project under Apache License v2.0

### Current Stable Version is [1.0.0](https://search.maven.org/#search|ga|1|g%3Aorg.javastack%20a%3Asimpleauth)

---

## DOC

#### Usage Example (client)

```java
/**
 * Artifact required: org.apache.httpcomponents:fluent-hc:4.5.3
 */
package org.javastack.simpleauth.example;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.javastack.simpleauth.SimpleAuth;

public class ExampleHttpFluent {
	public static void main(String[] args) throws Throwable {
		if (args.length < 2) {
			System.out.println(ExampleHttpFluent.class.getName() + " <url> <pre-shared-key>");
			return;
		}
		// Example of usage
		final SimpleAuth sa = new SimpleAuth();
		sa.setPreSharedKey(args[1]);
		doRequest(args[0], sa.sign());
		// Must fail (expect 403)
		doRequest(args[0], null);
	}

	private static void doRequest(final String uri, final String sign) {
		System.out.println("--- Signature: " + sign);
		try {
			final Request req = Request.Get(uri).connectTimeout(5000).socketTimeout(5000);
			if (sign != null) {
				req.setHeader(SimpleAuth.HTTP_HEADER, SimpleAuth.SCHEME + " " + sign);
			}
			final HttpResponse res = req.execute().returnResponse();
			final String status = String.valueOf(res.getStatusLine());
			final String body = EntityUtils.toString(res.getEntity()).trim();
			System.out.println("head> " + status);  // HTTP Status
			System.out.println("body> " + body);    // Body
		} catch (IOException e) {
			System.out.println("Unable to connect: " + e);
		}
	}
}
```

* More examples in [Example package](https://github.com/ggrandes/simpleauth/tree/master/src/main/java/org/javastack/simpleauth/example/)

#### Configuration (server):

```xml
<!-- Servlet Filter -->
<!-- WEB-INF/web.xml -->
<filter>
    <filter-name>SimpleAuthFilter</filter-name>
    <filter-class>org.javastack.simpleauth.servlet.SimpleAuthFilter</filter-class>
    <!-- Pre Shared Key (symmetric) -->
    <init-param>
        <param-name>pre-shared-key</param-name>
        <param-value>changeit</param-value>
    </init-param>
    <!-- Expiration of Tokens (seconds) -->
    <init-param>
        <param-name>token-expire</param-name>
        <param-value>600</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>SimpleAuthFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### Token/Header format:

```text
Authentication: torch SHA256,1503753381,,2F05A80ECA8536AD1AC420C7D7E0BAEDAA1A5C0AFB48BDED2D3C79A123D8E6A1
```

- HTTP Header: Authentication
- Auth-Scheme: torch
- Token (coma-separated):
  - HmacAlgorithm: SHA256
  - TimeStamp: Unix-Epoch-Seconds-UTC `(the difference, measured in seconds, between the current time and midnight, January 1, 1970 UTC)`
  - Data (optional): URL-Encoded-Key-Values `(example: key1=value1&key2=v2&key3=x%21z)`
  - Hash: Hexadecimal

#### Simple Test:

```bash
curl -i http://localhost:8080/svc/test
```

###### Output will be like:

```text
HTTP/1.1 403 Forbidden
Pragma: no-cache
Cache-Control: private, no-cache, no-store, must-revalidate
SimpleAuthFilter: deny
Content-Type: text/plain;charset=ISO-8859-1
Content-Length: 11
Date: Sun, 27 Aug 2017 10:07:04 GMT
Server: Apache

FORBIDDEN
```

---

## MAVEN

    <dependency>
        <groupId>org.javastack</groupId>
        <artifactId>simpleauth</artifactId>
        <version>1.0.0</version>
    </dependency>

---
Inspired in [OAuth2 Bearer Tokens - rfc6750](https://tools.ietf.org/html/rfc6750), this code is Java-minimalistic version.
