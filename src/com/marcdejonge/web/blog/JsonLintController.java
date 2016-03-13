package com.marcdejonge.web.blog;

import java.io.IOException;
import java.util.EnumSet;

import com.marcdejonge.codec.MixedMap;
import com.marcdejonge.codec.ParseException;
import com.marcdejonge.codec.json.JSONDecoder;
import com.marcdejonge.codec.json.JSONEncoder;
import com.marcdejonge.web.core.api.Controller;
import com.marcdejonge.web.core.api.ErrorView;
import com.marcdejonge.web.core.api.FileController;
import com.marcdejonge.web.core.api.View;
import com.marcdejonge.web.core.api.annotations.Header;
import com.marcdejonge.web.core.api.annotations.PostData;
import com.marcdejonge.web.core.api.annotations.RequestType;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JsonLintController extends FileController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(JsonLintController.class);

	public JsonLintController() {
		super("jsonlint");
	}

	public View index(@Header("If-None-Match") String tag) throws IOException {
		return findFile("index.html", tag);
	}

	@RequestType("POST")
	public Object validate(@PostData String input) {
		try {
			Object parsed = JSONDecoder.parse(input);
			StringBuilder sb = new StringBuilder();
			JSONEncoder encoder = new JSONEncoder(sb, EnumSet.of(JSONEncoder.Options.PRETTY));
			encoder.write(parsed);
			return new MixedMap().$("success", true).$("result", sb.toString());
		} catch (ParseException e) {
			return new MixedMap().$("success", false)
			                     .$("char", e.getCharNumber())
			                     .$("line", e.getLineNumber())
			                     .$("msg", e.getMessage());
		} catch (IOException e) {
			logger.error("Unexpected I/O error during JSON serialization", e);
			return ErrorView.INTERNAL_SERVER_ERROR;
		}
	}
}
