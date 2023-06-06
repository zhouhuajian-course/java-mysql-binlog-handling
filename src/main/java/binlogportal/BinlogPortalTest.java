package binlogportal;

import com.insistingon.binlogportal.BinlogPortalException;
import com.insistingon.binlogportal.BinlogPortalStarter;
import com.insistingon.binlogportal.config.BinlogPortalConfig;
import com.insistingon.binlogportal.config.RedisConfig;
import com.insistingon.binlogportal.config.SyncConfig;
import com.insistingon.binlogportal.distributed.RedisDistributedHandler;
import com.insistingon.binlogportal.event.EventEntity;
import com.insistingon.binlogportal.event.handler.IEventHandler;
import com.insistingon.binlogportal.position.BinlogPositionEntity;
import com.insistingon.binlogportal.position.IPositionHandler;
import com.insistingon.binlogportal.position.RedisPositionHandler;
import com.insistingon.binlogportal.tablemeta.TableMetaEntity;

public class BinlogPortalTest {
    public static void main(String[] args) {
        SyncConfig syncConfig = new SyncConfig();
        syncConfig.setHost("192.168.1.205");
        syncConfig.setPort(3306);
        syncConfig.setUserName("root");
        syncConfig.setPassword("root");

        syncConfig.addEventHandlerList(new IEventHandler() {
            @Override
            public void process(EventEntity eventEntity) throws BinlogPortalException {
                System.out.println(eventEntity);
                // System.out.println(eventEntity.getColumns().get(0).getName());
                System.out.print("列名：");
                for (TableMetaEntity.ColumnMetaData column : eventEntity.getColumns()) {
                    System.out.print(column.getName() + " ");
                }
                System.out.println();
            }
        });

        BinlogPortalConfig binlogPortalConfig = new BinlogPortalConfig();
        binlogPortalConfig.addSyncConfig(syncConfig);

        RedisConfig redisConfig = new RedisConfig("192.168.1.205", 6379);
        RedisPositionHandler redisPositionHandler = new RedisPositionHandler(redisConfig);
        binlogPortalConfig.setPositionHandler(redisPositionHandler);
        binlogPortalConfig.setDistributedHandler(new RedisDistributedHandler(redisConfig));

        BinlogPortalStarter binlogPortalStarter = new BinlogPortalStarter();
        binlogPortalStarter.setBinlogPortalConfig(binlogPortalConfig);
        try {
            binlogPortalStarter.start();
        } catch (BinlogPortalException e) {
            e.printStackTrace();
        }
    }
}
