package com.yuke.agentbackend.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DocumentLoader {

    /**
     * 从文件路径加载文档
     */
    public Document loadDocument(String filePath) {
        try {
            Path path = Paths.get(filePath);
            log.info("加载文档: {}", filePath);
            return FileSystemDocumentLoader.loadDocument(path, new TextDocumentParser());
        } catch (Exception e) {
            log.error("加载文档失败: {}", filePath, e);
            throw new RuntimeException("文档加载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从文本内容加载文档
     */
    public Document loadDocumentFromText(String content, String fileName) {
        log.info("从文本加载文档: {}", fileName);
        return Document.from(content, new dev.langchain4j.data.document.Metadata()
                .put("fileName", fileName));
    }

    /**
     * 从目录加载所有文档
     */
    public List<Document> loadDocumentsFromDirectory(String directoryPath) {
        List<Document> documents = new ArrayList<>();
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("目录不存在或不是目录: {}", directoryPath);
            return documents;
        }

        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".txt") || name.endsWith(".md") || name.endsWith(".pdf"));

        if (files != null) {
            for (File file : files) {
                try {
                    documents.add(loadDocument(file.getAbsolutePath()));
                } catch (Exception e) {
                    log.error("跳过文件: {}", file.getName(), e);
                }
            }
        }
        log.info("从目录加载了 {} 个文档", documents.size());
        return documents;
    }
}