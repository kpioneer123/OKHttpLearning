package com.haocai.http;

import java.util.Map;

/**
 * Created by Xionghu on 2018/7/6.
 * Desc:
 */

public interface NameValueMap<K, V> extends Map<K, V> {
    String get(String name);

    void set(String name, String value);

    void setAll(Map<String, String> map);
}
