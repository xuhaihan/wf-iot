package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.iot.api.enterprise.service.TagEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TagEnterpriseController {

    @Autowired
    private TagEnterpriseService tagEnterpriseService;

    @PostMapping(value = "/tag/add")
    public ResultVO addDeviceTag(Model model) {
        Tag tag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);

        if (CompareUtil.tagIdNotNull(tag)) {
            if (tag.getDeviceList() != null && !tag.getDeviceList().isEmpty()) {
                return tagEnterpriseService.addDeviceTag(tag, tag.getDeviceList());
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/tag/remove")
    public ResultVO removeDeviceTag(Model model) {
        Tag tag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);

        if (CompareUtil.tagIdNotNull(tag)) {
            if (tag.getDeviceList() != null && !tag.getDeviceList().isEmpty()) {
                return tagEnterpriseService.removeDeviceTag(tag, tag.getDeviceList());
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 新增
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/tag")
    public ResultVO createTag(Model model) {
        Tag tag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);

        if (CompareUtil.tagNotNull(tag)) {
            return tagEnterpriseService.createTag(tag);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    /**
     * 更新
     *
     * @param model
     * @return
     */
    @PutMapping(value = "/tag")
    public ResultVO updateTag(Model model) {
        Tag tag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);

        if (CompareUtil.tagIdNotNull(tag)) {
            return tagEnterpriseService.updateTag(tag);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/tag/{tagId}")
    public ResultVO queryTag(@PathVariable String tagId, HttpServletRequest request) {
//        Tag flag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Tag tag = new Tag(accountId, tagId);
        if (CompareUtil.tagIdNotNull(tag)) {
            return tagEnterpriseService.queryTag(tag);
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/tag")
    public ResultVO queryTagsByAccountId(HttpServletRequest request, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.TAG_DEFAULT_PAGE_SIZE);
            return tagEnterpriseService.queryTagByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO<>().fail(ResponseMsg.PARAMS_NULL);
    }

    @DeleteMapping(value = "/tag/{tagId}")
    public ResultVO deleteTag(HttpServletRequest request, @PathVariable String tagId) {
//        Tag flag = ModelBeanUtil.getBeanFromModel(model, "flag", Tag.class);
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Tag tag = new Tag(accountId, tagId);
        if (CompareUtil.tagIdNotNull(tag)) {
            return tagEnterpriseService.deleteTag(tag);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute("tag")
    public Tag setAccountId(@RequestBody(required = false) Tag tag, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (tag != null && !StringUtils.isEmpty(accountId)) {
            tag.setAccountId(accountId);
        }
        return tag;
    }

}
