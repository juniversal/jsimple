using System;

namespace jsimple.oauth.model {

    using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;


    /// <summary>
    /// @author: Pablo Fernandez
    /// </summary>
    public class Parameter : IComparable<Parameter> {
        private const string UTF = "UTF8";

        private readonly string key;
        private readonly string value;

        public Parameter(string key, string value) {
            this.key = key;
            this.value = value;
        }

        public virtual string asUrlEncodedPair() {
            return OAuthEncoder.encode(key) + "=" + OAuthEncoder.encode(value);
        }

        public override bool Equals(object other) {
            if (other == null)
                return false;
            if (other == this)
                return true;

            if (!(other is Parameter))
                return false;
            Parameter otherParam = (Parameter) other;
            return otherParam.key.Equals(key) && otherParam.value.Equals(value);
        }

        public override int GetHashCode() {
            return key.GetHashCode() + value.GetHashCode();
        }

        public virtual int CompareTo(Parameter parameter) {
            int keyDiff = key.CompareTo(parameter.key);

            return keyDiff != 0 ? keyDiff : value.CompareTo(parameter.value);
        }
    }

}