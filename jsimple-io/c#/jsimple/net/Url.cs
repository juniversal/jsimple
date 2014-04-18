namespace jsimple.net {



    /// <summary>
    /// @author Bret Johnson
    /// @since 10/7/12 3:28 AM
    /// </summary>
    public class Url {
        private string url;

        public Url(string url) {
            this.url = url;
        }

        /// <summary>
        /// Return the query part of the URL or null if there is no query party.  For example:
        /// <p/>
        /// http://www.acme.com/abc?foo=3&bar=4#abcdef  =>  foo=3&bar=4
        /// <p/>
        /// http://www.acme.com/abc  =>  null
        /// </summary>
        /// <returns> query part of URL or null </returns>
        public virtual string Query {
            get {
                int questionMarkIndex = url.IndexOf('?');
                if (questionMarkIndex == -1)
                    return null;
                else {
                    int queryStartIndex = questionMarkIndex + 1;
    
                    int hashIndex = url.IndexOf('#', queryStartIndex);
                    if (hashIndex == -1)
                        return url.Substring(queryStartIndex);
                    else
                        return url.Substring(queryStartIndex, hashIndex - queryStartIndex);
                }
            }
        }
    }

}