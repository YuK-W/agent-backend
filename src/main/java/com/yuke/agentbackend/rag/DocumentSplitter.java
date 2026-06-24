package com.yuke.agentbackend.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentSplitter {

    private final int chunkSize;
    private final int chunkOverlap;

    /**
     * 将文档分割成多个文本片段
     */
    public List<TextSegment> split(Document document) {
        log.info("分割文档，chunkSize={}, chunkOverlap={}", chunkSize, chunkOverlap);

        // 按字符分割
        return DocumentSplitters.recursive(chunkSize, chunkOverlap)
                .split(document);
    }

    /**
     * 将文本内容分割成多个片段
     */
    public List<TextSegment> splitText(String text) {
        Document document = Document.from(text);
        return split(document);
    }
}