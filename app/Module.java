import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import java.time.Clock;

import controllers.DemoHttpActionAdapter;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.play.ApplicationLogoutController;
import org.pac4j.play.CallbackController;
import org.pac4j.play.deadbolt2.Pac4jHandlerCache;
import org.pac4j.play.store.PlayCacheStore;
import org.pac4j.play.store.PlaySessionStore;
import play.Configuration;
import play.Environment;
import services.ApplicationTimer;
import services.AtomicCounter;
import services.Counter;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    private final Configuration configuration;


    public Module(final Environment environment, final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void configure() {
        final String baseUrl = configuration.getString("baseUrl");

        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
        // Set AtomicCounter as the implementation for Counter.
        bind(Counter.class).to(AtomicCounter.class);

        // pac4j related config
        bind(HandlerCache.class).to(Pac4jHandlerCache.class);
        bind(PlaySessionStore.class).to(PlayCacheStore.class);

        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId("Pn4ArX4AVre616dosuuvNgKRHAYa");
        oidcConfiguration.setSecret("DczkZgDuh2SHCdRENNxCJlJIUJga");
//        oidcConfiguration.addCustomParam("prompt", "consent");
        oidcConfiguration.setDiscoveryURI("https://localhost:9443/.well-known/webfinger");
        final OidcClient oidcClient = new OidcClient(oidcConfiguration);
        oidcClient.addAuthorizationGenerator(profile -> profile.addRole("ROLE_ADMIN"));

        final Clients clients = new Clients(baseUrl + "/callback",  oidcClient);

        final Config config = new Config(clients);
        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer<>("ROLE_ADMIN"));
        config.setHttpActionAdapter(new DemoHttpActionAdapter());
        bind(Config.class).toInstance(config);

        // callback
        final CallbackController callbackController = new CallbackController();
        callbackController.setDefaultUrl("/");
        callbackController.setMultiProfile(true);
        bind(CallbackController.class).toInstance(callbackController);
        // logout
        final ApplicationLogoutController logoutController = new ApplicationLogoutController();
        logoutController.setDefaultUrl("/?defaulturlafterlogout");
        bind(ApplicationLogoutController.class).toInstance(logoutController);
    }

}
