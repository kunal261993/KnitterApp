package com.example.knitter;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Post {
    @SerializedName("id")
    private Integer id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("currentPage")
    private Integer currentPage;

    @SerializedName("pageCount")
    private Integer pageCount;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Post(Integer id, Integer user_id, String title, String body, Integer currentPage, Integer pageCount) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.body = body;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
    }
    public Post()
    {}
    public Post(List<PostTable> postTable)
    {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
