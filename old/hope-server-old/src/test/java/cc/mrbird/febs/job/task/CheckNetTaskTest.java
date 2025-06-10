package cc.mrbird.febs.job.task;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckNetTaskTest extends TestCase {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CheckNetTask checkNetTask;

    @Value("${spring.mail.username}")
    private String sourceEmail;

    @Test
    public void testSendMail() {
        checkNetTask.sendMail("linuxmorebetter@gmail.com,949465194@qq.com,linux2011@qq.com","text","text");
    }
}