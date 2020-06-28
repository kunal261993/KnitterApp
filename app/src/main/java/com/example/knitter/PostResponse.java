package com.example.knitter;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostResponse {

    @SerializedName("result")
    private List<Post> result;
    @SerializedName("_meta")
    public Post metaData;

    public Post getMetaData() {
        return metaData;
    }

    public void setMetaData(Post metaData) {
        this.metaData = metaData;
    }

    public List<Post> getResult() {
        return result;
    }

    public void setResult(List<Post> result) {
        this.result = result;
    }
}
