package com.common.platform.base.config.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.*;

public class RequestData implements Serializable {
    private static final long serialVersionUID = 9081406366569775542L;

    private JSONObject data;

    private String ip;

    private String url;

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RequestData))
            return false;
        RequestData other = (RequestData)o;
        if (!other.canEqual(this))
            return false;
        Object this$data = getData(), other$data = other.getData();
        if ((this$data == null) ? (other$data != null) : !this$data.equals(other$data))
            return false;
        Object this$ip = getIp(), other$ip = other.getIp();
        if ((this$ip == null) ? (other$ip != null) : !this$ip.equals(other$ip))
            return false;
        Object this$url = getUrl(), other$url = other.getUrl();
        return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestData;
    }

    public String toString() {
        return "RequestData(data=" + getData() + ", ip=" + getIp() + ", url=" + getUrl() + ")";
    }

    public JSONObject getData() {
        return this.data;
    }

    public String getIp() {
        return this.ip;
    }

    public String getUrl() {
        return this.url;
    }

    public <T> T parse(Class<T> clazz) {
        Map<String, Object> innerMap = this.data.getInnerMap();
        HashMap<String, Object> resultMap = new HashMap<>();
        Set<Map.Entry<String, Object>> entries = innerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String fieldName = StrUtil.toCamelCase(key);
            resultMap.put(fieldName, entry.getValue());
        }
        return (T)BeanUtil.mapToBean(resultMap, clazz, true);
    }

    public <T> T parse(String key, Class<T> clazz) {
        return (T)this.data.getObject(key, clazz);
    }

    public Object[] getObjectArray(String key) {
        JSONArray jsonArray = this.data.getJSONArray(key);
        if (jsonArray != null)
            return jsonArray.toArray();
        return new Object[0];
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        JSONArray jsonArray = this.data.getJSONArray(key);
        if (jsonArray != null)
            return jsonArray.toJavaList(clazz);
        return new ArrayList<>();
    }

    public <T> T[] getArray(String key, T[] array) {
        JSONArray jsonArray = this.data.getJSONArray(key);
        if (jsonArray != null)
            return (T[])jsonArray.toArray((Object[])array);
        return array;
    }

    public Object get(String key) {
        return this.data.get(key);
    }

    public String getString(String key) {
        return this.data.getString(key);
    }

    public Integer getInteger(String key) {
        return this.data.getInteger(key);
    }

    public Long getLong(String key) {
        return this.data.getLong(key);
    }

    public Map<String, Object> parseMap() {
        return jsonObjet2Map(this.data);
    }

    private Map<String, Object> jsonObjet2Map(JSONObject jsonObj) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, Object>> entries = jsonObj.getInnerMap().entrySet();
        Iterator<Map.Entry<String, Object>> itera = entries.iterator();
        Map.Entry<String, Object> entry = null;
        Object value = null;
        while (itera.hasNext()) {
            entry = itera.next();
            value = entry.getValue();
            map.put(entry.getKey(), traversalData(value));
        }
        return map;
    }

    private Object jsonArray2List(JSONArray array) {
        List<Object> list = new ArrayList();
        Iterator<Object> itera = array.iterator();
        while (itera.hasNext()) {
            Object value = itera.next();
            list.add(traversalData(value));
        }
        return list;
    }

    private Object traversalData(Object value) {
        if (value instanceof JSONObject)
            return jsonObjet2Map((JSONObject)value);
        if (value instanceof JSONArray)
            return jsonArray2List((JSONArray)value);
        return value;
    }
}

