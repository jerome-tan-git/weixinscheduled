import java.util.ArrayList;
import java.util.List;


public class URLObj {
	private String url = "";
	private String id = "";
	private List<Scheduled> schedule = new ArrayList<Scheduled>();
	public List<Scheduled> getSchedule() {
		return schedule;
	}
	public void setSchedule(List<Scheduled> schedule) {
		this.schedule = schedule;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
