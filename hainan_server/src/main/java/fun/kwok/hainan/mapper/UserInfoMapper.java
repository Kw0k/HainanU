package fun.kwok.hainan.mapper;

import fun.kwok.hainan.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    @Insert("Insert into userInfo (hash,ksno,score1,score2,score3,score4,countScore) values(#{hash},#{ksno},#{score1},#{score2},#{score3},#{score4},#{countScore})")
    int addUserInfo(UserInfo userInfo);

    @Select("Select * from userInfo where hash=#{hash}")
    UserInfo login(String hash);

    @Select("SELECT ROW_NUMBER() over( ORDER BY countScore DESC) AS 'rank',score1,score2,score3,score4,countScore from userInfo where ksno=#{ksno}")
    List<UserInfo> getUserInfo(String ksno);

}
