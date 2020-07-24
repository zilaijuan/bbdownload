package edu.zlj.bbdownload.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

/**
 * @author Fcx
 */
public class FileUtils {
    private FileUtils() {
    }

    /**
     * NIO读取文件返回字符串
     *
     * @param path
     * @param cs 编码格式 Charset.forName("UTF8)
     * @return
     * @throws IOException
     */
    public static String readText(Path path, Charset cs) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, cs)) {
            StringBuffer stringBuffer = new StringBuffer();
            for (; ; ) {
                String line = reader.readLine();
                if (line == null)
                    break;
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }
    }

    /**
     * 处理文件上传
     *
     * @param file
     * @param basePath 存放文件的目录的绝对路径 servletContext.getRealPath("/upload")
     * @return
     */
    public static String upload(MultipartFile file, String basePath) throws IOException {
        String filePath = "C:\\java\\mgr_resource\\friendList";
        // 设置文件存储路径
        byte[] bytes = file.getBytes();
        Path path = Paths.get(filePath);
        //保存在本地
        Files.write(path, bytes);
        return path.toString();
    }

    /**
     * 下载网络文件返回byte数组
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(String url) throws IOException {
        //网络下载
        HttpURLConnection conn = (HttpURLConnection) new URL
                (url).openConnection();

        InputStream is = conn.getInputStream();
        ByteArrayOutputStream baos = null;

        if (conn.getResponseCode() == 200) {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];//1k
            int len = -1;
            //获取资源的总长度
            int totalLen = conn.getContentLength();
            int curLen = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
                //3.计算下载 进度
                curLen += len;
                int p = curLen * 100 / totalLen;
                System.out.println("jindu" + p + "%");
            }
            is.close();
            conn.disconnect();
        }
        return baos.toByteArray();
    }

    /**
     * 下载文件到本地
     * @param urlString
     * @param filename
     * @throws Exception
     */
    public static void download(String urlString, String path, String filename)
            throws Exception {
        URL url = new URL(urlString);// 构造URL
        URLConnection con = url.openConnection();// 打开连接
        InputStream is = con.getInputStream();// 输入流
        String code = con.getHeaderField("Content-Encoding");
        if ((null != code) && code.equals("gzip")) {
            GZIPInputStream gis = new GZIPInputStream(is);
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File file = new File(path,filename);
            OutputStream os = new FileOutputStream(file);
            // 开始读取
            while ((len = gis.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            gis.close();
            os.close();
            is.close();
        } else {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File file = new File(path,filename);
            OutputStream os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }
    }
}
