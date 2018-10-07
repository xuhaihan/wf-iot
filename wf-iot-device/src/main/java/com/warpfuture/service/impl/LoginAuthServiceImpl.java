package com.warpfuture.service.impl;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.Production;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.repository.FindProductInfoRepository;
import com.warpfuture.service.LoginAuthService;
import com.warpfuture.util.ECCUtils;
import com.warpfuture.util.IdUtils;
import com.warpfuture.util.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/** Created by fido on 2018/4/13. */
@Service
public class LoginAuthServiceImpl implements LoginAuthService {
  private static final String ENCRYPT_RSA = "RSA";
  private static final String ENCRYPT_ECC = "ECC";

  @Autowired private FindProductInfoRepository findProductInfoRepository;

  @Autowired private DeviceRepository deviceRepository;

  private Logger logger = LoggerFactory.getLogger(LoginAuthServiceImpl.class);

  @Override
  public Map<String, Object> checkWithKey(String productionKey, String deviceId) {
    long startTime = System.currentTimeMillis();
    Production production = findProductInfoRepository.findByProductionKey(productionKey);
    if (production == null) {
      return null;
    }
    boolean result = manageDevice(deviceId, production);
    if (result == true) {
      long endTime = System.currentTimeMillis();
      logger.info("==弱校验用时(成功)==:" + (endTime - startTime) + "ms");
      return this.getReturnMap(production.getAccountId(), production.getProductionId(), deviceId);
    } else return null;
  }

  @Override
  public Map<String, Object> checkWithSecure(String pksToken, String mode) {
    long startTime = System.currentTimeMillis();
    String[] args = pksToken.split("\\.");
    String encryptedStr = args[0]; // 加密串
    String productionPublicSecure = args[1]; // 公钥
    boolean result = false;
    Production production = findProductInfoRepository.findByPPS(productionPublicSecure);
    if (production == null) {
      return null;
    }
    // RSA解密
    String str = null;
    if (ENCRYPT_RSA.equals(mode)) {
      // 获得解密后的字符串，包括[productionKey,deviceId,nowTime]
      str = RSAUtils.decrypt(production.getProductionPrivateSecure(), encryptedStr);
    }
    // ECC解密
    else if (ENCRYPT_ECC.equals(mode)) {
      str = ECCUtils.decrypt(encryptedStr, production.getProductionPrivateSecure());
    }
    String[] strs = str.split(",");
    String key = strs[0]; // 传来的productionKey
    // 传来的productionKey与找到的productionKey不相同
    if (!production.getProductionKey().equals(key)) {
      return null;
    }
    StringBuffer buffer = new StringBuffer();
    for (int i = 1; i < strs.length - 1; i++) {
      buffer.append(strs[i]);
    }
    String deviceId = buffer.toString();
    result = manageDevice(deviceId, production);
    if (result == true) {
      long endTime = System.currentTimeMillis();
      logger.info("==强校验用时(成功)==:" + (endTime - startTime) + "ms");
      return this.getReturnMap(production.getAccountId(), production.getProductionId(), deviceId);
    } else return null;
  }

  /**
   * @param deviceId
   * @param production
   * @return
   */
  private boolean manageDevice(String deviceId, Production production) {
    Device deviceRecord =
        deviceRepository.findById(
            production.getAccountId(), production.getProductionId(), deviceId);
    boolean result = false;
    // 该设备之前有记录
    if (deviceRecord != null) {
      if (!deviceRecord.getProductionId().equals(production.getProductionId())) {
        result = false;
      } else {
        Long time = System.currentTimeMillis();
        deviceRecord.setLastConnectTime(time);
        deviceRecord.setSendMsgTime(time); // 该数据最后一次发包时间
        deviceRepository.updateDevice(deviceRecord);
        result = true;
      }
    } else {
      Device device = new Device();
      device.setDeviceCloudId(IdUtils.getId());
      device.setAccountId(production.getAccountId());
      device.setProductionId(production.getProductionId());
      device.setDeviceId(deviceId);
      device.setFirstConnectTime(System.currentTimeMillis());
      device.setLastConnectTime(System.currentTimeMillis());
      device.setUpdateTime(System.currentTimeMillis());
      device.setSendMsgTime(System.currentTimeMillis()); // 该数据最后一次发包时间
      deviceRepository.save(device);
      result = true;
    }
    return result;
  }

  private Map<String, Object> getReturnMap(String accountId, String productionId, String deviceId) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("accountId", accountId);
    map.put("productionId", productionId);
    map.put("deviceId", deviceId);
    return map;
  }
}
