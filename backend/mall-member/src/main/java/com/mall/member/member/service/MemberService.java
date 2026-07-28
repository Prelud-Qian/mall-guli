package com.mall.member.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.member.member.entity.MemberEntity;
import com.mall.member.member.exception.PhoneExistException;
import com.mall.member.member.exception.UsernameExistException;
import com.mall.member.member.vo.MemberRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author preludqian
 * @email 2551932043@qq.com
 * @date 2026-05-20 13:27:59
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;
    void checkUsernameUnique(String username) throws UsernameExistException;
}

