package burp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BurpExtender implements IBurpExtender,IHttpListener,IExtensionStateListener{
	IBurpExtenderCallbacks call;
	IExtensionHelpers helpers;
    PrintWriter out;
	private List<IParameter> parameters;
	FileOutputStream fWrite;
	String file="D:/filepath.txt";//参数字典存放位置
	String fileparm="D:/fileparma.txt";
	BufferedOutputStream bOut;
	BufferedOutputStream bOut1;
	FileOutputStream fWrite1;
	List<String> orlist=new ArrayList<String>();
	List<String> orlist1=new ArrayList<String>();
	private List<String> newlist;
	private List<String> newlist1;
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		 this.call=callbacks;
		 this.helpers=callbacks.getHelpers();
		 this.out=new PrintWriter(callbacks.getStdout(),true);
		 this.call.setExtensionName("httpparm");
		 this.out.println("start");
		 this.call.registerHttpListener(this);
		 File mFile=new File(file);
		 if(mFile.isFile()&&mFile.exists()) {
		 }else {
			 try {
				mFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 File mFile1=new File(fileparm);
		 if(mFile1.isFile()&&mFile.exists()) {
		 }else {
			 try {
				mFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 out.println("path:"+file);
		 out.println("parm:"+fileparm);
		 try {
			fWrite=new FileOutputStream(mFile);
			fWrite1=new FileOutputStream(mFile1);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 bOut=new BufferedOutputStream(fWrite);
		 bOut1=new BufferedOutputStream(fWrite1);
	}

	//获取爬行网站所有的url参数
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo){
	             if(messageIsRequest) {
				  parameters = helpers.analyzeRequest(messageInfo).getParameters();
				  for (IParameter parm : parameters) {
					if(parm.getType()!=IParameter.PARAM_COOKIE) {
						//out.print(parm.getName());
						//orlist1.add(parm.getName());
						try {
							bOut1.write(parm.getName().getBytes());
							bOut1.write("\r\n".getBytes());
							bOut1.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					}
				  }
				  String path = helpers.analyzeRequest(messageInfo).getUrl().getPath();
				  if(Pattern.matches("^/.*?/.*", path)){
						System.out.println("true");
						Pattern compile = Pattern.compile("^(/.*?/).*");
						Matcher m = compile.matcher(path);
						if(m.find()) {
							//out.println((m.group(1)));
							//orlist.add(m.group(1));//添加到list中
							try {
								bOut.write(m.group(1).getBytes());
								bOut.write("\r\n".getBytes());
								bOut.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 
						}
						 
			  }
	             }
	    		 
	    		 
	}

	@Override
	public void extensionUnloaded() {//退出burpsuite卸载插件的时候释放资源
		//单词去重,但没有文件去重
//		for (String orstring : orlist) {
//			if(!newlist.contains(orstring)) {//如果不包含就添加
//				newlist.add(orstring);
//				
//			}
//		}
//		for (String newstring : newlist) {
//			try {
//				bOut.write(newstring.getBytes());
//				bOut.write("\r\n".getBytes());
//				bOut.flush();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		for (String orstring : orlist1) {
//			if(!newlist1.contains(orstring)) {//如果不包含就添加
//				newlist1.add(orstring);
//				
//			}
//		}
//		for (String newstring : newlist1) {
//			try {
//				bOut1.write(newstring.getBytes());
//				bOut1.write("\r\n".getBytes());
//				bOut1.flush();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		try {
			bOut.close();
			bOut1.close();
			fWrite1.close();
			fWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
 