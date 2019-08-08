
package ir.drax.smartrider.data.db;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import ir.drax.smartrider.di.ApplicationContext;
import ir.drax.smartrider.di.DatabaseInfo;

/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
public class DbOpenHelper {

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        return ;
    }

}
