package hjho;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminClient extends JFrame implements ActionListener{
	//처음 인스턴스 될때 정보 받아오기 
	AdminLoginView alv = null;
	AdminShowtimeVO astVO = null;
//	String name = null;
//	String id = null;
//	String theaterloc = null;
//	String theatername= null;
	//소켓 통신을 위한 전역변수 
	Socket   		   socket 	= null;
	ObjectOutputStream oos 		= null; //서버에게 말하기
	ObjectInputStream  ois 		= null; //서버가 말한걸 듣기
	//화면 그리기 위한 변수
	JPanel jp_north = new JPanel();
	JPanel jp_center = new JPanel();
	
	String imgPath = "src\\hjho\\";
	JButton jbtn_all = new JButton("상영시간표 조회");
	JButton jbtn_sel = new JButton(new ImageIcon(imgPath+"detail.gif"));//,
	JButton jbtn_ins = new JButton(new ImageIcon(imgPath+"new.gif"));
	JButton jbtn_del = new JButton(new ImageIcon(imgPath+"delete.gif"));
	JButton jbtn_exit = new JButton("나가기");
	String cols[] = {"상영관","영화","날짜","시간"};
	String data[][] = new String[0][4];
	DefaultTableModel dtm_movie = new DefaultTableModel(data, cols);
	JTable jtb_movie = new JTable(dtm_movie); 
	JScrollPane jsp_movie  = new JScrollPane(jtb_movie);

	AdminDialog ad = null;
	List<String> first = new Vector<String>(); 
	List<String> second = new Vector<String>(); 
/******************************************************************************************************
 * 생성자 : 로그인하고 받은 이름 지역 지점 초기화, 화면 initDisplay()호출 및 서버접속을 위한 함수 init()호출
 * @param alv
 * @param id 
 * @param nickName 
 */
	public AdminClient(AdminLoginView alv, String nickName, String id) {
		this.alv = alv;
		astVO = new AdminShowtimeVO();
		astVO.setId(id);
		astVO.setName(nickName);
		astVO.setTheatername(alv.aDao.admin_t_name);
		astVO.setTheaterloc(alv.aDao.admin_t_loc);
		
		//JOptionPane.showMessageDialog(this, name);
		initDisplay();
		jbtn_sel.addActionListener(this);
		jbtn_ins.addActionListener(this);
		jbtn_del.addActionListener(this);
		jbtn_all.addActionListener(this);
		jbtn_exit.addActionListener(this);
		connect_process();
	}
/*******************************************************************************************************
 * initDisplay() : 화면 그리는 함수
 */
	public void initDisplay() {
		this.setLayout(new BorderLayout());
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		jbtn_sel.setToolTipText("당일 상영시간표 조회");
		jbtn_ins.setToolTipText("상영시간표 추가");
		jbtn_del.setToolTipText("상영시간표 삭제");
		
		jp_north.add(jbtn_all);
		jp_north.add(jbtn_sel);
		jp_north.add(jbtn_ins);
		jp_north.add(jbtn_del);
		jp_north.add(jbtn_exit);
		
		this.add("North", jp_north);
		this.add("Center", jsp_movie);
		this.setTitle(astVO.getTheaterloc()+" "+astVO.getTheatername()+" 상영시간표 정보");
		this.setSize(800, 500);
		this.setVisible(true);
	}
/*******************************************************************************************************
 * init() : 서버의 통신을 위한 함수 
 */
	public void connect_process() {
		try {
			//서버측의 ip주소 작성하기
			socket = new Socket("192.168.0.37",5100);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			//로그인 시 현재 사영되고있는 영화정보와
			//지점의 상영관 정보를 받아온다.
			oos.writeObject(AdminProtocol._DIAL+"#"+astVO.getId());
			
			AdminClientThread act = new AdminClientThread(this);
			act.start();//SocketException
		} catch (ConnectException ce) {
			JOptionPane.showMessageDialog(this
					, "서버와의 연결이 끊어졌습니다."
					, "에러"
					, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			ce.printStackTrace();
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}//end of connect_process

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == jbtn_all) {
			JOptionPane.showMessageDialog(this, "전체 상영시간표 조회");
			try {
				while(dtm_movie.getRowCount()>0) {
					dtm_movie.removeRow(0);
				}
				oos.writeObject(AdminProtocol._REFRESH
						      +"#"+astVO.getId());
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//상영시간표 조회
		//당일에 상영하는 상영시간표를 가져온다.
		else if(obj == jbtn_sel) {
			JOptionPane.showMessageDialog(this, "당일 상영시간표 조회");
			try {
				while(dtm_movie.getRowCount()>0) {
					dtm_movie.removeRow(0);
				}
				oos.writeObject(AdminProtocol._SEL
						      +"#"+astVO.getId()
						      +"#"+setYMD()
						      );
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//상영시간표 추가
		//다이얼로그 창이 뜬다.
		else if(obj == jbtn_ins) {
			JOptionPane.showMessageDialog(this, "상영시간표 추가");
			try {
				if(ad==null) {
					ad = new AdminDialog(this);
				}
				ad.initDisplay();
			} catch (Exception ec) {
				ec.printStackTrace();
			}
			
		}
		//상영시간표 삭제
		else if(obj == jbtn_del) {
			JOptionPane.showMessageDialog(this, "상영시간표 삭제");
			int indexs[] = jtb_movie.getSelectedRows();
			if(indexs.length==0) {
				JOptionPane.showMessageDialog(this, "삭제할 로우를 선택하세요.");
				return;
			} 
			else {
				
				AdminShowtimeVO del_astVO = null;
				for (int i=0; i<dtm_movie.getRowCount(); i++) {
					if(jtb_movie.isRowSelected(i)) { //i가 선택이 된거야?
						del_astVO = new AdminShowtimeVO();
						del_astVO.setScrName(dtm_movie.getValueAt(i,0).toString()); 
						del_astVO.setMovieTitle(dtm_movie.getValueAt(i,1).toString()); 
						del_astVO.setDate(dtm_movie.getValueAt(i,2).toString()); 
						del_astVO.setTime(dtm_movie.getValueAt(i,3).toString()); 
						break;
					}
				}
				del_astVO.setYy(del_astVO.getDate().substring(0,4));
				del_astVO.setMm(del_astVO.getDate().substring(4,6));
				del_astVO.setDd(del_astVO.getDate().substring(6));
				del_astVO.setHh24(del_astVO.getTime().substring(0,2));
				del_astVO.setMi(del_astVO.getTime().substring(3));
				del_astVO.setId(this.astVO.getId());
				try {
					oos.writeObject(AdminProtocol._DEL
								+"#"+del_astVO.getId()
								+"#"+del_astVO.getMovieTitle()
								+"#"+del_astVO.getScrName()
								+"#"+del_astVO.getYy()
								+"#"+del_astVO.getMm()
								+"#"+del_astVO.getDd()
								+"#"+del_astVO.getHh24()
								+"#"+del_astVO.getMi()
							   );
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			}
		}
		else if(obj == jbtn_exit) {
			int confirm = JOptionPane.showConfirmDialog(
					this, "나가시겠습니까?"
					, "나가기", JOptionPane.YES_NO_OPTION
					, JOptionPane.INFORMATION_MESSAGE);
			if(confirm==0) {
				try {
					oos.writeObject(AdminProtocol._EXIT
							+"#"+astVO.getId());
					//+"#"+loginForm.nickName+"님이 퇴장하였습니다.");
					//자바가상머신과 연결 고리를 끊는다.
					if(ois != null) ois.close();
					if(oos != null) oos.close();
					if(socket != null) socket.close();
					System.exit(0);
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			}
		}
		
	}
/********************************************************************
 * 시간비교를 위해 만든 함수들 
 * @return
 */
	public String setYMD() {
		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return yyyy
			+(mm < 10 ? "0"+mm:""+mm)
			+(day < 10 ? "0"+day:""+day);
	}//end of setTimer
	
	public String setHMS() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		return (hour < 10 ? "0"+hour:""+hour)
			   +(min < 10 ? "0"+min:""+min);
	}//end of setTimer
}
