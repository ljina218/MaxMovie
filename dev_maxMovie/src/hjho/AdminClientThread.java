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
					//로그인 --> 상영시간표 조회
					case 100: {
						Vector<String> v = new Vector<String>();
						v.add(st.nextToken()); //상영관 명
						v.add(st.nextToken()); //영화제목
						v.add(st.nextToken()); //날짜 
						v.add(st.nextToken()); //시간
						ac.dtm_movie.addRow(v);
				 	} break;
				 	//상영시간표 가져오기 
				 	case Admin_Protocol._REFRESH: {
				 		Vector<String> v = new Vector<String>();
						v.add(st.nextToken()); //상영관 명
						v.add(st.nextToken()); //영화제목
						v.add(st.nextToken()); //날짜 
						v.add(st.nextToken()); //시간
						ac.dtm_movie.addRow(v);
				 	} break;
				 	case Admin_Protocol._INS: {
				 		ac.first.add(st.nextToken()); //영화제목
				 		ac.second.add(st.nextToken()); //상영관
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
