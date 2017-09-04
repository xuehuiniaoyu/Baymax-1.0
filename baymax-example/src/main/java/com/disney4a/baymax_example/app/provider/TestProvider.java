package com.disney4a.baymax_example.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.disney4a.baymax.annotations.Tag_Provider;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Provider(name = "testProvider")
public class TestProvider extends ContentProvider {
    public static final String TAG = TestProvider.class.getSimpleName();
    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG, "query:"+uri);
        Toast.makeText(getContext(), "query:"+uri, Toast.LENGTH_SHORT).show();
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
