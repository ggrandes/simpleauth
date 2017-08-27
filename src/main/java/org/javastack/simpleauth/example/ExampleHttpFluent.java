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
