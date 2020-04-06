package hjho;

public class AdminShowtimeVO {
	//현재 접속한 상영관의 정보를 담기 위한 VO
	private String id = null;
	private String name = null;
	private String theaterloc = null;
	private String theatername = null;
	
	//상영 시간표를 넘기기위한 VO
	private String msg = null;
	private String movieTitle = null;
	private String scrName = null;
	private String date = null;
	private String yy = null;
	private String mm = null;
	private String dd = null;
	private String time = null;
	private String hh24 = null;
	private String mi = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTheaterloc() {
		return theaterloc;
	}
	public void setTheaterloc(String theaterloc) {
		this.theaterloc = theaterloc;
	}
	public String getTheatername() {
		return theatername;
	}
	public void setTheatername(String theatername) {
		this.theatername = theatername;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public String getScrName() {
		return scrName;
	}
	public void setScrName(String scrName) {
		this.scrName = scrName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getYy() {
		return yy;
	}
	public void setYy(String yy) {
		this.yy = yy;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getDd() {
		return dd;
	}
	public void setDd(String dd) {
		this.dd = dd;
	}
	public String getHh24() {
		return hh24;
	}
	public void setHh24(String hh24) {
		this.hh24 = hh24;
	}
	public String getMi() {
		return mi;
	}
	public void setMi(String mi) {
		this.mi = mi;
	}
	
}
