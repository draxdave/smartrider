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

package ir.drax.smartrider.data.db;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ir.drax.smartrider.data.db.model.Option;
import ir.drax.smartrider.data.db.model.Question;
import ir.drax.smartrider.data.db.model.User;


/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
public class AppDbHelper implements DbHelper {



    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
    //    mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertUser(final User user) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public Observable<List<Question>> getAllQuestions() {
        return Observable.fromCallable(new Callable<List<Question>>() {
            @Override
            public List<Question> call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public Observable<Boolean> isQuestionEmpty() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public Observable<Boolean> isOptionEmpty() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public Observable<Boolean> saveQuestion(final Question question) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveOption(final Option option) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveQuestionList(final List<Question> questionList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveOptionList(final List<Option> optionList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }
}
