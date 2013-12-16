package vphshare.driservice.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwiftURLTest {

    @Test
    public void shouldParseSwiftUrl() throws Exception {
        // given & when
        SwiftURL swiftURL = SwiftURL.from("swift://10.100.0.24:5000/v2.0/");

        // then
        assertEquals("http://10.100.0.24:5000/", swiftURL.getEndpoint());
        assertEquals("2.0", swiftURL.getApiVersion());
    }
}
