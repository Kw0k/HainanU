<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">
              <h4>海大录分排名系统</h4>
              <h6>仅支持计科学硕和网安学硕使用</h6>
            </div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input
                        show-word-limit
                        maxlength="18"
                        size='medium' v-model="param.username" placeholder="请输入身份证号"   @keyup.enter.native="submitForm()">
                        <el-button slot="prepend" icon="el-icon-user"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input

                        size='medium'
                        placeholder="请输入姓名"
                        v-model="param.password"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()" :loading.sync="loginBtnLoading">{{loginBtnLoading?'登录中':'登录'}}</el-button>
                </div>
                <p class="login-tips"></p>
              <el-link type="primary" href="/#/register">注册</el-link>
            </el-form>
        </div>
      <el-dialog
          title="使用前必读"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <span style="color: red">0.第一次使用时请先注册</span><br>
        <span>1.本网站只用于海南大学计科学硕和网安学硕的成绩录入排名使用</span><br>
        <span>2.本网站不会储存你的身份证和姓名信息（系统只存放散列值，用于登录验证）</span><br>
        <span>3.本网站不会储存你的考生号信息（只会储存前10位用于区分专业）</span><br>
        <span>4.填写的人数越多，数据越准确</span><br>
        <span style="color: red">5.本网站数据为海南大学考生所有，未经书面许可，任何人不得以任何方式盗用转载相关数据</span><br>
        <span >6.本网站源码已放在GitHub <a href="https://github.com/Kw0k/HainanU">GitHub链接</a></span><br>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
      </el-dialog>
    </div>

</template>

<script>
import axios from 'axios';

export default {
    data: function() {
        return {
          dialogVisible:true,
            param: {
                username: '',
                password: '',
            },
            rules: {
                username: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
                password: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
            },
          loginBtnLoading:false,
          serverUrl:axios.defaults.baseURL
        };
    },
    methods: {
        submitForm() {
          this.$refs.login.validate(valid => {
                if (valid) {
                  this.loginBtnLoading=true
                  axios.post(
                            '/login',
                            this.qs.stringify({
                              memberno: this.param.username,
                              memberpwd: this.param.password,
                            })
                        ).then((response) =>{
                    this.loginBtnLoading=false
                        if (response.data.flag){
                            this.$message.success(response.data.msg);
                          this.$router.push("/admin")
                        }else {
                            this.$message.error(response.data.msg);
                            if (response.data.code===403)
                              this.$router.push("/register")
                        }
                        })
                      .catch((error)=>{
                        this.$message.error(error);
                        this.loginBtnLoading=false
                        console.log(error);
                      });
                } else {
                    this.$message.error('请填写完整');
                  return false;
                }
            });
        }
    },
  created() {

  }
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: #242f42;
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 40px;
    color: #F5F5F5;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 450px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-code-input{
    width: 60%;
    float: left;
}
.login-code-img{
    margin-left: 5%;
    height: 35px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 35px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>
