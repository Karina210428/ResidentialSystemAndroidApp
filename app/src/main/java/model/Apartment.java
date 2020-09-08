package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Apartment implements Serializable {

    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    private Integer id;

    @SerializedName("number")
    @Expose
    private Integer number;

    @SerializedName("block")
    @Expose
    private Integer block;

    @SerializedName("floor")
    @Expose
    private Integer floor;

    public Apartment(Integer number, Integer block, Integer floor) {
        this.number = number;
        this.block = block;
        this.floor = floor;
    }

    public String getApartmentNumber(){
        return  floor + "-" + block + "-" + number;
    }

    public Apartment() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

}
