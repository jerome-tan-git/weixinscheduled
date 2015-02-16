import it.sauronsoftware.cron4j.SchedulingPattern;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CheckURL {
	public static void main(String args[]) throws ClientProtocolException,
			IOException {
		if (args.length == 0) {
			System.out.println("Please input the path of config file");
			System.exit(1);
		}
		String configStr = FileUtils.readFileToString(new File(args[0]));
		// System.out.println(configStr);

		Config conf = JSON.parseObject(configStr, Config.class);

		// URLObj a = new URLObj();
		// a.setId("1");
		// a.setUrl("http://app101.dev.la1.vcinv.net:8080/kibana/#/dashboard/elasticsearch/Dashboard");
		//
		// URLObj a1 = new URLObj();
		// a1.setId("2");
		// a1.setUrl("http://app101.dev.la1.vcinv.net:8080/kibana/#/dashboard/elasticsearch/Dashboard_traffic");
		//
		// List<URLObj> lists = new ArrayList<URLObj>();
		// lists.add(a);
		// lists.add(a1);
		//
		// Config conf = new Config();
		// conf.setUrls(lists);
		// conf.setPhantomJSPath("C:\\phantomjs\\phantomjs.exe");
		//
		// System.out.println(JSON.toJSONString(conf, true));

		// JSONObject jobj = JSON.p

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(
				"http://121.40.187.96/weixinsign/taskList?key="
						+ conf.getRedisKey());
		CloseableHttpResponse response = httpclient.execute(httpGet);

		try {

			HttpEntity entity2 = response.getEntity();
			String body = EntityUtils.toString(entity2);
			// System.out.println(body);
			JSONArray array = JSON.parseArray(body);
			for (Object o : array) {
				try {
					String xmlStr = o.toString();
					Document doc = Jsoup.parse(xmlStr);
					System.out.println(new Date().toLocaleString() + "\n"
							+ xmlStr);
					String fromUser = doc.select("FromUserName").text();
					String type = doc.select("MsgType").text();
					String urlID = "";
					if (type.trim().toLowerCase().equals("text")) {
						urlID = doc.select("Content").text();
					} else if (type.trim().toLowerCase().equals("event")) {
						urlID = doc.select("EventKey").text();
					}

					if (urlID.trim().equals("1") || urlID.trim().equals("2")
							|| urlID.trim().equals("3")
							|| urlID.trim().equals("0")) {

						
							if (fromUser != null && !"".equals(fromUser.trim())) {
								SendMessage.SendPic(fromUser, conf, urlID);
							}

						
					}
					else if (urlID.trim().equals("4"))
					{
						System.out.println("send any image:" + conf.getAppPath()+File.separator+"main.jpg");
						SendMessage.SendAnyImage(fromUser, conf.getAppPath()+File.separator+"main.jpg");
					}
						

				} catch (Exception e) {

				}
			}
			EntityUtils.consume(entity2);
		} finally {
			response.close();
		}

		System.out.println("Chech scheduled");
		// check schedule
		for (URLObj u : conf.getUrls()) {
			for (Scheduled sch : u.getSchedule()) {
				SchedulingPattern sp = new SchedulingPattern(sch.getTime());
				if (sp.match(System.currentTimeMillis())) {
					System.out.println("Match:" + sch.getTime()
							+ " System time: " + new Date().toLocaleString());
					try {
						SendMessage.SendPicByTag(sch.getToTag(), u.getUrl(),
								conf);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}
}
