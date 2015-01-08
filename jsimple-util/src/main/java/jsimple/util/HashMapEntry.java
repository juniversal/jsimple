package jsimple.util;

/**
 * @author Bret Johnson
 * @since 12/12/2014
 */
class HashMapEntry<K, V> extends MapEntryImpl<K, V> {
    final int origKeyHash;
    HashMapEntry<K, V> next;

    HashMapEntry(K theKey, int hash) {
        super(theKey);
        this.origKeyHash = hash;
    }

    HashMapEntry(K theKey, V theValue) {
        super(theKey, theValue);
        origKeyHash = theKey.hashCode();
    }
}
