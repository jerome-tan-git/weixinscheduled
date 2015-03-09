import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class SendMessage {
	public static String sendPost(String url, String params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(params);
			// flush������Ļ���
			out.flush();

			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("Post errot" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void SendAnyURL(String _toUser, Config _config, String _URL)
			throws ClientProtocolException, IOException, JSONException {
		System.setProperty("phantomjs.binary.path", _config.getPhantomJSPath());
		WebDriver driver = new PhantomJSDriver();
		String name1 = UUID.randomUUID().toString();
		String name2 = UUID.randomUUID().toString();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(800, 1000));
		driver.get(_URL);
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.io.File screenShotFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenShotFile, new java.io.File("./" + name1
				+ ".png"));

		BufferedImage bufferedImage = ImageIO.read(new File("./" + name1
				+ ".png"));

		// create a blank, RGB, same width and height, and a white
		// background
		BufferedImage newBufferedImage = new BufferedImage(
				bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
				Color.WHITE, null);

		// write to jpeg file
		ImageIO.write(newBufferedImage, "jpg", new File("./" + name2 + ".jpg"));
		// wx58af015bc449fdad
		// Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn
		// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet hg = new HttpGet(
				"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		// HttpGet hg = new HttpGet(
		// "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		HttpResponse response = httpclient.execute(hg);
		HttpEntity resEntity = response.getEntity();
		try {
			if (resEntity != null) {
				String page = EntityUtils.toString(resEntity);
				JSONObject jsonObject = new JSONObject(page);
				String token = "";
				token = (String) jsonObject.get("access_token");
				if (!token.trim().equals("")) {
					System.out.println("token: " + token);

					HttpPost httppost = new HttpPost(
							"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
									+ token + "&type=image");
					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					FileBody bin = new FileBody(new File("./" + name2 + ".jpg"));
					reqEntity.addPart("media", bin);

					httppost.setEntity(reqEntity);

					System.out.println("executing request "
							+ httppost.getRequestLine());
					HttpResponse response1 = httpclient.execute(httppost);
					HttpEntity resEntity1 = response1.getEntity();
					String mediaID = "";
					if (resEntity != null) {
						String page1 = EntityUtils.toString(resEntity1);
						JSONObject jsonObject1 = new JSONObject(page1);
						mediaID = (String) jsonObject1.get("media_id");
						String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
								+ token;
						String params = "{\"touser\":\""
								+ _toUser
								+ "\",\"msgtype\":\"image\",\"agentid\":\"2\",\"image\": {\"media_id\": \""
								+ mediaID + "\"},}";
						String str = SendMessage.sendPost(url, params);
						System.out.println(str);

					}

				}
			}
			File a = new File("./" + name1 + ".png");
			a.delete();
			a = new File("./" + name2 + ".jpg");
			a.delete();
		} catch (Exception e) {

		} finally {
			driver.quit();
		}

	}

	public static void SendPic(String _toUser, Config _config, String _urlID)
			throws IOException, InterruptedException, JSONException {
		Boolean validID = false;
		for (URLObj a : _config.getUrls()) {
			if (a.getId().trim().equals(_urlID)) {
				validID = true;
			}
		}

		if (_urlID.trim().equals("0")) {
			validID = true;
		}
		if (!validID) {
			System.out.println("wrong ID: " + _urlID);
			return;
		}
		System.setProperty("phantomjs.binary.path", _config.getPhantomJSPath());
		WebDriver driver = new PhantomJSDriver();
		String name1 = UUID.randomUUID().toString();
		String name2 = UUID.randomUUID().toString();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(800, 1000));

		for (URLObj pageUrl : _config.getUrls()) {
			if (!pageUrl.getId().trim().equals(_urlID)
					&& !_urlID.trim().equals("0")) {
				continue;
			}
			driver.get(pageUrl.getUrl());
			Thread.sleep(30000);
			java.io.File screenShotFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenShotFile, new java.io.File("./" + name1
					+ ".png"));

			BufferedImage bufferedImage = ImageIO.read(new File("./" + name1
					+ ".png"));

			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
					Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File("./" + name2
					+ ".jpg"));
			// wx58af015bc449fdad
			// Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn
			// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet hg = new HttpGet(
					"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
			// HttpGet hg = new HttpGet(
			// "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
			HttpResponse response = httpclient.execute(hg);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				String page = EntityUtils.toString(resEntity);
				JSONObject jsonObject = new JSONObject(page);
				String token = "";
				token = (String) jsonObject.get("access_token");
				if (!token.trim().equals("")) {
					System.out.println("token: " + token);

					HttpPost httppost = new HttpPost(
							"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
									+ token + "&type=image");
					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					FileBody bin = new FileBody(new File("./" + name2 + ".jpg"));
					reqEntity.addPart("media", bin);

					httppost.setEntity(reqEntity);

					System.out.println("executing request "
							+ httppost.getRequestLine());
					HttpResponse response1 = httpclient.execute(httppost);
					HttpEntity resEntity1 = response1.getEntity();
					String mediaID = "";
					if (resEntity != null) {
						String page1 = EntityUtils.toString(resEntity1);
						JSONObject jsonObject1 = new JSONObject(page1);
						mediaID = (String) jsonObject1.get("media_id");
						String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
								+ token;
						String params = "{\"touser\":\""
								+ _toUser
								+ "\",\"msgtype\":\"image\",\"agentid\":\"2\",\"image\": {\"media_id\": \""
								+ mediaID + "\"},}";
						String str = SendMessage.sendPost(url, params);
						System.out.println(str);

					}

				}
			}
		}
		File a = new File("./" + name1 + ".png");
		a.delete();
		a = new File("./" + name2 + ".jpg");
		a.delete();
		driver.quit();
	}

	public static void SendAnyImage(String _toUser, String _imagePath)
			throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet hg = new HttpGet(
				"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		// HttpGet hg = new HttpGet(
		// "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		HttpResponse response = httpclient.execute(hg);
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			String page = EntityUtils.toString(resEntity);
			JSONObject jsonObject = new JSONObject(page);
			String token = "";
			token = (String) jsonObject.get("access_token");
			if (!token.trim().equals("")) {
				System.out.println("token: " + token);

				HttpPost httppost = new HttpPost(
						"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
								+ token + "&type=image");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				FileBody bin = new FileBody(new File(_imagePath));
				reqEntity.addPart("media", bin);

				httppost.setEntity(reqEntity);

				System.out.println("executing request "
						+ httppost.getRequestLine());
				HttpResponse response1 = httpclient.execute(httppost);
				HttpEntity resEntity1 = response1.getEntity();
				String mediaID = "";
				if (resEntity != null) {
					String page1 = EntityUtils.toString(resEntity1);
					JSONObject jsonObject1 = new JSONObject(page1);
					mediaID = (String) jsonObject1.get("media_id");
					String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
							+ token;
					String params = "{\"touser\":\""
							+ _toUser
							+ "\",\"msgtype\":\"image\",\"agentid\":\"2\",\"image\": {\"media_id\": \""
							+ mediaID + "\"},}";
					String str = SendMessage.sendPost(url, params);
					System.out.println(str);

				}

			}
		}
	}

	public static void SendPicByTag(String _toTag, String _url, Config _config)
			throws IOException, InterruptedException, JSONException {

		System.setProperty("phantomjs.binary.path", _config.getPhantomJSPath());
		WebDriver driver = new PhantomJSDriver();
		String name1 = UUID.randomUUID().toString();
		String name2 = UUID.randomUUID().toString();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(800, 1000));
		driver.get(_url);
		Thread.sleep(30000);
		java.io.File screenShotFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenShotFile, new java.io.File("./" + name1
				+ ".png"));

		BufferedImage bufferedImage = ImageIO.read(new File("./" + name1
				+ ".png"));

		// create a blank, RGB, same width and height, and a white
		// background
		BufferedImage newBufferedImage = new BufferedImage(
				bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
				Color.WHITE, null);

		// write to jpeg file
		ImageIO.write(newBufferedImage, "jpg", new File("./" + name2 + ".jpg"));
		// wx58af015bc449fdad
		// Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn
		// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet hg = new HttpGet(
				"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		// HttpGet hg = new HttpGet(
		// "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx58af015bc449fdad&corpsecret=Q9N_PbMW9yW06YXXg1izxTRszztrsYxiCBALqEmIUewkLzr97oa9IsWLAulplxDn");
		HttpResponse response = httpclient.execute(hg);
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			String page = EntityUtils.toString(resEntity);
			JSONObject jsonObject = new JSONObject(page);
			String token = "";
			token = (String) jsonObject.get("access_token");
			if (!token.trim().equals("")) {
				System.out.println("token: " + token);

				HttpPost httppost = new HttpPost(
						"https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
								+ token + "&type=image");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				FileBody bin = new FileBody(new File("./" + name2 + ".jpg"));
				reqEntity.addPart("media", bin);

				httppost.setEntity(reqEntity);

				System.out.println("executing request "
						+ httppost.getRequestLine());
				HttpResponse response1 = httpclient.execute(httppost);
				HttpEntity resEntity1 = response1.getEntity();
				String mediaID = "";
				if (resEntity != null) {
					String page1 = EntityUtils.toString(resEntity1);
					JSONObject jsonObject1 = new JSONObject(page1);
					mediaID = (String) jsonObject1.get("media_id");
					String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
							+ token;
					String params = "{\"totag\":\""
							+ _toTag
							+ "\",\"msgtype\":\"image\",\"agentid\":\"2\",\"image\": {\"media_id\": \""
							+ mediaID + "\"},}";
					String str = SendMessage.sendPost(url, params);
					System.out.println(str);

				}

			}
		}

		File a = new File("./" + name1 + ".png");
		a.delete();
		a = new File("./" + name2 + ".jpg");
		a.delete();
		driver.quit();
	}

}
