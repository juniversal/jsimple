package jsimple.util;

/**
 * @author Bret Johnson
 * @since 10/6/13 3:24 PM
 */
public abstract class ChangeableValueBase<T> {
    public abstract T getValue();
    public abstract void changed();
}
