package com.mall.member.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.exception.BizCodeEnum;
import com.mall.member.member.exception.PhoneExistException;
import com.mall.member.member.exception.UsernameExistException;
import com.mall.member.member.feign.CouponFeignService;
import com.mall.member.member.vo.MemberLoginVo;
import com.mall.member.member.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mall.member.member.entity.MemberEntity;
import com.mall.member.member.service.MemberService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;



/**
 * 会员
 *
 * @author preludqian
 * @email 2551932043@qq.com
 * @date 2026-05-20 13:27:59
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R memberCoupons = couponFeignService.memberCoupons();

        return R.ok().put("member", memberEntity).put("coupons", memberCoupons.get("coupons"));
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){
        MemberEntity entity = memberService.login(vo);
        if (entity != null){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.LOGINACCOUNT_PASSWORD_INVALID_EXCEPTION.getCode(), BizCodeEnum.LOGINACCOUNT_PASSWORD_INVALID_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo){
        try{
            memberService.regist(vo);
        }catch (PhoneExistException e){
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UsernameExistException e){
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
