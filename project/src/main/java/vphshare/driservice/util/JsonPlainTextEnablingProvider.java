package vphshare.driservice.util;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML,
		MediaType.TEXT_PLAIN, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML,
		MediaType.TEXT_PLAIN, "text/json" })
public class JsonPlainTextEnablingProvider extends JacksonJsonProvider {

	@Override
	protected boolean isJsonType(MediaType mediaType) {
		if (mediaType.isCompatible(MediaType.TEXT_HTML_TYPE) || mediaType.isCompatible(MediaType.TEXT_PLAIN_TYPE))
			return true;
		else
			return super.isJsonType(mediaType);
	}
}
