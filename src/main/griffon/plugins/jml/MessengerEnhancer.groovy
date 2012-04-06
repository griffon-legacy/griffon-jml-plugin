/*
 * Copyright 2012 the original author or authors.
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

package griffon.plugins.jml

import net.sf.jml.MsnMessenger
import griffon.util.CallableWithArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
final class MessengerEnhancer {
    private static final Logger LOG = LoggerFactory.getLogger(MessengerEnhancer)

    private MessengerEnhancer() {}
    
    static void enhance(MetaClass mc, MessengerProvider provider = MessengerHolder.instance) {
        if(LOG.debugEnabled) LOG.debug("Enhancing $mc with $provider")
        mc.withMessenger = {Closure closure ->
            provider.withMessenger('default', closure)
        }
        mc.withMessenger << {CallableWithArgs callable ->
            provider.withMessenger('default', callable)
        }
    }
}
