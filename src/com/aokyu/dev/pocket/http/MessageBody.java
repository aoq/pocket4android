package com.aokyu.dev.pocket.http;

import java.io.UnsupportedEncodingException;

public interface MessageBody {

    public String toJson();
    public String toParameter();
    public String toEncodedParameter() throws UnsupportedEncodingException;

}
