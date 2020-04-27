package com.audioweb.common.utils.tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import it.sauronsoftware.jave.EncoderException;

public class TtsMain {

    public static void main(String[] args) throws IOException {
        (new TtsMain()).run();
    }

    //  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
    private final String appKey = "DIM7ejlHN0hR1pPsK4bH7wTZ";

    // 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
    private final String secretKey = "imtneWjdVVW703Sr1kURb95hxEguVNbi";

    // text 的内容为"欢迎使用百度语音合成"的urlencode,utf-8 编码
    // 可以百度搜索"urlencode"
    private final String text = "自行车向前转动着时光," + 
    		"在后座遗落了谁目光," + 
    		"不曾想你穿着校服模样," + 
    		"也从我生活离场," + 
    		"风吹过吹旧了那条小巷," + 
    		"到尽头追不到你余光," + 
    		"我的梦只有你懂得欣赏," + 
    		"那是最美的时光," + 
    		"都是我还来不及学会勇敢," + 
    		"让你一个人孤单," + 
    		"我害怕青春就此离散," + 
    		"让遗憾成遗憾," + 
    		"你我从此无关," + 
    		"遗憾是片青春的书签," + 
    		"夹在了最痛的那一页," + 
    		"那个夏天像秋天的落叶," + 
    		"假装灿烂不害怕离别," + 
    		"一场大雨湿透整个世界," + 
    		"流逝我们的一切," + 
    		"都是我还来不及学会勇敢," + 
    		"让你一个人走散," + 
    		"我多想能像昨天一样," + 
    		"任凭青春荒唐," + 
    		"你不要再悲伤," + 
    		"都是我还来不及学会勇敢," + 
    		"恨这一生太短暂," + 
    		"我愿意用所有去交换," + 
    		"换为时还不晚," + 
    		"换余生不孤单," + 
    		"啦啦啦啦啦啦啦啦啦," + 
    		"啦啦啦啦啦啦啦啦啦";

    // 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
    // 精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
    private final int per = 0;
    // 语速，取值0-15，默认为5中语速
    private final int spd = 5;
    // 音调，取值0-15，默认为5中语调
    private final int pit = 5;
    // 音量，取值0-9，默认为5中音量
    private final int vol = 5;

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private final int aue = 6;

    public final String url = "http://tsn.baidu.com/text2audio"; // 可以使用https

    private String cuid = "1234567JAVA";

    private void run() throws IOException {
        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
        holder.refresh();
        String token = holder.getToken();

        // 此处2次urlencode， 确保特殊字符被正确编码
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(text));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            String format = getFormat(aue);
            File file = File.createTempFile("wav", "."+format); // 打开mp3文件即可播放
            // System.out.println( file.getAbsolutePath());
            FileOutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            System.out.println("audio file write to " + file.getAbsolutePath());
            try {
				Wav2Mp3.wav2Mp3(file, "E:/test.mp3");
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            System.err.println(res);
        }
    }

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }
}
