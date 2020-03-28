package maxmovie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class JoinView extends JDialog {
	
	Calendar 			today 					= Calendar.getInstance();
	int 				year 					= today.get(Calendar.YEAR);
	int 				month 					= today.get(Calendar.MONTH)+1;
	int	 				day 					= today.get(Calendar.DAY_OF_MONTH);
	String 				choiceYear 				= "년도";
	String 				choiceMonth			 	= "월";
	String 				choiceDay 				= "일";
	
	String []			genders					= {"선택","남","여"};
	String				jcb_genderChoice		= null;
	
	JLabel 				jl_logo_m 		= new JLabel() {
		public void paint(Graphics g) {
			Image img = null;
			try {
				img = ImageIO.read(new File("src\\maxmovie\\maxmovielogom.png"));
				g.drawImage(img, 0, 0, 300, 140, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	JLabel 				jl_id 					= new JLabel("아이디   ");
	JLabel 				jl_pw 					= new JLabel("비밀번호   ");
	JLabel 				jl_name 				= new JLabel("이름   ");
	JLabel 				jl_nick 				= new JLabel("닉네임   ");
	JLabel 				jl_year 				= new JLabel("생년월일   ");
	JLabel 				jl_gender 				= new JLabel("성별   ");
	JLabel 				jl_email 				= new JLabel("이메일   ");
	JLabel 				jl_email_r 				= new JLabel("인증번호   ");
	JTextField 			jtf_id 					= new JTextField();
	JPasswordField		jpf_pw 					= new JPasswordField();
	JTextField 			jtf_name 				= new JTextField();
	JTextField 			jtf_nick 				= new JTextField();
	JComboBox<String>	jcb_year				= null;
	JComboBox<String>	jcb_month				= null;
	JComboBox<String>	jcb_day					= null;
	JComboBox<String>	jcb_gender				= new JComboBox<String>(genders);
	JTextField 			jtf_email 				= new JTextField();
	JTextField 			jtf_email_r 			= new JTextField();
	JButton				jbt_id_check			= new JButton("ID 중복 체크");
	JButton				jbt_email				= new JButton("인증번호 발송");
	JButton				jbt_email_r				= new JButton("인증번호 확인");	
	JButton				jbt_back				= new JButton("뒤로가기");
	JButton				jbt_joingo				= new JButton("회원가입");
	JLabel              jl_id_warning			= new JLabel();
	JLabel              jl_id_warning2			= new JLabel();
	JLabel              jl_id_success			= new JLabel();
	JLabel              jl_pw_warning  	   		= new JLabel();
	JLabel              jl_name_warning	    	= new JLabel();
	JLabel              jl_nick_warning	    	= new JLabel();
	JLabel              jl_year_warning	    	= new JLabel();
	JLabel              jl_month_warning	    = new JLabel();
	JLabel              jl_day_warning	    	= new JLabel();
	JLabel              jl_gender_warning 		= new JLabel();
	JLabel              jl_email_warning   		= new JLabel();
	JLabel              jl_email_warning2  		= new JLabel();
	JLabel              jl_email_r_success  	= new JLabel();
	JLabel              jl_email_r_warning  	= new JLabel();
	JLabel              jl_email_r_warning2  	= new JLabel();
	String				jcb_yearChoice			= null;
	String				jcb_monthChoice			= null;
	String				jcb_dayChoice			= null;
	EventMapping 		em 						= null;
	
	public JoinView(EventMapping em) {
		this.em = em;
		//생년월일 - 년도 콤보박스 생성
		String []years = new String[this.year-1918];
		years[0] = "년도";
		int year2 = this.year;
		for(int i=1; i<years.length; i++) {
			years[i]= Integer.toString(year2);
			year2--;
		}
		jcb_year = new JComboBox<String>(years);
		jcb_month = new JComboBox<String>();
		jcb_month.addItem("월");
		jcb_day = new JComboBox<String>();
		jcb_day.addItem("일");
		//화면호출
		initDisplay();
		eventMapping();
	}
	
	public void initDisplay(){
		this.setLayout(null);
		this.getContentPane().setBackground(Color.white);
		jtf_id.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jpf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jtf_name.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jtf_nick.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jcb_year.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jcb_month.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jcb_gender.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
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
		
		jl_logo_m.setBounds(150,50,300,140);
		jl_id.setBounds(80, 196, 70, 32);
		jl_pw.setBounds(80, 254, 70, 32);
		jl_name.setBounds(80, 312, 70, 32);
		jl_nick.setBounds(80, 370, 70, 32);
		jl_year.setBounds(80, 428, 70, 32);
		jl_gender.setBounds(80, 486, 70, 32);
		jl_email.setBounds(80, 544, 70, 32);
		jl_email_r.setBounds(80, 602, 70, 32);
		jtf_id.setBounds(150, 196 ,182 ,32);
		jpf_pw.setBounds(150, 254 ,300 ,32);
		jtf_name.setBounds(150, 312 ,300 ,32);
		jtf_nick.setBounds(150, 370 ,300 ,32);
		jcb_year.setBounds(150, 428 ,135 ,32);
		jcb_month.setBounds(288, 428 ,80 ,32);
		jcb_day.setBounds(370, 428 ,80 ,32);
		jcb_gender.setBounds(150, 486 ,300 ,32);
		jtf_email.setBounds(150, 544 ,182 ,32);
		jtf_email_r.setBounds(150, 602 ,182 ,32);
		jbt_back.setBounds(150, 660, 148, 32);
		jbt_joingo.setBounds(300, 660, 148, 32);
		jbt_id_check.setBounds(334, 196 ,115 ,32);
		jbt_email.setBounds(334, 544 ,115,32);
		jbt_email_r.setBounds(334, 602 ,115 ,32);
		jl_id_warning.setBounds(154, 230 ,300 ,18);
		jl_id_warning2.setBounds(154, 230 ,300 ,18);
		jl_id_success.setBounds(154, 230 ,300 ,18);
		jl_pw_warning.setBounds(154, 288 ,300 ,18);
		jl_name_warning.setBounds(154, 346 ,300 ,18);
		jl_nick_warning.setBounds(154, 404 ,300 ,18);
		jl_year_warning.setBounds(154, 460 ,300 ,18);
		jl_month_warning.setBounds(292, 460 ,300 ,18);
		jl_day_warning.setBounds(374, 460 ,300 ,18);
		jl_gender_warning.setBounds(154, 518 ,300 ,18);
		jl_email_warning.setBounds(154, 576 ,300 ,18);
		jl_email_warning2.setBounds(154, 576 ,300 ,18);
		jl_email_r_success.setBounds(154, 634 ,300 ,18);
		jl_email_r_warning.setBounds(154, 634 ,300 ,18);
		jl_email_r_warning2.setBounds(154, 634 ,300 ,18);
		
		jl_id_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_id_warning.setForeground(Color.red);
		jl_id_warning2.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_id_warning2.setForeground(Color.red);
		jl_id_success.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_id_success.setForeground(Color.green);
		jl_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_pw_warning.setForeground(Color.red);    
		jl_name_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_name_warning.setForeground(Color.red);
		jl_nick_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_nick_warning.setForeground(Color.red);
		jl_year_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_year_warning.setForeground(Color.red);
		jl_month_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_month_warning.setForeground(Color.red);
		jl_day_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_day_warning.setForeground(Color.red);
		jl_gender_warning.setFont(new Font("굴림체", Font.PLAIN, 11));	
		jl_gender_warning.setForeground(Color.red);
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
		jbt_id_check.setBackground(new Color(52, 152, 219));
		jbt_id_check.setForeground(Color.white);
		jbt_email.setBackground(new Color(52, 152, 219));
		jbt_email.setForeground(Color.white);
		jbt_email_r.setBackground(new Color(52, 152, 219));
		jbt_email_r.setForeground(Color.white);
		jbt_back.setBackground(new Color(52, 152, 219));
		jbt_back.setForeground(Color.white);		
		jbt_joingo.setBackground(new Color(52, 152, 219));
		jbt_joingo.setForeground(Color.white);
		
		this.add(jl_logo_m);
		this.add(jl_id);
		this.add(jbt_id_check);	
		this.add(jl_pw);
		this.add(jl_nick);
		this.add(jl_name);
		this.add(jl_year);
		this.add(jl_gender);
		this.add(jl_email);
		this.add(jl_email_r);
		this.add(jtf_id);
		this.add(jpf_pw);
		this.add(jtf_nick);
		this.add(jtf_name);
		this.add(jcb_year);
		this.add(jcb_month);
		this.add(jcb_day);
		this.add(jcb_gender);
		this.add(jtf_email);
		this.add(jbt_email);
		this.add(jtf_email_r);
		this.add(jbt_email_r);
		this.add(jbt_back);
		this.add(jbt_joingo);
		this.add(jl_id_warning);
		this.add(jl_id_warning2);
		this.add(jl_id_success);
		this.add(jl_pw_warning);
		this.add(jl_name_warning);
		this.add(jl_nick_warning);
		this.add(jl_year_warning);
		this.add(jl_month_warning);
		this.add(jl_day_warning);
		this.add(jl_gender_warning);
		this.add(jl_email_warning);
		this.add(jl_email_warning2);
		this.add(jl_email_r_success);
		this.add(jl_email_r_warning);
		this.add(jl_email_r_warning2);
		this.setSize(600, 800);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		jl_id_warning.setVisible(false);
		jl_id_warning2.setVisible(false);
		jl_id_success.setVisible(false);
		jl_pw_warning.setVisible(false);
		jl_name_warning.setVisible(false);
		jl_nick_warning.setVisible(false);
		jl_year_warning.setVisible(false);
		jl_month_warning.setVisible(false);
		jl_day_warning.setVisible(false);
		jl_gender_warning.setVisible(false);
		jl_email_warning.setVisible(false);
		jl_email_warning2.setVisible(false);
		jl_email_r_success.setVisible(false);
		jl_email_r_warning.setVisible(false);
		jl_email_r_warning2.setVisible(false);
		
		jl_id_warning.setText(" 동일한 아이디가 존재합니다.");
		jl_id_success.setText(" 사용 가능한 아이디입니다.");
		jl_id_warning2.setText(" 7~12자이어야 하고 영문 및 숫자만 입력할 수 있습니다.");
		jl_pw_warning.setText(" 7~12자이어야 하고 공백은 불가합니다.");
		jl_name_warning.setText(" 3~15자이어야 하고 특수문자는 사용할 수 없습니다");
		jl_nick_warning.setText(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.");
		jl_year_warning.setText(" 생년을 선택하여주세요");
		jl_month_warning.setText(" 생월을 선택하여주세요");
		jl_day_warning.setText(" 생일을 선택하여주세요");
		jl_gender_warning.setText(" 성별을 선택해주세요.");
		jl_email_warning.setText(" 이메일주소 형식에 맞지 않습니다.");
		jl_email_warning2.setText(" 인증번호 입력시간은 2분입니다.");
		jl_email_r_success.setText(" 인증성공");
		jl_email_r_warning.setText(" 인증번호가 일치하지 않습니다.");
		jl_email_r_warning2.setText(" 입력시간이 초과했습니다.");
/*********************************************************************
 * 		● #join10		jl_id_warning.setVisible(false);
 * 		● #join11		jl_id_warning.setVisible(true);
 * 		○ #join13		jl_pw_warning.setVisible(false);
 * 		○ #join15		jl_pw_warning.setVisible(true);
 * 		● #join16		jl_name_warning.setVisible(false);
 * 		● #join18		jl_name_warning.setVisible(true);
 *    	○ #join19		jl_nick_warning.setVisible(false);
 *  	○ #join21		jl_nick_warning.setVisible(true);
 *  
 *  	● #join22		jl_year_warning.setVisible(false);
 *  					jl_month_warning.setVisible(false);
 *  					jl_day_warning.setVisible(false);
 *  
 *  	● #join24		jl_year_warning.setVisible(true);
 *  					jl_month_warning.setVisible(false);
 *  					jl_day_warning.setVisible(false);
 *  
 *  	● #join25		jl_year_warning.setVisible(false);
 *   	  				jl_month_warning.setVisible(true);
 *  					jl_day_warning.setVisible(false);
 *  
 *  	● #join26		jl_year_warning.setVisible(false);
 *     					jl_month_warning.setVisible(false);
 *  					jl_day_warning.setVisible(true);
 *  
 * 		○ #join27		jl_gender_warning.setVisible(false);
 * 		○ #join29		jl_gender_warning.setVisible(true);
 *  	● #join30		jl_email_warning.setVisible(false);
 *  	● #join32		jl_email_warning.setVisible(true);
 *  	○ #join34		jl_email_r_warning.setVisible(false);
 *  	○ #join36		jl_email_r_warning.setVisible(true);
 *********************************************************************/
	}
	
	public void eventMapping() {
		jbt_id_check.addActionListener(em);
		jtf_id.addActionListener(em);
		jpf_pw.addKeyListener(em);
		jtf_nick.addKeyListener(em);
		jtf_name.addKeyListener(em);
		jcb_year.addItemListener(em);
		jcb_gender.addItemListener(em);
		jtf_email.addActionListener(em);
		jbt_email.addActionListener(em);
		jtf_email_r.addActionListener(em);
		jbt_email_r.addActionListener(em);
		jbt_back.addActionListener(em);
		jbt_joingo.addActionListener(em);
	}
	
}
