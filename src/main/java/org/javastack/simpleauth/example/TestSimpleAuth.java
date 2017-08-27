package org.javastack.simpleauth.example;

import java.util.LinkedHashMap;
import java.util.Map;

import org.javastack.simpleauth.SimpleAuth;
import org.javastack.simpleauth.SimpleAuth.HashAlg;

public class TestSimpleAuth {
	private static boolean checkExpired = true;

	public static void main(String[] args) throws Throwable {
		final SimpleAuth sa = new SimpleAuth();
		String s = null;

		// Test A: Signature without data
		// SHA256,1503754961,,40165BDD970907E4334BBBF0FEFFC77A01CC6EA5870C6F9CD64FD8241455FC1F
		sa.setPreSharedKey("testkey");
		System.out.println("signed(a)=" + sa.sign(HashAlg.SHA256));

		// Test B: Test with Optional Data
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("user", "lazaro");
		map.put("msg", "wake up! and give me 500\u20AC");
		sa.setPreSharedKey("testkey");
		s = sa.sign(HashAlg.SHA256, map);
		// Signature with optional data
		// SHA256,1503754961,user=lazaro&msg=wake+up%21+and+give+me+500%E2%82%AC,C48A4805A70DDB641A0C330A41FAED285D7131ECD46ED21096213150605EBA19
		System.out.println("signed(b)=" + s);
		sa.setExpire(SimpleAuth.DEFAULT_EXPIRE);
		// Decode of optional data
		System.out.println("decode(b)=" + sa.decode(s));
		// Verify signature
		System.out.println("verify(b)=" + (true == sa.verify(s)));

		// Test C: Verify old signature (expire max)
		s = "SHA256,1503752846,,3343048D984C2F8784D7F3F078D18A7F6B5781A89396171634A7478246518BDD";
		sa.setPreSharedKey("testkey").setExpire(Integer.MAX_VALUE);
		System.out.println("verify(c)=" + (true == sa.verify(s)));

		// Test D: Force fail (invalid key)
		sa.setPreSharedKey("testkey");
		s = sa.sign();
		sa.setPreSharedKey("failkey").setExpire(Integer.MAX_VALUE);
		System.out.println("verify(d)=" + (false == sa.verify(s)));

		// Test E: Force fail (expire token)
		if (checkExpired) {
			sa.setPreSharedKey("testkey");
			s = sa.sign();
			System.out.println("waiting... 2sec");
			Thread.sleep(2000L);
			sa.setExpire(1);
			System.out.println("verify(e)=" + (false == sa.verify(s))); // Expire in 1second
		}
	}
}
