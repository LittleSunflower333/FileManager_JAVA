package com.dalaoyang.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dalaoyang.dao.UserMapper;
import com.dalaoyang.entity.ResObject;
import com.dalaoyang.entity.User;
import com.dalaoyang.service.UserSerivce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.ga.LocaleNames_ga;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dalaoyang
 * @Description
 * @project springboot_learn
 * @package com.dalaoyang.controller
 * @email yangyang@dalaoyang.cn
 * @date 2018/7/20
 */
@Api(description = "用户接口")
@RestController
@RequestMapping("/index")
public class UserController {

    @Resource
    private UserSerivce userSerivce;

    @ApiOperation(value = "新增用户",notes = "新增注册")
    @RequestMapping(value = "/createUser",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResObject createUser(@RequestBody User user){
            userSerivce.insert(user);
         System.out.println("createUser"+user.toString());
        return new ResObject(HttpStatus.OK.value(),"新增成功");
    }

    @ApiOperation(value="修改用户",notes = "修改用户")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResObject updateUser(@RequestBody User user){
        userSerivce.updateById(user);
        System.out.println("updateUser:::"+user.toString());
        return new ResObject(HttpStatus.OK.value(),"修改成功");

    }
    @ApiOperation(value = "删除用户",notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户标识", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/deleteUser",method = RequestMethod.DELETE)
    public ResObject deleteUser(@RequestParam("id") Long id) {
            userSerivce.deleteById(id);
        System.out.println("deleteUser:::"+id);
        return new ResObject(HttpStatus.OK.value(),"删除成功");
    }
    @ApiOperation(value = "查询用户" ,  notes="查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户标识", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value="/queryUser",method=RequestMethod.GET)
    public ResObject queryUser(@RequestParam("id") Long id){
        System.out.println("queryUser:::"+id);
        User user = new User("test","123456");
        userSerivce.updateById(user);
        return new ResObject(HttpStatus.OK.value(), user);
    }
}
