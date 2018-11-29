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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.ga.LocaleNames_ga;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Transactional(propagation = Propagation.REQUIRED)
    @ApiOperation(value = "新增用户",notes = "新增注册")
    @RequestMapping(value = "/createUser",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResObject createUser(@RequestBody User user) throws Exception {
         user.setCreateBy("admin");
        if(user.getPassword() =="" || user.getPassword()==null ) {
            throw new Exception("密码不能为空");
        } if(user.getUser_name()=="" || user.getUser_name()==null){
            throw new Exception("用户名不能为空");
        }
        userSerivce.insert(user);
        System.out.println("createUser" + user.toString());
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
        List<User> usersList= userSerivce.getUserList(id);
        if(usersList!=null && usersList.size()>0){
            for(User users:usersList){
                usersList.get(0).getId();
            }
        }

        return new ResObject(HttpStatus.OK.value(), usersList);
    }
}
