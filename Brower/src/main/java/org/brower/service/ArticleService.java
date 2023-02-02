package org.brower.service;

import org.brower.pojo.Article;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ArticleService {

    void  createIndex(String name);

    boolean getIndex(String name) throws IOException;

    void createDocument(String dbname,Article article) throws IOException;

    boolean deleteDocument(String dbname,int id) throws IOException;

    int updateDocument(String dbname,Article article) throws  IOException;

    Article getDocument(String dbname,String id) throws IOException;

    public List<Article> queryByKey(String keyword,int pageSize,int pageNum);

    public  String setHighLight(String keywords, String info);
}
