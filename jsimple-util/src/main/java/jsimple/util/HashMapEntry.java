package jsimple.util;

/**
 * Created by Bret on 12/12/2014.
 */
class HashMapEntry<K, V> extends MapEntryImpl<K, V> {
    final int origKeyHash;

    HashMapEntry<K, V> next;

    HashMapEntry(K theKey, int hash) {
        super(theKey, null);
        this.origKeyHash = hash;
    }

    HashMapEntry(K theKey, V theValue) {
        super(theKey, theValue);
        origKeyHash = (theKey == null ? 0 : theKey.hashCode());
    }
}

