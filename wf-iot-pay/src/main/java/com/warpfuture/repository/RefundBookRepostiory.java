package com.warpfuture.repository;

import com.warpfuture.entity.RefundBook;

/** @Auther: fido @Date: 2018/6/5 21:22 @Description:退款证书的地址 */
public interface RefundBookRepostiory {
  public void insert(RefundBook refundBook);

  public RefundBook findById(String refundLocation);
}
