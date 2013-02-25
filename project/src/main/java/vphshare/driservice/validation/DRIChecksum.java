package vphshare.driservice.validation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.jclouds.blobstore.domain.Blob;

public class DRIChecksum {

	/**
	 * Note: using DigestUtils from apache commons. The util implementation is
	 * using 1024 buffer size for single InputStream read. Refer to
	 * MessageDigestTest for performance comparison using different buffer
	 * sizes. Generally if higher performance is needed, then use higher buffer
	 * size value.
	 */

	public static String getBlobMD5(Blob blob) {
		InputStream in = new AutoCloseInputStream(blob.getPayload().getInput());
		String checksum = null;

		try {
			checksum = DigestUtils.md5Hex(in);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return checksum;
	}

	public static String getBlobSHA256(Blob blob) {
		InputStream in = new AutoCloseInputStream(blob.getPayload().getInput());
		String checksum = null;

		try {
			checksum = DigestUtils.sha256Hex(in);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return checksum;
	}

	public static String getBlobDRIChecksum(Blob blob) {
		return getBlobSHA256(blob);
	}
}
