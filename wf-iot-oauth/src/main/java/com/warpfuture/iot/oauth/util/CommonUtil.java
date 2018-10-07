package com.warpfuture.iot.oauth.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.classify.PatternMatcher.match;

public class CommonUtil {

  /**
   * 判断字符串是否符合邮箱格式.
   *
   * @param str 指定的字符串
   * @return 是否是邮箱:是为true，否则false
   */
  public static Boolean isEmail(String str) {
    Boolean isEmail = false;
    String expr =
        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";

    if (str.matches(expr)) {
      isEmail = true;
    }
    return isEmail;
  }

  /**
   * 判断是否是手机号
   *
   * @param phone
   * @return
   */
  public static boolean checkPhone(String phone) {
    Pattern pattern = Pattern.compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
    Matcher matcher = pattern.matcher(phone);

    if (matcher.matches()) {
      return true;
    }
    return false;
  }

  /**
   * 判断字符串是否符合常规网址格式
   *
   * @param str 待验证的字符串
   * @return 如果是符合网址格式的字符串,返回true,否则为false
   */
  public static boolean isHomepage(String str) {
    String regex = "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*";
    return match(regex, str);
  }
}
