package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class BearPicture {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public
    long id;

    @ColumnInfo(name = "height")
    int height;

    @ColumnInfo(name = "width")
    int width;

    public String getTimeDownloaded() {
        return timeDownloaded;
    }

    public void setTimeDownloaded(String timeDownloaded) {
        this.timeDownloaded = timeDownloaded;
    }

    @ColumnInfo(name = "timeDownloaded")
    String timeDownloaded;

    public BearPicture(){

    }

    public BearPicture(Integer h, Integer w, String t){
        height = h;
        width = w;
        timeDownloaded = t;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
