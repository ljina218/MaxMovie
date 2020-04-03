package hjho;

import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class AdminClientThread extends Thread {
	AdminClient ac = null;
	
	public AdminClientThread(AdminClient ac) {
		this.ac = ac;
	}

	public void run() {
		String msg = ""; //100#apple
		boolean isStop = false;
		while(!isStop) {
			try {
				msg = (String)ac.ois.readObject();
				//JOptionPane.showMessageDialog(ac, msg);
				StringTokenizer st = null;
//				LOGIN_100 SEAT_200 SEL_300 INS_400 UPD_500 DEL_600;
				int protocol = 0; 
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
					//로그인 
					case AdminProtocol._LOGIN: {
						//혹시 몰라서 일단 만들어 둠.
				 	} break;
				 	
				 	//상영시간표 가져오기 
				 	case AdminProtocol._REFRESH: {
				 		Vector<String> v = new Vector<String>();
						v.add(st.nextToken()); //상영관 명
						v.add(st.nextToken()); //영화제목
						v.add(st.nextToken()); //날짜 
						v.add(st.nextToken()); //시간
						ac.dtm_movie.addRow(v);
				 	} break;
				 	
				 	//당일에 상영하는 상영시간표 가져오기
				 	case AdminProtocol._SEL: {
				 		Vector<String> v = new Vector<String>();
						v.add(st.nextToken()); //상영관 명
						v.add(st.nextToken()); //영화제목
						v.add(st.nextToken()); //날짜 
						v.add(st.nextToken()); //시간
						ac.dtm_movie.addRow(v);
				 	} break;
				 	
				 	//로그인 성공시 서버에 전송하여 상영중인 영화, 지점의 상영관을 불러오는프로토콜
				 	case AdminProtocol._DIAL: {
				 		ac.first.add(st.nextToken()); //영화제목
				 		ac.second.add(st.nextToken()); //상영관
				 	} break;
				 	
				 	//다이얼로그창에서 반영하기를 누를 때 오라클에 전송하는 프로토콜
				 	case AdminProtocol._INS: {
				 		String r_msg = st.nextToken();
				 		JOptionPane.showMessageDialog(ac.ad
								, r_msg
							    );
				 	} break;
				 	case AdminProtocol._DEL: {
				 		String r_msg = st.nextToken();
				 		JOptionPane.showMessageDialog(ac.ad
								, r_msg
							    );
				 	} break;
				}
			} catch (SocketException se) {
				isStop = true;
				se.printStackTrace();
			} catch (Exception e) {
				System.out.print("[TalkClientThread]-run():");
				e.printStackTrace();
			}//end of try
		}//end of while
	}//end of run
}
