package com.anbillon.routine.sample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class ParcelableExtra implements Parcelable {
  public final String name;

  public ParcelableExtra(String name) {
    this.name = name;
  }

  protected ParcelableExtra(Parcel in) {
    this.name = in.readString();
  }

  @Override public String toString() {
    return "{callerName=" + name + "}";
  }

  public static final Creator<ParcelableExtra> CREATOR = new Creator<ParcelableExtra>() {
    @Override public ParcelableExtra createFromParcel(Parcel source) {
      return new ParcelableExtra(source);
    }

    @Override public ParcelableExtra[] newArray(int size) {
      return new ParcelableExtra[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
  }
}
