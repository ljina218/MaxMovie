package maxmovie;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MyPageView extends JPanel{


	JButton						jbt_thv				= new JButton("예매화면");
	JButton						jbt_miv				= new JButton("회원정보");
	
	EventMapping				em					= null;
	TicketHistoryView			jp_thv				= null;
	MemInfoView					jp_miv				= null;
	MemUpdateView				jp_muv				= null;
	 
	JPanel						jp_center			= new JPanel();
	public MyPageView(EventMapping em) {
		this.em = em;
		jp_thv				= new TicketHistoryView(em);
		jp_miv				= new MemInfoView(em);
		jp_muv				= new MemUpdateView(em);
		initDisplay();
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);

		jbt_thv.setBounds(1060, 118, 88, 22);
		jbt_miv.setBounds(1150, 118, 88, 22);
		jp_center.setBorder(new TitledBorder(new LineBorder(new Color(0,0,0),3)));
		jp_center.setBounds(300,140,940,460);
		jp_thv.setBounds(310, 150, 920, 440);
		jp_miv.setBounds(310, 150, 920, 440);
		jp_muv.setBounds(310, 150, 920, 440);
		jp_center.setBackground(new Color(215, 215, 215));
		jp_thv.setVisible(true);
		jp_miv.setVisible(false);
		jp_muv.setVisible(false);
		jbt_thv.setFont(new Font("굴림체", Font.BOLD, 12));
		jbt_thv.setBackground(new Color(52, 152, 219));
		jbt_thv.setForeground(Color.white);
		jbt_miv.setFont(new Font("굴림체", Font.PLAIN, 11));
		jbt_miv.setBackground(new Color(52, 152, 219));
		jbt_miv.setForeground(Color.white);
		
		/***************************************************************
		 * 회원정보화면 or 회원정보수정화면 -> 예매내역화면
		 * jbt_thv.setFont(new Font("굴림체", Font.BOLD, 12));
		 * jbt_miv.setFont(new Font("굴림체", Font.PLAIN, 11));
		 * jp_thv.setVisible(true);
		 * jp_miv.setVisible(false);
		 * jp_muv.setVisible(false);
		 * 
		 * 예매내역화면 -> 회원정보화면(디폴트)
		 * jbt_thv.setFont(new Font("굴림체", Font.PLAIN, 11));
		 * jbt_miv.setFont(new Font("굴림체", Font.BOLD, 12));
		 * jp_thv.setVisible(false);
		 * jp_miv.setVisible(true);
		 * jp_muv.setVisible(false);
		 * 
		 * 회원정보화면(디폴트) -> 회원정보수정화면
		 * jp_thv.setVisible(false);
		 * jp_miv.setVisible(false);
		 * jp_muv.setVisible(true);
		 ***************************************************************/
		jbt_thv.addActionListener(em);
		jbt_miv.addActionListener(em);
		this.add(jbt_thv);
		this.add(jbt_miv);
		this.add(jp_muv);
		this.add(jp_thv);
		this.add(jp_miv);
		this.add(jp_center);
	}
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jl_logo_small.setVisible(true);
		mmv.jp_lv.setVisible(false);
		mmv.jp_mcv.setVisible(false);
		mmv.jp_scv.setVisible(false);
		mmv.jp_rv.setVisible(false);
		mmv.jp_mv.setVisible(true);
		
		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfo.setText("kong");
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
		
		MyPageView mpv = new MyPageView(mmv.em);
	}
}
