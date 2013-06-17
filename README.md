
MSN Messenger support
---------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/jml](http://artifacts.griffon-framework.org/plugin/jml)


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


