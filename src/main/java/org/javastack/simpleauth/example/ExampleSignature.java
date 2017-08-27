package org.javastack.simpleauth.example;

import org.javastack.simpleauth.SimpleAuth;

public class ExampleSignature {
	public static void main(String[] args) throws Throwable {
		if (args.length < 1) {
			System.out.println(ExampleSignature.class.getName() + " <pre-shared-key>");
			return;
		}
		// Generate Signature
		final SimpleAuth sa = new SimpleAuth();
		sa.setPreSharedKey(args[0]);
		System.out.println(sa.sign());
	}
}
