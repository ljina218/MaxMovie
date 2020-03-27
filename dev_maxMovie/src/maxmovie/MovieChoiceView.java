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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MovieChoiceView extends JPanel{
	JTextPane		jtp_id_warning		= new JTextPane();
	JTextPane		jtp_pw_warning		= new JTextPane();
	JTextField 		jtf_id 				= new JTextField(" 아이디");
	JTextField 		jtf_pw 				= new JTextField(" 비밀번호");
	JButton 		jbt_join 			= new JButton("회원가입");
	JButton 		jbt_login 			= new JButton("로그인");
	EventMapping 	em 					= null;
	public MovieChoiceView() {
		initDisplay();
	}
	public MovieChoiceView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	private void initDisplay() {
//		this.setLayout(null);
//		this.setBackground(Color.white);
//		jtf_id.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
//		jtf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
//
//		jl_logo_m.setBounds(612, 170, 300, 140);
//		jtf_id.setBounds(612, 310, 296, 40);
//		jtf_pw.setBounds(612, 352, 296, 40);
//		jbt_join.setBounds(612, 394, 146, 40);
//		jbt_login.setBounds(762, 394, 146, 40);
//		
//		jtp_id_warning.setBounds(912, 318, 300, 28);
//		jtp_pw_warning.setBounds(912, 360, 300, 28);
//		
//		jtf_id.setFont(new Font("굴림체", Font.PLAIN, 12));
//		jtf_id.setForeground(new Color(142, 142, 142));
//		
//		jtp_id_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
//		jtp_id_warning.setForeground(Color.red);
//		
//		jtf_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
//		jtf_pw.setForeground(new Color(142, 142, 142));
//
//		jtp_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
//		jtp_pw_warning.setForeground(Color.red);
//		
//		jbt_join.setBackground(new Color(52, 152, 219));
//		jbt_join.setForeground(Color.white);
//		
//		jbt_login.setBackground(new Color(52, 152, 219));
//		jbt_login.setForeground(Color.white);
//		this.add(jl_logo_m);
//		this.add(jtf_id);
//		this.add(jtf_pw);
//		this.add(jbt_join);
//		this.add(jbt_login);
//		this.add(jtf_id);
//		this.add(jtf_pw);
//		this.add(jtp_id_warning);
//		this.add(jtp_pw_warning);

	}
	
	public static void main(String[] args) {
		new LoginView();
	}
}
