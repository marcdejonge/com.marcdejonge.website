package com.marcdejonge.web.blog;

import java.io.IOException;

import com.marcdejonge.codec.MixedMap;
import com.marcdejonge.web.core.api.Controller;
import com.marcdejonge.web.core.api.FileController;
import com.marcdejonge.web.core.api.View;
import com.marcdejonge.web.core.api.annotations.Header;
import com.marcdejonge.web.core.api.annotations.Optional;
import com.marcdejonge.web.core.api.annotations.PathPart;
import com.marcdejonge.web.core.api.annotations.PathRest;
import com.marcdejonge.web.core.api.annotations.PostData;
import com.marcdejonge.web.core.api.annotations.RequestType;

import org.osgi.service.component.annotations.Component;

@Component
public class RootController extends FileController implements Controller {
	public View index(@PathRest String path, @Optional @Header("If-None-Match") String tag) throws IOException {
		return findFile(path, tag);
	}

	public MixedMap test() {
		return new MixedMap().$("test", Math.random());
	}

	@RequestType("POST")
	public MixedMap index(@PathPart String path, @PostData MixedMap postData) throws IOException {
		return new MixedMap().$("path", path).$("postdata", postData);
	}

	public String help() {
		return "Help";
	}
}
