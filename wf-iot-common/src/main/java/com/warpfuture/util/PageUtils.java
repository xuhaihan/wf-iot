package com.warpfuture.util;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.entity.HistoryDataPageModel;
import com.warpfuture.entity.PageModel;

/** Created by fido on 2018/4/19. 分页工具 */
public class PageUtils {

  /**
   * 用于计算mongodb分页时跳过的数据数量
   *
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public static Integer getSkip(Integer pageIndex, Integer pageSize) {
    return (pageIndex - 1) * pageSize;
  }

  /**
   * 设置页码请求极限值，防止错误页码
   *
   * @param pageModel
   * @return
   */
  public static PageModel dealPage(PageModel pageModel) {
    if (pageModel != null) {
      Integer pageIndex = pageModel.getPageIndex();
      Integer pageSize = pageModel.getPageSize();

      if (pageIndex < 1) {
        pageModel.setPageIndex(1);
      }

      if (pageSize < PageConstant.MIN_PAGE_SIZE || pageSize > PageConstant.MAX_PAGE_SIZE) {
        pageModel.setPageSize(PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
      }

      Integer rowCount = pageModel.getRowCount();
      if (rowCount != null) {
        Integer maxPageIndex = Integer.valueOf(String.valueOf(Math.ceil(rowCount / pageSize)));
        if (pageIndex > maxPageIndex) {
          pageModel.setPageIndex(maxPageIndex);
        }
      }
    } else {
      pageModel = new PageModel();
      pageModel.setPageIndex(1);
      pageModel.setPageSize(PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
    }
    return pageModel;
  }

  public static PageModel dealPage(Integer pageSize, Integer pageIndex, Integer defaultPageSize) {
    PageModel pageModel = new PageModel();
    if (pageSize != null && pageIndex != null) {

      if (pageIndex < 1) {
        pageModel.setPageIndex(1);
      } else {
        pageModel.setPageIndex(pageIndex);
      }

      if (pageSize < PageConstant.MIN_PAGE_SIZE || pageSize > PageConstant.MAX_PAGE_SIZE) {
        pageModel.setPageSize(defaultPageSize);
      } else {
        pageModel.setPageSize(pageSize);
      }
    } else {
      pageModel.setPageIndex(1);
      pageModel.setPageSize(defaultPageSize);
    }
    return pageModel;
  }

  public static PageModel dealOverPage(Integer pageIndex, Integer pageSize, Integer rowCount) {
    PageModel pageModel = new PageModel();
    pageModel.setPageSize(pageSize);
    pageModel.setRowCount(rowCount);
    pageModel.setPageIndex(pageIndex);
    if (pageIndex < 1) {
      pageModel.setPageIndex(1);
    }

    if (pageSize < PageConstant.MIN_PAGE_SIZE || pageSize > PageConstant.MAX_PAGE_SIZE) {
      pageModel.setPageSize(PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
    }
    if (rowCount != null) {
      Integer maxPageIndex = 0;
      if (rowCount % pageSize == 0) {
        maxPageIndex = rowCount / pageSize;
      } else maxPageIndex = (rowCount / pageSize) + 1;
      if (pageIndex > maxPageIndex) {
        pageModel.setPageIndex(maxPageIndex);
      }
    }
    pageModel.setSkip(PageUtils.getSkip(pageModel.getPageIndex(), pageModel.getPageSize()));
    return pageModel;
  }

  public static HistoryDataPageModel dealOverPage(
      Integer pageIndex, Integer pageSize, Long rowCount) {
    HistoryDataPageModel pageModel = new HistoryDataPageModel();
    pageModel.setPageSize(pageSize);
    pageModel.setRowCount(rowCount);
    pageModel.setPageIndex(pageIndex);
    if (pageIndex < 1) {
      pageModel.setPageIndex(1);
    }

    if (pageSize < PageConstant.MIN_PAGE_SIZE || pageSize > PageConstant.MAX_PAGE_SIZE) {
      pageModel.setPageSize(PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
    }
    if (rowCount != null) {
      Integer maxPageIndex = (int) Math.ceil(rowCount / pageSize);
      if (pageIndex > maxPageIndex) {
        pageModel.setPageIndex(maxPageIndex);
      }
    }
    pageModel.setSkip(PageUtils.getSkip(pageModel.getPageIndex(), pageModel.getPageSize()));
    return pageModel;
  }
}
