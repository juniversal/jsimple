package jsimple.util.properties;

/**
 * Created by Bret on 6/8/2014.
 */
public class Property {
    private PropertyCollection collectinon;
    private String name;

    public Property(PropertyCollection collectinon, String name) {
        this.collectinon = collectinon;
        this.name = name;
    }
}
