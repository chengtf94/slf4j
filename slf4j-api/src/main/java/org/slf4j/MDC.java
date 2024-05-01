package org.slf4j;

import java.io.Closeable;
import java.util.Map;

import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.spi.MDCAdapter;

/**
 * 映射诊断上下文
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @since 1.4.1
 */
public class MDC {
    private MDC() {
    }

    /** MDC适配器 */
    static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
    static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
    static MDCAdapter mdcAdapter;

    static {
        try {
            // 获取MDC适配器
            mdcAdapter = bwCompatibleGetMDCAdapterFromBinder();
        } catch (NoClassDefFoundError ncde) {
            mdcAdapter = new NOPMDCAdapter();
            String msg = ncde.getMessage();
            if (msg != null && msg.contains("StaticMDCBinder")) {
                Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
                Util.report("Defaulting to no-operation MDCAdapter implementation.");
                Util.report("See " + NO_STATIC_MDC_BINDER_URL + " for further details.");
            } else {
                throw ncde;
            }
        } catch (Exception e) {
            // we should never get here
            Util.report("MDC binding unsuccessful.", e);
        }
    }
    private static MDCAdapter bwCompatibleGetMDCAdapterFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMDCBinder.getSingleton().getMDCA();
        } catch (NoSuchMethodError nsme) {
            // binding is probably a version of SLF4J older than 1.7.14
            return StaticMDCBinder.SINGLETON.getMDCA();
        }
    }

    /**
     * 添加键值对
     */
    public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
        put(key, val);
        return new MDCCloseable(key);
    }
    public static void put(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        mdcAdapter.put(key, val);
    }

    /**
     * 获取KV对
     */
    public static String get(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }

        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        return mdcAdapter.get(key);
    }

    /**
     * 删除KV对
     */
    public static void remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        mdcAdapter.remove(key);
    }

    /**
     * 删除全部KV对
     */
    public static void clear() {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        mdcAdapter.clear();
    }

    /**
     * 获取上下文快照Map
     */
    public static Map<String, String> getCopyOfContextMap() {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        return mdcAdapter.getCopyOfContextMap();
    }

    /**
     * 设置上下文Map
     */
    public static void setContextMap(Map<String, String> contextMap) {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also " + NULL_MDCA_URL);
        }
        mdcAdapter.setContextMap(contextMap);
    }

    public static MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    public static class MDCCloseable implements Closeable {
        private final String key;

        private MDCCloseable(String key) {
            this.key = key;
        }

        public void close() {
            MDC.remove(this.key);
        }
    }

}
