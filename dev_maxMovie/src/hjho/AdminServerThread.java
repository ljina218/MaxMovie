package hjho;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AdminServerThread  extends Thread {
	public AdminServer as = null;
	Socket admin = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	String adminID = null;//현재서버에 입장한 클라이언트 스레드 ID 저장
	List<AdminMovieVO> movieList = null;
	Admin_Dao aDao = new Admin_Dao();
	
	/***************************************************************
	 * 생성자 : admin이 들어오면 그 admin에 상영시간표를 보내주고 
	 * 		List에 추가한다.
	 * @param as
	 */
	public AdminServerThread(AdminServer as) {
		this.as = as;
		this.admin = as.socket;
		
		try {
			oos = new ObjectOutputStream(admin.getOutputStream());
			ois = new ObjectInputStream(admin.getInputStream());
			String msg = (String)ois.readObject();
			String time = as.setTimer()+" "+as.setHMS();
			as.jta_log.append("*LOG*"+time+" : "+msg+"\n");
			StringTokenizer st = new StringTokenizer(msg,"#");
			st.nextToken(); // 프로토콜
			adminID = st.nextToken();
			movieList = aDao.refreshData(adminID);
			for(AdminMovieVO mVO: movieList) {
				this.send(100 
						 +"#"+mVO.getScr_name()
						 +"#"+mVO.getMovie_title()
						 +"#"+mVO.getShow_date()
						 +"#"+mVO.getShow_time());
			}
			as.adminList.add(this);
			int inwon = as.adminList.size();
			as.jta_log.append("    - 현재 인원 수  : "+inwon+" 명 \n\n");
			as.jta_log.setCaretPosition(as.jta_log.getDocument().getLength());
		} catch (Exception e) {
			System.out.print("[AdminServerThread]-생성자:");
			e.printStackTrace();
		}
	}
	//현재입장해 있는 친구들 모두에게 메시지 전송하기 구현
	public void broadCasting(String msg) {
		for (AdminServerThread ast : as.adminList) {
			ast.send(msg);
		}
	}
	//클라이언트에게 말하기 구현
	public void send(String msg) {
		try {
			oos.writeObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public void run() {
		boolean isStop = false;
		String msg = null;
		try {
			//while(true) { //무한루프에 빠질 수 있다.
			run_start:
			while(!isStop) {
				msg = (String)ois.readObject();
				as.jta_log.append("*LOG*"+as.setHMS()+" : "+msg+"\n");
				StringTokenizer st = null;
				// 100:LOGIN|200:n-n|201:1-1|202:닉네임|500:LOGOUT;
				int protocol = 0; 
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
					case Admin_Protocol._REFRESH: {
						adminID = st.nextToken();
						movieList = aDao.refreshData(adminID);
						for(AdminMovieVO mVO: movieList) {
							this.send(Admin_Protocol._REFRESH
									 +"#"+mVO.getScr_name()
									 +"#"+mVO.getMovie_title()
									 +"#"+mVO.getShow_date()
									 +"#"+mVO.getShow_time());
						}
					} break;
					//클라이언트에서 상영시간표 추가하기를 눌렀니?
					case Admin_Protocol._INS: {
						adminID = st.nextToken();
						List<Map<String, Object>> rList = null;
						rList = aDao.ins(adminID);
						for(Map<String, Object> map: rList) {
							this.send(Admin_Protocol._INS 
									 +"#"+map.get("First")
									 +"#"+map.get("Second"));
						}
					} break;
					//종료
					case Admin_Protocol._EXIT: {
						String nickName = st.nextToken();
						//String message = st.nextToken();
						as.jta_log.append("   -INFO : "+as.socket+"\n");
						as.adminList.remove(this);
						as.jta_log.append("    - 현재 인원 수  : "+as.adminList.size()+" 명 \n\n");
					} break run_start;
				}///end of switch
				as.jta_log.setCaretPosition(as.jta_log.getDocument().getLength());
			}			
		} catch (Exception e) {
			System.out.print("[TalkClientThread]-run():");
			e.printStackTrace();
		}
	}

}
