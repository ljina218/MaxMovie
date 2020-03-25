package maxmovie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class LoginView extends JPanel{
	JLabel 			jl_logo_m 		= new JLabel() {
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
	JLabel					jl_id				= new JLabel("아이디");
	JLabel					jl_pw				= new JLabel("비밀번호");	
	JTextField 				jtf_id 				= new JTextField();
	JPasswordField 			jpf_pw 				= new JPasswordField();
	JLabel					jl_id_warning		= new JLabel();
	JLabel					jl_pw_warning		= new JLabel();
	JButton 				jbt_join 			= new JButton("회원가입");
	JButton 				jbt_login 			= new JButton("로그인");
	EventMapping 			em 					= null;
	
	public LoginView() {
		initDisplay();
	}
	public LoginView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	private void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		jtf_id.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jpf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jl_id_warning.setText("아이디가 존재하지 않습니다.");
		jl_pw_warning.setText("비밀번호가 일치하지 않습니다.");
		jl_id.setHorizontalAlignment(JLabel.RIGHT);
		jl_pw.setHorizontalAlignment(JLabel.RIGHT);
		jl_id_warning.setVisible(false);
		jl_pw_warning.setVisible(false);
		
		jl_logo_m.setBounds(612, 170, 300, 140);
		jl_id.setBounds(560, 310, 50, 40);
		jl_pw.setBounds(560, 352, 50, 40);
		jtf_id.setBounds(612, 310, 296, 40);
		jpf_pw.setBounds(612, 352, 296, 40);

		jbt_join.setBounds(612, 394, 146, 40);
		jbt_login.setBounds(762, 394, 146, 40);
		jl_id_warning.setBounds(912, 318, 300, 28);
		jl_pw_warning.setBounds(912, 360, 300, 28);
		
		jl_id.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_id.setForeground(Color.black);

		jtf_id.setFont(new Font("굴림체", Font.PLAIN, 12));
		jtf_id.setForeground(new Color(142, 142, 142));
				
		jl_id_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_id_warning.setForeground(Color.red);

		jl_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_pw.setForeground(Color.black);
		
		jpf_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jpf_pw.setForeground(new Color(142, 142, 142));

		jl_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_pw_warning.setForeground(Color.red);
		
		jbt_join.setBackground(new Color(52, 152, 219));
		jbt_join.setForeground(Color.white);
		
		jbt_login.setBackground(new Color(52, 152, 219));
		jbt_login.setForeground(Color.white);
		
		this.add(jl_logo_m);
		this.add(jl_id);
		this.add(jl_pw);
		this.add(jtf_id);
		this.add(jpf_pw);
		this.add(jl_id_warning);
		this.add(jl_pw_warning);
		this.add(jbt_join);
		this.add(jbt_login);
/*******************************************************
 * 		●#login 13	jl_id_warning.setVisible(false);
 * 		●#login 14	jl_id_warning.setVisible(true);
 * 		○#login 17	jl_pw_warning.setVisible(false);
 * 		○#login 18	jl_pw_warning.setVisible(true);
 *******************************************************/
		jtf_id.addActionListener(em);
		jpf_pw.addActionListener(em);
		jbt_join.addActionListener(em);
		jbt_login.addActionListener(em);
	}
	
	public static void main(String[] args) {
		new LoginView();
	}
}
