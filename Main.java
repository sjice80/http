package PackEx;

import java.util.Arrays;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import ABC.Bicycle;
import ABC.CDE.*;
import ABC.CDE.EFG.*;
import java.net.*;

class Log implements Comparable<Log> {
	String time;
	String type;
	String msg;
	public Log(String line) {
		// TODO Auto-generated constructor stub
		this.time = line.split("#")[0];
		this.type = line.split("#")[1];
		this.msg = line.split("#")[2];
	}
	@Override
	public int compareTo(Log o) {
		// TODO Auto-generated method stub
		if(this.type.compareTo(o.type)>0)
			return 1;
		else if(this.type.compareTo(o.type)<0)
			return -1;
		return 0;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.time+"#"+this.type+"#"+this.msg+"\n";
	}
}
class ConvertThread implements Runnable {
	static Main obj = new Main();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int offset = 0;
		String type = Thread.currentThread().getName();
		FileWriter fw = null;
		try {
			fw = new FileWriter("./OUTFILE/TYPELOG_4_"+type+".TXT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < obj.loglist.size(); i++) {
			if(obj.loglist.get(i).type.equals(type)) {
				offset = i;
				break;
			}
		}
		String cmd = "./CODECONV.EXE";
		for (int i = offset; i < offset+obj.map.get(type); i++) {
			ProcessBuilder bld = new ProcessBuilder(cmd, obj.loglist.get(i).msg);
			Process p = null;
			try {
				p = bld.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line2 = "";
			try {
				line2 = br2.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			obj.loglist.get(i).msg = line2;
			try {
				fw.write(obj.loglist.get(i).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	static HashSet<String> set = new HashSet<String>();
	static String convertTable = "";
	static String fileS = "";
	static String fileN = "";
	
	static int cnt = 1;
	static TreeMap<String, Integer> map = new TreeMap<>();
	static ArrayList<Log> loglist = new ArrayList<>();
	static StringBuffer sb = new StringBuffer();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Bicycle b = new Bicycle();
		Train t = new Train();
		Car c = new Car();
		int[] arr = {1,2,3};
		perm(arr, 0);
		ArrayList<String> list = new ArrayList<String>(set);
		Collections.sort(list);
//		System.out.println(set);
		System.out.println(list);
//		hashsetTest();
//		runtimeExecTest();
		//keyword("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "LGCNS");
		//LGCNSABDEFHIJKMOPQRTUVWXYZ
		//convert("JKLMNOPQ");
		//convert("2#AABCD3EFFG4H3IJK");
		//seekFile();
		/*
		lineNumberRead();
		// 파일분석
		String line = "";
		FileReader fr = new FileReader("./INPUT/LOGFILE_B.TXT");
		BufferedReader br = new BufferedReader(fr);
		while((line = br.readLine())!=null) {
			Log l = new Log(line);
			loglist.add(l);
		}
		Collections.sort(loglist);
		// 로그 타입별 갯수만큼 메세지 변환 및 파일쓰기
		int i=0;
		for (i = 0; i < loglist.size()-1; i++) {
			if(loglist.get(i).type.equals(loglist.get(i+1).type)) {
//				sb.append(loglist.get(i).toString());
				cnt++;
			} else {
				map.put(loglist.get(i).type, cnt);
				/*
				sb.append(loglist.get(i).toString());
				FileWriter fw = new FileWriter("./OUTFILE/TYPELOG_3_"+loglist.get(i).type+".TXT");
				fw.write(sb.toString());
				sb = new StringBuffer();
				fw.close();
				*/
		/*
				ConvertThread ct = new ConvertThread();
				Thread t1 = new Thread(ct, loglist.get(i).type);
				t1.start();
				cnt = 1;
			}
		}
		/*
		sb.append(loglist.get(i).toString());
		FileWriter fw = new FileWriter("./OUTFILE/TYPELOG_3_"+loglist.get(i).type+".TXT");
		fw.write(sb.toString());
		sb = new StringBuffer();
		fw.close();
		*/
		/*
		map.put(loglist.get(i).type, cnt);
		ConvertThread ct = new ConvertThread();
		Thread t1 = new Thread(ct, loglist.get(i).type);
		t1.start();
		// 레포트
		FileWriter fwr = null;
		fwr = new FileWriter("./OUTFILE/REPORT_4.TXT");
		for (String key:map.keySet()) {
			fwr.write(key+"#"+map.get(key)+"\n");
		}
		fwr.close();
		*/
	}
	public static void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9876);
			while(true) {
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]에서 접속하였습니다.");
				ServerReceiver serverReceiver = new ServerReceiver(socket);
			}
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	static class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		int offset = 0;
		public ServerReceiver(Socket socket) {
			// TODO Auto-generated constructor stub
			this.socket = socket;
			byte[] b = new byte[1024];
			byte[] barr = new byte[1024];
			int length = 0;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				length = in.read(b);
				String f = new String(b,0,length);
				fileS = f.split("#")[0];
				String key = f.split("#")[1];
				subDirList("./BIGFILE");
				makeFile();
				FileReader fr = null;
				BufferedReader br = null;
				String line = "";
				int linenum = 0;
				if(fileN.endsWith(fileS)) {
					fr = new FileReader("./OUTFILE/"+fileS);
					br = new BufferedReader(fr);
					while((line = br.readLine())!=null) {
						linenum++;
						if(linenum==1) {
							line = keyword(line, key);
							barr = line.getBytes();
							out.write(barr, 0, barr.length);
							out.write('\n');
						} else {
							line = convert(line);
							barr = line.getBytes();
							out.write(barr, 0, barr.length);
							out.write('\n');
						}
						length = in.read(b);
						f = new String(b,0,length);
						if(f.endsWith("ERR")) {
							barr = line.getBytes();
							out.write(barr, 0, barr.length);
							out.write('\n');
							length = in.read(b);
							f = new String(b,0,length);
						}
						if(!f.endsWith("ACK")) {
							
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public static void lineNumberRead() throws IOException {
		String line = Files.readAllLines(Paths.get("./temp_4717564146971558403.txt")).get(1);
		System.out.println(line);
		String line2;
		int n=2;
		try (BufferedReader br = new BufferedReader(new FileReader("./temp_4717564146971558403.txt"))) {
		    for (int i = 0; i < n; i++)
		        br.readLine();
		    line2 = br.readLine();
		}
		String line3;
		try (Stream<String> lines = Files.lines(Paths.get("./temp_4717564146971558403.txt"))) {
		    line3 = lines.skip(n).findFirst().get();
		}
		/*
		FileReader fr = new FileReader("./temp_4717564146971558403.txt");
		if(fr!=null) {
			LineNumberReader lnr = new LineNumberReader(fr);
			String line = "";
			while((line = lnr.readLine())!=null) {
				System.out.println(lnr.getLineNumber()+":"+line);
			}
		}
		*/
	}
	public static void seekFile() throws IOException {
		File f = File.createTempFile("temp_", ".txt", new File("./"));
		System.out.println(f.getAbsolutePath());
		String[] out = new String[] {"apple", "orange", "melon"};
		FileWriter fw = new FileWriter(f);
		for (int i = 0; i < out.length; i++) {
			fw.write(out[i]+"\n");
		}
		fw.close();
		FileReader fr = new FileReader(f);
		char[] cbuf = new char[1024];
		int offset = 0;
		String s = "";
		for (int i = 0; i < out.length; i++) {
			fr.read(cbuf, offset, out[i].length()+1);
			s = new String(cbuf, offset, out[i].length());
			System.out.println(s);
			offset += out[i].length();
		}
		
	}
	public static String convert(String s) {
		String result = "";
		char ch = 0x00;
		char[] carr = convertTable.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)>='A' && s.charAt(i)<='Z') {
				for (int j = 0; j < carr.length; j++) {
					ch = carr[s.charAt(i)-'A'];
				}
			} else {
				ch = s.charAt(i);
			}
			result += ch;
		}
		return result;
	}
	public static String keyword(String s, String key) {
		String result = "";
		int find = 0;
		char[] carr = key.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			find = 0;
			char ch = s.charAt(i);
			for (int j = 0; j < carr.length; j++) {
				if(ch==carr[j]) {
					find = 1;
					break;
				}
			}
			if(find == 0) {
				result += ch;
			}
		}
		convertTable = key + result;
		return convertTable;
	}
	public static String ceaser(String s, int n) {
		String result = "";
		n = n%26;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if(Character.isUpperCase(ch)) {
				 ch = (char)((ch-'A'+n)%26+'A');
			}
			result += ch;
		}
		return result;
	}
	public static void subDirList(String source) throws IOException {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		for(File file:fileList) {
			if(file.isFile()) {
				fileN = file.getPath();
			} else if(file.isDirectory()) {
				subDirList(file.getPath());
			}
		}
		/*
		File[] list = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				return file.isFile() && file.getName().length()==27 &&
						file.getName().substring(9,17).equals("java");
			}
		});
		*/
	}
	public static void makeFile() throws IOException {
		String line = "", prev = "";
		int cnt = 1;
		FileReader fr = null;
		BufferedReader br = null;
		fr = new FileReader(fileN);
		br = new BufferedReader(fr);
		FileWriter fw = new FileWriter("./OUTFILE/ABCDFILE.TXT");

		while((line = br.readLine())!=null) {
			if(line.equals(prev)) {
				cnt++;
			} else {
				if(prev.equals("")) {
					prev = line;
					continue;
				} else if(cnt == 1) {
					prev = archive(prev);
					fw.write(prev+"\n");
				} else {
					prev = archive(prev);
					prev = cnt+"#"+prev;
					fw.write(prev+"\n");
				}
				prev = line;
				cnt = 1;
			}
		}
		if(cnt == 1) {
			prev = archive(prev);
			fw.write(prev+"\n");
		} else {
			prev = archive(prev);
			prev = cnt+"#"+prev;
			fw.write(prev+"\n");
		}
		br.close();
		fw.close();
	}
	public static void makeFileLine(int n) throws IOException {
		String line = "", prev = "";
		int cnt = 1;
		FileReader fr = null;
		BufferedReader br = null;
		fr = new FileReader(fileN);
		br = new BufferedReader(fr);
		FileWriter fw = new FileWriter("./OUTFILE/ABCDFILE.TXT");
		for (int i = 0; i < n-1; i++) {
			line = br.readLine();
		}
		while((line = br.readLine())!=null) {
			if(line.equals(prev)) {
				cnt++;
			} else {
				if(prev.equals("")) {
					prev = line;
					continue;
				} else if(cnt == 1) {
					prev = archive(prev);
					fw.write(prev+"\n");
				} else {
					prev = archive(prev);
					prev = cnt+"#"+prev;
					fw.write(prev+"\n");
				}
				prev = line;
				cnt = 1;
			}
		}
		if(cnt == 1) {
			prev = archive(prev);
			fw.write(prev+"\n");
		} else {
			prev = archive(prev);
			prev = cnt+"#"+prev;
			fw.write(prev+"\n");
		}
		br.close();
		fw.close();
	}
	public static String archive(String line) {
		char ch_prev = ' ';
		int ch_cnt = 1;
		String tmp = "";
		for (int i = 0; i < line.length(); i++) {
			if(ch_prev == line.charAt(i)) {
				ch_cnt++;
			} else {
				if(ch_prev == ' ') {
					ch_prev = line.charAt(i);
					continue;
				} else if(ch_cnt == 1) {
					tmp = tmp + ch_prev;
					ch_prev = line.charAt(i);
				} else if(ch_cnt == 2) {
					tmp = tmp + ch_prev + ch_prev;
					ch_prev = line.charAt(i);
				} else {
					tmp = tmp + ch_cnt + ch_prev;
					ch_prev = line.charAt(i);
				}
				ch_cnt = 1;
			}
		}
		if(ch_cnt<3) {
			tmp = tmp + ch_prev;
		} else {
			tmp = tmp + ch_cnt + ch_prev;
		}
		return tmp;
	}
	public static void execProcess() throws IOException {
		ProcessBuilder bld = new ProcessBuilder("SIGNAGE.EXE");
		Process p = bld.start();
		String line = "";
		Scanner sc = new Scanner(System.in);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader br = new BufferedReader(new FileReader("./OUTFILE/CMP_SIGNAGE.TXT"));
		while((line = br.readLine())!=null) {
			bw.write(line+"\r\n");
		}
		/*
		while((line = sc.nextLine())!=null) {
			bw.write(line+"\r\n");
			if(line.equals("")) {
				break;
			}
		}
		*/
		bw.flush();
		bw.close();
	}
	public static void runtimeExecTest() {
		try {
			Process proc = Runtime.getRuntime().exec("ls -al "+"./");	//windows:"cmd /c dir "
			InputStream in = proc.getInputStream();
			byte[] buffer = new byte[1024];
			int n = -1;
			while((n = in.read(buffer))!=-1) {
				System.out.print(new String(buffer, 0, n));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void hashsetTest() {
		HashSet s = new HashSet();
		s.add(new String("1"));
		s.add(new String("1"));
		s.add(new String("2"));
		s.add("1");
		System.out.println(s);
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			System.out.println(o);
		}
	}
	public static void perm(int[] arr, int pivot) {
		for(int i=pivot; i<arr.length; i++) {
			System.out.println("1. Arr:"+Arrays.toString(arr)+", i:"+i+", pivot:"+pivot);
			swap(arr, i, pivot);
			System.out.println("2. Arr:"+Arrays.toString(arr)+", i:"+i+", pivot:"+pivot);
			set.add(Arrays.toString(arr));
			perm(arr, pivot+1);
			swap(arr, i, pivot);
			System.out.println("3. Arr:"+Arrays.toString(arr)+", i:"+i+", pivot:"+pivot);			
		}
		
	}
	public static void swap(int[] arr, int i, int j) {
		int temp = 0;
		temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
