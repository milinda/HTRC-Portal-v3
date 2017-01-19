/**
 * Copyright 2016 Milinda Pathirage
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import play.mvc.Result;

import static play.mvc.Results.*;


public class DemoHttpActionAdapter extends DefaultHttpActionAdapter {

  @Override
  public Result adapt(int code, PlayWebContext context) {
    if (code == HttpConstants.UNAUTHORIZED) {
      return unauthorized("401: Unauthorized").as((HttpConstants.HTML_CONTENT_TYPE));
    } else if (code == HttpConstants.FORBIDDEN) {
      return forbidden("403: forbidden").as((HttpConstants.HTML_CONTENT_TYPE));
    } else {
      return super.adapt(code, context);
    }
  }
}