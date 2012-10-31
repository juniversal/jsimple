package jsimple.oauth.services;

import jsimple.util.PlatformUtils;

import java.util.Random;

/**
 * Implementation of {@link TimestampService} using plain java classes.
 *
 * @author Pablo Fernandez
 */
public class TimestampServiceImpl implements TimestampService {
    /**
     * {@inheritDoc}
     */
    public String getNonce() {
        Long ts = getTs();
        return String.valueOf(ts + new Random().nextInt());
    }

    /**
     * {@inheritDoc}
     */
    public String getTimestampInSeconds() {
        return String.valueOf(getTs());
    }

    private Long getTs() {
        return PlatformUtils.getCurrentTimeMillis() / 1000;
    }
}
