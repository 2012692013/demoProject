package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.FileUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * wangeditor轻量级富文本
 * @author qiuyuehao 2018年3月23日上午10:22:07
 *
 */
@RestController
@RequestMapping("wangImg")
public class WangEditorImgController {

	private final String IP = "120.78.172.166:8088";
	
	//private final String IP = "192.168.0.119:8088";
	
	private final String PROJECTNAME = "demo";
	
	@RequestMapping(value="imgUpload",headers="api-version=1")
	public Map<String,Object> imgUpload(HttpServletRequest request){
		Map<String,String> fileMap = FileUtil.upLoadFile("uploadFile/editor", request);
		Set<Entry<String,String>> set = fileMap.entrySet();
		String[] urls = new String[fileMap.size()];
		
		int i = 0;
		for(Entry<String,String> e : set){
			urls[i] = "http://"+IP+"/"+PROJECTNAME+"/"+e.getValue();
			i++;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("errno", "0");
		map.put("data", urls);
		return map;
	}
}


/*文件拆分
public static void splitFileByLines(String fileReadName, String textSize) {
    File file = new File(fileReadName);
    BufferedReader bf = null;
    try {
        System.out.println("begin");
        long stime = System.currentTimeMillis();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
        bf = new BufferedReader(isr);
        String tempString = null;
        // 行数
        BigInteger line = new BigInteger("1");
        // 分割后的每个文件的行数
        BigInteger txtSize = new BigInteger(textSize);
        String fileWriteName = null;
        while ((tempString = bf.readLine()) != null) {
            // 当行数整除每个文件的数量之后就是文件的序号
            BigInteger txtNo = line.divide(txtSize).add(new BigInteger("1"));
            // 根据文件的序号来命名分割后的文件
            fileWriteName = "D:\\youdao\\" + txtNo + ".txt";
            FileWriter writer = new FileWriter(fileWriteName, true);
            writer.write(tempString);
            writer.write("\n");
            writer.close();
            // 读取一行就给行号加一
            line = line.add(new BigInteger("1"));
        }
        bf.close();
        long etime = System.currentTimeMillis();
        // 计算总共所用时间
        long spendTime = (etime - stime) / 60 / 1000;
        System.out.println("花费时间为" + spendTime + "分");
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (bf != null) {
            try {
                bf.close();
            } catch (IOException e1) {
            }
        }
    }
}

public static void main(String[] args) {
    // 这是源文件
    String fileReadName = "D:\\youdao\\catalina.txt";
    // 定义每个文件的行数，这里是80万行一个文件
    String textSize = "800000";
    splitFileByLines(fileReadName, textSize);
}*/
