package maxmovie;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	
	//네자리 난수 저장 변수
	String rnum = null;
	
	//네자리 난수발생 메소드
	public String randomNum() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i=0; i<4; i++) {
			int num = random.nextInt(10);
			sb.append(num);
		}
		this.rnum = sb.toString();
		return rnum;
	}
	
	//메일 발송하는 메소드
	public SendMail(String email) throws UnsupportedEncodingException{
		//SMTP 서버 정보를 설정
		Properties props = new Properties();
		//발송 STMP 서버
		props.put("mail.smtp.host", "smtp.naver.com");
		//SMTP서버의 포트
		props.put("mail.smtp.port", 465);		
		props.put("mail.smtp.auth", "true");
		//SSL 활성화
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.naver.com");
		//SMTP 서버 정보와 사용자 계정를 기반으로 
		//Session 클래스의 인스턴스를 생성
		String smtpServer = "smtp.naver.com"; 
		int smtpPort = 465;
		final String sendId = "ljina0218"; //naver ID
		final String sendPass = "wlsdk20150514"; //naver PW
		String sendEmailAddress =  "ljina0218@naver.com";
		Session session = Session.getDefaultInstance(props, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(sendId, sendPass);
			}
		});
		//메일 발신자와 수신자, 제목 그리고 내용 작성을 위한 MimeMessage객체 생성
		MimeMessage message = new MimeMessage(session);
		String receiveEmailAddress = email;
		String subject = "인증번호 메일발송";
		String content = "인증번호는 ["+randomNum()+"] 입니다.";
		try {
			//발신자 설정
			message.setFrom(new InternetAddress(sendEmailAddress));
			//수신자 설정
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmailAddress));
			//제목 설정
			message.setSubject(subject);
			//메일 내용 설정
			message.setText(content);
			//일반 테스트 형태
			System.out.print("메일 전송 성공! "); 
			//이메일 보내기
			Transport.send(message);
			System.out.println("메일이 전송 되었습니다.");
		} 
		catch (MessagingException e) {			
			e.printStackTrace();
		}
	}
	

}
