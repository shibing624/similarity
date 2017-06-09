package org.xm.similarity.word.hownet.sememe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.Similarity;
import org.xm.similarity.ISimilarity;
import org.xm.similarity.util.DicReader;
import org.xm.similarity.word.hownet.IHownetMeta;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * 义原解析器
 *
 * @author xuming
 */
public abstract class SememeParser implements IHownetMeta, ISimilarity {
    private static final Logger logger = LoggerFactory.getLogger(SememeParser.class);
    /**
     * 所有的义原都存放到一个MultiMap, Key为Sememe的中文定义, Value为义原的Id
     */
    protected static Multimap<String, String> SEMEMES = null;
    private static final String path = Similarity.Config.SememeXmlPath;

    public SememeParser() throws IOException {
        if (SEMEMES != null) {
            return;
        }
        SEMEMES = HashMultimap.create();
        InputStream inputStream = new GZIPInputStream(DicReader.getInputStream(path));
        load(inputStream);
    }

    /**
     * 文件加载义原
     */
    private void load(InputStream inputStream) throws IOException {
        long time = System.currentTimeMillis();
        int count = 0;
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(inputStream);

            while (xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().toString().equals("sememe")) {
                        String cnWord = startElement.getAttributeByName(QName.valueOf("cn")).getValue();
                        String id = startElement.getAttributeByName(QName.valueOf("id")).getValue();
                        SEMEMES.put(cnWord, id);
                        count++;
                    }
                }
            }
            inputStream.close();
        } catch (Exception e) {
            logger.error("xml err:" + e.toString());
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;
        logger.info("complete! count num:" + count + ". time spend:" + time + "ms");
    }

    /**
     * 关联度
     *
     * @param sememeName1
     * @param sememeName2
     * @return
     */
    public double getAssociation(String sememeName1, String sememeName2) {
        return 0.0;
    }
}
