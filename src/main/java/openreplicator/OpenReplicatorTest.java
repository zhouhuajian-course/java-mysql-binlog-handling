package openreplicator;

import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OpenReplicatorTest {
    public static void main(String[] args) throws Exception {
        final OpenReplicator or = new OpenReplicator();
        or.setUser("root");
        or.setPassword("root");
        or.setHost("192.168.1.205");
        or.setPort(3306);
        // or.setServerId(6789);
        or.setBinlogPosition(157);
        or.setBinlogFileName("binlog.000001");
        or.setBinlogEventListener(new BinlogEventListener() {
            public void onEvents(BinlogEventV4 event) {
                // your code goes here
                System.out.println(event);
            }
        });
        or.start();

        System.out.println("press 'q' to stop");
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for(String line = br.readLine(); line != null; line = br.readLine()) {
            if(line.equals("q")) {
                // or.stop();
                break;
            }
        }
    }
}
