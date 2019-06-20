package com.itstar.findjobleip.util;

import org.jsoup.nodes.Document;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

//import javax.swing.text.html.parser.Element;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/*****
 *
 */

public class GetHtmlByUrl {

    public static void main(String[] args) {
        //System.out.println("test");

        //创建文件输出流
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\job.txt"));
            for (int i = 0; i < 10; i++) {
                //String urlObj="https://www.qq.com";
                //String urlObj = "https://m.liepin.com/zhaopin/pn" + i + "/?degradeFlag=0";
                String urlObj = "https://m.liepin.com/zhaopin/pn" + i + "/?keyword=%E5%A4%A7%E6%95%B0%E6%8D%AE&industrys=000&dqs=020";
                //String encoding = "GBK";
                String encoding = "UTF-8";
                String html = getHtml(urlObj, encoding);
                System.out.println(html);

                //解析源代码 获取对象
                Document doc = Jsoup.parse(html);

                //获取招聘列表 JavaScript
                Elements jobList = doc.getElementsByClass("job-card");

                //遍历集合获取地址
                for (Element jobUrl : jobList) {
                    //获取地址
                    String href = jobUrl.getElementsByClass("job-name").get(0).attr("href");
                    //过滤 结尾
                    if (href.endsWith(".shtml")) {
                        //招聘获取源码
                        String jobHtml = getHtml(href, "UTF-8");

                        //解析招聘获取源码
                        Document jobparse = Jsoup.parse(jobHtml);

                        //获取招聘内容
                        String text = jobparse.getElementsByClass("content-word").get(0).text();

                        //清洗过滤非中文字符
                        String[] attr = text.split("[^a-zA-Z]+");

                        for (String words : attr) {
                            //大小写转换
                            String key = words.toLowerCase() + " ";
                            fos.write(key.getBytes());
                            fos.flush();
                        }

                        ////创建集合保存字符串
                        //ArrayList stringCount=new ArrayList();

                        System.out.println(text);
                        System.out.println("====================");


                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭 fos
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*****
     *
     * @param urlObj
     * @param encoding
     * @return
     */
    public static String getHtml(String urlObj, String encoding) {
        BufferedReader bfr = null;
        StringBuilder sb = new StringBuilder();

        try {

            URL url = null;

            //1. 建立连接
            url = new URL(urlObj);

            //2. c+a+t 打开链接
            URLConnection uc = url.openConnection();

            //3. 读取源代码
            //3.1 创建文件输入流--》管道
            InputStream isr = uc.getInputStream();
            //3.2 创建一个缓冲流 创建一个转换流 装饰者模式 关闭一个就行
            bfr = new BufferedReader(new InputStreamReader(isr, encoding));

            //isr.read();  //字节读取效率低 平凡访问磁盘伤害大

            //3.3 开始读取 判断条件：如果没有读到内容，就读完了
            String line = "";
            while ((line = bfr.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4 关闭流
            if (bfr != null) {
                try {
                    bfr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
