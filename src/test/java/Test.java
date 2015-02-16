import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.SchedulingPattern;


public class Test {
	public static void main(String[] args) throws IOException
	{
//		SchedulingPattern sp = new SchedulingPattern("*/5 * * * *");
//		System.out.println(sp.match(System.currentTimeMillis()));
		
		String configStr = FileUtils.readFileToString(new File("config.json"));
		
		Config conf = JSON.parseObject(configStr, Config.class);
		
		Scheduled s = new Scheduled();
		s.setTime("* * * * *");
		s.setToTag("1|2");
		
		Scheduled s1 = new Scheduled();
		s1.setTime("*/5 * * * *");
		s1.setToTag("1|2");
		
		URLObj u = conf.getUrls().get(0);
		u.getSchedule().add(s);
		u.getSchedule().add(s1);
		
		
		System.out.println(JSON.toJSONString(conf, true));
		
		
		
	}
}
