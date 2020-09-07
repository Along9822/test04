package com.common.platform.base.config.json;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.MappingFastJsonValue;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.common.platform.base.config.context.HttpContext;
import com.common.platform.base.config.request.RequestData;
import com.common.platform.base.config.request.RequestDataHolder;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    public static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript");
    /** @deprecated */
    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];
    /** @deprecated */
    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];
    /** @deprecated */
    @Deprecated
    protected String dateFormat;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public CustomFastJsonHttpMessageConverter() {
        super(MediaType.ALL);
    }

    /** @deprecated */
    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    /** @deprecated */
    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    /** @deprecated */
    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    /** @deprecated */
    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    /** @deprecated */
    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    /** @deprecated */
    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    /** @deprecated */
    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    /** @deprecated */
    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    /** @deprecated */
    @Deprecated
    public void addSerializeFilter(SerializeFilter filter) {
        if (filter != null) {
            int length = this.fastJsonConfig.getSerializeFilters().length;
            SerializeFilter[] filters = new SerializeFilter[length + 1];
            System.arraycopy(this.fastJsonConfig.getSerializeFilters(), 0, filters, 0, length);
            filters[filters.length - 1] = filter;
            this.fastJsonConfig.setSerializeFilters(filters);
        }
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(contextClass, mediaType);
    }

    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return super.canWrite(clazz, mediaType);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return this.readType(this.getType(type, contextClass), inputMessage);
    }

    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.write(o, contentType, outputMessage);
    }

    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) {
        return this.readType(this.getType(clazz, (Class)null), inputMessage);
    }

    private Object readType(Type type, HttpInputMessage inputMessage) {
        try {
            InputStream in = inputMessage.getBody();
            Object parseObject = JSON.parseObject(in,
                    fastJsonConfig.getCharset(),
                    type,
                    fastJsonConfig.getParserConfig(),
                    fastJsonConfig.getParseProcess(),
                    JSON.DEFAULT_PARSER_FEATURE,
                    fastJsonConfig.getFeatures());
            if (parseObject != null) {
                Map<String, Object> map = BeanUtil.beanToMap(parseObject);
                JSONObject jsonObject = new JSONObject(map);
                RequestDataHolder.put(getRequestData(jsonObject));
            }
            return parseObject;
        } catch (JSONException var4) {
            throw new HttpMessageNotReadableException("JSON parse error: " + var4.getMessage(), var4);
        } catch (IOException var5) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", var5);
        }
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();

        try {
            HttpHeaders headers = outputMessage.getHeaders();
            SerializeFilter[] globalFilters = this.fastJsonConfig.getSerializeFilters();
            List<SerializeFilter> allFilters = new ArrayList(Arrays.asList(globalFilters));
            boolean isJsonp = false;
            Object value = this.strangeCodeForJackson(object);
            if (value instanceof FastJsonContainer) {
                FastJsonContainer fastJsonContainer = (FastJsonContainer)value;
                PropertyPreFilters filters = fastJsonContainer.getFilters();
                allFilters.addAll(filters.getFilters());
                value = fastJsonContainer.getValue();
            }

            if (value instanceof MappingFastJsonValue) {
                if (!StringUtils.isEmpty(((MappingFastJsonValue)value).getJsonpFunction())) {
                    isJsonp = true;
                }
            } else if (value instanceof JSONPObject) {
                isJsonp = true;
            }

            int len = JSON.writeJSONString(outnew, this.fastJsonConfig.getCharset(), value, this.fastJsonConfig.getSerializeConfig(), (SerializeFilter[])allFilters.toArray(new SerializeFilter[allFilters.size()]), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures());
            if (isJsonp) {
                headers.setContentType(APPLICATION_JAVASCRIPT);
            }

            if (this.fastJsonConfig.isWriteContentLength()) {
                headers.setContentLength((long)len);
            }

            outnew.writeTo(outputMessage.getBody());
        } catch (JSONException var14) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + var14.getMessage(), var14);
        } finally {
            outnew.close();
        }

    }

    private Object strangeCodeForJackson(Object obj) {
        if (obj != null) {
            String className = obj.getClass().getName();
            if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(className)) {
                return obj.toString();
            }
        }

        return obj;
    }

    protected Type getType(Type type, Class<?> contextClass) {
        return CustomFastJsonHttpMessageConverter.Spring4TypeResolvableHelper.isSupport() ? CustomFastJsonHttpMessageConverter.Spring4TypeResolvableHelper.getType(type, contextClass) : type;
    }

    private static class Spring4TypeResolvableHelper {
        private static boolean hasClazzResolvableType;

        private Spring4TypeResolvableHelper() {
        }

        private static boolean isSupport() {
            return hasClazzResolvableType;
        }

        private static Type getType(Type type, Class<?> contextClass) {
            if (contextClass != null) {
                ResolvableType resolvedType = ResolvableType.forType(type);
                if (type instanceof TypeVariable) {
                    ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable)type, ResolvableType.forClass(contextClass));
                    if (resolvedTypeVariable != ResolvableType.NONE) {
                        return resolvedTypeVariable.resolve();
                    }
                } else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
                    ParameterizedType parameterizedType = (ParameterizedType)type;
                    Class<?>[] generics = new Class[parameterizedType.getActualTypeArguments().length];
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();

                    for(int i = 0; i < typeArguments.length; ++i) {
                        Type typeArgument = typeArguments[i];
                        if (typeArgument instanceof TypeVariable) {
                            ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable)typeArgument, ResolvableType.forClass(contextClass));
                            if (resolvedTypeArgument != ResolvableType.NONE) {
                                generics[i] = resolvedTypeArgument.resolve();
                            } else {
                                generics[i] = ResolvableType.forType(typeArgument).resolve();
                            }
                        } else {
                            generics[i] = ResolvableType.forType(typeArgument).resolve();
                        }
                    }

                    return ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType();
                }
            }

            return type;
        }

        private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
            ResolvableType resolvedType;
            if (contextType.hasGenerics()) {
                resolvedType = ResolvableType.forType(typeVariable, contextType);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }

            ResolvableType superType = contextType.getSuperType();
            if (superType != ResolvableType.NONE) {
                resolvedType = resolveVariable(typeVariable, superType);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }

            ResolvableType[] var4 = contextType.getInterfaces();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ResolvableType ifc = var4[var6];
                resolvedType = resolveVariable(typeVariable, ifc);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }

            return ResolvableType.NONE;
        }

        static {
            try {
                Class.forName("org.springframework.core.ResolvableType");
                hasClazzResolvableType = true;
            } catch (ClassNotFoundException var1) {
                hasClazzResolvableType = false;
            }

        }
    }

    private RequestData getRequestData(JSONObject jsonObject) {
        RequestData requestData = new RequestData();
        HttpServletRequest request = HttpContext.getRequest();
        if (request != null) {
            requestData.setIp(request.getRemoteHost());
            requestData.setUrl(request.getServletPath());
        }
        requestData.setData(jsonObject);
        return requestData;
    }

}
