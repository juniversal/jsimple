package jsimple.pushnotifications;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Bret on 5/24/2014.
 */
public class PushNotificationsReceiver {
    private static volatile @Nullable PushNotificationsReceiverFactory factory;

    public static PushNotificationsReceiver register() {
        if (factory == null)
            throw new RuntimeException("HttpRequest factory isn't set; did you forget to call JSimpleIO.init()?");
        return factory.register();
    }

    /**
     * Set the global (default) factory used to register a push notification receiver.  Clients normally don't call this
     * method directly and just call JSimpleIO.init at app startup instead, which sets the factory to the default
     * implementation appropriate for the current platform.
     *
     * @param pushNotificationsReceiverFactory push notifications factory for the platform
     */
    public static void setFactory(@Nullable PushNotificationsReceiverFactory pushNotificationsReceiverFactory) {
        factory = pushNotificationsReceiverFactory;
    }

    public interface PushNotificationsReceiverFactory {
        PushNotificationsReceiver register();
    }
}
