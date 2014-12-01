package jsimple.lang;

/**
 * Created by Bret on 11/28/2014.
 */
public abstract class Iterable<E> implements java.lang.Iterable<E> {
    public abstract jsimple.util.Iterator<E> iterator();
}
