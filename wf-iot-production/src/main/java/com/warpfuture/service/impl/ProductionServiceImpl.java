package com.warpfuture.service.impl;

import com.warpfuture.constant.Constant;
import com.warpfuture.constant.EncryptConstant;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.ProductionRepository;
import com.warpfuture.service.ProductionService;
import com.warpfuture.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Created by fido on 2018/4/16. */
@Service
public class ProductionServiceImpl implements ProductionService {
  @Autowired private ProductionRepository productionRepository;

  // 新建产品时需要根据加密方式生成对应的前端并没有传过来的参数
  @Override
  public Production createProduction(Production production) {
    String productionName = production.getProductionName();
    String accountId = production.getAccountId();
    Production findProduction = productionRepository.getByProductionName(accountId, productionName);
    if (findProduction != null) {
      throw new ParameterIllegalException("产品名称已经存在");
    }
    int[] encryptRole = production.getEncryptRole(); // 获得加密规则数组
    // 判断加密规则
    int role = this.getRole(encryptRole);
    if (role == -1) {
      throw new ParameterIllegalException("非法参数");
    }
    // ECC
    if (role == Constant.ENCRYPT_ECC) {
      Map<String, String> keyPair = ECCUtils.getECCKeyPair();
      production.setProductionPublicSecure(keyPair.get(EncryptConstant.mapPublicKey));
      production.setProductionPrivateSecure(keyPair.get(EncryptConstant.mapPrivateKey));
    }
    // RSA
    else if (role == Constant.ENCRYPT_RSA) {
      Map<String, String> keyPair = RSAUtils.generateKeyPair();
      production.setProductionPublicSecure(keyPair.get(EncryptConstant.mapPublicKey));
      production.setProductionPrivateSecure(keyPair.get(EncryptConstant.mapPrivateKey));
    }
    production.setProductionKey(KeyUtils.createProductionKey());
    production.setProductionStatus(Constant.STATUS_READY); // 未发布
    production.setProductionId(IdUtils.getId());
    production.setCreateTime(System.currentTimeMillis());
    production.setUpdateTime(System.currentTimeMillis());
    production.setIsDelete(false);
    productionRepository.saveProduction(production);
    return productionRepository.findByProductionId(production.getProductionId());
  }

  @Override
  public PageModel<Production> findByAccountId(
      String accountId, Integer pageSize, Integer pageIndex) {
    return productionRepository.findByAccountId(accountId, pageSize, pageIndex);
  }

  @Override
  public Production findById(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    boolean permission = PermissionUtils.permission(production, accountId);
    if (!permission) {
      throw new PermissionFailException("查询权限错误");
    }
    return production;
  }

  @Override
  public Production updateProductionInfo(Production production) {
    String productionId = production.getProductionId();
    String accountId = production.getAccountId();
    Production originProduction = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(originProduction, accountId)) {
      throw new PermissionFailException("更新权限错误");
    }
    int[] originEncryptRole = originProduction.getEncryptRole(); // 原始的加密方式
    int[] newEncrypteRole = production.getEncryptRole(); // 新的加密方式
    int[] currentEncryptRole = null;
    boolean modify = false;
    if (originEncryptRole.length != newEncrypteRole.length) {
      currentEncryptRole = newEncrypteRole;
      modify = true;
    } else {
      for (int i = 0; i < originEncryptRole.length; i++) {
        if (originEncryptRole[i] != newEncrypteRole[i]) {
          modify = true;
          break;
        }
      }
    }
    if (modify) {
      int role = this.getRole(currentEncryptRole);
      if (role == -1) {
        throw new ParameterIllegalException("非法参数");
      }
      // TODO ECC
      if (role == Constant.ENCRYPT_ECC) {
        Map<String, String> keyPair = ECCUtils.getECCKeyPair();
        originProduction.setProductionPublicSecure(keyPair.get(EncryptConstant.mapPublicKey));
        originProduction.setProductionPrivateSecure(keyPair.get(EncryptConstant.mapPrivateKey));
      }
      // RSA
      else if (role == Constant.ENCRYPT_RSA) {
        Map<String, String> keyPair = RSAUtils.generateKeyPair();
        originProduction.setProductionPublicSecure(keyPair.get(EncryptConstant.mapPublicKey));
        originProduction.setProductionPrivateSecure(keyPair.get(EncryptConstant.mapPrivateKey));
      }
      originProduction.setProductionKey(KeyUtils.createProductionKey());
    }
    // 需要更新的字段：名称，描述，图片地址，拓展属性
    originProduction.setProductionLogo(production.getProductionLogo());
    originProduction.setProductionName(production.getProductionName());
    originProduction.setProductionDesc(production.getProductionDesc());
    originProduction.setUpdateTime(System.currentTimeMillis());
    productionRepository.updateProductionInfo(originProduction);
    return productionRepository.findByProductionId(productionId);
  }

  @Override
  public String regenKey(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("重新生成产品密码权限错误");
    }
    String productionKey = KeyUtils.createProductionKey();
    productionRepository.updateProductionKey(productionId, productionKey);
    return productionRepository.findByProductionId(productionId).getProductionKey();
  }

  @Override
  public String regenKeyPair(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("重新生成密钥对权限错误");
    }
    Integer role = this.getRole(production.getEncryptRole());
    String productionPublicSecure = null;
    String productionPrivateSecure = null;
    // RSA
    if (role == Constant.ENCRYPT_RSA) {
      Map<String, String> keyPair = RSAUtils.generateKeyPair();
      productionPublicSecure = keyPair.get(EncryptConstant.mapPublicKey);
      productionPrivateSecure = keyPair.get(EncryptConstant.mapPrivateKey);
    }
    // ECC
    else if (role == Constant.ENCRYPT_ECC) {
      Map<String, String> keyPair = ECCUtils.getECCKeyPair();
      productionPublicSecure = keyPair.get(EncryptConstant.mapPublicKey);
      productionPrivateSecure = keyPair.get(EncryptConstant.mapPrivateKey);
    } else {
      throw new ParameterIllegalException("非法参数");
    }
    productionRepository.regenKeyPair(
        productionId, productionPublicSecure, productionPrivateSecure);
    return productionRepository.findByProductionId(productionId).getProductionPublicSecure();
  }

  @Override
  public Production publishProduction(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("产品发布权限错误");
    }
    productionRepository.publishProduction(productionId);
    return productionRepository.findByProductionId(productionId);
  }

  @Override
  public Production revokeProduction(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("产品下架权限错误");
    }
    productionRepository.revokeProduction(productionId);
    return productionRepository.findByProductionId(productionId);
  }

  @Override
  public void deleteProduction(String productionId, String accountId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("删除产品权限错误");
    }
    productionRepository.deleteProduction(productionId);
  }

  @Override
  public PageModel<Production> findByName(
      String accountId, String productionName, Integer pageSize, Integer pageIndex) {
    return productionRepository.findByName(accountId, productionName, pageSize, pageIndex);
  }

  /**
   * 判断加密规则
   *
   * @param encryptRole
   * @return
   */
  private int getRole(int[] encryptRole) {
    if (encryptRole.length > 2) {
      return -1;
    }
    int result = 0;
    int sum = 0;
    for (int i = 0; i < encryptRole.length; i++) {
      sum = encryptRole[i] + sum;
      if (encryptRole[i] == Constant.ENCRYPT_ECC) {
        result = Constant.ENCRYPT_ECC;
      } else if (encryptRole[i] == Constant.ENCRYPT_RSA) {
        result = Constant.ENCRYPT_RSA;
      }
    }
    if (sum == 3) {
      return -1;
    }
    return result;
  }

  @Override
  public Production updateExtensions(Production production) {
    String productionId = production.getProductionId();
    String accountId = production.getAccountId();
    Production originProduction = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(originProduction, accountId)) {
      throw new PermissionFailException("更新权限错误");
    }
    originProduction.setExtensions(production.getExtensions());
    originProduction.setUpdateTime(System.currentTimeMillis());
    productionRepository.updateProductionInfo(originProduction);
    return productionRepository.findByProductionId(productionId);
  }
}
