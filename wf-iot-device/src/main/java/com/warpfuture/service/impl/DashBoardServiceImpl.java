package com.warpfuture.service.impl;

import com.warpfuture.constant.DashBoardConstant;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.locationInfo.*;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.service.DashBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/20. 获取账户下在线设备的区域 */
@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {
  @Autowired private DeviceRepository deviceRepository;
  @Autowired private RestTemplate restTemplate;

  @Override
  public List<DashBoardInfo> dashBoard(String accountId) {
    List<Device> deviceList = deviceRepository.getByAccountId(accountId);
    Map<String, DashBoardInfo> devInfoMap = new HashMap<>();
    for (Device device : deviceList) {
      if (device.getLocations() != null) {
        String city = device.getLocations().get("city");
        DashBoardInfo data = this.getDashBoardInfo(city);
        if (devInfoMap.containsKey(city)) {
          DashBoardInfo dashBoardInfo = devInfoMap.get(city);
          dashBoardInfo.setNums(dashBoardInfo.getNums() + 1);
          dashBoardInfo.setCity(city);
          devInfoMap.put(city, dashBoardInfo);
        } else {
          data.setNums(1L);
          data.setCity(city);
          devInfoMap.put(city, data);
        }
      } else {
        String url =
            DashBoardConstant.QUERY_IP_ADDRESS
                + "?ip="
                + device.getDeviceIp()
                + "&key="
                + DashBoardConstant.QUERY_IP_KEY;
        String encodeURL = null;
        try {
          encodeURL = URLEncoder.encode(url, "UTF8");
        } catch (Exception e) {
          log.info("进行URL编码出错");
        }
        ResponseEntity<IPResponse> result = restTemplate.getForEntity(url, IPResponse.class);
        IPResponse ipResponse = result.getBody(); // 获得请求响应对象
        log.info("==得到的响应对象为==" + ipResponse);
        if (0 == ipResponse.getStatus()) {
          Result ipResult = ipResponse.getResult();
          Location location = ipResult.getLocation();
          String lat = location.getLat(); // 纬度
          String lng = location.getLng(); // 经度
          AddressInfo addressInfo = ipResult.getAd_info();
          String city = addressInfo.getCity();
          log.info("==调用接口获得的城市信息为==" + city);
          Map<String, String> map = new HashMap<>();
          map.put("city", city);
          map.put("latitude", String.valueOf(lat));
          map.put("longitude", String.valueOf(lng));
          device.setLocations(map);
          deviceRepository.updateDevice(device);
          DashBoardInfo data = this.getDashBoardInfo(city);
          if (devInfoMap.containsKey(city)) {
            DashBoardInfo dashBoardInfo = devInfoMap.get(city);
            dashBoardInfo.setNums(dashBoardInfo.getNums() + 1);
            dashBoardInfo.setCity(city);
            devInfoMap.put(city, dashBoardInfo);
          } else {
            data.setNums(1L);
            devInfoMap.put(city, data);
          }
        }
      }
    }
    List<DashBoardInfo> list = new ArrayList<>();
    for (Map.Entry<String, DashBoardInfo> entry : devInfoMap.entrySet()) {
      list.add(entry.getValue());
    }
    return list;
  }

  public DashBoardInfo getDashBoardInfo(String city) {
    DashBoardInfo dashBoardInfo = new DashBoardInfo();
    String url =
        DashBoardConstant.QUERY_CITY_ADDRESSS
            + "?address="
            + city
            + "&key="
            + DashBoardConstant.QUERY_IP_KEY;
    String encodeURL = null;
    try {
      encodeURL = URLEncoder.encode(url, "UTF8");
    } catch (Exception e) {
      log.info("进行URL编码出错");
    }
    ResponseEntity<IPResponse> result = restTemplate.getForEntity(url, IPResponse.class);
    IPResponse ipResponse = result.getBody(); // 获得请求响应对象
    log.info("==得到的响应对象为==" + ipResponse);
    if (0 == ipResponse.getStatus()) {
      Result ipResult = ipResponse.getResult();
      Location location = ipResult.getLocation();
      String lat = location.getLat(); // 纬度
      String lng = location.getLng(); // 经度
      dashBoardInfo.setLat(lat);
      dashBoardInfo.setLng(lng);
    }
    return dashBoardInfo;
  }
}
