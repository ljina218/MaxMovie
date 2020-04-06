package maxmovie;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MemUpdateView extends JPanel  {
	
	JLabel					jl_pageInfoLeft 		= new JLabel();
	JLabel					jl_pageInfoRight		= new JLabel("님의 회원정보");
	JLabel 					jl_id 					= new JLabel("아이디   ");
	JLabel 					jl_pw 					= new JLabel("비밀번호   ");
	JLabel 					jl_name 				= new JLabel("이름   ");
	JLabel 					jl_nick 				= new JLabel("닉네임   ");
	JLabel 					jl_year 				= new JLabel("생년월일   ");
	JLabel 					jl_gender 				= new JLabel("성별   ");
	JLabel 					jl_email 				= new JLabel("이메일   ");
	JLabel 					jl_email_r 				= new JLabel("인증번호   ");
	JLabel					jl_mem_id				= new JLabel();
	JPasswordField			jpf_pw 					= new JPasswordField();
	JLabel 					jl_mem_name 			= new JLabel();
	JTextField 				jtf_nick 				= new JTextField();
	JLabel					jl_mem_ymd				= new JLabel("년                    월                      일");
	JLabel					jl_mem_year				= new JLabel();
	JLabel					jl_mem_month			= new JLabel();
	JLabel					jl_mem_day				= new JLabel();
	JLabel					jl_mem_gender			= new JLabel();
	JTextField 				jtf_email 				= new JTextField();
	JTextField 				jtf_email_r 			= new JTextField();
	JButton					jbt_email				= new JButton("인증번호 발송");
	JButton					jbt_email_r				= new JButton("인증번호 확인");	
	JButton					jbt_back				= new JButton("뒤로가기");
	JButton					jbt_modifiedGo			= new JButton("정보수정");
	JLabel              	jl_pw_warning  	   		= new JLabel(" 7~12자이어야 하고 공백은 불가합니다.");
	JLabel              	jl_nick_warning	    	= new JLabel(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.");
	JLabel             		jl_email_warning   		= new JLabel(" 이메일주소 형식에 맞지 않습니다.");
	JLabel              	jl_email_warning2  		= new JLabel(" 인증번호 입력시간은 2분입니다.");
	JLabel              	jl_email_r_success  	= new JLabel(" 인증성공");
	JLabel              	jl_email_r_warning  	= new JLabel(" 인증번호가 일치하지 않습니다.");
	JLabel              	jl_email_r_warning2  	= new JLabel(" 입력시간이 초과했습니다.");
	EventMapping 			em 						= null;
	
	public MemUpdateView(EventMapping em) {
		this.em = em;
		initDisplay();
	}

	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(new Color(215, 215, 215));
//		현접속유저 정보(수정불가한)
//		jl_pageInfoLeft.setText(em.mmv.mem_nick);
//		jl_mem_id.setText(em.mmv.mem_nick);
//		jl_mem_month.setText(em.mmv.mem_nick);
//		jl_mem_day.setText(em.mmv.mem_nick);	
//		jl_mem_gender.setText(em.mmv.mem_nick);

		jl_pageInfoLeft.setText("kong");
		jl_mem_id.setText("cloudsky7");
		jl_mem_name.setText("박미경");
		jl_mem_year.setText("1992");
		jl_mem_month.setText("10");
		jl_mem_day.setText("31");
		jl_mem_gender.setText("남자");
		
		jpf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jtf_nick.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jtf_email.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jtf_email_r.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));

		jl_id.setHorizontalAlignment(JLabel.RIGHT);
		jl_pw.setHorizontalAlignment(JLabel.RIGHT);
		jl_name.setHorizontalAlignment(JLabel.RIGHT);
		jl_nick.setHorizontalAlignment(JLabel.RIGHT);
		jl_year.setHorizontalAlignment(JLabel.RIGHT);
		jl_gender.setHorizontalAlignment(JLabel.RIGHT);
		jl_email.setHorizontalAlignment(JLabel.RIGHT);
		jl_email_r.setHorizontalAlignment(JLabel.RIGHT);
		
		jl_pageInfoLeft.setBounds(260, 0, 160, 40);
		jl_pageInfoRight.setBounds(430, 0, 170, 40);
		jl_id.setBounds(280, 50, 70, 32);
		jl_pw.setBounds(280, 85, 70, 32);
		jl_name.setBounds(280, 120, 70, 32);
		jl_nick.setBounds(280, 155, 70, 32);
		jl_year.setBounds(280, 190, 70, 32);
		jl_gender.setBounds(280, 225, 70, 32);
		jl_email.setBounds(280, 260, 70, 32);
		jl_email_r.setBounds(280, 295, 70, 32);
		jl_mem_id.setBounds(360, 50 ,172 ,32);
		jpf_pw.setBounds(350, 85 ,260 ,32);
		jl_mem_name.setBounds(360, 120 ,260 ,32);
		jtf_nick.setBounds(350, 155 ,260 ,32);
		jl_mem_ymd.setBounds(440, 190, 260, 32);
		jl_mem_year.setBounds(404, 190 ,30 ,32);
		jl_mem_month.setBounds(480, 190 ,30 ,32);
		jl_mem_day.setBounds(560, 190 ,30 ,32);
		jl_mem_gender.setBounds(360, 225 ,30 ,32);
		jtf_email.setBounds(350, 260 ,142 ,32);
		jbt_email.setBounds(495, 260 ,115,32);
		jtf_email_r.setBounds(350, 295 ,142 ,32);
		jbt_email_r.setBounds(495, 295 ,115 ,32);
		jbt_back.setBounds(350, 340, 128, 32);
		jbt_modifiedGo.setBounds(482, 340, 128, 32);
		jl_pw_warning.setBounds(612, 91 ,300 ,18);
		jl_nick_warning.setBounds(612, 161 ,300 ,18);
		jl_email_warning.setBounds(612, 266 ,300 ,18);
		jl_email_warning2.setBounds(612, 266 ,300 ,18);
		jl_email_r_success.setBounds(612, 301 ,300 ,18);
		jl_email_r_warning.setBounds(612, 301 ,300 ,18);
		jl_email_r_warning2.setBounds(612, 301 ,300 ,18);
		/*********************************************
		 * jl_pw_warning.setVisible(true);      
		 * jl_nick_warning.setVisible(true);       
		 * jl_email_warning.setVisible(true);   
		 * jl_email_warning2.setVisible(true);  
		 * jl_email_r_success.setVisible(true);    
		 * jl_email_r_warning.setVisible(true);  
		 * jl_email_r_warning2.setVisible(true);
		 **********************************************/
		jl_pw_warning.setVisible(false); 
		jl_nick_warning.setVisible(false);  
		jl_email_warning.setVisible(false);
		jl_email_warning2.setVisible(false);
		jl_email_r_success.setVisible(false); 
		jl_email_r_warning.setVisible(false);
		jl_email_r_warning2.setVisible(false);

		jl_mem_year.setHorizontalAlignment(JLabel.RIGHT);
		jl_mem_month.setHorizontalAlignment(JLabel.RIGHT);
		jl_mem_day.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoLeft.setForeground(Color.black);
		jl_pageInfoRight.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoRight.setForeground(Color.black);
		jl_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_pw_warning.setForeground(Color.red); 
		jl_nick_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_nick_warning.setForeground(Color.red);
		jl_email_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_email_warning.setForeground(Color.red);
		jl_email_warning2.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_email_warning2.setForeground(Color.red);
		jl_email_r_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_email_r_warning.setForeground(Color.red);
		jl_email_r_warning2.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_email_r_warning2.setForeground(Color.red);
		jl_email_r_success.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_email_r_success.setForeground(Color.green);
		jbt_email.setBackground(new Color(52, 152, 219));
		jbt_email.setForeground(Color.white);
		jbt_email_r.setBackground(new Color(52, 152, 219));
		jbt_email_r.setForeground(Color.white);
		jbt_back.setBackground(new Color(52, 152, 219));
		jbt_back.setForeground(Color.white);
		jbt_modifiedGo.setBackground(new Color(52, 152, 219));
		jbt_modifiedGo.setForeground(Color.white);
		
		this.add(jl_pageInfoLeft);
		this.add(jl_pageInfoRight);
		this.add(jl_id);
		this.add(jl_pw);
		this.add(jl_name);
		this.add(jl_nick);
		this.add(jl_year);
		this.add(jl_gender);
		this.add(jl_email);
		this.add(jl_email_r);
		this.add(jl_mem_id);
		this.add(jpf_pw);
		this.add(jl_mem_name);
		this.add(jtf_nick);
		this.add(jl_mem_ymd);
		this.add(jl_mem_year);
		this.add(jl_mem_month);
		this.add(jl_mem_day);
		this.add(jl_mem_gender);
		this.add(jtf_email);
		this.add(jtf_email_r);
		this.add(jbt_email);
		this.add(jbt_email_r);
		this.add(jbt_back);
		this.add(jbt_modifiedGo);
		this.add(jl_pw_warning);
		this.add(jl_nick_warning);
		this.add(jl_email_warning);
		this.add(jl_email_warning2);
		this.add(jl_email_r_success);
		this.add(jl_email_r_warning);
		this.add(jl_email_r_warning2);
		
	
	}
	
	public void eventMapping() {
		jpf_pw.addActionListener(em);
		jtf_nick.addActionListener(em);
		jbt_email.addActionListener(em);
		jbt_email_r.addActionListener(em);
		jbt_back.addActionListener(em);
		jbt_modifiedGo.addActionListener(em);
	}	
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(false);
		mmv.jp_mrv.jp_mcv.setVisible(false);
		mmv.jp_mrv.jp_scv.setVisible(false);
		mmv.jp_mrv.jp_pv.setVisible(false);
		mmv.jp_mv.setVisible(true);
		mmv.jp_mv.jp_thv.setVisible(false);
		mmv.jp_mv.jp_miv.setVisible(false);
		mmv.jp_mv.jp_muv.setVisible(true);
		mmv.jp_rv.setVisible(false);
		
		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
	}

}
