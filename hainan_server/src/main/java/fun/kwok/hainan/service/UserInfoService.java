package fun.kwok.hainan.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.kwok.hainan.entity.UserInfo;
import fun.kwok.hainan.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

   public int addUserInfo(UserInfo userInfo){
        return userInfoMapper.addUserInfo(userInfo);
    }
    public UserInfo login(String hash){
       return userInfoMapper.login(hash);
    }
    public PageInfo<UserInfo> getUserInfo(int pageNum, int pageSize, String ksno){
        PageHelper.startPage(pageNum,pageSize,true);
        List<UserInfo> userInfoList=userInfoMapper.getUserInfo(ksno);
        return new PageInfo<UserInfo>(userInfoList);
    }
}
