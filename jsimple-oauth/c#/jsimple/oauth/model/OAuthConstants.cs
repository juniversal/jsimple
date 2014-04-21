/* 
Copyright 2010 Pablo Fernandez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
namespace jsimple.oauth.model {

    /// <summary>
    /// This class contains OAuth constants, used project-wide
    /// 
    /// @author Pablo Fernandez
    /// </summary>
    public class OAuthConstants {
        public const string TIMESTAMP = "oauth_timestamp";
        public const string SIGN_METHOD = "oauth_signature_method";
        public const string SIGNATURE = "oauth_signature";
        public const string CONSUMER_SECRET = "oauth_consumer_secret";
        public const string CONSUMER_KEY = "oauth_consumer_key";
        public const string CALLBACK = "oauth_callback";
        public const string VERSION = "oauth_version";
        public const string NONCE = "oauth_nonce";
        public const string PARAM_PREFIX = "oauth_";
        public const string TOKEN = "oauth_token";
        public const string TOKEN_SECRET = "oauth_token_secret";
        public const string OUT_OF_BAND = "oob";
        public const string VERIFIER = "oauth_verifier";
        public const string HEADER = "Authorization";
        public static readonly Token EMPTY_TOKEN = new Token("", "");
        public const string SCOPE = "scope";

        //OAuth 2.0
        public const string ACCESS_TOKEN = "access_token";
        public const string CLIENT_ID = "client_id";
        public const string CLIENT_SECRET = "client_secret";
        public const string REDIRECT_URI = "redirect_uri";
        public const string CODE = "code";
        public const string GRANT_TYPE = "grant_type";
    }

}