package fun.kwok.hainan.Utils;

import fun.kwok.hainan.entity.UserInfo;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


public class Spider {

    public static void code(HttpServletRequest httprequest, HttpServletResponse httpresponse) throws Exception {
        httpresponse.setHeader("pragma", "no-cache");
        httpresponse.setHeader("cache-control", "no-cache");
        httpresponse.setHeader("expires", "0");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://cxx.hnks.gov.cn/hnscore/serialno.do?time=")
                .method("GET", null)
                .addHeader("Host", "cxx.hnks.gov.cn")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:96.0) Gecko/20100101 Firefox/96.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-User", "?1")
                .build();
        Response response = client.newCall(request).execute();
        String cookie=response.headers().get("Set-Cookie");
        httprequest.getSession().setAttribute("cookie", cookie);
        httpresponse.getOutputStream().write(response.body().bytes());
    }

    public static boolean cxLogin(String cookie,String memberno,String memberpwd,String rand) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "kslx=yjs_2022&memberno="+memberno+"&memberpwd="+memberpwd+"&rand="+rand);
        Request request = new Request.Builder()
                .url("https://cxx.hnks.gov.cn/hnscore/login/kslogin")
                .method("POST", body)
                .addHeader("Host", "cxx.hnks.gov.cn")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:96.0) Gecko/20100101 Firefox/96.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .addHeader("Origin", "https://cxx.hnks.gov.cn")
                .addHeader("Referer", "https://cxx.hnks.gov.cn/hnscore/login/indexks?kslx=yjs_2022")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Cookie",cookie)
                .build();
        Response response = client.newCall(request).execute();
        if (response.body().string().contains("登录成功"))
                return true;
        return false;
    }

    public static UserInfo getInfo(String cookie) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://cxx.hnks.gov.cn/hnscore/login/loginScoreIndexKs")
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:96.0) Gecko/20100101 Firefox/96.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Referer", "https://cxx.hnks.gov.cn/hnscore/login/kslogin")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en;q=0.7")
                .addHeader("Cookie",cookie)
                .build();
        Response response = client.newCall(request).execute();
        String html=response.body().string();
        Document doc = Jsoup.parse(html);
        if ("思想政治理论".equals(doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[1]/td[1]/p").html())
            &&"英语（一）".equals(doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[2]/td[1]/p").html())
            &&"数学（一）".equals(doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[3]/td[1]/p").html())
            &&"计算机学科专业基础".equals(doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[4]/td[1]/p").html())
        ){
            String name=doc.selectXpath("//*[@id=\"print\"]/div[2]/div[1]/div[1]/span[2]").html();
            String ksno=doc.selectXpath("//*[@id=\"print\"]/div[2]/div[1]/div[2]/span[2]").html();
            String idcard=doc.selectXpath("//*[@id=\"print\"]/div[2]/div[2]/div/span[2]").html();
            String score1=doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[1]/td[2]/p").html();
            String score2=doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[2]/td[2]/p").html();
            String score3=doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[3]/td[2]/p").html();
            String score4=doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[4]/td[2]/p").html();
            String countScore=doc.selectXpath("//*[@id=\"print\"]/div[3]/table/tbody/tr[5]/td[2]/p").html();
            UserInfo userInfo=new UserInfo();
            userInfo.setHash(MD5Util.MD5Lower(idcard.toUpperCase()+name));
            userInfo.setKsno(ksno.substring(0,10));
            userInfo.setScore1(Float.parseFloat(score1));
            userInfo.setScore2(Float.parseFloat(score2));
            userInfo.setScore3(Float.parseFloat(score3));
            userInfo.setScore4(Float.parseFloat(score4));
            userInfo.setCountScore(Float.parseFloat(countScore));
            return userInfo;
        }else {
            return null;
        }
    }


}
