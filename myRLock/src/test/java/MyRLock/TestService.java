package MyRLock;

import com.yen.MyRLock.annotation.MyRLock;
import com.yen.MyRLock.annotation.MyRLockKey;
import com.yen.MyRLock.model.LockTimeoutStrategy;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @MyRLock(waitTime = 10,leaseTime = 60,keys = {"#param"},lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    public String getValue(String param) throws Exception {
//        if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
//        Thread.sleep(1000*3);
//        }
        Thread.sleep(1000*3);
        return "success";
    }

    @MyRLock(keys = {"#userId"})
    public String getValue(String userId,@MyRLockKey Integer id)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }

    @MyRLock(keys = {"#user.name","#user.id"})
    public String getValue(User user)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }

}