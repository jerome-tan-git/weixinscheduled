import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;

public class TmpCSUSScr {
	public static void main(String[] args) throws IOException, JSONException {
		if (args.length == 0) {
			System.out.println("Please input the path of config file");
			System.exit(1);
		}
		String configStr = FileUtils.readFileToString(new File(args[0]));
		// System.out.println(configStr);

		Config conf = JSON.parseObject(configStr, Config.class);
		String[] urls = { "http://www.idealhomegarden.com/topic/color%2Bideas%2Bfor%2Bbathrooms/",
				"http://www.candofinance.com/topic/low%2Bincome%2Bhousing/",
				"http://www.smarterschooling.com/topic/credit%2Bcard/",
				"http://www.internetcorkboard.com/sst/houses%2Bfor%2Brent/",
				"http://www.everymantravel.com/topic/cheap%2Bflights/",
				"http://www.everymanbusiness.com/topic/scrap%2Bmetal%2Bprices/"};

		for (String url : urls) {
			SendMessage.SendAnyURL(args[1], conf, url);
		}
	}
}
