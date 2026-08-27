package com.mall.product.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.product.product.entity.SpuInfoDescEntity;
import com.mall.product.product.entity.SpuInfoEntity;
import com.mall.product.product.vo.SpuSaveVo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * spu信息
 *
 * @author preludqian
 * @email 2551932043@qq.com
 * @date 2026-05-19 21:42:34
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo) throws InvocationTargetException, IllegalAccessException;

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

