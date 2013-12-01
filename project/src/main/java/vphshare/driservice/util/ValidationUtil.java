package vphshare.driservice.util;

import vphshare.driservice.domain.CloudFile;

public class ValidationUtil {

    public static boolean hasNonZeroDataSources(CloudFile file) {
        return file.getDataSources().size() > 0;
    }
}
