package com.warpfuture.iot.oauth.util;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
  /**
   * 解析URL包含的参数
   *
   * @param url 路径
   * @return
   */
  public static Map<String, Object> analyzeUrl(String url) {
    Map<String, Object> map = new HashMap<>();
    if (url != null && !url.isEmpty()) {
      String strAllParam = null;
      String nurl = isoToUrl(url);
      String[] arrSplit = null;
      String[] arrParam = null;
      arrSplit = nurl.split("\\?");
      if (arrSplit.length > 2) {
        int index = nurl.lastIndexOf("?");
        nurl = nurl.substring(0, index) + urlToISO(nurl.substring(index, nurl.length()));
        arrSplit = nurl.split("\\?");
      }
      if (nurl.length() > 1) {
        if (arrSplit.length > 1) {
          if (arrSplit[1] != null) {
            nurl = arrSplit[0];
            strAllParam = arrSplit[1];
          }
          arrParam = strAllParam.split("&");
        }
      }
      if (arrParam != null) {
        for (int i = 0; i < arrParam.length; i++) {
          map.put(arrParam[i].split("=")[0], arrParam[i].split("=")[1]);
        }
      }
      map.put("url", nurl);
    }
    return map;
  }

  /**
   * 组装URL
   *
   * @param url
   * @param params
   * @return
   */
  public static String assembleURL(String url, Map<String, Object> params) {
    return assembleURL(url, params, false);
  }

  /**
   * 组装URL
   *
   * @param url 路径
   * @param params 组装的参数
   * @param trans 是否转码成ISO
   * @return
   */
  public static String assembleURL(String url, Map<String, Object> params, boolean trans) {
    if (url != null && !"".equals(url) && params != null) {
      Map<String, Object> temp = analyzeUrl(url);
      url = (String) temp.get("url");
      temp.remove("url");
      params.putAll(temp);
      StringBuffer newUrl = new StringBuffer();
      newUrl.append(url);
      int row = 0;
      for (String key : params.keySet()) {
        if (row == 0) {
          newUrl.append("?");
        } else {
          newUrl.append("&");
        }
        Object obj = params.get(key);
        if (obj instanceof Object[]) {
          Object[] o = (Object[]) obj;
          for (int i = 0; i < o.length; i++) {
            String val = urlToISO(o[i].toString());
            if (i == 0) {
              newUrl.append(key + "=" + val);
            } else {
              newUrl.append("&" + key + "=" + val);
            }
          }
        } else {
          String val = urlToISO(obj.toString());
          newUrl.append(key + "=" + val);
        }
        row++;
      }
      String returnUrl = newUrl.toString();
      if (trans) {
        return urlToISO(returnUrl);
      } else {
        return returnUrl;
      }
    }
    return null;
  }

  public static String appendParam(String url, Map<String, Object> params) {
    return appendParam(url, params, false);
  }

  public static String appendParam(String url, Map<String, Object> params, boolean trans) {
    if (url != null && !url.isEmpty() && params != null && params.size() > 0) {
      Map<String, Object> temp = analyzeUrl(url);
      String tempUrl = url;
      if (temp != null && temp.size() > 0) {
        tempUrl = (String) temp.get("url");
        temp.remove("url");
        params.putAll(temp);
      }
      String newUrl = assembleURL(tempUrl, params, trans);
      return newUrl;
    }
    return url;
  }

  /**
   * 获取URL的域名
   *
   * @param url 路径
   * @return
   */
  public static String getURLHost(String url) {
    return getURLHost(url, true, true);
  }

  public static String getURLHost(String url, boolean contrastHeard, boolean contrastPort) {
    if (url != null && !url.isEmpty()) {
      String tempUrl = isoToUrl(url);
      int start = 0, cut = 0, stop = 0;
      if (tempUrl.toLowerCase().startsWith("http://")) {
        cut = 7;
      } else if (tempUrl.toLowerCase().startsWith("https://")) {
        cut = 8;
      } else if (tempUrl.startsWith("//")) {
        cut = 2;
      } else {
        cut = 0;
      }
      if (!contrastHeard) {
        start = cut;
      }
      stop = tempUrl.indexOf("/", cut);
      if (!contrastPort) {
        int port = tempUrl.indexOf(":", cut);
        if (port > 0 && port < stop) {
          stop = port;
        }
      }
      if (stop < 0) {
        stop = tempUrl.length();
      }
      return tempUrl.substring(start, stop);
    }
    return null;
  }

  public static String urlToISO(String url) {
    if (url != null && !url.isEmpty()) {
      url =
          url.replace("?", "%3F")
              .replace("&", "%26")
              .replace(":", "%3A")
              .replace("/", "%2F")
              .replace(" ", "%20")
              .replace("=", "%3D");
      return url;
    }
    return null;
  }

  public static String isoToUrl(String url) {
    if (url != null && !url.isEmpty()) {
      url =
          url.replace("%3F", "?")
              .replace("%26", "&")
              .replace("%3A", ":")
              .replace("%2F", "/")
              .replace("%20", " ")
              .replace("%3D", "=");
      return url;
    }
    return null;
  }

  public static String toUTF8(String s) {
    if (s != null) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
        int c = s.charAt(i);
        if (c > 255) {
          sb.append("\\u" + Integer.toString(c, 16));
        } else {
          char c1 = (char) c;
          sb.append(c1);
        }
      }
      return sb.toString();
    }
    return null;
  }
}
