package com.syh.rag.demo1BaseRag.controller;

import com.syh.rag.demo1BaseRag.document.embedding.EmbeddingService;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 检索生成基础流程
 * 通过运行项目 resource 下的 static 的 ask.html 页面进行问答
 * 该流程仅包括基础的相似度召回和召回内容拼接回复，不包括混合检索、重排序等高级功能，这些功能会在demo2中单独进行编码
 */
@RestController
@RequestMapping("/retrieval")
public class RetrievalGenerationController {

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private ChatModel chatModel;

    private ChatClient chatClient;

    @PostConstruct
    public void init() {
        // 初始化操作
        chatClient = ChatClient.builder(chatModel).build();
    }

    private static final int DEFAULT_TOP_K = 5;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.5;

    @PostMapping("/ask")
    public Flux<String> ask(@RequestBody String question) {
        // 对问题进行相似度检索，召回相关文档
        List<Document> similarityDocuments = embeddingService.similaritySearch(
                SearchRequest.builder()
                             .query(question)
                             .topK(DEFAULT_TOP_K)
                             .similarityThreshold(DEFAULT_SIMILARITY_THRESHOLD)
                             .build()
        );
        // 上下文融合，构造提示词
        String promptTemplate = """
                请基于以下提供的参考文档内容，回答用户的问题。
                如果参考文档中没有相关信息，请直接说明"没有找到相关信息"，不要编造内容。
                除此之外，回复的格式要规范、美观，不要出现任何不美观符号或者其他不明确缩进，例如md文档常见的**符号或者```符号
                参考文档:
                {documents}
                
                用户问题: {question}
                """;

        PromptTemplate prompt = new PromptTemplate(promptTemplate);
        Prompt realPrompt = prompt.create(Map.of("documents", similarityDocuments, "question", question));
        return chatClient.prompt(realPrompt).stream().content();
    }


}
