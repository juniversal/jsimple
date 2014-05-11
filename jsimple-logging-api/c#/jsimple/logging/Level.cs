namespace jsimple.logging {

    /// <summary>
    /// Logging level.  Conceptually this is an enum, but creating it as a class works better across all languages.
    /// 
    /// @author Bret Johnson
    /// @since 4/7/13 11:42 PM
    /// </summary>
    public class Level {
        public static readonly Level OFF = new Level(0, "OFF");
        public static readonly Level TRACE = new Level(1, "TRACE");
        public static readonly Level DEBUG = new Level(2, "DEBUG");
        public static readonly Level INFO = new Level(3, "INFO");
        public static readonly Level WARN = new Level(4, "WARN");
        public static readonly Level ERROR = new Level(5, "ERROR");
        private readonly int value;
        private readonly string defaultDisplayName;

        private Level(int value, string defaultDisplayName) {
            this.value = value;
            this.defaultDisplayName = defaultDisplayName;
        }

        public virtual int IntValue {
            get {
                return value;
            }
        }

        public virtual string DefaultDisplayName {
            get {
                return defaultDisplayName;
            }
        }
    }

}