package com.audioweb.common.utils.audio;
import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
/**
 * wav或pcm音频转码MP3
 * @ClassName: Wav2Mp3 
 * @Description: wav或pcm音频转码MP3
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年4月28日 下午1:55:52
 */
public class Wav2Mp3 {
	/**
	 * wav转化mp3
	 * @param source
	 * @param desFileName
	 * @throws EncoderException
	 */
	public static void wav2Mp3 (File source, String desFileName) throws EncoderException {
		File target = new File (desFileName);
		AudioAttributes audio = new AudioAttributes ();
		audio.setCodec ("libmp3lame");
		audio.setBitRate (128000);
		audio.setChannels (1);
		audio.setSamplingRate (44100);
		EncodingAttributes attrs = new EncodingAttributes ();
		attrs.setFormat ("mp3");
		attrs.setAudioAttributes (audio);
		Encoder encoder = new Encoder ();
		encoder.encode (source, target, attrs);
	}
	
	public static void main(String[] args) {
		File file = new File("C:\\Users\\10155\\AppData\\Local\\Temp\\wav3944596981757957040.wav");
		try {
			wav2Mp3(file,"E:/test.mp3");
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}