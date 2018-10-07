package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Tag;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "wf-iot-production-service")
public interface TagEnterpriseFeignService {

    @PostMapping(value = "/tag/create")
    ResultVO<Tag> createTag(@RequestBody Tag tag);

    @PostMapping(value = "/tag/update")
    ResultVO<Tag> updateTag(@RequestBody Tag tag);

    @PostMapping(value = "/tag/delete")
    ResultVO<Tag> deleteTag(@RequestBody Tag tag);

    @PostMapping(value = "/tag/query")
    ResultVO<Tag> queryTag(@RequestBody Tag tag);

    @PostMapping(value = "/tag/account/query")
    ResultVO<PageModel<Tag>> queryTagsByAccountId(@RequestParam(value = "accountId") String accountId,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/tag/addDevs")
    ResultVO addDeviceTag(@RequestBody Tag tag, @RequestParam(value = "deviceList") List<String> deviceList);

    @PostMapping(value = "/tag/removeDevs")
    ResultVO removeDeviceTag(@RequestBody Tag tag, @RequestParam(value = "deviceList") List<String> deviceList);

}
