package cn.onlyloveyd.demo.ext;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 媒体库工具
 */
public class MediaStoreUtils {
    private static final String IMAGE_MEDIA = "image/*";
    private static final String AUDIO_MEDIA = "audio/*";
    private static final String VIDEO_MEDIA = "video/*";

    /**
     * 根据媒体Uri路径查询实际文件地址(只用于图片、音频和视频)
     *
     * @param context 上下文
     * @param uri     媒体文件Uri路径
     * @return 文件实际路径
     */
    public static String getMediaPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        if (!TextUtils.isEmpty(path) && new File(path).exists()) {
            return path;
        }

        String authority = uri.getAuthority();
        if ("com.android.providers.media.documents".equals(authority)) {
            String wholeId = DocumentsContract.getDocumentId(uri);
            String[] typeAndId = wholeId.split(":");
            String type = typeAndId[0];
            if ("primary".equalsIgnoreCase(type)) {
                path = Environment.getExternalStorageDirectory() + "/" + typeAndId[1];
                return path;
            } else {
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{typeAndId[1]};
                if (contentUri != null) {
                    String[] pathColumn = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = context.getContentResolver().query(contentUri, pathColumn, selection, selectionArgs, null);
                    if (cursor == null) {
                        return null;
                    }
                    try {
                        int index = cursor.getColumnIndexOrThrow(pathColumn[0]);
                        cursor.moveToNext();
                        path = cursor.getString(index);
                        return path;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        cursor.close();
                    }
                }
            }
        } else {
            String[] pathColumn = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, pathColumn, null, null, null);
            if (cursor == null) {
                return null;
            }
            try {
                int index = cursor.getColumnIndexOrThrow(pathColumn[0]);
                cursor.moveToNext();
                path = cursor.getString(index);
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return null;
    }

    /**
     * 文件路径转为Uri路径
     *
     * @param context 上下文
     * @param file    文件
     * @return 文件Uri地址
     */
    public static Uri getIntentUri(Context context, File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private static void launchForResult(Object presenter, Intent intent, int requestCode) {
        if (presenter instanceof Activity) {
            ((Activity) presenter).startActivityForResult(intent, requestCode);
        } else if (presenter instanceof Fragment) {
            ((Fragment) presenter).startActivityForResult(intent, requestCode);
        } else if (presenter instanceof androidx.fragment.app.Fragment) {
            ((androidx.fragment.app.Fragment) presenter).startActivityForResult(intent, requestCode);
        }
    }

    private static Context getContext(Object presenter) {
        if (presenter instanceof Activity) {
            return (Activity) presenter;
        } else if (presenter instanceof Fragment) {
            return ((Fragment) presenter).getActivity();
        } else if (presenter instanceof androidx.fragment.app.Fragment) {
            return ((androidx.fragment.app.Fragment) presenter).getActivity();
        }
        return null;
    }
}
