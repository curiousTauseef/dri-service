/*****************************************************************/
/* Copyright 2009 avajava.com                                    */
/* This code may be freely used and distributed in any project.  */
/* However, please do not remove this credit if you publish this */
/* code in paper or electronic form, such as on a web site.      */
/*****************************************************************/
package performance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

/**
 * Test downloaded from:
 * http://www.avajava.com/tutorials/lessons/should-i-update
 * -my-messagedigest-with-byte-arrays-or-one-byte-at-a-time.html compares
 * performance of getting digest via MessageDigest using different buffer sizes
 * of InputStream.
 */
public class MessageDigestTest {

	public static void main(String[] args) throws NoSuchAlgorithmException,
			FileNotFoundException, IOException {
		MessageDigestTest test = new MessageDigestTest();
		test.run();
	}

	public void run() throws NoSuchAlgorithmException, FileNotFoundException,
			IOException {
		String file = this.getClass().getResource("movie.flv").getPath();

		MessageDigest md = MessageDigest.getInstance("MD5");
		getDigestViaByteArray(new FileInputStream(file), md, 128);
		getDigestViaByteArray(new FileInputStream(file), md, 256);
		getDigestViaByteArray(new FileInputStream(file), md, 512);
		getDigestViaByteArray(new FileInputStream(file), md, 1024);
		getDigestViaByteArray(new FileInputStream(file), md, 2048);
		getDigestViaByteArray(new FileInputStream(file), md, 4096);
		getDigestViaByteArray(new FileInputStream(file), md, 8192);
		getDigestViaByteArray(new FileInputStream(file), md, 16384);
		getDigestViaByteArray(new FileInputStream(file), md, 32768);
		getDigestViaByteArray(new FileInputStream(file), md, 65536);

		getDigestViaOneByteAtATime(new FileInputStream(file), md);
	}

	public String getDigestViaByteArray(InputStream is, MessageDigest md,
			int arraySize) throws NoSuchAlgorithmException, IOException {
		Date t1 = new Date();

		md.reset();
		byte[] bytes = new byte[arraySize];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));

		Date t2 = new Date();

		System.out.println("MD5 Digest:" + result);
		System.out.print("Using byte array (size " + arraySize + "): ");
		System.out.println((t2.getTime() - t1.getTime()) + " milliseconds\n");

		return result;
	}

	public String getDigestViaOneByteAtATime(InputStream is, MessageDigest md)
			throws NoSuchAlgorithmException, IOException {
		Date t1 = new Date();

		md.reset();
		int oneByte;
		while ((oneByte = is.read()) != -1) {
			md.update((byte) oneByte);
		}

		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));

		Date t2 = new Date();

		System.out.println("MD5 Digest:" + result);
		System.out.print("One byte at a time: ");
		System.out.println((t2.getTime() - t1.getTime()) + " milliseconds\n");

		return result;
	}

}