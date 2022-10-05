package com.solidigm.core.services;

//import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.apache.sling.api.resource.LoginException;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface ProcessDynamicsArticles {
    String aquireToken() throws UnsupportedEncodingException;
//    String getKnowledgeArticles(String accessToken);
    String fetchArticles(String accessToken);
    void processArticles(String articleJson) throws UnsupportedEncodingException, LoginException;
}

