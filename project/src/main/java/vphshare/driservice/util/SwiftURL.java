package vphshare.driservice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwiftURL {

    private static final String PATTERN_STRING = "^(swift://.*/)v([\\w.]*)/$";

    private final Matcher matcher;

    public SwiftURL(String swiftUrl) {
        Pattern swiftUrlPattern = Pattern.compile(PATTERN_STRING);
        matcher = swiftUrlPattern.matcher(swiftUrl);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    String.format("Pattern [%s] does not match [%s]", PATTERN_STRING, swiftUrl));
        }
    }

    public static SwiftURL from(String swiftUrl) {
        return new SwiftURL(swiftUrl);
    }

    public String getEndpoint() {
        return matcher.group(1).replace("swift", "http");

    }

    public String getApiVersion() {
        return matcher.group(2);
    }
}
