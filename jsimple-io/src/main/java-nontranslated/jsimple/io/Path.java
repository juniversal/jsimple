package jsimple.io;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bret Johnson
 * @since 11/3/12 9:17 PM
 */
public class Path extends PathBase {
    private String path;

    @Override public List<String> listChildren() {
        String[] childrenNames = new File(path).list();
        if (childrenNames == null)
            throw new IOException("Could get the children of " + path + "; perhaps it isn't a directory");

        return Arrays.asList(childrenNames);
    }
}
