package com.fileManager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.entity.ApiLog;
import com.fileManager.mapper.ApiLogMapper;
import com.fileManager.service.IApiLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {

    @Override
    public Page<ApiLog> getLogsBySearch(Page<ApiLog> page, String search) {
        // 进行模糊查询，按时间倒序排序
        return baseMapper.selectLogsBySearch(page, search);
    }
    @Override
// 导入日志
    public void importLogsFromXml(MultipartFile file, boolean isSafe) throws Exception {
        // 解析 XML 文件
        List<ApiLog> logs = parseXmlFile(file, isSafe);

        // 批量插入日志到数据库
        this.saveBatch(logs);
    }

    // 解析 XML 文件
    private List<ApiLog> parseXmlFile(MultipartFile file, boolean isSafe) throws Exception {
        List<ApiLog> logs = new ArrayList<>();

        // 创建并配置 DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // 根据 isSafe 控制是否禁用外部实体
        if (isSafe) {
            // 防止 XXE 攻击，禁用外部实体
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);  // 禁用 DTD（Document Type Definition）声明
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);  // 禁用外部实体
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);  // 禁用外部参数实体
            factory.setXIncludeAware(false);  // 禁用 XInclude 特性
            factory.setNamespaceAware(true);  // 启用命名空间
        }

        // 使用安全配置的 DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = file.getInputStream();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        // 获取日志节点列表
        NodeList nodeList = document.getElementsByTagName("log");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // 读取 XML 元素内容
                String message = element.getElementsByTagName("message").item(0).getTextContent();
                String logLevel = element.getElementsByTagName("logLevel").item(0).getTextContent();
                String timestamp = element.getElementsByTagName("timestamp").item(0).getTextContent();

                // 创建 Log 对象
                ApiLog log = new ApiLog();
                log.setMethod("导入日志");
                log.setResult(message);
                log.setError(logLevel);
                log.setCreatedAt(LocalDateTime.parse(timestamp));  // 假设时间格式为 ISO 8601
                logs.add(log);
            }
        }

        return logs;
    }

    // 假设你有 saveBatch 方法来批量保存日志
    private void saveBatch(List<ApiLog> logs) {
        // 实现保存日志的方法
    }
}
