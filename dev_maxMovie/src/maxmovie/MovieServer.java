package maxmovie;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MovieServer extends JFrame implements Runnable{
	
	//서버연결 선언부
	List<MovieServerThread> globalist = null;
	boolean stop = false;
	ServerSocket server = null;
	Socket socket = null;
	
	//오늘의 영화정보 list 선언부
	/*
	 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
		showMap.put("T_LOC", rs.getString("T_LOC"));
		showMap.put("T_NAME", rs.getString("T_NAME"));
		showMap.put("S_DATE", rs.getString("S_DATE"));
		showMap.put("S_TIME", rs.getString("S_TIME"));
		showMap.put("SC_NAME", rs.getString("SC_NAME"));
	 */
	List<Map<String, Object>> movieList = null;
	
	//MovieDao 생성부
	MovieDao md = new MovieDao();
	
	//서버화면 선언부
	JTextArea jta_log = new JTextArea();
	JScrollPane jsc_log = new JScrollPane(jta_log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
													,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	String [] cols = {"아이디","접속 상태", "기록","접속시간"};
	String [][] data = new String [0][4];
	DefaultTableModel dtm_server = new DefaultTableModel(data, cols);
	JTable jt_server = new JTable(dtm_server);
	JScrollPane jsp_server = new JScrollPane(jt_server);
	JTableHeader jth_server = new JTableHeader();
	
	//생성자
	public MovieServer () {//오늘날짜 보내서 영화3일치 가져오기
		//this.movieList = md.refreshMovieAll(setTimer(null,"날짜"));
	}
	
	//메인메소드
	public static void main(String[] args) {
		MovieServer ms = new MovieServer();
		ms.display();
		Thread th = new Thread();
		th.start();
	}
	
	//소켓정보를 받아드리고, 스레스를 형성하는 메소드
	@Override
	public void run() {
		globalist = new Vector<MovieServerThread>();
		try {
			server = new ServerSocket(5000);
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
		jta_log.append("서버 연결 성공.....\n");
		while(!stop) {
			try {
				socket = server.accept();
				jta_log.append("새로운 ip: "+socket.getInetAddress().getHostAddress()+"\n");
				MovieServerThread mst = new MovieServerThread(this);
				mst.start();
			} catch (Exception e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			}
		}
	}
	
	//셋 타이머 메소드
	public String setTimer(Calendar cal, String format) {
		if(cal==null) {
			cal = Calendar.getInstance();
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		if("시간".equals(format)) {//00:00:00
			return (hour<10? "0"+hour: ""+hour)+":"+(min<10? "0"+min: ""+min)+":"+(sec<10? "0"+sec: ""+sec);
		}
		else if("날짜".equals(format)) {//20200324
			return year+""+(month<10? "0"+month: ""+month)+""+(day<10? "0"+day: ""+day);
		}else {//20200324 00:00:00
			return year+""+(month<10? "0"+month: ""+month)+""+(day<10? "0"+day: ""+day)+"  "+
					(hour<10? "0"+hour: ""+hour)+":"+(min<10? "0"+min: ""+min)+":"+(sec<10? "0"+sec: ""+sec);
		}
	}
	
	//서버화면 메소드
	public void display() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimension.width-400, dimension.height-200);
		System.out.println((dimension.width-400)+", "+(dimension.height-200));
		setVisible(true);
		setTitle("서버창");
		//로그정보
		add("West",jsc_log);
		jsc_log.setPreferredSize(new Dimension(500,880));
		//테이블
		add("Center",jsp_server);
		jth_server = jt_server.getTableHeader();
		jth_server.getColumnModel().getColumn(2).setPreferredWidth(300);
	}
	
	
}
