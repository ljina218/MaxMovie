package hjho;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
	String name = null;
	String id = null;
	String theaterloc = null;
	String theatername= null;
	//소켓 통신을 위한 전역변수 
	Socket   		   socket 	= null;
	ObjectOutputStream oos 		= null; //서버에게 말하기
	ObjectInputStream  ois 		= null; //서버가 말한걸 듣기
	//화면 그리기 위한 변수
	JPanel jp_north = new JPanel();
	JPanel jp_center = new JPanel();
	
	String imgPath = "src\\hjho\\";
	JButton jbtn_all = new JButton("새로고침");
	JButton jbtn_sel = new JButton(new ImageIcon(imgPath+"detail.gif"));//,
	JButton jbtn_ins = new JButton(new ImageIcon(imgPath+"new.gif"));
	JButton jbtn_del = new JButton(new ImageIcon(imgPath+"delete.gif"));
	JButton jbtn_exit = new JButton("나가기");
	String cols[] = {"상영관","영화","날짜","시간"};
	String data[][] = new String[0][4];
	DefaultTableModel dtm_movie = new DefaultTableModel(data, cols);
	JTable jtb_movie = new JTable(dtm_movie); 
	JScrollPane jsp_movie  = new JScrollPane(jtb_movie);
/******************************************************************************************************
 * 생성자 : 로그인하고 받은 이름 지역 지점 초기화, 화면 initDisplay()호출 및 서버접속을 위한 함수 init()호출
 * @param alv
 */
	public AdminClient(AdminLoginView alv) {
		this.alv = alv;
		this.name = alv.aDao.admin_name;
		this.id = alv.id;
		this.theatername = alv.aDao.admin_t_name;
		this.theaterloc = alv.aDao.admin_t_loc;
		//JOptionPane.showMessageDialog(this, name);
		initDisplay();
		jbtn_sel.addActionListener(this);
		jbtn_ins.addActionListener(this);
		jbtn_del.addActionListener(this);
		jbtn_all.addActionListener(this);
		jbtn_exit.addActionListener(this);
		init();
	}
/*******************************************************************************************************
 * initDisplay() : 화면 그리는 함수
 */
	public void initDisplay() {
		this.setLayout(new BorderLayout());
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		jbtn_sel.setToolTipText("상영시간표 조회");
		jbtn_ins.setToolTipText("상영시간표 추가");
		jbtn_del.setToolTipText("상영시간표 삭제");
		
		jp_north.add(jbtn_all);
		jp_north.add(jbtn_sel);
		jp_north.add(jbtn_ins);
		jp_north.add(jbtn_del);
		jp_north.add(jbtn_exit);
		
		this.add("North", jp_north);
		this.add("Center", jsp_movie);
		this.setTitle(theaterloc+" "+theatername+" 상영시간표 정보");
		this.setSize(800, 500);
		this.setVisible(true);
	}
/*******************************************************************************************************
 * init() : 서버의 통신을 위한 함수 
 */
	public void init() {
		try {
			//서버측의 ip주소 작성하기
			socket = new Socket("192.168.0.10",5001);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			//initDisplay에서 닉네임이 결정된 후 init메소드가 호출되므로
			//내가 입장한 사실을 알린다.(말하기)
			oos.writeObject(100+"#"+id);
			//서버에 말을 한 후 들을 준비를 해야하니까
			AdminClientThread act = new AdminClientThread(this);
			act.start();//SocketException
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception e) {
			//예외가 발생했을때 직접적인 원인Client]-init():");
			e.printStackTrace();
		}
	}//end of init

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == jbtn_all) {
			JOptionPane.showMessageDialog(this, "ALL호출");
			try {
				oos.writeObject(Admin_Protocol._DATA
						       +"#"+name
						       +"#");
				//리프레쉬 데이타
				
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//상영시간표 조회
		else if(obj == jbtn_sel) {
			JOptionPane.showMessageDialog(this, "SEL호출");
			try {
				while(dtm_movie.getRowCount()>0) {
					dtm_movie.removeRow(0);
				}
				oos.writeObject(300+"#"+id);
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//상영시간표 추가
		else if(obj == jbtn_ins) {
			JOptionPane.showMessageDialog(this, "INS호출");
			try {
				oos.writeObject(400);
				
				
			} catch (Exception ec) {
				ec.printStackTrace();
			}
			
		}
		//상영시간표 삭제
		else if(obj == jbtn_del) {
			JOptionPane.showMessageDialog(this, "DEL호출");
			try {
				oos.writeObject(500);
				
				
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		else if(obj == jbtn_exit) {
			JOptionPane.showMessageDialog(this, "EXIT호출");
			try {
				oos.writeObject(600+"#"+id);
						//+"#"+loginForm.nickName+"님이 퇴장하였습니다.");
			//자바가상머신과 연결 고리를 끊는다.
			System.exit(0);
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		
	}
}
