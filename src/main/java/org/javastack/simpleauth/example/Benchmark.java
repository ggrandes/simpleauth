package org.javastack.simpleauth.example;

import org.javastack.simpleauth.SimpleAuth;
import org.javastack.simpleauth.SimpleAuth.HashAlg;

public class Benchmark {
	public static void main(String[] args) throws Throwable {
		final SimpleAuth sa = new SimpleAuth();
		String s = null;
		sa.setPreSharedKey("benchkey").setExpire(Integer.MAX_VALUE);
		System.out.println("benchmark...");
		// Signature
		final long ts1 = System.currentTimeMillis();
		final int TOTAL = 1000000;
		for (int i = 0; i < TOTAL; i++) {
			s = sa.sign(HashAlg.SHA256);
		}
		final long diff1 = Math.max(1, (System.currentTimeMillis() - ts1) / 1000);
		System.out.println("bench sign/s=" + (TOTAL / diff1));
		// Verify
		final long ts2 = System.currentTimeMillis();
		for (int i = 0; i < TOTAL; i++) {
			sa.verify(s);
		}
		final long diff2 = Math.max(1, (System.currentTimeMillis() - ts2) / 1000);
		System.out.println("bench verify/s=" + (TOTAL / diff2));
	}
}
