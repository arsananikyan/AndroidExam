package com.picsarttraining.androidexam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Arsen on 23.03.2016.
 */
public class ImagesAdapter extends BaseAdapter {
    ArrayList<Uri> images;
    Context context;
    private LayoutInflater inflater;

    public ImagesAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setImages(ArrayList<Uri> images) {
        this.images.clear();
        this.images = new ArrayList<>(images);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Uri getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Uri imageUri = images.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        try {
            holder.image.setImageBitmap(decodeUri(context, imageUri, 90));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView image;

        ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.item_image_view);
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}
