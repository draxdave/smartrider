/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package ir.drax.smartrider.data.db.model;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by janisharali on 08/12/16.
 */

public class Question {

    @Expose
    @SerializedName("id")
    private Long id;

    @Expose
    @SerializedName("question_text")
    private String questionText;

    @Expose
    @SerializedName("question_img_url")
    private String imgUrl;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;

    private List<Option> optionList;

    /**
     * Used to resolve relations
     */

    /**
     * Used for active entity operations.
     */

    public Question(Long id, String questionText, String imgUrl, String createdAt,
                    String updatedAt) {
        this.id = id;
        this.questionText = questionText;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Question() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */

    
}