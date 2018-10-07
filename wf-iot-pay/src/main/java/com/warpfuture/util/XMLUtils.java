package com.warpfuture.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/18. 处理微信支付的XML工具类 */
@Slf4j
public class XMLUtils {

  /**
   * 将map转为xml
   *
   * @param map
   * @return
   */
  public static String map2str(Map<String, String> map) {
    StringBuilder xml = new StringBuilder("<xml>");
    if (map != null) {
      Iterator<String> keyIt = map.keySet().iterator();
      while (keyIt.hasNext()) {
        String key = keyIt.next();
        String val = map.get(key);
        if (StringUtils.isNumeric(val)) {
          xml.append("<" + key + ">").append(val).append("</" + key + ">");
        } else {
          xml.append("<" + key + "><![CDATA[").append(val).append("]]></" + key + ">");
        }
      }
      xml.append("</xml>");
    }
    String xmlStr = null;
    try {
      xmlStr = new String(xml.toString().getBytes(), "UTF-8");
    } catch (Exception e) {
      log.info("转成xml时编码出现错误");
    }
    return xmlStr;
  }

  /**
   * xml形式字符转为map
   *
   * @param xml
   * @return
   */
  public static Map<String, String> xmlStr2Map(String xml) {
    Map<String, String> map = new HashMap<>();
    try {
      if (StringUtils.isNotBlank(xml)) {
        Reader reader = new StringReader(xml);
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(reader);
        Element root = document.getRootElement();
        List<Element> children = root.getChildren();
        if (children != null && !children.isEmpty()) {
          for (Element child : children) {
            String name = child.getName();
            String value = child.getValue();
            if (StringUtils.isNotBlank(name)) {
              map.put(name, value);
            }
          }
        }
      }
    } catch (Exception e) {
      log.info("将xml转为map时出错");
    }
    return map;
  }
}
