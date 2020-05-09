import com.audioweb.common.enums.CastWorkType;
import com.audioweb.server.protocol.InterCmdProcess;

import java.nio.ByteBuffer;

public class testMain {
    public static void main(String[] args) {
        ByteBuffer buffer = InterCmdProcess.sendCast(true,"127.0.0.1",11000,40, CastWorkType.PAGING);
        System.out.println(buffer.array().toString());
    }

}
