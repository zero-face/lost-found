package com.example.lostfound.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.LossThingVO;
import com.example.lostfound.entity.TFoundThing;
import com.example.lostfound.entity.TFoundThingVO;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.service.TFoundThingService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.utils.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.Tags;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.ws.soap.Addressing;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/api/v1/found")
@Api(tags = "寻物接口")
@Validated
@Slf4j
public class TFoundThingController extends BaseController{
    @Autowired
    private TFoundThingService foundThingService;
    @Autowired
    private TLossThingService lossThingService;


    /**
     * 发布寻物
     * @param name
     * @param pic
     * @param address
     * @param lossTime
     * @param des
     * @param type
     * @param userId
     * @return
     * @throws BusinessException
     */
    @GetMapping("/pubfound")
    public CommonReturnType publishFound(@RequestParam("name")@NotBlank String name,
                                         @RequestParam(value = "pic",required = false)MultipartFile pic,
                                         @RequestParam("address")@NotBlank String address,
                                         @RequestParam("lossTime")@NotNull Long lossTime,
                                         @RequestParam("des")@NotBlank String des,
                                         @RequestParam("type")@NotBlank String type,
                                         @RequestParam("userId")@NotEmpty Integer userId) throws BusinessException {
        String picUrl = null;
        if(pic != null) {
            picUrl = lossThingService.uploadImage(pic);
            if(StringUtils.isEmpty(picUrl)) {
                log.info("没有上传图片");
            }
        }
        String finalPicUrl = picUrl;
        TFoundThing foundThing = new TFoundThing() {{
            setName(name);// 名称
            setAddress(address); //地址
            setDescription(des); //描述
            setLoseTime(lossTime); //丢失时间
            setPictureUrl(finalPicUrl); //图片
            setPublishUserId(userId); //发布人
            setType(type); //发布类型
        }};
        //放入寻物表
        foundThingService.save(foundThing);
        return CommonReturnType.success(null,"发布成功");
    }

    /**
     * 分页查询所有的寻物信息
     * @param pn
     * @return
     */
    @GetMapping("/founds/{pn}")
    public CommonReturnType getAllFoundThing(@PathVariable("pn")Integer pn){
        //查询第pn，每页5条，不查询数据总数
        final Page<TFoundThing> page = new Page<>(pn, 5,false);
        final Page<TFoundThing> lossThingPage = foundThingService.page(page,new QueryWrapper<TFoundThing>().eq("status", 0));
        final List<TFoundThing> records = lossThingPage.getRecords();
        if(records != null || records.size() > 0) {
            final List<TFoundThingVO> tFoundThingVOS = foundThingService.converToLossVO(records);
            return CommonReturnType.success(tFoundThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");
    }

    /**
     * 按类型查找失物
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    public CommonReturnType getFoundBytype(@PathVariable("type")String type) {
        final List<TFoundThing> list = foundThingService.list(new QueryWrapper<TFoundThing>().eq("type", type).eq("status", 0));
        if(list == null || list.size() == 0) {
            return CommonReturnType.fail(null,"没有该类型的失物");
        }
        final List<TFoundThingVO> foundThingVOS = foundThingService.converToLossVO(list);
        return CommonReturnType.success(foundThingVOS,"查找成功");
    }

}

