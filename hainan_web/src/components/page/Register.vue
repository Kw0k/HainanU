<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title"><h4>海大录分排名系统-注册</h4>
              <h6>仅支持计科学硕和网安学硕使用</h6></div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input
                        show-word-limit
                        maxlength="18"
                        size='medium'
                        v-model="param.username"
                        placeholder="请输入身份证号"
                        @keyup.enter.native="submitForm()">
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
                <el-form-item prop="checkcode">
                    <el-input
                        show-word-limit
                        maxlength="4"
                        class='login-code-input' size='medium' v-model="param.checkcode" placeholder="请输入验证码"   @keyup.enter.native="submitForm()">
                        <el-button slot="prepend" icon="el-icon-circle-check"></el-button>
                    </el-input>
                        <img ref='checkCodeRef' @click='getCheckCode()' class='login-code-img' :src="serverUrl+'/checkcode'" alt=''>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()" :loading.sync="loginBtnLoading">{{loginBtnLoading?'注册中':'注册'}}</el-button>
                </div>
                <p class="login-tips"></p>
              <el-link type="primary" href="/#/login">登录</el-link>
            </el-form>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data: function() {
        return {
            param: {
                username: '',
                password: '',
                checkcode:''
            },
            rules: {
                username: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
                password: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                checkcode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
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
                            '/register',
                            this.qs.stringify({
                              memberno: this.param.username,
                              memberpwd: this.param.password,
                                rand:this.param.checkcode
                            })
                        ).then((response) =>{
                    this.loginBtnLoading=false
                        if (response.data.flag){
                            this.$message.success(response.data.msg);
                          this.$router.push("/login")
                        }else {
                            this.$message.error(response.data.msg);
                            if (response.data.code===200)
                              this.$router.push("/login")
                            this.getCheckCode();
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
        },
        getCheckCode(){
            this.$refs.checkCodeRef.src=this.axios.defaults.baseURL+"/checkcode"+"?time="+new Date().getTime();
        },
    },
  created() {
    this.getUser();
    this.getCheckCode();
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
