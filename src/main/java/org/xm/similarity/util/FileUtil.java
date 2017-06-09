package org.xm.similarity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件工具类
 *
 * @author xuming
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 从指定流读入每一行文字
     *
     * @param input    输入流
     * @param encoding 编码
     * @param event    触发的事件
     * @throws IOException
     */
    public static void traverseLines(InputStream input, String encoding, TraverseEvent<String> event)
            throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input, encoding));
        String line;

        while ((line = in.readLine()) != null) {
            event.visit(line);
        }

        input.close();
        in.close();
    }

    /**
     * 保存字符串到文件中
     *
     * @param content
     * @param fileName
     * @return
     */
    public static boolean saveStringToFile(String content, String fileName) {
        boolean rtn = false;
        BufferedOutputStream out = null;
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();

            out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(content.getBytes("GBK"));
            out.close();
            rtn = true;
        } catch (Exception e) {
            logger.error("saveStringToFile error:{}", e.getMessage());
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                logger.error("Exception:{}", e.getMessage());
            }
        }
        return rtn;
    }
}
