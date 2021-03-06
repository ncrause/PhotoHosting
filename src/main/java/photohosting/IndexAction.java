/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photohosting;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import photohosting.services.beans.Photo;

/**
 * 
 */
@Conversion()
public class IndexAction extends ActionSupport implements SlideshowRenderable {
	
	@Getter @Setter
	private List<Photo> slideshow;
	
	@Getter @Setter
	private Map<String, String> nonces;
    
	@Override
    public String execute() throws Exception {
        return SUCCESS;
    }
	
}
