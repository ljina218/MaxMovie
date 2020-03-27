package maxmovie;

import java.util.StringTokenizer;

public class ClientThread extends Thread{
	
	//진아수정
	MaxMovieView iv = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView iv) {
		this.iv = iv;
	}
	
	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		String msg = null;
		int protocol = 0;
		try {
			while(!stop) {
				//msg = (String)iv.ois.readObject();
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer("","#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//프로토콜에 따라 send 파라미터 달라지는...
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
}
