/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */
class JmlGriffonPlugin {
    // the plugin version
    String version = '0.3'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '0.9.5 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [:]
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-jml-plugin'

    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'MSN Messenger support'
    String description = '''
Comunicate with MSN Messenger.

Usage
-----
Upon installation the plugin will generate the following artifacts in `$appdir/griffon-app/conf`:

 * MessengerConfig.groovy - contains MSN login and listener definitions.

This plugin provides a helper class that knows how to connect/disconnect to and and from the MSN network: 
`griffon.plugins.jml.MessengerConnector`. The following Controller shows how this class can be used to parse the
configuration and connect to the network

        import griffon.plugins.jml.MessengerConnector
 
        class MsnController {
            def model
            def view
 
            def login = { evt = null ->
                def msn = MessengerConnector.instance
                def msnConfig = msn.createConfig(app)
                msnConfig.messenger.username = model.username
                msnConfig.messenger.password = model.password
 
                msn.connect(app, msnConfig)
            }
        }

This Controller assumes that the login/password are found in the Model, most likely set by bindings on the View.

A new dynamic method named `withMessenger` will be injected into all controllers.

This method is also accessible to any component through the singleton `griffon.plugins.messenger.MessengerConnector`.
You can inject these methods to non-artifacts via metaclasses. Simply grab hold of a particular metaclass and call
`MessengerEnhancer.enhance(metaClassInstance)`.

Configuration
-------------
### Dynamic method injection

The `withMessenger()` dynamic method will be added to controllers by default. You can
change this setting by adding a configuration flag in `griffon-app/conf/Config.groovy`

    griffon.msn.injectInto = ['controller', 'service']

### Events

The following events will be triggered by this addon

 * MessengerConnectStart[config] - triggered before connecting to MSN
 * MessengerConnectEnd[config, messenger] - triggered after connecting MSN
 * MessengerDisconnectStart[config, messenger] - triggered before disconnecting from MSN
 * MessengerDisconnectEnd[config] - triggered after disconnecting from MSN

Testing
-------
The `withMessenger()` dynamic method will not be automatically injected during unit testing, because addons are simply not initialized
for this kind of tests. However you can use `MessengerEnhancer.enhance(metaClassInstance, messengerProviderInstance)` where 
`messengerProviderInstance` is of type `griffon.plugins.messenger.MessengerProvider`. The contract for this interface looks like this

    public interface MessengerProvider {
        Object withMessenger(Closure closure);
        <T> T withMessenger(CallableWithArgs<T> callable);
    }

It's up to you define how these methods need to be implemented for your tests. For example, here's an implementation that never
fails regardless of the arguments it receives

    class MyMessengerProvider implements MessengerProvider {
        Object withMessenger(Closure closure) { null }
        public <T> T withMessenger(CallableWithArgs<T> callable) { null }      
    }
    
This implementation may be used in the following way

    class MyServiceTests extends GriffonUnitTestCase {
        void testSmokeAndMirrors() {
            MyService service = new MyService()
            MessengerEnhancer.enhance(service.metaClass, new MyMessengerProvider())
            // exercise service methods
        }
    }

'''
}
