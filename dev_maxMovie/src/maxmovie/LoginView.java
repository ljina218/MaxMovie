package maxmovie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class LoginView extends JPanel{
	
	JPanel 			jp_center 		= new JPanel();
	JPanel 			jp_center1 		= new JPanel();
	JPanel 			jp_center2 		= new JPanel();
	JPanel 			jp_center2_north= new JPanel();
	JPanel 			jp_center2_south= new JPanel();
	
	
	JLabel 			jl_logo_large 		= new JLabel() {
		public void paint(Graphics g) {
			Image img = null;
			try {
				img = ImageIO.read(new File("src\\maxmovie\\maxmovielogol.png"));
				g.drawImage(img, 0, 0, 400, 200, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	
	JTextField 		jtf_id 			= new JTextField(" 아이디");
	JTextField 		jtf_pw 			= new JTextField(" 비밀번호");
	JButton 		jbt_join 		= new JButton("회원가입");
	JButton 		jbt_login 		= new JButton("로그인");
	MaxMovieView 	mv 				= null;
	
	Font font_jl_id = new Font("굴림체", Font.PLAIN, 12);
	Font font_jtf_id = new Font("굴림체", Font.PLAIN, 12);
	Font font_jl_pw = new Font("굴림체", Font.PLAIN, 12);
	Font font_jtf_pw = new Font("굴림체", Font.PLAIN, 12);
	
	public LoginView(MaxMovieView mv) {
		this.mv = mv;
		initDisplay();
	}
	private void initDisplay() {
		GridBagConstraints gbc_jp_center, gbc_jp_center1, gbc_jp_center2,
						   gbc_jp_center2_north, gbc_jp_center2_south;

		this.setLayout(new GridBagLayout());
		
		gbc_jp_center = new GridBagConstraints();
		gbc_jp_center.gridx = 1;
		gbc_jp_center.gridy = 0;
		gbc_jp_center.gridwidth = 400;
		gbc_jp_center.gridheight = 1600;
		this.add(jp_center, gbc_jp_center);
		
		gbc_jp_center1 = new GridBagConstraints();
		gbc_jp_center1.gridx = 0;
		gbc_jp_center1.gridy = 0;
		gbc_jp_center1.gridwidth = 400;
		gbc_jp_center1.gridheight = 400;
		jp_center.add(jp_center1, gbc_jp_center1);
		
		jp_center1.add(jl_logo_large);
		jl_logo_large.setPreferredSize(new Dimension(400,200));
		
		gbc_jp_center2 = new GridBagConstraints();
		gbc_jp_center2.gridx = 0;
		gbc_jp_center2.gridy = 1;
		gbc_jp_center2.gridwidth = 400;
		gbc_jp_center2.gridheight = 800;
		jp_center.add(jp_center2, gbc_jp_center2);
		
		
		gbc_jp_center2_north = new GridBagConstraints();
		gbc_jp_center2_north.gridx = 0;
		gbc_jp_center2_north.gridy = 1;
		gbc_jp_center2_north.gridwidth = 400;
		gbc_jp_center2_north.gridheight = 200;
		jp_center2.add(jp_center2_north, gbc_jp_center2_north);

		jtf_id.setPreferredSize(new Dimension(400,40));

		jtf_pw.setPreferredSize(new Dimension(400,40));
		jp_center2_north.add(jtf_id);
		jp_center2_north.add(jtf_pw);
		
		gbc_jp_center2_south = new GridBagConstraints();
		gbc_jp_center2_south.gridx = 0;
		gbc_jp_center2_south.gridy = 2;
		gbc_jp_center2_south.gridwidth = 400;
		gbc_jp_center2_south.gridheight = 200;
		jp_center2.add(jp_center2_south, gbc_jp_center2_south);
		jp_center2_south.add(jbt_join);
		jp_center2_south.add(jbt_login);
		

		
		jtf_id.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),2)));
		jtf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),2)));
/*****************************************
 * 		텍스트필드 마우스클릭오버시 필요함.
 *		jtf_id.setText("");
 *		jtf_pw.setText("");
 *		jtf_id.setForeground(Color.BLACK);
 *		jtf_pw.setForeground(Color.BLACK);
 *		jtf_pw * 표시 처리 필요요망
 *
 *		텍스트필드의 길이가 다시 0이 되었을떄
 *		jtf_id.setText(" 아이디");
 *		jtf_pw.setText(" 비밀번호");
 *		jtf_id.setForeground(new Color(142, 142, 142));
 *		jtf_pw.setForeground(new Color(142, 142, 142));
 ******************************************/
		this.setBackground(Color.white);
		jp_center.setBackground(Color.white);
		jp_center2.setBackground(Color.white);
		jp_center2_north.setBackground(Color.white);
		jp_center2_south.setBackground(Color.white);
		
//		jp_center1.add(jl_logo_large);
//		jp_center2.add(jp_center2_north);
//		jp_center2.add(jp_center2_south);
		
		
		jtf_id.setForeground(new Color(142, 142, 142));
		jtf_pw.setForeground(new Color(142, 142, 142));
		jtf_id.setFont(font_jtf_id);
		jtf_pw.setFont(font_jtf_pw);
//		jl_logo_large.setBounds(560, 120, 400, 200);
//		jtf_id.setBounds(562, 340, 396, 40);
//		jtf_pw.setBounds(562, 390, 396, 40);
//		jbt_join.setBounds(562, 440, 197, 40);
//		jbt_login.setBounds(761, 440, 197, 40);
//		this.add(jp_center);
//		this.add(jp_center2);
		System.out.println("여기까지옴");
	}
}
