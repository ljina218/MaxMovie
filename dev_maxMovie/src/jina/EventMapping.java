package jina;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


import com.sun.mail.iap.Protocol;

public class EventMapping implements ActionListener, MouseListener{

	
	/* 뷰 패널들의 변수이름
	//로그인뷰
	JPanel 					jp_lv 			= new LoginView(this);
	//마이페이지뷰
	JPanel 					jp_mv 			= new MyPageView(this);
	//무비초이스뷰
	JPanel 					jp_mcv 			= new MovieChoiceView(this);
	//시트초이스뷰
	JPanel 					jp_scv 			= new SeatChoiceView(this);
	//리절트뷰
	JPanel 					jp_rv 			= new ResultView(this);
	 */
	
	/* 마이페이지 뷰의 변수 이름
	 * MemInfoView miv = new MemInfoView();
		MemUpdateView muv = new MemUpdateView();
		TicketHistoryView thv = new TicketHistoryView();
	 */
	
	//회원 아이디 및 닉네임
	//String myid = null;
	//String mynickname = null;
	
	//필요한 주소값 선언부
	MaxMovieView mmv = null;
	
	//생성자
	public EventMapping(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	
	//서버스레드에게 말하기 위한 메소드 
	private void send(String msg) {
		try {
			mmv.oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	
	/**************************************************************************************************
	 * ActionListener(로그인,회원가입,회원가입-이메일,마이페이지,화면전환)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();
		if(obj==mmv.jbt_ticketing) {
			mmv.jp_mrv.jp_mcv.movielist = new Vector<Map<String,String>>();
			send(MovieProtocol.SELECT+"#");
		}
		else if(mmv.jp_mrv.jp_mcv.jt_movie==obj) {
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		if(mmv.jp_mrv.jp_mcv.jt_movie==obj) {
			for (int j = 0; j < mmv.jp_mrv.jp_mcv.jt_movie.getRowCount(); j++) {
				String movietitle = mmv.jp_mrv.jp_mcv.dtm_movie.getValueAt(j, 0).toString();
				System.out.println(movietitle);
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
