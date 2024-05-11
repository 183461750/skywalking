package org.apache.skywalking.oap.server.core.analysis.manual.log;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogStrUtil {

    /**
     * Parsing log content
     */
    public static Map<String, String> parseAllBy(String content) {
        String pattern = "\\#\\[[^\\]]*\\]";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(content);
        ArrayList<String> objects = new ArrayList<>();
        while (matcher.find()) {
            objects.add(unWrap(matcher.group(), "#[", "]"));
        }
        return objects.stream().map(s -> s.split("=")).collect(Collectors.toMap(s -> s[0], s -> s[1], (v1, v2) -> v2));
    }

    public static boolean isWrap(CharSequence str, String prefix, String suffix) {
        String str2 = str.toString();
        return str2.startsWith(prefix) && str2.endsWith(suffix);
    }

    public static String unWrap(CharSequence str, String prefix, String suffix) {
        return isWrap(str, prefix, suffix) ? sub(str, prefix.length(), str.length() - suffix.length()) : str.toString();
    }

    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        int len = str.length();
        if (fromIndexInclude < 0) {
            fromIndexInclude += len;
            if (fromIndexInclude < 0) {
                fromIndexInclude = 0;
            }
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }

        if (toIndexExclude < 0) {
            toIndexExclude += len;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }

        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }

        return fromIndexInclude == toIndexExclude ? "" : str.toString().substring(fromIndexInclude, toIndexExclude);
    }

}