package ir.drax.smartrider.data;

import io.reactivex.Observable;
import io.reactivex.Single;
import ir.drax.smartrider.data.db.DbHelper;
import ir.drax.smartrider.data.network.ApiHelper;
import ir.drax.smartrider.data.network.model.GoogleDirectionResponse;
import ir.drax.smartrider.data.prefs.PreferencesHelper;

public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {


    void updateApiHeader(Long userId, String accessToken);

    void setUserAsLoggedOut();

    Observable<Boolean> seedDatabaseQuestions();

    Observable<Boolean> seedDatabaseOptions();

    void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
