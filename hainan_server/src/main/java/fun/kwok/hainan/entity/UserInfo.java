package fun.kwok.hainan.entity;

import lombok.Data;

@Data
public class UserInfo {
    private int rank;
    private String hash;
    private String ksno; //考生号 取前10位 用于区分不同专业
    private float score1; //政治
    private float score2; //英语一
    private float score3; //数学一
    private float score4; //408
    private float countScore; //总分

}
