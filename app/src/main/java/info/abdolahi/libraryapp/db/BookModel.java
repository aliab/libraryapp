package info.abdolahi.libraryapp.db;

import java.io.Serializable;

public class BookModel implements Serializable{

    private int id, category_id, is_faved;
    private String sarsehnase, title, publisher, motarjem, publish_year;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getIs_faved() {
        return is_faved;
    }

    public void setIs_faved(int is_faved) {
        this.is_faved = is_faved;
    }

    public String getSarsehnase() {
        return sarsehnase;
    }

    public void setSarsehnase(String sarsehnase) {
        this.sarsehnase = sarsehnase;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getMotarjem() {
        return motarjem;
    }

    public void setMotarjem(String motarjem) {
        this.motarjem = motarjem;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
