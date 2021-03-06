package autotest.api.gyApi.testCase.user;

import autotest.api.gyApi.bean.changePwd.ChangePwdReq;
import autotest.api.gyApi.bean.changePwd.ChangePwdResp;
import autotest.api.gyApi.bean.user.login.LoginReq;
import autotest.api.gyApi.bean.user.login.LoginResp;
import autotest.api.gyApi.bean.user.signUp.SignUpReq;
import autotest.api.gyApi.bean.user.signUp.SignUpResp;
import autotest.api.common.BaseApi;
import autotest.tools.CSVReader;
import autotest.tools.JdbcTools;
import autotest.tools.PinYinTools;
import autotest.tools.PropertiesTools;
import autotest.tools.RandomTool;
import java.util.Map;
import java.util.Properties;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserTest extends BaseApi {



  @Test
  public void signUpTest(){
    Properties prop= PropertiesTools.getProperties("src\\test\\resources\\api\\api.properties");
    SignUpReq signUpReq=new SignUpReq();
    String userName= PinYinTools.toFirstChar(RandomTool.getChineseName())+RandomTool.randomInt(100, 999);
    String phone=RandomTool.getTel();
    System.out.println(userName);
    signUpReq.setUserName(userName);
    signUpReq.setPwd(prop.getProperty("api.common.password"));
    signUpReq.setPhone(phone);
    signUpReq.setRePwd(prop.getProperty("api.common.password"));
    SignUpResp signUpResp=(SignUpResp)httpTools.sendHttpJsonAPI(prop.getProperty("api.host.baseurl")+"/user/signup",signUpReq, SignUpResp.class);
  }


  @Test
  public void changePwd(){
    ChangePwdReq changePwdReq=new ChangePwdReq();
    changePwdReq.setNewPwd("a123456");
    changePwdReq.setOldPwd("a234567");
    changePwdReq.setReNewPwd("a123456");
    changePwdReq.setUserName("wuling999");
    ChangePwdResp changePwdResp=(ChangePwdResp)httpTools.sendHttpJsonAPI(baseUrl+"/user/changepwd", changePwdReq, ChangePwdResp.class);
  }

@Test
  public void lockTest(){
    String sql="select t.user_name,t.pwd from t_user_user t where t.status=0 limit 0,1";
    JdbcTools jdbcTools=new JdbcTools();
    Map result=jdbcTools.getRecord(sql);
    String userName=(String)result.get("user_name");
    String pwd=(String)result.get("pwd");
    System.out.println("用户名："+userName+",密码："+pwd);

    LoginReq loginReq=new LoginReq();
    loginReq.setUserName(userName);
    loginReq.setPwd(pwd);
    LoginResp loginResp= (LoginResp) httpTools.sendHttpJsonAPI(baseUrl+"/user/login", loginReq, LoginResp.class);
  }

  @DataProvider(name="loginData")
  public Object[][] csvProvider(){
    Object[][] data=CSVReader.readCSV("src\\test\\resources\\api\\loginData.csv");
    return data;
  }

  @Test
  public void loginBatch(){
    Object[][] data=CSVReader.readCSV("src\\test\\resources\\api\\loginData.csv");
    for (int i=0;i<data.length;i++){
      Object[] item=data[i];
      LoginReq loginReq=new LoginReq();
      loginReq.setUserName((String)item[0]);
      loginReq.setPwd((String)item[1]);
      LoginResp loginResp= (LoginResp) httpTools.sendHttpJsonAPI(baseUrl+"/user/login", loginReq, LoginResp.class);

    }
  }

  @Test
  public void testMethod(){
    String[] s1={"a","b","c"};
    String[] s2={"A","B","C"};
    String[][] s3={s1,s2};
    String[] x=s3[0];
    String y=s3[0][1];
    System.out.println(y);
  }
}
