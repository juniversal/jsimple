using System.Diagnostics;

namespace jsimple.oauth.model {

    /// <summary>
    /// Represents an OAuth verifier code.
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class Verifier {

        private readonly string value;

        /// <summary>
        /// Default constructor.
        /// </summary>
        /// <param name="value"> verifier value </param>
        public Verifier(string value) {
            Debug.Assert(value != null, "Must provide a valid string as verifier");
            this.value = value;
        }

        public virtual string Value {
            get {
                return value;
            }
        }
    }

}