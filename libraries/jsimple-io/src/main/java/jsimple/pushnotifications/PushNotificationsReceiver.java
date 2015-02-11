/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.pushnotifications;

import jsimple.util.ProgrammerError;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Bret on 5/24/2014.
 */
public class PushNotificationsReceiver {
    private static volatile @Nullable PushNotificationsReceiverFactory factory;

    public static PushNotificationsReceiver register() {
        if (factory == null)
            throw new ProgrammerError("HttpRequest factory isn't set; did you forget to call JSimpleIO.init()?");
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

    public static interface PushNotificationsReceiverFactory {
        PushNotificationsReceiver register();
    }
}
