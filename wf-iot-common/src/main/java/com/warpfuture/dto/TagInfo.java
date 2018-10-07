package com.warpfuture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/** Created by fido on 2018/4/23. 用于标明设备属于哪个标签 */
@Data
@ToString
@AllArgsConstructor
public class TagInfo {
  private String tagId; // 标签Id

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TagInfo)) {
      return false;
    }
    TagInfo tag = (TagInfo) o;
    return tagId.equals(((TagInfo) o).getTagId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagId);
  }
}
