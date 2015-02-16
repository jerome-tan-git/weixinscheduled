import java.util.ArrayList;
import java.util.List;


public class Config {
	private List<URLObj> urls= new ArrayList<URLObj>();
	private String phantomJSPath = "";
	private String redisKey = "";
	private String appPath="";
	
	public String getAppPath() {
		return appPath;
	}
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	public String getRedisKey() {
		return redisKey;
	}
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	public List<URLObj> getUrls() {
		return urls;
	}
	public void setUrls(List<URLObj> urls) {
		this.urls = urls;
	}
	public String getPhantomJSPath() {
		return phantomJSPath;
	}
	public void setPhantomJSPath(String phantomJSPath) {
		this.phantomJSPath = phantomJSPath;
	}
	
}
