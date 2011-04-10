package pl.edu.mimuw.gtimoszuk.ldap.operations.search;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Demonstrates how to perform a search and limit the number of results returned.
 * 
 * usage: java SearchCountLimit
 */
class SearchCountLimit {
	static int expected = 1;

	public static void printSearchEnumeration(NamingEnumeration srhEnum) {
		int count = 0;
		try {
			while (srhEnum.hasMore()) {
				SearchResult sr = (SearchResult) srhEnum.next();
				System.out.println(">>>" + sr.getName());
				++count;
			}
			System.out.println("number of answers: " + count);
		} catch (SizeLimitExceededException e) {
			if (count == expected)
				System.out.println("number of answers: " + count);
			else
				e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// Set up the environment for creating the initial context
		Hashtable<String, Object> env = new Hashtable<String, Object>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:11389/o=JNDITutorial");

		try {
			// Create initial context
			DirContext ctx = new InitialDirContext(env);

			// Set search controls to limit count to 'expected'
			SearchControls ctls = new SearchControls();
			ctls.setCountLimit(expected);

			// Search for objects with those matching attributes
			NamingEnumeration answer = ctx.search("ou=People", "(sn=M*)", ctls);

			// Print the answer
			printSearchEnumeration(answer);

			// Close the context when we're done
			ctx.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}