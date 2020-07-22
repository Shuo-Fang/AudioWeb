import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.shiro.codec.Base64;

import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkTerminal;

public class testMain {
	
    public static void main(String[] args) throws NoSuchAlgorithmException {
        //ByteBuffer buffer = InterCmdProcess.sendCast(true,"127.0.0.1",11000,40, CastWorkType.PAGING);
        //System.out.println(buffer.array().toString());
		/*
		 * WorkTerminal ter = new WorkTerminal(); WorkCastTask task = new
		 * WorkCastTask(); task.setTaskName("测试1"); ter.setCastTask(task);
		 * ter.getCastTaskList().add(ter.getCastTask()); WorkCastTask task2 = new
		 * WorkCastTask(); task2.setTaskName("测试2"); ter.setCastTask(task2);
		 * System.out.println(ter.toString());
		 */
    	KeyGenerator keygen = KeyGenerator.getInstance("AES"); 
    	SecretKey deskey = keygen.generateKey(); 
    	System.out.println(Base64.encodeToString(deskey.getEncoded()));
    }
}
