package fun.kwok.hainan.controller;
import fun.kwok.hainan.Utils.IDCard;
import fun.kwok.hainan.Utils.MD5Util;
import fun.kwok.hainan.Utils.Spider;
import fun.kwok.hainan.entity.ResultInfo;
import fun.kwok.hainan.entity.UserInfo;
import fun.kwok.hainan.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
@Controller
public class MainController {

    @Value("${hainan.csno}")
    private String csno;
    @Value("${hainan.wano}")
    private String wano;


    @Autowired
    UserInfoService userInfoService;
    @GetMapping("/checkcode")
    public void checkcode(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        try {
            Spider.code(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @PostMapping("/getwa") //获取网安分页列表
    public ResultInfo getWA(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute("userInfo");
        if (userInfo!=null)
            return new ResultInfo(true,200,"获取完成",
                    userInfoService.getUserInfo(pageNum,pageSize,wano));
        return new ResultInfo(false,403,"请先登录",null);
    }

    @ResponseBody
    @PostMapping("/getcs") //获取计科分页列表
    public ResultInfo getCS(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute("userInfo");
        if (userInfo!=null)
            return new ResultInfo(true,200,"获取完成",
                    userInfoService.getUserInfo(pageNum,pageSize,csno));
        return new ResultInfo(false,403,"请先登录",null);
    }


    @ResponseBody
    @PostMapping("/login")
    public ResultInfo login(@RequestParam("memberpwd") String memberpwd,
                            @RequestParam("memberno") String memberno,
                            HttpSession session
                            ){
        if (!IDCard.isIDCard(memberno))
            return new ResultInfo(false,404,"请输入正确的身份证号码",null);
        UserInfo userInfo=userInfoService.login(MD5Util.MD5Lower(memberno.toUpperCase()+memberpwd));
        if (userInfo!=null) {
            session.setAttribute("userInfo", userInfo);
            return new ResultInfo(true,200,"登录成功",null);
        }
            return new ResultInfo(false,403,"请先注册",null);
    }

    @ResponseBody
    @PostMapping("/register")
    public ResultInfo register(@RequestParam("memberpwd") String memberpwd,
                               @RequestParam("memberno") String memberno,
                               @RequestParam("rand") String rand,
                               HttpSession session
                               ){
        if (!IDCard.isIDCard(memberno))
            return new ResultInfo(false,404,"请输入正确的身份证号码",null);
        UserInfo userInfo=userInfoService.login(MD5Util.MD5Lower(memberno.toUpperCase()+memberpwd));
        if (userInfo!=null) {
            session.setAttribute("userInfo", userInfo);
            return new ResultInfo(false,200,"你已注册过，请直接登录",null);
        }
        String cookie= (String) session.getAttribute("cookie");
        if (cookie!=null&&!"".equals(cookie)){
            try {
                if (Spider.cxLogin(cookie,memberno,memberpwd,rand)){
                    if (userInfoService.addUserInfo(Spider.getInfo(cookie))>0)
                    return new ResultInfo(true,200,"注册成功，请登录。",null);
                return new ResultInfo(false,500,"注册失败，服务器异常",null);
                }
                return new ResultInfo(false,404,"注册失败，请检查身份证、姓名以及验证码",null);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false,500,"服务器异常",e.toString());
            }
        }
        return new ResultInfo(false,404,"参数错误",null);
    }



}
