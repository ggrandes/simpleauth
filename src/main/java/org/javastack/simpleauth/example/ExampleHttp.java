package org.javastack.simpleauth.example;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.javastack.simpleauth.SimpleAuth;

public class ExampleHttp {
	public static void main(String[] args) throws Throwable {
		if (args.length < 2) {
			System.out.println(ExampleHttp.class.getName() + " <url> <pre-shared-key>");
			return;
		}
		final URL url = new URL(args[0]);
		// Check without Signature
		doRequest(url, null);
		// Check with Signature
		final SimpleAuth sa = new SimpleAuth();
		sa.setPreSharedKey(args[1]);
		doRequest(url, sa.sign());
	}

	private static void doRequest(final URL url, final String sign) throws IOException {
		// Request HTTP with Authentication header
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (sign != null) {
			conn.setRequestProperty(SimpleAuth.HTTP_HEADER, SimpleAuth.SCHEME + " " + sign);
		}
		System.out.println("--- Signature: " + sign);
		final BufferedReader in = new BufferedReader(new InputStreamReader(getStream(conn)));
		String line = null;
		System.out.println("head> " + conn.getHeaderField(0));	// HTTP Status
		while ((line = in.readLine()) != null) {
			System.out.println("body> " + line); 		// Body
		}
		in.close();
	}

	private static InputStream getStream(final HttpURLConnection conn) {
		InputStream is = null;
		try {
			is = conn.getInputStream();
		} catch (Exception e) {
		}
		if (is == null) {
			try {
				is = conn.getErrorStream();
			} catch (Exception ee) {
			}
			if (is == null) {
				is = new ByteArrayInputStream("ERROR".getBytes());
			}
		}
		return is;
	}
}
